<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.Leaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>

<x:config>
    <page name="viewLeaveEntry">
          <com.tms.hr.leave.ui.ViewLeaveEntryForm name="form"/>
    </page>
</x:config>

<c:choose>
    <c:when test="${forward.name == 'approve'}">
        <script>
            alert('<fmt:message key="leave.label.leaveApproved"/>');
            document.location = "<c:url value="/ekms/hrleave/viewLeaveEntryList.jsp" />";
        </script>
    </c:when>
    <c:when test="${forward.name == 'reject'}">
        <script>
            alert('<fmt:message key="leave.label.leaveRejected"/>');
            document.location = "<c:url value="/ekms/hrleave/viewLeaveEntryList.jsp" />";
        </script>
    </c:when>
    <c:when test="${forward.name == 'cancel'}">
        <script>
            alert('<fmt:message key="leave.label.leaveCancelled"/>');
            document.location = "<c:url value="/ekms/hrleave/viewLeaveEntryList.jsp" />";
        </script>
    </c:when>
    <c:when test="${forward.name == 'cancel_form_action'}">
        <c:redirect url="index.jsp"/>
    </c:when>
</c:choose>

<c:choose>
    <c:when test="${!empty param.entryId}">
        <x:set name="viewLeaveEntry.form" property="id" value="${param.entryId}" />
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='leave.label.LeaveSummary'/> > <fmt:message key='leave.label.viewLeave'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <%--display form--%>
        <x:display name="viewLeaveEntry.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
