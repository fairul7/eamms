<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.collab.rss.ui.ChannelListTable"%>

<x:permission permission="com.tms.collab.rss.managerRss" module="com.tms.collab.rss.model.RssHandler" url="rssNoPermission.jsp">
<x:config>
	<page name="setting">
		<com.tms.collab.rss.ui.ChannelListTable name="ListChannel"/>
	</page>
</x:config>

<c:if test="${!empty param.channelId}">
	<c:redirect url="channelEdit.jsp?channelId=${param.channelId}"/>
</c:if>
<c:if test="${forward.name == 'add'}">
    <c:redirect url="channelAdd.jsp" />
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
	 <tr valign="top">
	 	<td class="contentTitleFont" style="padding:5px;"><fmt:message key="rss.channel.rss"/> > <fmt:message key="rss.channel.channelListing"/></td>
	 </tr>
	 <tr>
	    <td class="contentBgColor">
			<x:display name="setting.ListChannel" />
	 	</td>
	 </tr>
</table>
</x:permission>
<%@ include file="/ekms/includes/footer.jsp" %>
