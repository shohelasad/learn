<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="messagesList" type="java.util.List" required="true" %>


<div style="margin-top:1em;" class="left-align">
	<c:if test="${not empty messagesList}">
		<ul class="subheader">
			<!-- <div class="subheader"> -->
			<c:forEach var="message" items="${messagesList}">
				<li class="subheader1 message ${message.category}">
				    ${message.message}
	            </li>
			</c:forEach>
			<!--  </div> -->
		</ul>
	</c:if>
</div>
