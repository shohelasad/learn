<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="question" type="org.learn.model.Question" required="true" %>
<%@attribute name="simple" type="java.lang.Boolean" required="false" %>


<c:if test="${simple == null}">
	<c:set var="simple" value="false"/>
</c:if>
 
<li class="post-item question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<%-- <div class="post-information question-information">
		<c:set scope="request" value="${question}" var="currentQuestion"/>
		<tags:brutal-include value="questionStats" />
	</div> --%>
	
	<div class="summary question-summary">
		<div class="item-title-wrapper">	
			  
			<h3 class="title item-title main-thread-title">
				<tags:questionLinkFor question="${question}"/>
			</h3>
			<div class="answer-header">
				<c:if test="${question.solution.information.answer.markedDescription.length() > 0}">
					<c:if test="${!simple}">
						<tags:lastTouchFor touchable="${question.solution.information.answer}"/>
				    </c:if>
				</c:if>
			</div>
			<c:choose>
				<c:when test="${fn:length(question.solution.information.answer.attachments) != 0 && fn:length(question.solution.information.answer.markedDescription) > 0}">		
					<div style="text-align: left;" class="post-image" id="answer-${question.solution.information.answer.id }">
						<div class="question-image1">
							<img class="answer-profile-image" src="${question.solution.information.answer.attachments.iterator().next().getAttachmentId()}"/>
						</div> 
						<a class="mobile-view more" href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
							<c:if test="${question.solution.information.answer.markedDescription.length() > 200}">
						 	  	${question.solution.information.answer.getTrimmedContent().substring(0, 200)}... <span class="continue"> আরও পড়ুন </span>
							</c:if>	 
							<c:if test="${question.solution.information.answer.markedDescription.length() <= 200}">
								${question.solution.information.answer.getTrimmedContent()}
							</c:if>
						</a>
						<a class="web-view more" href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
							<c:if test="${question.solution.information.answer.markedDescription.length() > 200}">
						 	  	${question.solution.information.answer.getTrimmedContent().substring(0, 200)}... <span class="continue"> আরও পড়ুন </span>
							</c:if>	 
							<c:if test="${question.solution.information.answer.markedDescription.length() <= 200}">
								${question.solution.information.answer.getTrimmedContent()}
							</c:if>
						</a>
					</div>
				</c:when>
				
				<c:otherwise>
					<div style="text-align: left;" class="post-answer" id="answer-${question.solution.information.answer.id }">
						<a class="mobile-view more" href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}"> 
							<c:if test="${question.solution.information.answer.markedDescription.length() > 200}">
						 		${question.solution.information.answer.markedDescription.substring(0, 200)}...<span class="continue"> আরও পড়ুন </span>
							</c:if>	
							<c:if test="${question.solution.information.answer.markedDescription.length() <= 200}">
								${question.solution.information.answer.markedDescription}
							</c:if>

						</a>
						<a class="web-view more" href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}"> 
							<c:if test="${question.solution.information.answer.markedDescription.length() > 200}">
						 		${question.solution.information.answer.markedDescription.substring(0, 200)}...<span class="continue">  আরও পড়ুন </span>
							</c:if>	
							<c:if test="${question.solution.information.answer.markedDescription.length() <= 200}">
								${question.solution.information.answer.markedDescription}
							</c:if>
						</a>
					</div>
				</c:otherwise>
			</c:choose>
			
			<%-- <c:if test="${!simple}">
				<tags:tagsFor taggable="${question}"/>
			</c:if>	 --%>

			<%-- <div class="post-information-x question-information">
				<c:set scope="request" value="${question}" var="currentQuestion"/>
				<tags:brutal-include value="questionStats" />
			</div>  --%>
		</div>
		<%-- <c:if test="${question.solution.information.answer.markedDescription.length() > 0}">
			<c:if test="${!simple}">
				<tags:lastTouchFor touchable="${question}"/>
		    </c:if>
		</c:if> --%>
		<div class="post-information-x question-information">
				<c:set scope="request" value="${question}" var="currentQuestion"/>
				<tags:brutal-include value="questionStats" />
		</div> 
	</div>
	<%-- <c:if test="${!simple}">
		<div class="${question.hasInteraction(currentUser.current) ? 'interaction' : ''}" title="${t['user.interactions']}"> </div>
	</c:if> --%>
</li>


