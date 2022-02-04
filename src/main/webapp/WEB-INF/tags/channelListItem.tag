<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="channel" type="org.learn.model.Channel" required="true" %>
<%@attribute name="simple" type="java.lang.Boolean" required="false" %>


<c:if test="${simple == null}">
	<c:set var="simple" value="false"/>
</c:if>
 
<li class="post-item question-item">

	<div class="summary question-summary">
		<div class="item-title-wrapper">	

			<tags:channelLinkFor channel="${channel}"/>
		
			<div class="post-information-x question-information">
					<c:set scope="request" value="${channel}" var="currentQuestion"/>
			</div> 
		</div>
	</div>
</li>


