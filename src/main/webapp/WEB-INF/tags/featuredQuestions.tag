<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="questions" type="java.util.List" required="true" %>

<div class="summary-rightnav question-summary1"> 
<!-- <div class="subheader"> -->
	<%-- <h3 class="title page-title">${t['questions.related']}</h3> --%>
	<!-- <h3 class="title item-title main-thread-title question-title">ফিচার</h3> -->
	<div class="questions-title">
		<span>সর্বশেষ প্রশ্ন </span> 
	</div>
	<div class="item-title-wrapper">
		<ol class="featured-questions">
			<c:forEach items="${questions}" var="question">
				<li>
					<table><tr>
					<!-- <td>&nbsp;</td> -->
					<td> <span class="ans-count ${question.mark}">${question.answersCount}</span></td>
					<%-- <td> <span class="question-mark-${question.mark}">${question.answersCount}</span> &nbsp; </td> --%>
					<td style="width:100%;"><tags:questionLinkForRelated question="${question}"/>
					</tr></table>
				</li>
			</c:forEach>
		</ol>
		<span class="see-all">
			<a href="${linkTo[ListController].unanswered}">আরও দেখুন  </a>
		</span>
	</div>
</div>