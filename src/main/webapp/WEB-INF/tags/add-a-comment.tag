<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="item" type="org.learn.model.interfaces.Commentable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="votes" type="org.learn.model.CommentsAndVotes" required="true" %>
<%@attribute name="groupComments" type="java.lang.Boolean" required="false" %>
<%@attribute name="startFormHidden" type="java.lang.Boolean" required="false" %>

<c:if test="${empty groupComments}">
	<c:set var="groupComments" value="true" />
</c:if>
<c:if test="${empty startFormHidden}">
	<c:set var="startFormHidden" value="true" />
</c:if>

<div class="comments">

	<%-- <c:set value="${t['comment.submit']}" var="submit"/>
	<tags:simpleAjaxFormWith startHidden="${startFormHidden}" action="${linkTo[CommentController].comment(item.id, type, '', false)}" field="comment" 
	onCallback="append" callbackTarget="${ajaxResultName}" submit="${submit}">
		<a href="#" class="requires-login post-action add-comment requires-karma" data-karma="${CREATE_COMMENT}">
			${submit}
		</a>
	</tags:simpleAjaxFormWith> --%>
	
	<c:set var="ajaxResultName" value="new-comment-for-${type}-new-comment-${item.id}"/>
	<ul class="comment-list item-title-wrapper   ${empty item.getVisibleCommentsFor(currentUser.current) ? 'hidden' : ''}" id="${ajaxResultName}">
		<c:forEach var="comment" items="${item.getVisibleCommentsFor(currentUser.current)}" varStatus="status">
			<tags:commentWith type="${type}" item="${item}" comment="${comment}" collapsed="${status.count > 1 && groupComments}" currentUserVote="${votes.getVotes(comment)}"/>
		</c:forEach>
	</ul>
	
	<c:set var="commentsSize" value="${fn:length(item.getVisibleCommentsFor(currentUser.current))}"/>
	<c:if test="${commentsSize > 1  && groupComments}">
		<span class="more-comments" size="${commentsSize}">
			<c:set var="commentSizeBold" value="<strong>${commentsSize}</strong>"/>
			${t["comment.show_all"].args(commentSizeBold)}>
		</span>
	</c:if>
	
	<c:set value="${t['comment.submit']}" var="submit"/>
	<tags:simpleAjaxFormWith startHidden="${startFormHidden}" action="${linkTo[CommentController].comment(item.id, type, '', false)}" field="comment" 
	onCallback="append" callbackTarget="${ajaxResultName}" submit="${submit}"> &nbsp;
		<a href="#" class="post-action1 requires-login post-action add-comment requires-karma" data-karma="${CREATE_COMMENT}">
			${submit}
		</a>
	</tags:simpleAjaxFormWith>
</div>

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
       control.makeTransliteratable(['commentText']);
     }
     google.setOnLoadCallback(onLoad);
</script>