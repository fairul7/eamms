<%@include file="/common/header.jsp" %>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.AttendantRequestListingTable name="attendantRequestListing"/>
    </page>
</x:config>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.requestListing"/></td>
	</tr>
	<tr>
		<td class="contentBgColor"><x:display name="isr.attendantRequestListing" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>