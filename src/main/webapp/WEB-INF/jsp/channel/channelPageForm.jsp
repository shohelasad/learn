<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.page.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="left-align">
<tags:channelPageForm uri="${linkTo[ChannelController].newChannelPage}" />
</div>

<style>

select {
   font-size: 12px;
   padding-bottom:5px; 
}

</style>