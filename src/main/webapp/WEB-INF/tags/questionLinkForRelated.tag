<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="question" type="org.learn.model.Question" required="false" %>
<%@ attribute name="answer" type="org.learn.model.Answer" required="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${empty answer and not empty question}">
	<div class="title question-title">
		<%-- <c:if test="${fn:length(question.attachments) != 0}">	
			<div class="channel-image">
				<img class="channel-profile-image" src="${question.attachments.iterator().next().getAttachmentId()}"/>
			</div> 
		</c:if> --%>
		<a href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
			<c:out value="${question.title}" escapeXml="${true}"/>	
		</a>
	</div>
</c:if>
<c:if test="${empty question and not empty answer}">
	<c:if test="${fn:length(question.attachments) != 0}">	
		<div class="channel-image">
			<img class="question-profile-image" src="${question.attachments.iterator().next().getAttachmentId()}"/>
		</div> 
	</c:if>
	<a class="more1" href="${linkTo[QuestionController].showQuestion(answer.question,answer.question.sluggedTitle)}#answer-${answer.id}">
		<c:out value="${answer.question.title}" escapeXml="${true}"/>
	</a>
</c:if>
