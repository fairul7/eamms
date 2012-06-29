<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.CheckAvailabilityForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_check" value="<%= CheckAvailabilityForm.FORWARD_CHECK %>"/>
<c-rt:set var="forward_empty" value="<%= CheckAvailabilityForm.FORWARD_EMPTY %>"/>

<x:config>
    <page name="CheckAvailability">
    	<com.tms.fms.facility.ui.CheckAvailabilityForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == forward_empty}">
		<script>alert("Please select at least one facility/ equipment.");</script>
	</c:when>
</c:choose>

<x:set name="CheckAvailability.form" property="cancelUrl" value="index.jsp"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<c:choose>
	<c:when test="${forward.name == forward_check}">
		<form name="check" action="CheckAvailabilityTable.jsp" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['check'].submit();
		</script>	
	</c:when>
</c:choose>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.checkAvailability'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="CheckAvailability.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>