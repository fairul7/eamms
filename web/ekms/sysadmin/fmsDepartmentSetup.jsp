 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="departmentSetup">
        <com.tms.fms.department.ui.FMSDepartmentForm name="departmentform"/>
     </page>
</x:config>


<c:if test="${forward.name == 'Back to Dept List'}" > 
  <c:redirect url="fmsDepartmentList.jsp"/> 
</c:if>

  <%@include file="/ekms/includes/header.jsp" %>
  
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">

  <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='general.label.systemAdministration'/> > <fmt:message key='fms.label.departmentSetup'/>  
      
    </td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
	<x:display name="departmentSetup" ></x:display>
</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>
