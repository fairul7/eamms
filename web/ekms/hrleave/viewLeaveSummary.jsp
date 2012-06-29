<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.Leaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms"/>

<x:config>
    <page name="viewLeaveSummary">
             <com.tms.hr.leave.ui.LeaveSummaryByYear name="table" width="100%" />
    </page>
</x:config>
 
 <c:if test="${!empty param.leaveType}">
 <script>
   window.location="viewLeaveEntryList.jsp?leaveType=<c:out value="${param.leaveType}"/>&year=<c:out value="${widgets['viewLeaveSummary.table'].selected_year}"/>";
 </script>
 </c:if>

<c:choose>
    <c:when test="${param.cn == 'viewLeaveSummary.table' && !empty param.leaveType}">
        <c:redirect url="viewLeaveEntryList.jsp?leaveType=${param.leaveType}"/>
    </c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><fmt:message key='leave.label.LeaveApp'/> > <a href="viewLeaveSummary.jsp" style="text-decoration: none;"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='leave.label.LeaveSummary'/></font></b></a></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="viewLeaveSummary.table" ></x:display>

        <p>
        <fmt:message key="leave.label.takenNote"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
