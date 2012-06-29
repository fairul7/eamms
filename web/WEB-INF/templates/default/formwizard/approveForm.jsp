<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp" flush="true"/>

<c:if test="${form.childMap.publicFormAccess.checked}">
	<c:set var="formAccessUsers" value="none"/>
	<c:set var="formUserAccess" value="none"/>
	<c:set var="formGroupAccess" value="none"/>
	<c:set var="queryUsers" value="block"/>		
	<c:set var="workflow" value="none"/>
	<c:set var="approveUsers" value="none"/>
</c:if>

<c:if test="${form.childMap.privateFormAccess.checked}">
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
</c:if>

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

<script>
<!--
function viewFields(){
	window.open('<c:url value="fw_viewPreviewForm.jsp"/>?formID=<c:out value="${param.formID}"/>',
                 'preview','scrollbars=yes,resizable=yes,status=yes,width=700,height=500,location=yes')
}
// -->
</script>

<table>

<tr>
    <td valign="top" align="right" width="30%"><B>Form Name</B></td>
    <td>
    	<x:display name="${form.formNameLabel.absoluteName}"/>
    </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B>Form Header</B></td>
	<td>
		<x:display name="${form.formHeader.absoluteName}"/>
	</td>
</tr>

<tr>
    <td valign="top" align="right" width="30%"><B>Submission Message</B></td>
    <td align="left">
    	<x:display name="${form.submissionMessage.absoluteName}"/>
    </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B>Form Access Type</B></td>
	<td>
		<x:display name="${form.publicFormAccess.absoluteName}"/> public &nbsp;&nbsp; 
		<x:display name="${form.privateFormAccess.absoluteName}"/> private
	</td>
</tr>

<tr>
	<td colspan="2">
        <div id="formAccessUsers" style='display:<c:out value="${formAccessUsers}"/>'>
		   <table width="100%">
			   <tr>
			       <td valign="top" align="right" width="30%"><B>Form User Type</B></td>
				   <td>
					   <x:display name="${form.publicFormUserAccess.absoluteName}"/> public &nbsp;&nbsp; 
					   <x:display name="${form.privateFormUserAccess.absoluteName}"/> private&nbsp;&nbsp; 
					   <x:display name="${form.groupFormUserAccess.absoluteName}"/>  group
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
                	<td valign="top" align="right" width="30%"><B>Users</B><br>(For private Forms )</td>
                    <td><x:display name="${form.sbAccessUsers.absoluteName}"/></td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
   <td colspan="2">
    	<div id="groupFormAccess" style='display:<c:out value="${formGroupAccess}"/>'>
     		<table width="100%">
		        <td valign="top" align="right"><B>Form Groups</B></td>
        		<td align="left">
			        <x:display name="${form.cbGroups.absoluteName}"/>
		        </td>       
	       </table>
    	</div>
   </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B>Save To DataBase</B></td>
	<td align="left">
		<x:display name="${form.yesDB.absoluteName}"/>Yes  &nbsp;&nbsp; 
		<x:display name="${form.noDB.absoluteName}"/>No
	</td>
</tr>

<tr>
    <td valign="top" align="right" width="30%"><B>Email To</B></td>
    <td align="left">
    <x:display name="${form.emailTo.absoluteName}"/>
    </td>
</tr>

<tr>

    <td valign="top" align="right" width="30%"><B>Memo To</B></td>
    <td align="left">
     	<x:display name="${form.sbMemoTo.absoluteName}"/>
    </td>
</tr>

<tr>
	<td colspan="2">
    	<div id="queryUsers" style='display:<c:out value="${queryUsers}"/>'>
        	<table width="100%">
            	<tr>
                	<td valign="top" align="right" width="30%"> <B>Allow Query By</B></td>
                    <td align="left"><x:display name="${form.sbQueryBy.absoluteName}"/></td>
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
		            <td valign="top" align="right" width="30%"><B>Approval Required</B></td>
        		    <td align="left">
			            <x:display name="${form.workFlowYes.absoluteName}"/> Yes &nbsp;&nbsp; 
			            <x:display name="${form.workFlowNo.absoluteName}"/>No
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
                	<td valign="top" align="right" width="30%"> <B>Approve By</B><br>(For private Forms )</td>
	                <td align="left">
    	            	<B>&nbsp;&nbsp; Low Priority</B><br><x:display name="${form.sbApprover.absoluteName}"/> <B>&nbsp;&nbsp;High Priority</B>
	                </td>
	            </tr>
	            
	            <tr>
    		        <td valign="top" align="right" width="30%"><B>Approval Notification Method :</B></td>
		            <td align="left">
         			   <x:display name="${form.memoApproveNotification.absoluteName}"/> Memo &nbsp;&nbsp; <x:display name="${form.memoEmailApproveNotification.absoluteName}"/> Memo & Email
		            </td>
        	    </tr>
	        </table>
        </div>
    </td>
</tr>

<tr>
	<td valign="top" align="right" width="30%"><B>Link to another form upon submission<B></td>
	<td align="left">
		<x:display name="${form.sbForms.absoluteName}"/>
	</td>
</tr>

<tr>
	<td valign="top" align="right" width="30%">  <B>Form Fields:</B></td>
	<td align="left"><a href="javascript:viewFields()">View Fields</a></td>
</tr>

<tr>
	<td valign="top"  align="center" colspan = "2">
		<x:display name="${form.approveButton.absoluteName}"/>
	</td>
</tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
