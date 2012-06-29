<%@ include file="/common/header.jsp" %>
<%--<x:permission permission="com.tms.cms.leave.ManageLeaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>--%>
<x:config>
    <page name="leavePage">
        <com.tms.hr.leave.ui.HolidaySetupPanel name="holidays"/>
       <%-- <com.tms.hr.leave.ui.HolidaySetupPanelByYear name="holidays"/>  --%>

    </page>
</x:config>
<c:set var ="form">

<c:choose>
<c:when test="${forward.name == 'requiredManualErase'}" >
<script>
alert('<fmt:message key='leave.error.manualerrorholiday'/>');
</script>
</c:when>
</c:choose>



<x:display name="leavePage.holidays"/>
</c:set>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
        	&nbsp;<fmt:message key='leave.label.Holidays'/> >  <c:out value="${widgets['leavePage.holidays'].title}"/>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${form}" escapeXml="false" /></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
