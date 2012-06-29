<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="printFormPage">
    		<com.tms.collab.formwizard.ui.PrintForm name="printForm"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name="printFormPage.printForm" property="id" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="printFormPage.printForm" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.hidden}">
    <x:set name="printFormPage.printForm" property="hidden" value="${param.hidden}"/>
</c:if>

<html>
<head>
<style>
td, .contentBgColor { font: 8pt Arial; }
.footer { font: 7.5pt Arial; color: gray }
</style>
</head>
<body>
<table width="100%">
<tr>
    <td>
        <x:display name="printFormPage.printForm" ></x:display>
    </td>
</tr>
</table>
<script>
<!--
    window.print();
//-->
</script>
</body>
</html>



