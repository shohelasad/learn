<tags:header facebookMetas="${true}" 
	title="${question.mostImportantTag.name} - ${question.title}" 
	description="${question.metaDescription}"/>

<div class="left-align">
<tags:detailsList votable="${question}"/>
<c:forEach items="${question.answers}" var="answer">
	</br>
	<tags:detailsList votable="${answer}"/>
</c:forEach>
</div>
