<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.question_edit.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>
<div class="left-align">
<h2 class="title subheader page-title">পেজ আপডেট করুন </h2>

<div class="summary question-summary item-title-wrapper">
<form class="validated-form1 question-form hinted-form form-with-upload"
	  action='${linkTo[ChannelController].edit(channel,null,null,null,null)}' method="post">
	<label for="question-title">পেজ </label>
	<input id="channel-title" type="text" class="required hintable text-input"
		   value="<c:out value="${channel.title}" escapeXml="true"/>" data-hint-id="question-title-hint" minlength="1"
		   maxlength="150" name="title">
	
	<br/></br>
	<label for="channel-title">বিস্তারিত লিখুন  [optional]</label>
			   
	<tags:markDown value="${channel.description}" hintId="question-description-hint" htmlClass=""
				   minlength="0"/>

    <c:if test="${env.supports('feature.inhouse.upload')}">
        <tags:fileUploader attachmentsTarget="${channel}"/>
    </c:if>

	<label for="tags">${t['question.tags.label']}</label>
	<ul class="tags autocompleted-tags hidden" id="question-tags-autocomplete"></ul>


	<input id="tags" type="text" autocomplete="off" name="tagNames"
		   class="question-tags-input hintable autocomplete only-existent-tags text-input ${tagsRequired}"
		   value="${channel.getTagsAsString(environment.get('tags.splitter.char'))}" data-hint-id="question-tags-hint"
		   data-autocomplete-id="newquestion-tags-autocomplete"/>


	<%-- <label for="comment">${t['edit_form.comment.label']}</label> --%>
	<%-- <input type="text" name="comment"
		   class="hintable text-input required"
		   minlength="5" data-hint-id="question-comment-hint"
		   value="${editComment}"
		   placeholder="${t['edit_form.comment.placeholder']}"/> --%>
	<input class="post-submit big-submit" type="submit" value="${t['question.edit_form.submit']}"/>
</form>

<div class="form-hints">
	<%-- <div id="question-title-hint" class="hint">${t['question.title.hint']}</div> --%>
	<div id="question-description-hint" class="hint">${t['question.description.hint']}</div>
	<div id="question-tags-hint" class="hint">${t['question.tags.hint']}</div>
	<%-- <div id="question-comment-hint" class="hint">${t['edit_form.comment.hint']}</div> --%>
</div>
</div>
</div>
<%--<script src="/assets/js/deps/jquery.js"></script>--%>
<%--<script src="/assets/js/deps/select2.js"></script>--%>
<script type="text/javascript">
</script>