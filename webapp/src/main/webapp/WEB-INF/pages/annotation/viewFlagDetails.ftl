<#--
  $HeadURL::                                                                            $
  $Id$
  
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
<html>
	<head>
		<title>Flag details</title>
	</head>

	<body>

    <fieldset>
        <legend>Flag details</legend>

          id          =${flag.id?c}            <br/>
          annotates   =${flag.annotates}     <br/>
          body        =${flag.comment}       <br/>
          bodyWithUrlLinking        =${flag.commentWithUrlLinking}          <br/>
          created     =${flag.created}       <br/>
          creator     =${flag.creator}       <br/>
          mediator    =${flag.mediator}      <br/>
          type        =${flag.type}          <br/>

          <@s.url id="createReplyURL" action="createReplySubmit" root="${flag.id?c}" inReplyTo="${flag.id?c}" namespace="/annotation/secure"/>
          <@s.a href="%{createReplyURL}">create reply</@s.a> <br/>

          <@s.url id="listReplyURL" action="listAllReplies" root="${flag.id?c}" inReplyTo="${flag.id?c}"/>
          <@s.a href="%{listReplyURL}">list all replies</@s.a> <br/>

    </fieldset>

  </body>
</html>