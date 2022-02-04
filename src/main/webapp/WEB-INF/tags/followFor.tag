<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="watchable" type="org.learn.model.interfaces.Watchable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>


<a id="${watchable.id}" rel="nofollow" class="follow requires-login" href="${linkTo[WatchController].watch(watchable.id,type)}">
	<c:choose>
		<c:when test="${isFollowing}">
			<!-- <span class="icon-eye icon-2x container" 
			      title="Follow">
			</span> -->
			<span class="button-follow" style="float:right;font-size:12px;">Following</span>
		</c:when>
		<c:otherwise>
			<!-- <span class="icon-eye-off icon-muted icon-2x container" 
			      title="Following">
			</span> -->
			<span class="button-following" style="float:right;font-size:12px;">Follow</span>
		</c:otherwise>
	</c:choose>
</a>

