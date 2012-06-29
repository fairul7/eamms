<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.CheckAvailabilityDetailForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Check">
    	<com.tms.fms.facility.ui.CheckAvailabilityDetailForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${not empty(paramValues.selectedKeys)}">
		<c:set var="selectedKeys" value="${paramValues.selectedKeys}"/>
	</c:when>
	<c:otherwise>
		<c:set var="selectedKeys" value="${widgets['Check.form'].facility_id}"/>
	</c:otherwise>
</c:choose>

<x:set name="Check.form" property="facility_id" value="${selectedKeys}"/>
<x:set name="Check.form" property="cancelUrl" value="CheckAvailability.jsp"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.availabilityOfFacilityEquipment'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Check.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>