<#--
  $HeadURL:: http://ambraproject.org/svn/plos/templates/branches/ginzu/journals/PLoSDef#$
  $Id: pageNotFound.ftl 12105 2012-10-25 22:09:34Z josowski $

  Copyright (c) 2007-2010 by Public Library of Science
  http://plos.org
  http://ambraproject.org

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<div id="pagebdy-wrap">
  <div id="pagebdy">
    <div id="static-wrap">
      <div id="page-not-found">
      <@s.url action="feedback" namespace="/feedback" includeParams="none" id="feedbackURL"/>
      <@s.url action="advancedSearch" namespace="/search" includeParams="all" noSearchFlag="true" query="" id="searchAdvancedURL" />

        <h1>Article Not Found</h1>

        <div id="image">
          <img id="bottles" src="/images/error.404.png" alt="no image to display"/>

          <p><a href="http://creativecommons.org/licenses/by/2.0/deed.en">CC</a> Image courtesy of
            <a href="http://www.flickr.com/photos/heypaul/107326169/">Hey Paul on Flickr</a></p>
        </div>

        <div id="text">
          <p>Sorry, the article that you've requested cannot be found; it may have been moved, changed or removed.</p>

          <p class="in-between">Please use the <a href="${searchAdvancedURL}">advanced search form</a> to locate an
            article.</p>

          <p class="in-between">If you continue to experience problems with the site, please provide a detailed account of
            the circumstances
            on
            our <a href="${feedbackURL}">feedback form</a>.</p>

          <p>Thank you for your patience.</p>
        </div>
      </div>
    </div><!-- static-wrap -->
  </div><!-- pagebdy -->
</div><!-- pagebdy-wrap -->
<!-- end : main contents -->