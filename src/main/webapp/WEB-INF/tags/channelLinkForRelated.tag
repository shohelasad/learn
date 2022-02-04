<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="channel" type="org.learn.model.Channel" required="false" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="channel-${channel.id}" class="title item-title channel-thread-title channel-title">
	<h2>
		<c:if test="${fn:length(channel.attachments) != 0}">	
			<div class="channel-image">
				<img class="channel-profile-image" src="${channel.attachments.iterator().next().getAttachmentId()}"/>
			</div> 
		</c:if>
		<a class="more1" href="${linkTo[ListController].withChannel(channel.information.url, 1, false)}">
			<c:out value="${channel.title}" escapeXml="${true}"/> 
		</a>
		<%-- <span style="float:right; font-size:10px; padding-right:5px;" class="btn1"
			 href="${linkTo[ChannelController].channelEditForm(channel)}">x
		</span>  --%> 
	</h2>
	<tags:followHide watchable="${channel}" type="${t['channel.type_name']}"/>
</div>


