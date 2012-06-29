<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.collab.rss.ui.RssListTable"%>

<c:if test="${!empty param.channelId}">
	<c:redirect url="RssXml.jsp?channelId=${param.channelId}"/>
</c:if>

<c:if test="${forward.name == 'back'}">
    <c:redirect url="rss/channelListing.jsp" />
</c:if>

<x:config>
	<page name="setting">
		<com.tms.collab.rss.ui.RssListTable name="RssList"/>
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%">
  <tr valign="top">
  	<td class="contentTitleFont" style="padding:5px;"><fmt:message key="rss.channel.rss"/> > <fmt:message key="rss.channel.rssListing"/></td>
  </tr>
  <tr>
    <td class="contentBgColor">
		<x:display name="setting.RssList" />
	</td>
  </tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>

