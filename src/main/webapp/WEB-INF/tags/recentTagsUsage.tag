<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="tagsUsage" type="java.util.List" required="true" %>

<div class="summary question-summary item-title-wrapper"> 
	<%-- <h3 class="title page-title">${t['tags.recent']}</h3> --%>
	<!-- <h3 class="title item-title main-thread-title question-title">সম্পর্কিত ট্যাগ </h3> -->
	<div class="tags-title">সাম্প্রতিক ট্রেন্ড  </div>
	<ol class="tags-usage">
		<c:forEach items="${tagsUsage}" var="tagUsage">
			<li class="tags-item"><tags:tag tag="${tagUsage.tag}"/> x ${tagUsage.usage} post</li>
			<%-- <tags:tag tag="${tagUsage.tag}"/> --%>
		</c:forEach>
	</ol>
</div> 
