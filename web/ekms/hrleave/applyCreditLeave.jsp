<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.Leaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>

<x:config>
    <page name="applyCreditLeave">
          <com.tms.hr.leave.ui.ApplyLeaveForm name="form" credit="true"/>
    </page>
</x:config>

<x:set name="applyCreditLeave.form" property="showButton" value="false"/>

<c:choose>
    <c:when test="${forward.name == 'success'}">
        <script>
            alert('<fmt:message key="leave.label.creditLeaveApplied"/>');
            document.location = "<c:url value="/ekms/hrleave/index.jsp" />";
        </script>
    </c:when>
    <c:when test="${forward.name == 'cancel_form_action'}">
        <c:redirect url="index.jsp"/>
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='leave.label.LeaveApp'/> > <fmt:message key='leave.label.ApplyCreditLeave'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="applyCreditLeave.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
