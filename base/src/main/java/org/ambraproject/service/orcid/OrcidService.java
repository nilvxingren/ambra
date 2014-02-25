/*
 * Copyright (c) 2007-2014 by Public Library of Science
 *
 * http://plos.org
 * http://ambraproject.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.service.orcid;

import org.ambraproject.views.OrcidAuthorization;

/**
 * Service for interacting with the oAuth provider for ORCiD
 *
 * http://support.orcid.org/knowledgebase/topics/19683-orientation
 */
public interface OrcidService {
  //Key for property stored in the ambra configuration
  public static final String ORCID_AUTHORIZATION_URL = "ambra.services.orcid.url.authorizationEndPoint";

  /**
   * When the user has granted us access to ORCiD.  We're given an authorization token.  Using this token query ORCiD
   * using this token, client-id and client-secret for an access token.  This API call also returns the user's ORCiD
   *
   * @param authorizationCode the authorization token generated by ORCiD
   *
   * @return a OrcidAuthorization object populated with the response from ORCiD (or null in the case of error)
   *
   * @throws Exception
   */
  public OrcidAuthorization authorizeUser(String authorizationCode) throws Exception;

  /**
   * The clientID of this application.  Stored in the configuration as ..services/orcid/client-id
   *
   * This is the client identifier assigned by ORCiD to associate this application with an ORCiD account and secret
   *
   * @return the client ID
   */
  public String getClientID();

  /**
   * The scope of authority to be granted for.  There are varying levels of access to be asked of by the
   * application.  Note:
   *
   * http://support.orcid.org/knowledgebase/articles/120162-orcid-scopes
   *
   * More comments provided in the implementation inline
   *
   * @return The scope of authority to be granted for
   */
  public String getScope();
}
