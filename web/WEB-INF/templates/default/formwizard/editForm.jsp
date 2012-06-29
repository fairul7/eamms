<%@ page import="kacang.Application,
                 com.tms.collab.formwizard.model.FormModule"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c-rt:set var="memo" value="<%= Application.getInstance().getProperty(FormModule.PROPERTY_MEMO) %>"/>
<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp" flush="true"/>
<table>

<script>
<!--
function toggleFormLayer(layerId, displayType) {
	document.getElementById(layerId).style.display = displayType;
}
    
function toggleLayers(field){

	var formName = '<c:out value="${form.absoluteName}"/>';
	var form = document.forms[formName];
	var radioButtonName = formName + '.<c:out value="${form.publicFormAccess.groupName}"/>';
	var approvalRequireButtonName = formName + '.<c:out value="${form.workFlowYes.groupName}"/>';
	var saveDBButtonName = formName + '.<c:out value="${form.yesDB.groupName}"/>';
    var formAccessUsersButtonName = formName + '.<c:out value="${form.publicFormUserAccess.groupName}"/>';

	if(field=='privateform'){
    	toggleFormLayer('formAccessUsers','block');
        toggleFormLayer('queryUsers','block');
        toggleFormLayer('workflow','block');

        if (form[approvalRequireButtonName][1].checked)
	        toggleFormLayer('approveUsers','none');
	    else
	        toggleFormLayer('approveUsers','block');

        if (form[formAccessUsersButtonName][1].checked)
            toggleFormLayer('sbAccessUsers','block');
        else
            toggleFormLayer('sbAccessUsers','none');

        if (form[formAccessUsersButtonName][2].checked)
            toggleFormLayer('groupFormAccess','block');
        else
            toggleFormLayer('groupFormAccess','none');



    }
    else if (field=='publicform'){
        toggleFormLayer('formAccessUsers','none');
        toggleFormLayer('sbAccessUsers','none');
        toggleFormLayer('groupFormAccess','none');


        if (form[saveDBButtonName][0].checked)
  	        toggleFormLayer('queryUsers','block');
        else
	        toggleFormLayer('queryUsers','none');

        toggleFormLayer('workflow','none');
        toggleFormLayer('approveUsers','none');
    }
    else if (field=='privateFormAccess'){
        toggleFormLayer('sbAccessUsers','block');
        toggleFormLayer('groupFormAccess','none');
    }
    else if (field=='publicFormAccess'){
        toggleFormLayer('groupFormAccess','none');
        toggleFormLayer('sbAccessUsers','none');
    }
    else if(field=='groupFormAccess'){
        toggleFormLayer('groupFormAccess','block');
        toggleFormLayer('sbAccessUsers','none');
    }
    else if(field=='yesDB'){
	    toggleFormLayer('queryUsers','block');
           toggleFormLayer('workflow','block');
        if (form[approvalRequireButtonName][1].checked)
            toggleFormLayer('approveUsers','none');
        else
            toggleFormLayer('approveUsers','block');

    }
    else if(field=='noDB'){
        toggleFormLayer('queryUsers','none');
        toggleFormLayer('workflow','none');
        toggleFormLayer('approveUsers','none');
    }
    else if (field == 'yesApproval') {
    	toggleFormLayer('approveUsers','block');
    }
    else if (field == 'noApproval') {
    	toggleFormLayer('approveUsers','none');
    }
}

function editFields(){
    document.location = '<c:url value="frwEditFormField.jsp?formId=" /><c:out value="${form.formId}"/>';
	window.open('<c:url value="frwEditPreviewForm.jsp"/>?formId=<c:out value="${form.formId}"/>',
                'preview','scrollbars=yes,resizable=yes,status=yes,width=700,height=500,location=yes')
}

//-->
</script>
	
<%--
<c:if test="${form.childMap.publicFormAccess.checked}">
	<c:set var="formAccessUsers" value="none"/>
	<c:set var="formUserAccess" value="none"/>
	<c:set var="formGroupAccess" value="none"/>
	<c:set var="queryUsers" value="block"/>
	<c:set var="workflow" value="none"/>
	<c:set var="approveUsers" value="none"/>
</c:if>
--%>

<%--<c:if test="${form.childMap.privateFormAccess.checked}">--%>
	<c:set var="formAccessUsers" value="block"/>
	<c:set var="queryUsers" value="block"/>		
	<c:set var="workflow" value="block"/>
	<c:choose>
		<c:when test="${form.childMap.privateFormUserAccess.checked}">
			<c:set var="formUserAccess" value="block"/>
		</c:when>
		<c:otherwise>
			<c:set var="formUserAccess" value="none"/>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${form.childMap.groupFormUserAccess.checked}">
			<c:set var="formGroupAccess" value="block"/>
		</c:when>
		<c:otherwise>
			<c:set var="formGroupAccess" value="none"/>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${form.workFlowYes.checked}">
			<c:set var="approveUsers" value="block"/>
		</c:when>
		<c:otherwise>
			<c:set var="approveUsers" value="none"/>
		</c:otherwise>
	</c:choose>
<%--</c:if>--%>

<c:choose>
	<c:when test="${form.childMap.noDB.checked}">
		<c:set var="queryUsers" value="none"/>		
		<c:set var="workflow" value="none"/>
		<c:set var="approveUsers" value="none"/>
	</c:when>
	<c:otherwise>
		<c:set var="queryUsers" value="block"/>
	</c:otherwise>
</c:choose>



<tr>
    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formName'/></B></td>
    <td>
    <x:display name="${form.formNameLabel.absoluteName}"/>
    </td>
</tr>

<tr>
<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formHeader'/> *&nbsp;</B></td>
<td>
<x:display name="${form.formHeader.absoluteName}"/>
</td>
</tr>
<tr>
    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.submissionMessage'/> *&nbsp;</B></td>
    <td align="left">
    <x:display name="${form.submissionMessage.absoluteName}"/>
    </td>
</tr>

<%--<tr>
	<td valign="top" align="right" width="30%"><B>Form Access Type</B></td>
	<td>
		<x:display name="${form.publicFormAccess.absoluteName}"/> public &nbsp;&nbsp;
		<x:display name="${form.privateFormAccess.absoluteName}"/> private
	</td>
</tr>--%>

<tr>
	<td colspan="2">
        <div id="formAccessUsers" style='display:<c:out value="${formAccessUsers}"/>'>
		   <table width="100%">
			   <tr>
			       <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formUserType'/></B></td>
				   <td>
					   <x:display name="${form.publicFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.public'/> &nbsp;&nbsp;
					   <x:display name="${form.privateFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.private'/> &nbsp;
					   <x:display name="${form.groupFormUserAccess.absoluteName}"/>  <fmt:message key='formWizard.label.group'/>
				   </td>
			   </tr>
		   </table>
	    </div>
	</td>
</tr>

<tr>
	<td colspan="2">
    	<div id="sbAccessUsers" style='display:<c:out value="${formUserAccess}"/>'>
        	<table width="100%">
            	<tr>
                	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.users'/> *</B></td>
                    <td>
						<x:display name="${form.sbAccessUsers.absoluteName}"/>
						<c:out value ="${form.sbAccessUsers.message}"/>
					</td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
   	<td colspan="2">
    	<div id="groupFormAccess" style='display:<c:out value="${formGroupAccess}"/>'>
		     <table width="100%">
        		<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formGroup'/></B></td>
		        <td align="left">
        			<x:display name="${form.cbGroups.absoluteName}"/>
					<c:out value ="${form.cbGroups.message}"/>
		        </td>
	       </table>
    	</div>
   </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.saveToDatabase'/></B></td>
	<td align="left">
		<x:display name="${form.yesDB.absoluteName}"/><fmt:message key='formWizard.label.yes'/>  &nbsp;&nbsp;
		<x:display name="${form.noDB.absoluteName}"/><fmt:message key='formWizard.label.no'/>
	</td>
</tr>

<tr>
    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.emailTo'/></B></td>
    <td align="left">
	    <x:display name="${form.emailTo.absoluteName}"/>
	    <br><fmt:message key='formWizard.label.separateEmails'/>
    </td>
</tr>

<c:if test="${memo == 'true'}">
<tr>
    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.messageViaIntranet'/></B></td>
    <td align="left">
    	 <x:display name="${form.sbMemoTo.absoluteName}"/>
		  <c:out value ="${form.noDB.message}"/>
    </td>
</tr>
</c:if>

<tr>
	<td colspan="2">
    	<div id="queryUsers" style='display:<c:out value="${queryUsers}"/>'>
        	<table width="100%">
            	<tr>
                	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.allowQueryBy'/>&nbsp; *</B></td>
                    <td align="left">
						<x:display name="${form.sbQueryBy.absoluteName}"/>
						<c:out value ="${form.sbQueryBy.message}"/>
					</td>
                </tr>
            </table>
        </div>
	</td>
</tr>

<tr>
	<td colspan="2">
    	<div id="workflow" style='display:<c:out value="${workflow}"/>'>
      	   <table width="100%">
		    	<tr>
		            <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formUserType.approvalRequired'/></B></td>
        		    <td align="left">
			            <x:display name="${form.workFlowYes.absoluteName}"/> <fmt:message key='formWizard.label.formUserType.yes'/> &nbsp;&nbsp;
			            <x:display name="${form.workFlowNo.absoluteName}"/><fmt:message key='formWizard.label.formUserType.no'/>
            		</td>
		       </tr>
	       </table>
        </div>
	</td>
</tr>

<tr>
    <td colspan="2">
        <div id="approveUsers" style='display:<c:out value="${approveUsers}"/>'>
        	<table width="100%">
            	<tr>
                	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.approveBy'/></B><br><font color = "red"><fmt:message key='formWizard.label.approvalCycle'/></font></td>
	                <td align="left">
						<x:display name="${form.sbApprover.absoluteName}"/>
						<c:out value ="${form.sbApprover.message}"/>
	                </td>
	            </tr>

	            <c:if test="${memo == 'true'}">
	            <tr>
    		        <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.approvalNotificationMethod'/></B></td>
		            <td align="left">
         			   <x:display name="${form.memoApproveNotification.absoluteName}"/> <fmt:message key='formWizard.label.approval.messageViaIntranet'/> &nbsp;&nbsp;
                        <x:display name="${form.emailApproveNotification.absoluteName}"/> <fmt:message key='formWizard.label.Email'/>
		            </td>
        	    </tr>
                </c:if>
	        </table>
        </div>
    </td>
</tr>


<tr>
	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.linkToAnotherForm'/><B></td>
	<td align="left">
		<x:display name="${form.sbForms.absoluteName}"/>
	</td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.tableColumn'/> *<B></td>
	<td align="left">
		<x:display name="${form.tfTableColumn.absoluteName}"/>
        <c:out value ="${form.tfTableColumn.message}"/>
	</td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.active'/><B></td>
	<td align="left">
		<x:display name="${form.activeCheckbox.absoluteName}"/>
	</td>
</tr>

 <tr>
<td valign="top" align="right" width="30%">  <B><fmt:message key='formWizard.label.formFields'/></B></td>
<td align="left">
<a href="javascript:editFields()"><fmt:message key='formWizard.label.editFields'/></a></td>
</tr>
<tr>
	<td valign="top"  align="center" colspan = "2">
		<x:display name="${form.submit.absoluteName}"/>
		<x:display name="${form.reset.absoluteName}"/>
	</td>
</tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
