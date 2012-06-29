<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.cms.leave.ManageLeaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="leaveType">
          <com.tms.hr.leave.ui.LeaveTypeTable name="table" width="100%"/>
    </page>
</x:config>

<c:choose>
    <c:when test="${param.cn == 'leaveType.table' && !empty param.id}">
        <c:redirect url="leaveTypeSetupEdit.jsp?id=${param.id}"/>
    </c:when>
    <c:when test="${forward.name == 'add'}">
        <c:redirect url="leaveTypeSetupAdd.jsp"/>
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='leave.label.Administration'/> > <fmt:message key='leave.label.leaveTypes'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="leaveType.table" ></x:display></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
