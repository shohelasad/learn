<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="question" type="org.learn.model.Question" required="true" %>
<%@attribute name="commentVotes" type="org.learn.model.CommentsAndVotes" required="true" %>

<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="vote" type="org.learn.model.Vote" required="true" %>
<%@attribute name="item" type="org.learn.model.interfaces.Votable" required="true" %>


<section itemscope itemtype="http://schema.org/Article" class="summary question-summary item-title-wrapper ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post1' : '' }" >
	<div itemprop="name" class="title main-thread-title question-title"><c:out value="${question.title}" escapeXml="${true}"/>
		<div style="font-size:.8em; float:right; padding-right:.5em">
			<c:if test="${currentUser.current.isAuthorOf(question) or currentUser.moderator}">
				<div class="dropdown">
				  <span onclick="questionwithFunction()" class="fa fa-angle-down fa-lg dropicon">							
				  </span> 
				  
				  <div  id="questionwith"  class="dropdown-content" data-dropdown-content class="f-dropdown" style="padding-right:0px;">
			
						<a class="post-action1 edit-question1 requires-login requires-karma fa fa-pencil"
						    data-author="${currentUser.current.isAuthorOf(question)}"
						    data-karma="${EDIT_QUESTION}" 
						    href="${linkTo[QuestionController].questionEditForm(question)}">
							Edit
						</a>
	
						<%-- <c:if test="${currentUser.loggedIn && !question.alreadyFlaggedBy(currentUser.current)}">
							<a href="#" data-author="${currentUser.current.isAuthorOf(question)}" data-karma="${CREATE_FLAG}"
								data-modal-id="question-flag-modal${question.id}" 
								class="post-action1 author-cant requires-login flag-it requires-karma fa fa-close">
								Hide
							</a>
						</c:if>
						<tags:flagItFor type="${t['question.type_name']}" modalId="question-flag-modal${question.id}" flaggable="${question}"/> --%>
	
						<c:if test="${env.supports('deletable.questions')}">
							<c:if test="${currentUser.current.isAuthorOf(question) and not currentUser.moderator and question.deletable}">
								<li class="nav-item">
									<a href="#" class="delete-post1 fa fa-close" data-confirm-deletion="true" data-delete-form="delete-question-form">${t['question.delete']}</a>
								</li>
								<form class="hidden delete-question-form" method="post" action="${linkTo[QuestionController].deleteQuestion(question)}">
									<input type="hidden" value="DELETE" name="_method">
								</form>
							</c:if>
							<c:if test="${currentUser.moderator}">
								<a href="#" class="delete-post1 fa fa-close" data-delete-form="delete-question-fully-form" data-confirm-deletion="true">
									Delete
								</a>
								<form class="hidden delete-question-fully-form" method="post" action="${linkTo[QuestionController].deleteQuestionFully(question)}">
									<input type="hidden" value="DELETE" name="_method">
								</form>
							</c:if>
						</c:if>
							
						
				  </div> 
				</div> 
			</c:if>
		</div>
	</div>
	<div class="tags-banner"><tags:tagsFor taggable="${question}"/></div>
	
	<c:if test="${shouldShowAds && !markAsSolution && !showUpvoteBanner}">
		<tags:brutal-include value="questionTopAd" />
	</c:if>
	<%-- <div class="post-meta">
		<tags:voteFor item="${question}" type="${question.typeName}" vote="${currentVote}"/>
		<tags:watchFor watchable="${question}" type="${t['question.type_name']}"/>
	</div> --%>
	<div class="vote-container post-container">
	
		<div itemprop="articleBody" class="post-text question-description" id="question-description-${question.id }">
			${question.markedDescription}
		</div>
		<%-- <tags:tagsFor taggable="${question}"/> --%>

		<div class="post-interactions">					
			<ul class="post-action-nav piped-nav nav">
				<li class="nav-item1">
					<a class="button-share post-action show-popup fa fa-share" href="#">
						${t['share']}
					</a>
					<div class="popup share small">
						<form class="validated-form">
							<label for="share-url">${t['share.text']}</label>
							<input type="text" class="text-input required" id="share-url" value="${currentUrl}"/>
						</form>
						<a target="_blank" class="share-button" 
							data-shareurl="http://www.facebook.com/sharer/sharer.php?u=${currentUrl}">
							<i class="icon-facebook-squared icon-almost-3x"></i>
						</a>
						<a target="_blank" class="share-button" 
							data-shareurl="https://twitter.com/share?text=<c:out value="${question.title}" escapeXml="true" />&url=${currentUrl}">
							<i class="icon-twitter-squared icon-almost-3x"></i>
						</a>
						<a target="_blank" class="share-button" 
							data-shareurl="https://plus.google.com/share?&url=${currentUrl}">
							<i class="icon-gplus-squared icon-almost-3x"></i>
						</a>
						<a class="close-popup">${t['popup.close']}</a>
					</div>
				</li>
				
				<!-- <li> -->
					<%-- <c:if test="${currentUser.moderator}">
						<a class="message moderator-link" href="${linkTo[QuestionController].showVoteInformation(question, question.sluggedTitle)}">${t['user.moderation.details']}</a><br/>
					</c:if> --%>
				<!-- </li> -->
				
				<%-- <tags:add-a-comment type="${t['question.type_name']}" item="${question}" votes="${commentVotes}"/> --%>
			</ul>
			
			
			<%-- <tags:touchesFor touchable="${question}" microdata="true"/> --%>
		</div>
		
		<c:if test="${currentUser.moderator && question.hasPendingEdits()}">
			<a class="message moderator-alert" href="${linkTo[HistoryController].similarQuestions(question.id)}">${t['question.warns.has_edits']}</a>
		</c:if>
	</div>
</section>
<%-- <ol id="intro">
	<tags:joyrideTip className="post-meta" options="tipLocation:right" key="intro.question.post_meta" />
	<tags:joyrideTip className="edit-question" options="tipLocation:bottom" key="intro.question.edit_question" />
	<tags:joyrideTip className="add-comment" options="tipLocation:bottom" key="intro.question.add_comment" />
	<tags:joyrideTip className="solution-tick" options="tipLocation:right" key="intro.question.solution_mark" />
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about" />
</ol> --%>

<script>
	/* When the user clicks on the button, 
	toggle between hiding and showing the dropdown content */
	function questionwithFunction() {
	    document.getElementById("questionwith").classList.toggle("show");
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
