<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="relatedQuestions" type="java.util.List" required="false"%>
<%@attribute name="featuredQuestions" type="java.util.List" required="false"%>
<%@attribute name="relatedChannels" type="java.util.List" required="false"%>

<div id="rightnav" class="rightnav"  style="top:75px;"> 
<!-- <div class="rightnav">  -->
	<div id="container1">
	    <div id="container2">
			<c:if test="${featuredQuestions != null }">
				<tags:featuredQuestions questions="${featuredQuestions}"/>
			</c:if> 
		</div>
	</div>
</div>

<script type="text/javascript">	
	var wrap2 = $("#rightnav");
	$(window).scroll(function(){ 
      if($(window).scrollTop() > 40){ 
	  	wrap2.addClass("fix-header-rightnav");
	  } else {
		wrap2.removeClass("fix-header-rightnav");
      }
  	});
</script>