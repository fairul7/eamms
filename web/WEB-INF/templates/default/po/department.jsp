<%@include file="/common/header.jsp" %>
<x:config>
	<page name="departmentList">
		<com.tms.sam.po.ui.DepartmentListing name="departmentList"/>
	</page>
</x:config>

<c:set var="w" value="${widget}" />
<table width="100%" border="0" cellspacing="1" cellpadding="5">
	<jsp:include page="/WEB-INF/templates/ekms/form_header.jsp" />
 	<tr valign="middle">
		<td height="22" class="contentTitleFont" colspan="2">
			<fmt:message key="po.label.prePurchase"/> > <fmt:message key="po.label.addNewDepartment"/>
		</td>
	</tr>
    <tr>
        <td class="contentBgColor" width="20%" valign="top" align="left"><b>
            <fmt:message key='po.label.department'/>*
        </b></td>
        <td class="contentBgColor" width="80%" valign="top">
             <x:display name="${w.txtDpt.absoluteName}" />
        </td>
    </tr>
    <tr>
        <td class="contentBgColor" width="20%" valign="top" align="left"><b>
           	<fmt:message key='department.label.code'/>*
        </b></td>
        <td class="contentBgColor" width="80%" valign="top">
            <x:display name="${w.txtDptCode.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="contentBgColor" width="20%" valign="top" align="left"><b>
           	<fmt:message key='department.label.hod'/>*
          </b></td>
          <td class="contentBgColor" width="80%" valign="top">
    	    <x:display name="${w.txtHOD.absoluteName}" />
          </td>
    </tr>
    <tr>
          <td class="contentBgColor"></td>
          <td class="contentBgColor" width="15%" valign="top" align="left"><b>
             <x:display name="${w.buttonPanel.absoluteName}" />
          </b></td>
    </tr>
    	      
    <jsp:include page="/WEB-INF/templates/ekms/form_footer.jsp" />
</table>

<p></p>

<table width="100%" border="0" cellspacing="1" cellpadding="5">
	
	<tr valign="middle">
		<td height="22" class="contentTitleFont" colspan="2" >
			 <fmt:message key='department.label.view'/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor" width="25%" valign="top" align="left" colspan="2">
             <x:display name="departmentList.departmentList"/>
        </td>
	</tr>
		
</table>