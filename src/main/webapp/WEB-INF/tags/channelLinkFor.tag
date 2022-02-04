<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="channel" type="org.learn.model.Channel" required="false" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="title item-title channel-thread-title question-title">
	<h2>
		<c:if test="${fn:length(channel.attachments) != 0}">	
			<div class="channel-image">
				<img class="channel-profile-image" src="${channel.attachments.iterator().next().getAttachmentId()}"/>
			</div> 
		</c:if>
		<a class="more1" href="${linkTo[ListController].withChannel(channel.information.url, 1, false)}">
			<c:out value="${channel.title}" escapeXml="${true}"/> 
		</a>
	</h2>
	<div style="float:left;">
		<tags:followFor watchable="${channel}" type="${t['channel.type_name']}"/>
	</div>
</div>

<%-- <a style="float:left; font-size:12px; padding-right:10px;" class="btn1" href="${linkTo[ChannelController].channelEditForm(channel)}">Edit 
</a> --%>


			<ul class="post-action-nav piped-nav nav">	
				<li class="nav-item">
					<a class="post-action edit-question 
					    requires-login requires-karma"
					    data-author="${currentUser.current.isAuthorOf(channel)}"
					    data-karma="${EDIT_CHANNEL}" 
					    href="${linkTo[ChannelController].channelEditForm(channel)}">
						${t['edit']}
					</a>
				</li>
				<%-- <li class="nav-item">
					<c:if test="${currentUser.loggedIn && !question.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(question)}" data-karma="${CREATE_FLAG}"
							data-modal-id="question-flag-modal${question.id}" 
							class="post-action author-cant requires-login flag-it requires-karma">
							${t['flag']}
						</a>
					</c:if>
					<tags:flagItFor type="${t['question.type_name']}" modalId="question-flag-modal${question.id}" flaggable="${question}"/>
				</li> --%>
				<%-- <c:if test="${env.supports('deletable.questions')}">
					<c:if test="${currentUser.current.isAuthorOf(question) and not currentUser.moderator and question.deletable}">
						<li class="nav-item">
							<a href="#" class="delete-post" data-confirm-deletion="true" data-delete-form="delete-question-form">${t['question.delete']}</a>
						</li>
						<form class="hidden delete-question-form" method="post" action="${linkTo[QuestionController].deleteQuestion(question)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
					<c:if test="${currentUser.moderator}">
						<li class="nav-item">
							<a href="#" class="delete-post" data-delete-form="delete-question-fully-form" data-confirm-deletion="true">
								${t['question.delete.fully']}
							</a>
						</li>
						<form class="hidden delete-question-fully-form" method="post" action="${linkTo[QuestionController].deleteQuestionFully(question)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
				</c:if> --%>
				
			</ul>
