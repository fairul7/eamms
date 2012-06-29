<%@ page import="kacang.ui.Widget,
				 kacang.stdui.Form,
				 java.util.*,
				 java.awt.*,
                 kacang.stdui.Hidden"%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<script  language=javascript>
isLeave();
</script>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.manpower.form.type'/>&nbsp;*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${w.radLeave.absoluteName}"/>
				<x:display name="${w.radHoliday.absoluteName}"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%">
				<div id="leaveTitleShow" style="display:none"><fmt:message key='fms.manpower.form.leaveType'/>&nbsp;*</div>
			</td>
			<td class="classRow" valign="top" width="40%">
				<div id="leaveShow" style="display:none"><x:display name="${w.leaveType.absoluteName}"/></div>
			</td>
		</tr>
		
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%">
				<div id="holidayTitleShow" style="display:none"><fmt:message key='fms.manpower.form.holiday'/>&nbsp;*</td></div>
			<td class="classRow" valign="top" width="40%">
				<div id="holidayShow" style="display:none"><x:display name="${w.holiday.absoluteName}"/></div>
			</td>
		</tr>
	
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.manpower.form.dateFrom'/>&nbsp;*</td>
				<td class="classRow" valign="top" width="40%"><x:display name="${w.dateFrom.absoluteName}"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.manpower.form.dateTo'/>&nbsp;*</td>
			<td class="classRow" valign="top" width="40%"><x:display name="${w.dateTo.absoluteName}"/></td>
		</tr>
		<tr id="child" name="child">
			<td class="classRowLabel" valign="top" align="right" width="15%">
			<div id="manpowerTitleShow" style="display:none"><fmt:message key='fms.manpower.form.manpower'/></div>
			</td>
			<td class="classRow" valign="top" width="40%">
			<div id="manpowerShow" style="display:none"><x:display name="${w.userSelectBox.absoluteName}"/></div>
			</td>
		</tr>
		<tr>
		<td class="classRowLabel" valign="top" align="right" width="15%">&nbsp;</td>
		<td class="classRow" valign="top" width="40%"><x:display name="${w.btnsave.absoluteName}"/><x:display name="${w.btncancel.absoluteName}"/></td>
		</tr>
	 	
	 	<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
	 	</table>
	 	</td>
	 	</tr>	
	 	</table>

<script  language=javascript>
function isLeave(type){
	
	var formName = '<c:out value="${w.absoluteName}"/>';
	var form = document.forms[formName];
	var radioButtonName = formName + '.<c:out value="${w.radLeave.groupName}"/>';
	var radioButtonName2 = formName + '.<c:out value="${w.radHoliday.groupName}"/>';
	
	if(form[radioButtonName][0].checked){
		
		document.getElementById("leaveShow").style.display="block";
		document.getElementById("leaveTitleShow").style.display="block";
		document.getElementById("manpowerShow").style.display="block";
		document.getElementById("manpowerTitleShow").style.display="block";
		document.getElementById("holidayShow").style.display="none";
		document.getElementById("holidayTitleShow").style.display="none";
	}else{
		
		document.getElementById("leaveShow").style.display="none";
		document.getElementById("leaveTitleShow").style.display="none";
		document.getElementById("manpowerShow").style.display="none";
		document.getElementById("manpowerTitleShow").style.display="none";
		document.getElementById("holidayShow").style.display="block";
		document.getElementById("holidayTitleShow").style.display="block";
	}
} 
isLeave();
</script>


