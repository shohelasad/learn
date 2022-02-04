<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="title" type="java.lang.String" required="false"%>
<%@attribute name="channel" type="org.learn.model.Channel" required="false"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="questions" type="java.util.List" required="true"%>
<%@attribute name="tag" type="org.learn.model.Tag" required="false"%>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="tabs" type="java.util.List" required="false"%>

<%-- <tags:questionForm uri="${linkTo[QuestionController].newQuestion}" /> --%>

<%-- <tags:rssTagHeader unansweredTagLinks="${unansweredTagLinks}" tag="${tag}" 
					channel="${channel}" showTabs="${not empty tabs}"/> --%>
					
<c:if test="${not empty tag}">
	<tags:tagTabs tag="${tag}" hasAbout="${hasAbout}"/>
</c:if>

<div id="contents">
	<c:if test="${not empty questions}">
		<ol class="question-list">
			<c:forEach var="question" items="${questions}">
				<tags:questionListItem question="${question}"/>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty questions}">
		<h2 class="title section-title">${t['questions.empty_list']}</h2>
	</c:if>
</div>

<div id="content"></div>

<a id="inifiniteLoader">Loading... <img src="/assets/imgs/loading.gif"/></a>


<%-- <tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/> --%>


<script src="/assets/js/deps/jquery.js"></script>

<script type="text/javascript">
   var count = 2;
   $(window).scroll(function(){ 
      if($(window).scrollTop() == $(document).height() - $(window).height()){ 
    	 if(count > 0) {
         	loadArticle(count);
         	count++;
    	 } 
      }
   }); 

   function loadArticle(pageNumber){    
	   //$('a#inifiniteLoader').show('fast');
       $.ajax({
           url: "http://localhost:8080/?p=" + pageNumber,
           type:'GET',
           //type:'POST',
           //data: "action=infinite_scroll&page_no="+ pageNumber + '&loop_file=loop', 
           success: function(html){
        	  //$('a#inifiniteLoader').hide('1000');
        	  var contents = $(html).find("#contents").html();
              $("#content").append(contents);   // This will be the div where our content will be loaded
           },
	       error: function (status) {
	    	  count = 0; 
	    	  $('a#inifiniteLoader').hide('0'); 
	       }
       });
       
       return false;
   }
</script>