<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="field" type="java.lang.String" required="true"%>
<%@attribute name="value" required="false"%>
<%@attribute name="callbackTarget" type="java.lang.String"
	required="true"%>
<%@attribute name="action" type="java.lang.String" required="true"%>
<%@attribute name="submit" type="java.lang.String" required="true"%>
<%@attribute name="onCallback" type="java.lang.String" required="false"%>
<%@attribute name="startHidden" type="java.lang.Boolean"
	required="false"%>

<c:if test="${empty startHidden}">
	<c:set var="startHidden" value="${true}" />
</c:if>

<div class="simple-ajax-form">
	<jsp:doBody />
	<div class="ajax-form ${startHidden? 'hidden' : '' }">
		<form method="post" action="${action}" class="validated-form ajax"
			data-ajax-on-callback="${onCallback}"
			data-ajax-result="${callbackTarget}">
			<textarea id="commentText" class="comment-textarea required to-focus hintable text-input"
				minlength="15" maxlength="600" name="${field}"
				data-hint-id="${callbackTarget}-hint"><c:out
					value="${value}" escapeXml="true" /></textarea>
	
			<input class="comment-post-submit submit" type="submit" value="Save" />
			
			<button class="comment-post-submit  submit cancel">
				${t['cancel_button']}
			</button>	
			
			<%-- <div class="comment-remaining-characters">${t['comment.remaining_characters']}<span class="comment-length-counter"> 600</span></div> --%>
			<%-- <tags:checkbox-watch /> --%>
			
		</form>

	</div>
	
</div>


