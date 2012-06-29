<%@ include file="/common/header.jsp" %>
<!-- Declare Widgets -->
<x:config>
    <page name="previewPanelPage">
          <com.tms.collab.formwizard.ui.PreviewPanel name="previewPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name = "previewPanelPage.previewPanel" property = "formId" value = "${param.formId}" />
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
     <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>

<body onload="this.focus()">
<table width="100%" style="border: 1px ridge silver" cellspacing="0" cellpadding="4">
	<tr valign="MIDDLE">
    	<td height="22" style="background: #003366; color: white; font-weight: bold">&nbsp;<fmt:message key='formWizard.label.formPreview'/></td>
	    <td align="right" style="background: #003366; color: white; font-weight: bold">&nbsp;</td>
	</tr>
	
	<tr>
    	<td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
    </tr>
	<tr>
    	<td colspan="2" valign="TOP"><i><fmt:message key='formWizard.label.italicHiddenField'/></i></td>
	</tr>
	
	<tr>
	    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
	</tr>

	<tr>
     	<td colspan="2" valign="TOP">
			<x:display name="previewPanelPage.previewPanel" ></x:display>
	    </td>
    </tr>
</table>
</body>
</html>


