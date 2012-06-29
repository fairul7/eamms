<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="pageNonPrint">
        <com.tms.collab.timesheet.ui.TimeSheetNonProjectView name="project"/>
        <com.tms.collab.timesheet.ui.TimeSheetTaskView name="task"/>
    </page>
</x:config>

<c:if test="${!empty param.projectid}">
    <x:set name="pageNonPrint.project" property="projectId" value="${param.projectid}"/>
    <x:set name="pageNonPrint.project" property="print" value="${true}"/>
</c:if>

<html>
  <head><title>Print Time Sheet</title></head>
  <style>
  .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .folderlink {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  </style>
  <body>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td>
<x:display name="pageNonPrint.project"></x:display>
</td>
</tr>
<tr>
    <td bgcolor="#CCCCCC"><spacer type="block" height="1"></td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td>
<c:forEach items="${widgets['pageNonPrint.project'].task}" var="t">
<x:set name="pageNonPrint.task" property="header" value="${false}"/>
<x:set name="pageNonPrint.task" property="taskId" value="${t.id}"></x:set>
<x:set name="pageNonPrint.task" property="print" value="${true}"></x:set>
<table width="100%" cellpadding="4" cellspacing="0">
<tr>
    <td colspan="3" class="contentBgColor">
    <b><fmt:message key="timesheet.label.task"/>:</b>
    <c:out value="${t.title}"/>
    </td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td class="contentBgColor">
    <b><fmt:message key="timesheet.label.startdate"/>:</b> <fmt:formatDate value="${t.startDate}" pattern="${globalDateLong}"/>
    &nbsp;&nbsp;&nbsp;
    <b><fmt:message key="timesheet.label.duedate"/>:</b> <fmt:formatDate value="${t.dueDate}" pattern="${globalDateLong}"/>
    </td>
    <td width="1%">&nbsp;</td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td><x:display name="pageNonPrint.task"/></td>
    <td width="1%">&nbsp;</td>
</tr>
</table>
</c:forEach>
<c:forEach items="${widgets['pageNonPrint.project'].noTSTask}" var="ntst">
<x:set name="pageNonPrint.task" property="header" value="${false}"/>
<x:set name="pageNonPrint.task" property="taskId" value="${ntst.id}"></x:set>
<x:set name="pageNonPrint.task" property="print" value="${true}"></x:set>
<table width="100%" cellpadding="4" cellspacing="0">
<tr>
    <td colspan="3" class="contentBgColor">
    <b><fmt:message key="timesheet.label.task"/>:</b>
    <c:out value="${ntst.title}"/>
    </td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td class="contentBgColor">
    <b><fmt:message key="timesheet.label.startdate"/>:</b> <fmt:formatDate value="${ntst.startDate}" pattern="${globalDateLong}"/>
    &nbsp;&nbsp;&nbsp;
    <b><fmt:message key="timesheet.label.duedate"/>:</b> <fmt:formatDate value="${ntst.dueDate}" pattern="${globalDateLong}"/>
    </td>
    <td width="1%">&nbsp;</td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td><x:display name="pageNonPrint.task"/></td>
    <td width="1%">&nbsp;</td>
</tr>
</table>
</c:forEach>
    </td>
</tr>
</table>
  </body>
</html>