/* $HeadURL::                                                                                     $
 * $Id$
 *
 * Copyright (c) 2006 by Topaz, Inc.
 * http://topazproject.org
 *
 * Licensed under the Educational Community License version 1.0
 * http://opensource.org/licenses/ecl1.php
 */
package org.plos.models;

import java.net.URI;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import org.topazproject.otm.annotations.Entity;
import org.topazproject.otm.annotations.Id;
import org.topazproject.otm.annotations.Predicate;
import org.topazproject.otm.annotations.PredicateMap;
import org.topazproject.otm.annotations.Rdf;

/**
 * Model for PLOS articles.
 *
 * @author Eric Brown
 * @author Amit Kapoor
 */
@Entity(type = Rdf.topaz + "Article", model = "ri")
public class Article extends ObjectInfo {
  /** Article state of "Active" */
  public static final int STATE_ACTIVE   = 0;
  /** Article state of "Disabled" */
  public static final int STATE_DISABLED = 1;

  /** Active article states */
  public static final int[] ACTIVE_STATES = {STATE_ACTIVE};

  @Predicate(uri = Rdf.dc_terms + "hasPart")
  private List<ObjectInfo> parts = new ArrayList<ObjectInfo>();
  // This will be used to indicate the different types the article conforms to
  @Predicate(uri = Rdf.rdf + "type", dataType = Rdf.xsd + "anyURI")
  private Set<URI> articleType;

  /**
   * The categories the article belongs to
   *
   * TODO: This needs to be changed.
   */
  @Predicate(uri = Rdf.topaz + "hasCategory")
  private Set<Category> categories = new HashSet<Category>();

  /** 
   * @return the different parts of the article 
   */
  public List<ObjectInfo> getParts() {
    return parts;
  }

  /** 
   * @param parts the different parts of the article 
   */
  public void setParts(List<ObjectInfo> parts) {
    this.parts = parts;
  }

  /**
   * Get the list of categories for the article
   *
   * @return the categories
   */
  public Set<Category> getCategories() {
    return categories;
  }

  /**
   * A single PLoS article can have multiple types associated with it. For
   * example, it could have a PMC article type and also a PLoS specific article
   * type. This setter allows the application to set the different types this
   * article conforms to.
   *
   * @param articleType the different types this article conforms to.
   */
  public void setArticleType(Set<URI> articleType) {
    this.articleType = articleType;
  }

  /**
   * Return the different types of the article
   *
   * @return the article types
   */
  public Set<URI> getArticleType() {
    return articleType;
  }

  /**
   * Set the list of categories for the article
   *
   * @param categories the categories to article belongs to
   */
  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }
}