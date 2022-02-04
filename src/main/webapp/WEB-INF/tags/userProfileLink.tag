<%-- <%@ tag language="java" pageEncoding="US-ASCII"%> --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="user" type="org.learn.model.User" required="true" %>
<%@attribute name="value" type="java.lang.String" required="false" %>
<%@attribute name="htmlClass" type="java.lang.String" required="false" %>
<%@attribute name="before" type="java.lang.String" required="false" %>
<%@attribute name="after" type="java.lang.String" required="false" %>
<%@attribute name="microdata" required="false" %>
<%@attribute type="org.learn.model.Tag" name="tag" required="false"%>

<a class="${htmlClass}" ${microdata ? 'itemprop="name"' : ''} href="${linkTo[UserProfileController].showProfile(user,user.sluggedName)}">
	${before}
	<c:choose>
		<c:when test="${not empty value}">
			${t[value]}
		</c:when>
		<c:otherwise>
			${user.name}
		</c:otherwise>
	</c:choose>
	${after} 
</a>
