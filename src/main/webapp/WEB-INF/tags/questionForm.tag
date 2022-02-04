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
			  <span>
			  	<a href="${linkTo[UserProfileController].showProfile(currentUser.current,currentUser.current.sluggedName)}"><img border="0" class="user-image" src="${userMediumPhoto ? currentUser.current.getMediumPhoto(env.get('gravatar.avatar.url')) : currentUser.current.getSmallPhoto(env.get('gravatar.avatar.url'))}"/></a>
			  	<%-- <img class="user-image complete-user" src="${currentUser.current.getSmallPhoto(env.get('gravatar.avatar.url'))}"/> --%>  							
			  </span> 
			
			
			<form class="validated-form1 question-form form-with-upload  asked-form" action='${uri}' method="post" autocomplete="off">
				<%-- <label for="question-title">${t['question.title.label']}</label> --%>
				<textarea id="question-title" class="asked-text-input question-title-input"
					   value="${question.title}" data-hint-id="question-title-hint" minlength="15" maxlength="150"
					   name="title" placeholder="কী জানতে চান?"></textarea>
				<input type="hidden" value="bn" id="languageOptions">
				<input class="post-submit big-submit" type="submit" value="Ask"/>
			</form>
			</div> 
			<%-- <c:if test="${env.supports('feature.solr')}">
				<div id="question-suggestions" class="hidden">
					<h2 class="title section-title">${t['question.similars']}</h2>
					<ul class="suggested-questions-list"></ul>
				</div>
			</c:if> --%>
		</div>
	</div>
	<c:if test="${env.supports('feature.solr')}">
		<div id="question-suggestions" class="hidden">
			<h2 class="title section-title">সম্পর্কিত প্রশ্ন </h2>
			<ul class="suggested-questions-list"></ul>
		</div>
	</c:if>
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