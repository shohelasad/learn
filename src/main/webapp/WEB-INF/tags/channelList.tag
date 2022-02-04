<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="title" type="java.lang.String" required="false"%>
<%@attribute name="channel" type="org.learn.model.Channel" required="false"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="channels" type="java.util.List" required="true"%>
<%@attribute name="tag" type="org.learn.model.Tag" required="false"%>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="tabs" type="java.util.List" required="false"%>

<%-- <div>
	<a href="${linkTo[ChannelController].newChannel}">
		<span class="btn btn-shadow">Create Page</span>
	</a>
</div> --%>


<%-- <tags:rssTagHeader unansweredTagLinks="${unansweredTagLinks}" tag="${tag}" title ="${title}" showTabs="${not empty tabs}"/> --%>

<tags:rssTagHeader unansweredTagLinks="${unansweredTagLinks}" tag="${tag}" channel="${channel}" showTabs="${not empty tabs}"/>
<c:if test="${not empty tag}">
	<tags:tagTabs tag="${tag}" hasAbout="${hasAbout}"/>
</c:if>

<c:if test="${not empty channels}">
	<ol class="question-list">
		<c:forEach var="channel" items="${channels}">
			<tags:channelListItem channel="${channel}"/>
			<%-- <a class="trend-button more1" href="${page.information.url}">${page.information.title}  </a> --%>
			
		</c:forEach>
	</ol>
</c:if>
<c:if test="${empty channels}">
	<h2 class="title section-title">${t['pages.empty_list']}</h2>
</c:if>
<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>

