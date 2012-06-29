<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.Leaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>

<x:config>
    <page name="viewApproveList">
          <com.tms.hr.leave.ui.ApproveLeaveEntryTable name="table" width="100%" />
          <com.tms.hr.leave.ui.ApproveLeaveEntryTable name="table2" width="100%" credit="true" />
    </page>
</x:config>


<c:choose>
    <c:when test="${param.cn == 'viewApproveList.table' && !empty param.id}">
        <c:redirect url="approveLeaveEntry.jsp?entryId=${param.id}"/>
    </c:when>
    <c:when test="${param.cn == 'viewApproveList.table2' && !empty param.id}">
        <c:redirect url="approveLeaveEntry.jsp?entryId=${param.id}"/>
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <a href="viewLeaveSummary.jsp" style="text-decoration: none;"><b><font color="#FFCF63" class="contentTitleFont"> <fmt:message key='leave.label.LeaveApp'/> > <fmt:message key='leave.label.approveLeave'/></font></b></a>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="viewApproveList.table" ></x:display>
         <fmt:message key='leave.label.approve.balance.leave'/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>

</table>

<p>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <a href="viewLeaveSummary.jsp" style="text-decoration: none;"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='leave.label.LeaveApp'/> >  <fmt:message key='leave.label.approveCreditLeave'/></font></b></a>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="viewApproveList.table2" ></x:display>
         <fmt:message key='leave.label.approve.balance.credit.leave'/>

    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
