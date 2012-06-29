 <%@ include file="/common/header.jsp" %>
 

 <c:set var="form" value="${widget}"/>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
<%-- 
    <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="profileRow">
                    <fmt:message key='fms.label.instruction'/>&nbsp;
                </td>
            </tr>
--%>            
            
     <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                    <fmt:message key='fms.label.personalInfo'/>&nbsp;
                </td>
            </tr>
       
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.firstName'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.fname.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.lastName'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.lname.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.department'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.sbDepartment.absoluteName}" ></x:display>
        </td>
    </tr>
    
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.unit'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.sbUnit.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.designation'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.designation.absoluteName}" ></x:display>
        </td>
    </tr>

    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.staffID'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.tfStaffNo.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.mobileNo'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.tfTelMobile.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.officeNoExt'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.tfTelOffice.absoluteName}"/>
        </td>
    </tr>
    <tr>
		<td colspan="2">&nbsp;</td>
	</tr>

    <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="classRowLabel">
                    <fmt:message key='fms.label.authenticationInfo'/>&nbsp;
                </td>
            </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.mediaPrimaEmailAdd'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.email.absoluteName}" ></x:display>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"></td>
        <td class="classRow">
            **&nbsp;<fmt:message key='fms.label.note'/>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.password'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.password.absoluteName}" ></x:display>
        </td>
    </tr>
    
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.confirmPassword'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.confirmPassword.absoluteName}" ></x:display>
        </td>
    </tr>   
        
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">
            <x:display name="${form.saveButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>



