<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>


<table width="100%" cellpadding="1" cellspacing="1">
<tr>
	<td class="calendarHeader" height="25">
	<fmt:message key="taxonomy.title.permissionForm"/>
	</td>
</tr>
<tr>
	<td>
 		<table width="100%" cellpadding="4" cellspacing="2" class="contentBgColor">
 		<tr>
 			<td colspan=2 class="classRowLabel">
 			<fmt:message key="taxonomy.label.permissionFormNote"/>
 			</td>
 		</tr>
 		<tr>
 			<td class="classRowLabel" align="right" valign="top" width="20%">
 			<fmt:message key="taxonomy.label.permissionRoleSelect"/> <font color="#ff0000">*</font>
 			</td>
 			<td class="classRow">
 			<x:display name="${w.permissionRoleComboBox.absoluteName }"/>
 			</td>
 		</tr>
 		<tr>
 	 	 	<td class="classRowLabel" align="right">
 	 	 	
 	 	 	</td>
 	 	 	<td class="classRow">
 	 	 	<x:display name="${w.btnSubmit.absoluteName }"/>
 	 	 	<x:display name="${w.btnCancel.absoluteName }"/>
 	 	 	</td>
 	 	</tr>
 	 	</table>
	</td>
</tr>
</table>		


<jsp:include page="../form_footer.jsp" flush="true"/>
