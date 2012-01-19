/* $HeadURL::                                                                            $
 * $Id$
 *
 * Copyright (c) 2007 by Topaz, Inc.
 * http://topazproject.org
 *
 * Licensed under the Educational Community License version 1.0
 * http://opensource.org/licenses/ecl1.php
 */
package org.topazproject.otm.stores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.topazproject.mulgara.itql.Answer;
import org.topazproject.mulgara.itql.AnswerException;

import org.topazproject.otm.ClassMetadata;
import org.topazproject.otm.Criteria;
import org.topazproject.otm.Filter;
import org.topazproject.otm.ModelConfig;
import org.topazproject.otm.OtmException;
import org.topazproject.otm.criterion.Criterion;
import org.topazproject.otm.criterion.Order;
import org.topazproject.otm.filter.AbstractFilterImpl;
import org.topazproject.otm.filter.ConjunctiveFilterDefinition;
import org.topazproject.otm.filter.JunctionFilterDefinition;
import org.topazproject.otm.mapping.RdfMapper;

/**
 * A helper class to build ITQL query for a Criteria.
 *
 * @author Pradeep Krishnan
 */
public class ItqlCriteria {
  private Criteria criteria;

  /**
   * Creates a new ItqlCriteria object.
   *
   * @param criteria the criteria
   */
  ItqlCriteria(Criteria criteria) {
    this.criteria = criteria;
  }

  /**
   * Builds an ITQL query.
   *
   * @return the query string
   *
   * @throws OtmException on an exception
   */
  String buildUserQuery() throws OtmException {
    StringBuilder qry   = new StringBuilder(500);
    ClassMetadata cm    = criteria.getClassMetadata();
    String        model = getModelUri(criteria, cm.getModel());

    qry.append("select $s");

    boolean hasOrderBy = buildProjections(criteria, qry, "$s", true);

    qry.append(" from <").append(model).append("> where ");

    int len = qry.length();
    buildWhereClause(criteria, qry, "$s", "$s", true, false);

    if (qry.length() == len)
      throw new OtmException("No criterion list to filter by and the class '" + cm
                             + "' does not have an rdf:type.");

    qry.setLength(qry.length() - 4);

    if (hasOrderBy) {
      qry.append("order by ");

      List<String> orders = new ArrayList();
      buildOrderBy(criteria, orders, "$s");

      for (String o : orders)
        qry.append(o);
    }

    if (criteria.getMaxResults() > 0)
      qry.append(" limit " + criteria.getMaxResults());

    if (criteria.getFirstResult() >= 0)
      qry.append(" offset " + criteria.getFirstResult());

    qry.append(";");

    return qry.toString();
  }

  private boolean buildProjections(Criteria criteria, StringBuilder qry, String subject,
                                   boolean isTop) {
    int    i      = 0;
    String prefix = " " + subject + "o";

    String id = criteria.getClassMetadata().getIdField().getName();

    boolean hasOrder = !criteria.getOrderList().isEmpty();
    for (Order o : criteria.getOrderList()) {
      if (!id.equals(o.getName()))
        qry.append(prefix + i++);
      else if (!isTop)
        qry.append(" " + subject);
    }

    i = 0;

    for (Criteria cr : criteria.getChildren())
      hasOrder |= buildProjections(cr, qry, subject + "c" + i++, false);

    return hasOrder;
  }

  private static void buildWhereClause(Criteria criteria, StringBuilder qry, String subject,
                                       String pfx, boolean needType, boolean isFilter)
                         throws OtmException {
    ClassMetadata cm    = criteria.getClassMetadata();
    String        model = getModelUri(criteria, cm.getModel());

    if (needType && cm.getType() != null)
      qry.append(subject).append(" <rdf:type> <").append(cm.getType()).append("> in <").append(model)
          .append("> and ");

    //XXX: there is problem with this auto generated where clause
    //XXX: it could potentially limit the result set returned by a trans() criteriom
    int    i      = 0;
    String object = pfx + "o";
    String id = criteria.getClassMetadata().getIdField().getName();

    if (!isFilter) {
      for (Order o : criteria.getOrderList())
        if (!id.equals(o.getName()))
          buildPredicateWhere(criteria.isReferrer() ? criteria.getClassMetadata() : cm, criteria,
                              o.getName(), subject, object + i++, qry, model);
    }

    i = 0;

    String tmp = pfx + "t";

    for (Criterion c : criteria.getCriterionList())
      qry.append(c.toItql(criteria, subject, tmp + i++)).append(" and ");

    if (!isFilter) {
      for (Filter f : criteria.getFilters()) {
        if (isFilterApplicable(criteria, f)) {
          buildFilter((AbstractFilterImpl) f, qry, subject, pfx + "f" + i++);
          qry.append(" and ");
        }
      }
    }

    i = 0;

    for (Criteria cr : criteria.getChildren()) {
      String child = pfx + "c" + i++;
      buildPredicateWhere(cr.isReferrer() ? cr.getClassMetadata() : cm, cr,
                          cr.getMapping().getName(), subject, child, qry, model);
      buildWhereClause(cr, qry, child, child, true, isFilter);
    }
  }

  /** 
   * Build a where-clause fragment for the given filter. 
   * 
   * @param f       the filter
   * @param qry     the query to which to attach the fragment
   * @param subject the query variable being filtered
   * @param pfx     the prefix to use for variables
   */
  static void buildFilter(AbstractFilterImpl f, StringBuilder qry, String subject, String pfx)
       throws OtmException {
    if (f instanceof JunctionFilterDefinition.JunctionFilter) {
      JunctionFilterDefinition.JunctionFilter jf = (JunctionFilterDefinition.JunctionFilter) f;
      if (jf.getFilters().size() == 0)
        return;

      String op = (jf instanceof ConjunctiveFilterDefinition.ConjunctiveFilter) ? " and " : " or ";
      qry.append("(");

      int idx = 0;
      for (AbstractFilterImpl cf : jf.getFilters()) {
        buildFilter(cf, qry, subject, pfx + "j" + idx++);
        qry.append(op);
      }

      qry.setLength(qry.length() - op.length());
      qry.append(")");
    } else {
      Criteria fc = f.getCriteria();
      buildWhereClause(fc, qry, subject, pfx, false, true);
      qry.setLength(qry.length() - 5);
    }
  }

  private static void buildPredicateWhere(ClassMetadata cm, Criteria c, String name, String subject,
                                          String object, StringBuilder qry, String model)
                            throws OtmException {
    RdfMapper m = (RdfMapper)cm.getMapperByName(name);

    if (m == null)
      throw new OtmException("No field with the name '" + name + "' in " + cm);

    String mUri = (m.getModel() != null) ? getModelUri(c, m.getModel()) : model;

    if (m.hasInverseUri() ^ c.isReferrer()) {
      String tmp = object;
      object     = subject;
      subject    = tmp;
    }

    qry.append(subject).append(" <").append(m.getUri()).append("> ").append(object).append(" in <")
        .append(mUri).append("> and ");
  }

  private static void buildOrderBy(Criteria criteria, List<String> orders, String subject) {
    int    i      = 0;
    String prefix = subject + "o";
    String id = criteria.getClassMetadata().getIdField().getName();

    for (Order o : criteria.getOrderList()) {
      int pos = criteria.getOrderPosition(o);

      while (pos >= orders.size())
        orders.add("");

      String var = id.equals(o.getName()) ? subject : (prefix + i++);
      orders.set(pos, var + (o.isAscending() ? " asc " : " desc "));
    }

    i = 0;

    for (Criteria cr : criteria.getChildren())
      buildOrderBy(cr, orders, subject + "c" + i++);
  }

  private static String getModelUri(Criteria criteria, String modelId) throws OtmException {
    ModelConfig mc = criteria.getSession().getSessionFactory().getModel(modelId);

    if (mc == null) // Happens if using a Class but the model was not added
      throw new OtmException("Unable to find model '" + modelId + "'");

    return mc.getUri().toString();
  }

  private static boolean isFilterApplicable(Criteria current, Filter f) throws OtmException {
    ClassMetadata cm =
        current.getSession().getSessionFactory()
               .getClassMetadata(f.getFilterDefinition().getFilteredClass());

    return (cm != null && cm.isAssignableFrom(current.getClassMetadata()));
  }

  /**
   * Create the results list from an Itql Query response.
   *
   * @param ans the ITQL query response
   *
   * @return list of results
   *
   * @throws OtmException on an error
   */
  List createResults(List<Answer> ans) throws OtmException {
    // parse
    List          results = new ArrayList();
    ClassMetadata cm      = criteria.getClassMetadata();

    try {
      // check if we got something useful
      if (ans.isEmpty())
        return Collections.emptyList();

      Answer qa = ans.get(0);
      if (qa.getVariables() == null)
        throw new OtmException("query failed: " + qa.getMessage());

      // go through the rows and build the results
      qa.beforeFirst();

      while (qa.next()) {
        String s = qa.getString("s");
        Object o = criteria.getSession().get(cm, s, false);

        if (o != null)
          results.add(o);
      }

      qa.close();
    } catch (AnswerException ae) {
      throw new OtmException("Error parsing answer", ae);
    }

    return results;
  }
}