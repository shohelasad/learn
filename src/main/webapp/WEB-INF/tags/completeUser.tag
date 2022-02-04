<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="user" type="org.learn.model.User" required="true" %>
<%@attribute name="touchable" type="org.learn.model.interfaces.Touchable" required="true" %>
<%@attribute name="microdata" required="false" %>
<%@attribute name="edited" required="false" %>
<%@attribute name="showTime" type="java.lang.Boolean" required="false" %>
<%@attribute name="prettyFormat" type="java.lang.Boolean" required="false" %>

<c:if test="${empty showTime}">
	<c:set var="showTime" value="${true}" />
</c:if>

<c:if test="${empty prettyFormat}">
	<c:set var="prettyFormat" value="${true}" />
</c:if>

<div class="complete-user">
	<jsp:doBody/>
	<a href="${linkTo[UserProfileController].showProfile(user,user.sluggedName)}"><img border="0" class="user-image" src="${userMediumPhoto ? user.getMediumPhoto(env.get('gravatar.avatar.url')) : user.getSmallPhoto(env.get('gravatar.avatar.url'))}"/></a>
	<div class="user-info" 
		<c:if test="${microdata}">
			itemscope itemtype="http://schema.org/Person" itemprop="${edited ? 'editor' : 'author'}"
		</c:if> 
	>
		<tags:userProfileLink user="${user}" htmlClass="user-name ellipsis" microdata="${microdata}"/>
		<c:if test="${showTime}">
			<time class="when" ${microdata ? 'itemprop="dateCreated"' : ''} datetime="${touchable.createdAt}">${t['touch.created']}
	 			<c:if test="${prettyFormat}">
					<tags:prettyTime time="${touchable.createdAt}"/>
				</c:if>
				<c:if test="${not prettyFormat}">
					<fmt:formatDate value="${touchable.createdAt.toGregorianCalendar().time}" pattern="dd/MM/yyyy"/>
				</c:if>
			 </time>
		</c:if>
		
		
		<%-- <div title="${t['touch.karma.title']}" class="user-karma ellipsis">${user.karma}<tags:pluralize key="touch.karma" count="${user.karma}" /></div> --%>
	</div>
</div> 
