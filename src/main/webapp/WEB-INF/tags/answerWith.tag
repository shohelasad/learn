<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="answer" type="org.learn.model.Answer" required="true" %>
<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="vote" type="org.learn.model.Vote" required="true" %>
<%@attribute name="item" type="org.learn.model.interfaces.Votable" required="true" %>
<%@attribute name="commentVotes" type="org.learn.model.CommentsAndVotes" required="true" %>

<section class="post-area ${answer.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<%-- <div class="post-meta">
		<tags:voteFor item="${answer}" type="${answer.typeName}" vote="${vote}"/>
		<tags:solutionMarkFor answer="${answer}"/>
	</div> --%>
	<div class="vote-container post-container">
		<div class="answer-header">
			<tags:lastTouchFor touchable="${answer}"/>
			<c:if test="${currentUser.current.isAuthorOf(answer) or currentUser.moderator}">
				<div class="dropdown">
				  <div onclick="anwerwithFunction(${answer.id})" class="fa fa-angle-down fa-lg dropicon">							
				  </div> 
				  
				  <div  id="answerwith-${answer.id}"  class="dropdown-content" data-dropdown-content class="f-dropdown" style="padding-right:0px;">
					<a class="post-action1 edit-post1 requires-login requires-karma a fa fa-pencil"
							data-author="${currentUser.current.isAuthorOf(answer)}"
							data-karma="${EDIT_ANSWER}" 
							href="${linkTo[AnswerController].answerEditForm(answer)}"> ${t['edit']}</a>
	
					<c:if test="${env.supports('deletable.answers') and (currentUser.current.isAuthorOf(answer) or currentUser.moderator) and answer.deletable}">
						<a class="post-action delete-post fa fa-close" data-confirm-deletion="true" data-delete-form="delete-answer-form" href="#"> Delete</a>
						<form class="hidden delete-answer-form" method="post" action="${linkTo[AnswerController].delete(answer)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
		
					<%-- <c:if test="${currentUser.loggedIn && !answer.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(answer)}"
							data-modal-id="answer-flag-modal${answer.id}" 
							data-karma="${CREATE_FLAG}" class="post-action author-cant requires-login flag-it requires-karma fa fa-close">
							Hide
						</a>
					</c:if> --%>
					<tags:flagItFor type="${t['answer.type_name']}" modalId="answer-flag-modal${answer.id}" flaggable="${answer}"/>			
				  </div>
				</div> 
			</c:if>
		</div>
		<div style="width:100%;" class="post-text" id="answer-${answer.id }">	
			<div>${answer.markedDescription}</div>
		</div>

		<div class="post-interactions">
			<ul class="post-action-nav nav count-list">
				<li class="nav-item">
					<span class="button-like">
						<span class="fa fa-thumbs-up"></span>
					</span>
					<span class="vote-count">${answer.voteCount}</span>
					<i class="" aria-hidden="true"></i>
				</li>
				<%-- <li>
					<c:if test="${answer.solution && !currentUser.current.isAuthorOf(answer.question)}">
						<span class="icon-ok-circled icon-1x icon-muted1 container solution-tick"></span>
					</c:if>
				</li> --%>
			
			</ul>

			<ul class="post-action-nav nav piped-nav">
				<li class="nav-item">
					<a rel="nofollow" class="post-action requires-login requires-karma1 fa fa-thumbs-up author-cant1
					      up-vote1 up-arrow1 arrow1 vote-option 
					       ${(not empty vote and vote.countValue == 1) ? 'voted' : '' }"
					      data-value="positivo" data-author="${currentUser.current.isAuthorOf(item)}"
					      data-type="${type}"
					      data-karma="${VOTE_UP}"
					      data-id="${item.id}"
					      title="${t[titleUp]}"> Like
					</a>
				</li> 
				
				<li class="nav-item ${answer.solution ? 'solution-container' : 'not-solution-container'}">
					<c:if test="${currentUser.current.isAuthorOf(answer.question)}">
						<a class="mark-as-solution requires-login" href="${linkTo[AnswerController].markAsSolution(answer.id)}">
							<span class="icon-ok-circled icon-1x icon-muted container solution-tick"></span>
							<span class="post-action">${t['answer.mark_as_solution']}</span>
						</a>
					</c:if>
					<%-- <c:if test="${answer.solution && !currentUser.current.isAuthorOf(answer.question)}">
						<span class="icon-ok-circled icon-1x icon-muted container solution-tick"></span>
					</c:if> --%>
				</li>
				<tags:add-a-comment type="${t['answer.type_name']}" item="${answer}" votes="${commentVotes}"/>
				
			</ul>
			
			<%-- <tags:touchesFor touchable="${answer}" /> --%>
		</div>
		<%-- <tags:solutionMarkFor answer="${answer}"/> --%>
	
		<%-- <tags:add-a-comment type="${t['answer.type_name']}" item="${answer}" votes="${commentVotes}"/> --%>
		<c:if test="${currentUser.moderator && answer.hasPendingEdits()}">
			<a class="message moderator-alert" href="${linkTo[HistoryController].similarAnswers(answer.id)}">${t['answer.warns.has_edits']}</a>
		</c:if>
		
	</div>
</section>

<script>
	/* When the user clicks on the button, 
	toggle between hiding and showing the dropdown content */
	function anwerwithFunction(id) { 
	    document.getElementById("answerwith-" + id).classList.toggle("show");
	}
	
	// Close the dropdown if the user clicks outside of it
	window.onclick = function(event) {
	  if (!event.target.matches('.dropbtn') && !event.target.matches('.dropicon')) {
	
	    var dropdowns = document.getElementsByClassName("dropdown-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	    }
	  }
	}
</script>