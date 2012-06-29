<%@ page import="kacang.Application,
                 com.tms.collab.formwizard.model.FormModule"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c-rt:set var="memo" value="<%= Application.getInstance().getProperty(FormModule.PROPERTY_MEMO) %>"/>

<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp" flush="true"/>

<%--<c:if test="${form.childMap.publicFormAccess.checked}">
	<c:set var="formAccessUsers" value="none"/>
	<c:set var="formUserAccess" value="none"/>
	<c:set var="formGroupAccess" value="none"/>
	<c:set var="queryUsers" value="block"/>
	<c:set var="workflow" value="none"/>
	<c:set var="approveUsers" value="none"/>
</c:if>--%>

<%--<c:if test="${form.childMap.privateFormAccess.checked}">--%>
	<c:set var="formAccessUsers" value="block"/>
	<c:set var="formAccessUsers2" value="block"/>
	<c:set var="queryUsers" value="block"/>		
	<c:set var="queryUsers2" value="block"/>
	<c:set var="workflow" value="block"/>
	<c:set var="workflow2" value="block"/>
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
			<c:set var="approveUsers2" value="block"/>
			<c:set var="approveUsers3" value="block"/>
			<c:set var="approveUsers4" value="block"/>
		</c:when>
		<c:otherwise>
			<c:set var="approveUsers" value="none"/>
			<c:set var="approveUsers2" value="none" />
			<c:set var="approveUsers3" value="none" />
			<c:set var="approveUsers4" value="none" />
		</c:otherwise>
	</c:choose>
<%--</c:if>--%>

<c:choose>
	<c:when test="${form.childMap.noDB.checked}">
		<c:set var="queryUsers" value="none"/>
		<c:set var="queryUsers2" value="none" />		
		<c:set var="workflow" value="none"/>
		<c:set var="workflow2" value="none" />
		<c:set var="approveUsers" value="none"/>
		<c:set var="approveUsers2" value="none" />
		<c:set var="approveUsers3" value="none" />
		<c:set var="approveUsers4" value="none" />
	</c:when>
	<c:otherwise>
		<c:set var="queryUsers" value="block"/>
		<c:set var="queryUsers2" value="block"/>
	</c:otherwise>
</c:choose>

<table width="100%">

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


	if (field=='publicform'){
    	toggleFormLayer('formAccessUsers','none');
    	toggleFormLayer('formAccessUsers2', 'none');
        toggleFormLayer('groupFormAccess','none');
        toggleFOrmLayer('groupFormAccess2', 'none');
        toggleFormLayer('sbAccessUsers','none');
        toggleFormLayer('sbAccessUsers2', 'none');

        if (form[saveDBButtonName][0].checked) {
  	        toggleFormLayer('queryUsers','block');
  	        toggleFormLayer('queryUsers2', 'block');
        } else {
	        toggleFormLayer('queryUsers','none');
	        toggleFormLayer('queryUsers2', 'none');
	    }

        toggleFormLayer('workflow','none');
        toggleFormLayer('workflow2', 'none');
        toggleFormLayer('approveUsers','none');
        toggleFormLayer('approveUsers2', 'none');
        toggleFormLayer('approveUsers3', 'none');
        toggleFormLayer('approveUsers4', 'none');
    }
    else if(field=='privateform'){
    	toggleFormLayer('formAccessUsers','block');
    	toggleFormLayer('formAccessUsers2','block');
        toggleFormLayer('queryUsers','block');
        toggleFormLayer('queryUsers2', 'block');
        toggleFormLayer('workflow','block');
        toogleFormLayer('workflow2', 'block');

        if (form[formAccessUsersButtonName][1].checked) {
            toggleFormLayer('sbAccessUsers','block');
            toggleFormLayer('sbAccessUsers2', 'block');
        } else {
            toggleFormLayer('sbAccessUsers','none');
            toggleFormLayer('sbAccessUsers2', 'none');
        }

        if (form[formAccessUsersButtonName][2].checked){
            toggleFormLayer('groupFormAccess','block');
            toggleFormLayer('groupFormAccess2', 'block');
        } else {
            toggleFormLayer('groupFormAccess','none');
            toggleFormLayer('groupFormAccess2', 'none');
		}

        if (form[approvalRequireButtonName][1].checked) {
	        toggleFormLayer('approveUsers','none');
	        toggleFormLayer('approveUsers2', 'none');
	        toggleFormLayer('approveUsers3', 'none');
	        toggleFormLayer('approveUsers4', 'none');
	    } else {
	        toggleFormLayer('approveUsers','block');
	        toggleFormLayer('approveUsers2', 'block');
	        toggleFormLayer('approveUsers3', 'block');
	        toggleFormLayer('approveUsers4', 'block');
	    }
    }
    else if (field=='privateFormAccess'){
        toggleFormLayer('sbAccessUsers','block');
        toggleFormLayer('sbAccessUsers2','block');
        toggleFormLayer('groupFormAccess','none');
        toggleFormLayer('groupFormAccess2', 'none');
    }
    else if (field=='publicFormAccess'){
        toggleFormLayer('groupFormAccess','none');
        toggleFormLayer('groupFormAccess2', 'none');
        toggleFormLayer('sbAccessUsers','none');
        toggleFormLayer('sbAccessUsers2', 'none');
    }
    else if(field=='groupFormAccess' || field=='groupFormAccess2'){
        toggleFormLayer('groupFormAccess','block');
        toggleFormLayer('groupFormAccess2', 'block');
        toggleFormLayer('sbAccessUsers','none');
        toggleFormLayer('sbAccessUsers2', 'none');
    }
    else if(field=='yesDB'){
        toggleFormLayer('queryUsers','block');
        toggleFormLayer('queryUsers2', 'block');
        toggleFormLayer('workflow','block');
        toggleFormLayer('workflow2', 'block');
        if (form[approvalRequireButtonName][1].checked) {
            toggleFormLayer('approveUsers','none');
            toggleFormLayer('approveUsers2', 'none');
            toggleFormLayer('approveUsers3', 'none');
            toggleFormLayer('approveUsers4', 'none');
        } else {
            toggleFormLayer('approveUsers','block');
            toggleFormLayer('approveUsers2', 'block');
            toggleFormLayer('approveUsers3', 'block');
            toggleFormLayer('approveUsers4', 'block');
		}
    }
    else if(field=='noDB'){
        toggleFormLayer('queryUsers','none');
        toggleFormLayer('queryUsers2', 'none');
        toggleFormLayer('workflow','none');
        toggleFormLayer('workflow2', 'none');
        toggleFormLayer('approveUsers','none');
        toggleFormLayer('approveUsers2','none');
        toggleFormLayer('approveUsers3','none');
        toggleFormLayer('approveUsers4', 'none');
    }
    else if (field == 'yesApproval') {
    	toggleFormLayer('approveUsers','block');
    	toggleFormLayer('approveUsers2','block');
    	toggleFormLayer('approveUsers3','block');
    	toggleFormLayer('approveUsers4', 'block');
    }
    else if (field == 'noApproval') {
    	toggleFormLayer('approveUsers','none');
    	toggleFormLayer('approveUsers2', 'none');
    	toggleFormLayer('approveUsers3', 'none');
    	toggleFormLayer('approveUsers4', 'none');
    }
}
//-->
</script>


<tr>
    <td valign="top" align="right" width="30%" class="classRowLabel">
    	<fmt:message key='formWizard.label.formName'/> *&nbsp;</td>
    <td class="classRow">
    	<x:display name="${form.formName.absoluteName}"/>
		<c:out value ="${form.formName.message}"/>
    </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%" class="classRowLabel">
		<fmt:message key='formWizard.label.formHeader'/> *&nbsp;
	</td>
	<td class="classRow">
		<x:display name="${form.formHeader.absoluteName}"/>
	</td>
</tr>

<tr>
    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.submissionMessage'/> *&nbsp;</B></td>
    <td align="left" class="classRow">
	    <x:display name="${form.submissionMessage.absoluteName}"/>
    </td>
</tr>


<%--<tr>
	<td valign="top" align="right" width="30%"><B>Form Access Type</B></td>
	<td>
		<x:display name="${form.publicFormAccess.absoluteName}"/> public &nbsp;&nbsp;
		<x:display name="${form.privateFormAccess.absoluteName}"/>private
	</td>
</tr>--%>

<tr>
	<td align="right" class="classRowLabel">
		<div id="formAccessUsers2" style='display:<c:out value="${formAccessUsers}"/>'>
			<fmt:message key='formWizard.label.formUserType'/>
		</div>
	</td>
	<td class="classRow">
		<div id="formAccessUsers" style='display:<c:out value="${formAccessUsers}"/>'>
			<x:display name="${form.publicFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.public'/> &nbsp;&nbsp;
        	<x:display name="${form.privateFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.private'/> &nbsp;&nbsp;
        	<x:display name="${form.groupFormUserAccess.absoluteName}"/>  <fmt:message key='formWizard.label.group'/>
		</div>                    
	</td>
</tr>

<%-- // 1]
<tr>
	<td colspan="2">
        <div id="formAccessUsers" style='display:<c:out value="${formAccessUsers}"/>'>
		   <table width="100%">
			   <tr>
			    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.formUserType'/></B></td>
			    <td>
					<x:display name="${form.publicFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.public'/> &nbsp;&nbsp;
                    <x:display name="${form.privateFormUserAccess.absoluteName}"/> <fmt:message key='formWizard.label.private'/> &nbsp;&nbsp;
                    <x:display name="${form.groupFormUserAccess.absoluteName}"/>  <fmt:message key='formWizard.label.group'/>
			    </td>
			   </tr>
		   </table>
	    </div>
	</td>
</tr>
--%>


<tr>
	<td class="classRowLabel" align="right">
		<div id="sbAccessUsers2" style='display:<c:out value="${formUserAccess}"/>'>
			<fmt:message key='formWizard.label.users'/>
		</div>
	</td>
	<td class="classRow">
		<div id="sbAccessUsers" style='display:<c:out value="${formUserAccess}"/>'>
			<x:display name="${form.sbAccessUsers.absoluteName}"/>
			<c:out value ="${form.sbAccessUsers.message}"/>
		</div>
	</td>
</tr>

<%-- // 2]
<tr>
	<td colspan="2">
    	<div id="sbAccessUsers" style='display:<c:out value="${formUserAccess}"/>'>
        	<table width="100%">
            	<tr>
                    <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.users'/></B></td>
                    <td>
						<x:display name="${form.sbAccessUsers.absoluteName}"/>
					    <c:out value ="${form.sbAccessUsers.message}"/>
					</td>
                </tr>
            </table>
        </div>
    </td>
</tr>
--%>


<tr>
	<td align="right" class="classRowLabel">
	<div id="groupFormAccess2" style='display:<c:out value="${formGroupAccess}"/>'>
	<fmt:message key='formWizard.label.formGroup'/>
	</div>
	</td>
	<td class="classRow">
		<div id="groupFormAccess" style='display:<c:out value="${formGroupAccess}"/>'>
			<x:display name="${form.cbGroups.absoluteName}"/>
			<c:out value ="${form.cbGroups.message}"/>
		</div>	
	</td>
</tr>
<%-- // 3] 
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
--%>

<tr>
	<td valign="top" align="right" width="30%" align="right" class="classRowLabel"><fmt:message key='formWizard.label.saveToDatabase'/></td>
	<td align="left" class="classRow">
		<x:display name="${form.yesDB.absoluteName}"/><fmt:message key='formWizard.label.yes'/>  &nbsp;&nbsp;
		<x:display name="${form.noDB.absoluteName}"/><fmt:message key='formWizard.label.no'/>
	</td>
</tr>

<tr>
    <td valign="top" align="right" width="30%" class="classRowLabel"><fmt:message key='formWizard.label.emailTo'/></td>
	<td align="left" class="classRow">
    	<x:display name="${form.emailTo.absoluteName}"/>
    	<br><fmt:message key='formWizard.label.separateEmails'/>
    </td>
</tr>

<c:if test="${memo == 'true'}">
<tr>
    <td valign="top" align="right" width="30%" class="classRowLabel"><fmt:message key='formWizard.label.messageViaIntranet'/></td>
    <td align="left" class="classRow">
    	<x:display name="${form.sbMemoTo.absoluteName}"/>
		<c:out value ="${form.noDB.message}"/>
    </td>
</tr>
</c:if>


<tr>
	<td align="right" class="classRowLabel">
		<div id="queryUsers2" style='display:<c:out value="${queryUsers2}"/>'>
		<fmt:message key='formWizard.label.allowQueryBy'/>&nbsp; *
		</div>
	</td>
	<td class="classRow">
		<div id="queryUsers" style='display:<c:out value="${queryUsers}"/>'>
			<x:display name="${form.sbQueryBy.absoluteName}"/>
			<c:out value ="${form.sbQueryBy.message}"/>
		</div>
	</td>
</tr>
<%-- // 4] 
<tr>
	<td colspan="2">
    	<div id="queryUsers" style='display:<c:out value="${queryUsers}"/>'>
        	<table width="100%">
            	<tr>
                	<td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.allowQueryBy'/>&nbsp; *</B></td>
                    <td align="left"><x:display name="${form.sbQueryBy.absoluteName}"/>
						<c:out value ="${form.sbQueryBy.message}"/>
					</td>
                </tr>
            </table>
        </div>
    </td>
</tr>
--%>
 
 
<tr>
	<td align="right" class="classRowLabel">
		<div id="workflow2" style='display:<c:out value="${workflow}"/>'>
		<fmt:message key='formWizard.label.formUserType.approvalRequired'/>
		</div>
	</td>
	<td class="classRow">
		<div id="workflow" style='display:<c:out value="${workflow}"/>'>
			<x:display name="${form.workFlowYes.absoluteName}"/> <fmt:message key='formWizard.label.formUserType.yes'/> &nbsp;&nbsp;
       		<x:display name="${form.workFlowNo.absoluteName}"/><fmt:message key='formWizard.label.formUserType.no'/>
       	</div>
	</td>
</tr> 
 
<%-- // 5]  
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
--%>


<tr>
	<td align="right" class="classRowLabel">
		<div id="approveUsers3" style='display:<c:out value="${approveUsers3}"/>'>
		<fmt:message key='formWizard.label.approveBy'/>
		</div>
	</td>
	<td class="classRow">
		<div id="approveUsers" style='display:<c:out value="${approveUsers}"/>'>
			<x:display name="${form.sbApprover.absoluteName}"/>
			<c:out value ="${form.sbApprover.message}"/>
		</div>
	</td>
</tr>
<tr>
	<td align="right" class="classRowLabel">
		<div id="approveUsers4" style='display:<c:out value="${approveUsers4}"/>'>
			<fmt:message key='formWizard.label.approvalNotificationMethod'/>
		</div>
	</td>
	<td class="classRow">
		<div id="approveUsers2" style='display:<c:out value="${approveUsers2}"/>'>
			<x:display name="${form.memoApproveNotification.absoluteName}"/> <fmt:message key='formWizard.label.approval.messageViaIntranet'/> &nbsp;&nbsp;
        	<x:display name="${form.emailApproveNotification.absoluteName}"/> <fmt:message key='formWizard.label.Email'/>
        </div>	
	</td>
</tr>

<%-- // 6] 
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
--%>


<tr>
	<td valign="top" align="right" width="30%" class="classRowLabel"><fmt:message key='formWizard.label.linkToAnotherForm'/></td>
	<td align="left" class="classRow">
		<x:display name="${form.sbForms.absoluteName}"/>
	</td>
</tr>

<tr>
	<td valign="top" align="right" width="30%" class="classRowLabel"><fmt:message key='formWizard.label.copyFieldsFrom'/></td>
	<td align="left" class="classRow">
		<x:display name="${form.sbDuplicateFieldForm.absoluteName}"/>
	</td>
</tr>

<tr>
	<td valign="top" align="right" width="30%" class="classRowLabel"><fmt:message key='formWizard.label.tableColumn'/> *</td>
	<td align="left">
		<x:display name="${form.tfTableColumn.absoluteName}"/>
        <c:out value ="${form.tfTableColumn.message}"/>
	</td>
</tr>

<tr>
	<td valign="top"  align="center" colspan = "2" class="classRow">
		<x:display name="${form.submit.absoluteName}"/>
		<x:display name="${form.reset.absoluteName}"/>
	</td>
</tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
