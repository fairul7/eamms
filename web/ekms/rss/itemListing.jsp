<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.collab.rss.ui.ItemListForm"%>

<c-rt:set var="forwardClose" value="<%=ItemListForm.FORWARD_CLOSE %>" />

<x:config>
	<page name="setting">
		<com.tms.collab.rss.ui.ItemListForm name="ListItem"/>
    	<com.tms.collab.rss.ui.ChannelEditForm name="EditItem"/>
	</page>
</x:config>

<c:if test="${forward.name eq forwardClose}">
    <script>
    	window.close();
    </script>
</c:if>
<c:if test="${!empty param.channelId}">
	<x:set name="channels.EditForm" property="channelId" value="${param.channelId}" />
</c:if>

<link rel="stylesheet" href="/ekms/images/ekp2005/default.css">
<table  cellpadding="4" cellspacing="1" width="100%">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="rss.channel.rss"/> > <fmt:message key="rss.channel.listItem"/></td>
	</tr>
	<tr class="contentBgColor">
		<td>
			<x:display name="setting.ListItem" />
		</td>
	</tr>
</table>
