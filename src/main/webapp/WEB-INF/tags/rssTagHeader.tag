<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute type="org.learn.model.Tag" name="tag" required="false"%>
<%-- <%@attribute type="java.lang.String" name="title" required="false"%> --%>
<%@attribute name="channel" type="org.learn.model.Channel" required="false"%>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="showTabs" type="java.lang.Boolean" required="false"%>

<c:if test="${empty unansweredTagLinks}">
	<c:set value="${false}" var="unansweredTagLinks" />
</c:if>


<div class="rss-subheader">
	<c:if test="${unansweredTagLinks}">
		<tags:brutal-include value="headerTagsWithNoAnswer"/>
	</c:if>
	<c:if test="${not unansweredTagLinks}">
		<tags:brutal-include value="headerTags"/>
	</c:if>

	<c:if test="${not empty channel}">
		<%-- <c:if test="${empty tabs}"> --%>
			<h2 class="nav-item tab-button">${channel.title}</h2>
			<div style="float:right;">
				<tags:followFor watchable="${channel}" type="${t['channel.type_name']}"/>
			</div>
		<%-- </c:if> --%>
	</c:if>
	<c:if test="${not empty tabs}">
		<tags:tabs title="${title}" useSubheader="${false}">
			<c:forEach var="tab" items="${tabs}">
				<c:set var="tabText" value="menu.top.${tab}"/>
				<a href="${linkTo[ListController].top(tab)}">${t[tabText]}</a>
			</c:forEach>
		</tags:tabs>
	</c:if>
</div> 
