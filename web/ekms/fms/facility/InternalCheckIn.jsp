<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.InternalCheckInForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= InternalCheckInForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= InternalCheckInForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_more" value="<%= InternalCheckInForm.FORWARD_MORE %>"/>

<x:config>
    <page name="CheckIn">
    	<com.tms.fms.facility.ui.InternalCheckInForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == forward_more}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");document.location = "<c:url value="InternalCheckIn.jsp?"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("<c:out value="${message}"/>");</script>
	</c:when>
</c:choose>

<x:set name="CheckIn.form" property="cancelUrl" value="index.jsp"/>
<x:set name="CheckIn.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>


<script>

function nextbox(fldobj,nbox) {
	if(fldobj.value.length>15) {
	document.getElementsByName(nbox)[0].focus()
	}
}

</script>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.internalCheckIn'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="CheckIn.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>