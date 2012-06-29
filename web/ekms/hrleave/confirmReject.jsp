<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.Leaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>

<x:config>
    <page name="approveLeaveReject">
          <com.tms.hr.leave.ui.confirmReject name="form"/>
    </page>
</x:config>





 <c:if test="${!empty param.userId}">

    <x:set name="approveLeaveReject.form" property="userId" value="${param.userId}" ></x:set>

</c:if>



 <c:if test="${!empty param.id}">

    <x:set name="approveLeaveReject.form" property="id" value="${param.id}" ></x:set>

</c:if>

           <%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='leave.label.viewLeave'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">



            <x:display name="approveLeaveReject.form" />

    <x:display name="approveLeaveEntry.form" />
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
