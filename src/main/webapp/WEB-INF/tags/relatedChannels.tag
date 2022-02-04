<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="channels" type="java.util.List" required="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${fn:length(channels) != 0}">	
	<div class="summary question-summary"> 
		<div class="item-title-wrapper1">
			<ol class="related-channels1">
				<c:forEach items="${channels}" var="channel">
					<li><tags:channelLinkForRelated channel="${channel}"/></li>
				</c:forEach>
			</ol>
		</div>
	</div>
</c:if>