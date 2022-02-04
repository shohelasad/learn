<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<c:set var="siteName" value="${t['site.name']}"/>
<c:set var="title" value="${t['metas.home.title']}"/>
<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>
<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<%-- <c:set var="title" value="${t['menu.questions']}"/> --%>
<%-- <c:set var="title" value=""/> --%>
<%-- <c:set var="title" value="সর্বশেষ"/> --%>
<c:set var="channel" value="${channel}"/>

<c:if test="${!currentUser.loggedIn}">
	 <section class="banner-position about-banner">
		<!-- <span class="minimize-banner icon-minus"></span> -->
		<div style="width:50%;" class="about-content tell-me-more">
			<h3 class="about-title title">
				স্বাগতম জিজ্ঞাসা!
			</h3>
			<div class="about-text">
				বাংলা ভাষায় প্রশ্ন উত্তর সম্পর্কিত তথ্য সেবা
				<%-- <a href="${linkTo[NavigationController].about}">
					বিস্তারিত জানুন 
				</a> --%>
			</div>
			<div>
				<a href="${linkTo[NavigationController].about}">
					বিস্তারিত জানুন 
				</a>
			</div>
		</div>
		<div style="width:50%; padding-left: 1em;" class="about-content how-it-works">
			<ul>
				<tags:howItWorksItem icon="icon-comment" key="about.home_banner.how_it_works.anyone_ask"/>
				<tags:howItWorksItem icon="icon-chat-empty" key="about.home_banner.how_it_works.anyone_answer"/>
			</ul> 
		</div>
	</section>
</c:if>

<section class="first-content content">
	<tags:questionForm uri="${linkTo[QuestionController].newQuestion}" />
	<tags:questionList recentTags="${recentTags}" questions="${questions}" channel="${channel}" tabs="${tabs}"/>
</section>
<tags:joyrideIntro />

<tags:sideBar recentTags="${recentTags}" />
<%-- <tags:adBar recentTags="${recentTags}" /> --%>


<%-- <section class="first-content content">
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" title="${title}" tabs="${tabs}"/>
	<tags:questionList recentTags="${recentTags}" 
		questions="${questions}" channel="${channel}" tabs="${tabs}"/>
</section>

<tags:sideBar recentTags="${recentTags}" />
<tags:joyrideIntro /> --%>


 

