<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%-- <c:set var="tagsRequired" value="${env.supports('feature.tags.mandatory') ? 'required' : ''}"/> --%>
<c:if test="${currentUser.loggedIn}">
	<div class="summary question-summary">
		<div class="item-title-wrapper">	
			 <div class="complete-user-ask">
			  	<%-- <span>
			  		<a href="${linkTo[UserProfileController].showProfile(currentUser.current,currentUser.current.sluggedName)}"><img border="0" class="user-image" src="${userMediumPhoto ? currentUser.current.getMediumPhoto(env.get('gravatar.avatar.url')) : currentUser.current.getSmallPhoto(env.get('gravatar.avatar.url'))}"/></a>
			  		<img class="user-image complete-user" src="${currentUser.current.getSmallPhoto(env.get('gravatar.avatar.url'))}"/>  							
			  	</span> --%> 
		
				<form class="validated-form1 question-form form-with-upload  asked-form" action='${uri}' method="post" autocomplete="off">
					<%-- <label for="question-title">মেনু পেজ </label>		
					<div class="styled-select">		
					<select name="parent">
						  <!-- <option value="">Please Select</option> -->
				          <c:forEach var="item" items="${pages}">
				            <option value="${item.id}">${item.title}</option>
				          </c:forEach>
				    </select> 
				    </div>
				    <br/> --%>
					  
					<label for="question-title">পেজ নাম </label>
					<input id="question-title" type="text" class="required hintable text-input"
						   value="<c:out value="${page.title}" escapeXml="true"/>" data-hint-id="question-title-hint" minlength="1"
						   maxlength="150" name="title">
					
					<label for="question-title">পেজ URL </label>
					<input id="question-title" type="text" class="required hintable text-input"
						   value="<c:out value="${page.url}" escapeXml="true"/>" data-hint-id="question-title-hint" minlength="1"
						   maxlength="150" name="url">
						   
					<br/></br>
					<label for="question-title">বিস্তারিত লিখুন  [optional]</label>
							   
					<tags:markDown value="${page.description}" hintId="question-description-hint" htmlClass=""
								   minlength="0"/>
				
				    <c:if test="${env.supports('feature.inhouse.upload')}">
				        <tags:fileUploader attachmentsTarget="${page}"/>
				    </c:if>
				
					<label for="tags">${t['question.tags.label']}</label>
					<ul class="tags autocompleted-tags hidden" id="question-tags-autocomplete"></ul>
				
				
					<input id="tags" type="text" autocomplete="off" name="tagNames"
						   class="question-tags-input hintable autocomplete only-existent-tags text-input ${tagsRequired}"
						   value="${question.getTagsAsString(environment.get('tags.splitter.char'))}" data-hint-id="question-tags-hint"
						   data-autocomplete-id="newquestion-tags-autocomplete"/>
						   
					<input class="post-submit big-submit" type="submit" value="Save"/>

				</form>
			</div> 
		</div>
	</div>
</c:if>
  
<script type="text/javascript" src="/js/jsapi.js"></script>
<script type="text/javascript">

     // Load the Google Transliterate API
     google.load("elements", "1", {
           packages: "transliteration"
         });

     function onLoad() {
       var options = {
           sourceLanguage:
               google.elements.transliteration.LanguageCode.ENGLISH,
           destinationLanguage:
               [google.elements.transliteration.LanguageCode.BENGALI],
           shortcutKey: 'ctrl+g',
           transliterationEnabled: true
       };

       // Create an instance on TransliterationControl with the required
       // options.
       var control =
           new google.elements.transliteration.TransliterationControl(options);

       // Enable transliteration in the textbox with id
       // 'transliterateTextarea'.
       control.makeTransliteratable(['question-title']);
     }
     google.setOnLoadCallback(onLoad);
</script>
 
<%-- <ol class="${currentUser.current.karma <= 0 ? 'automatically-joyride' : ''}" id="intro">
	<tags:joyrideTip className="question-title-input" options="tipLocation:bottom" key="intro.new_question.title"/>
	<tags:joyrideTip className="description-input" options="tipLocation:bottom" key="intro.new_question.description"/>
	<tags:joyrideTip className="question-tags-input" options="tipLocation:bottom" key="intro.new_question.tags"/>
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about"/>
</ol> --%>