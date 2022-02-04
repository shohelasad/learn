<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="relatedQuestions" type="java.util.List" required="false"%>
<%@attribute name="featuredQuestions" type="java.util.List" required="false"%>
<%@attribute name="relatedChannels" type="java.util.List" required="false"%>

<aside id="sidebar" class="sidebar">
	<c:set var="newses" value="${sidebarNews}" scope="request" />
	<tags:brutal-include value="homeNewsList" />
	<%-- <tags:brutal-include value="sideBarAd" /> --%>
	<c:if test="${isTagVisible}">
		<div>
			<tags:recentTagsUsage tagsUsage="${recentTags}"/>
		</div>
	</c:if>
	<tags:feed rssUrl="${env.get('jobs.url')}" rssFeed="${jobs}" rssType="jobs"/>
	<tags:feed rssUrl="${env.get('infoq.url')}" rssFeed="${infoq}" rssType="infoq"/>
	<c:if test="${relatedQuestions != null }">
		<tags:relatedQuestions questions="${relatedQuestions}"/>
	</c:if>
	<c:if test="${relatedChannels != null }">
		<tags:relatedChannels channels="${relatedChannels}"/>
	</c:if>
	<tags:brutal-include value="sideBarAd" />
</aside>

<script type="text/javascript">	
	var wrap3 = $("#sidebar1"); //TODO: fix add block
	$(window).scroll(function(){ 
      if($(window).scrollTop() > 40){ 
	  	wrap3.addClass("fix-header-sidenav");
	  } else {
		wrap3.removeClass("fix-header-sidenav");
      }
  	});
</script>
