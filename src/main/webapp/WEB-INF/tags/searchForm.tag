<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${env.supports('feature.solr') || env.supports('feature.google_search')}">

	<c:if test="${env.supports('feature.solr')}">
		<c:set var="linkToSearch" value="${linkTo[SolrSearchController].search()}"/>
	</c:if>
	<c:if test="${env.supports('feature.google_search')}">
		<c:set var="linkToSearch" value="${linkTo[GoogleSearchController].search()}"/>
	</c:if>
	
	<form class="search-form" action="${linkToSearch}" method="get">
		<input id="languageTextarea" class="text-input" name="query" placeholder="${t['search.placeholder']}" type="text" />
		<input type="hidden" value="bn" id="languageOptions">
		<button class="submit search-icon sprite" type="submit" value="Submit" >Go</button> 
	</form>
</c:if>

<!-- <script type="text/javascript" src="/js/jsapi.js"></script> -->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
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
       control.makeTransliteratable(['languageTextarea']);
     }
     google.setOnLoadCallback(onLoad);
</script>
