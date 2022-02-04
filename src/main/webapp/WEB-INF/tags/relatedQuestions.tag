<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="questions" type="java.util.List" required="true" %>

<div class="summary1 question-summary1"> 
<!-- <div class="subheader"> -->
	<%-- <h3 class="title page-title">${t['questions.related']}</h3> --%>
	<h3 class="related-question-title">সম্পর্কিত প্রশ্ন </h3>
	<div class="item-title-wrapper">
		<ol class="related-questions">
			<c:forEach items="${questions}" var="question">
				<%-- <li>
					<table><tr>
					<td> <span class="question-mark">${question.answersCount}</span> &nbsp; </td>
					<td style="width:100%;"><tags:questionLinkFor question="${question}"/></td>
					</tr></table>
				</li> --%>
				<li>
					<table><tr>
					<!-- <td>&nbsp;</td> -->
					<td> <span class="ans-count">${question.answersCount}</span></td>
					<%-- <td> <span class="question-mark-${question.mark}">${question.answersCount}</span> &nbsp; </td> --%>
					<td style="width:100%;"><tags:questionLinkForRelated question="${question}"/>
					</tr></table>
				</li>
			</c:forEach>
			<%-- <li>
				<a class="unanswered ${not empty unansweredActive ? 'current' : ''}" href="${linkTo[ListController].unanswered}">
					<span class="see-more" style="float:right;">সব প্রশ্ন  </span>
				</a>
			</li> --%>
		</ol>
	</div>
</div>