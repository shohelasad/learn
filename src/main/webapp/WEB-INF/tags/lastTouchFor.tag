<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="touchable" type="org.learn.model.interfaces.Touchable" required="true" %>
<%@attribute name="showTime" type="java.lang.Boolean" required="false" %>
<%@attribute name="prettyFormat" type="java.lang.Boolean" required="false" %>

<c:if test="${empty showTime}">
	<c:set var="showTime" value="${true}" />
</c:if>
<c:if test="${empty prettyFormat}">
	<c:set var="prettyFormat" value="${true}" />
</c:if>

<div class="last-touch">
	<c:choose>
		<c:when test="${touchable.edited}">
			<tags:editedTouch touchable="${touchable}" showTime="${showTime}" prettyFormat="${prettyFormat}"/>		
		</c:when>
		<c:otherwise>
			<tags:createdTouch touchable="${touchable}" showTime="${showTime}"  prettyFormat="${prettyFormat}"/>
		</c:otherwise>
	</c:choose>
</div>

			<%-- <div class="dropdown" style="float:right;">
 				<span class="dropbtn">v</span>
 					<div class="dropdown-content">
					<ul>
						<li class="nav-item">
							<a class="post-action edit requires-login requires-karma a fa fa-edit"
									data-author="${currentUser.current.isAuthorOf(answer)}"
									data-karma="${EDIT_ANSWER}" 
									href="${linkTo[AnswerController].answerEditForm(answer)}"> ${t['edit']}</a>
						</li>
						<li class="nav-item">
							<c:if test="${env.supports('deletable.answers') and (currentUser.current.isAuthorOf(answer) or currentUser.moderator) and answer.deletable}">
								<a class="post-action delete-post" data-confirm-deletion="true" data-delete-form="delete-answer-form" href="#">Delete</a>
								<form class="hidden delete-answer-form" method="post" action="${linkTo[AnswerController].delete(answer)}">
									<input type="hidden" value="DELETE" name="_method">
								</form>
							</c:if>
						</li>
						<li class="nav-item">
							<c:if test="${currentUser.loggedIn && !answer.alreadyFlaggedBy(currentUser.current)}">
								<a href="#" data-author="${currentUser.current.isAuthorOf(answer)}"
									data-modal-id="answer-flag-modal${answer.id}" 
									data-karma="${CREATE_FLAG}" class="post-action author-cant requires-login flag-it requires-karma fa fa-flag">
									${t['flag']}
								</a>
							</c:if>
							<tags:flagItFor type="${t['answer.type_name']}" modalId="answer-flag-modal${answer.id}" flaggable="${answer}"/>
						</li>
					
					</ul>
				</div>
			</div> --%>
	