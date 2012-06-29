<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 26, 2005
  Time: 2:12:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="printPage">
        <com.tms.collab.timesheet.ui.TimeSheetMonthlyReport name="monthlyReport"/>
    </page>
</x:config>

<c:if test="${!empty param.month}">
    <x:set name="printPage.monthlyReport" property="month" value="${param.month}"/>
</c:if>
<c:if test="${!empty param.projectId}">
    <x:set name="printPage.monthlyReport" property="projectId" value="${param.projectId}"/>
    <x:set name="printPage.monthlyReport" property="reportType" value="project"/>
</c:if>
<c:if test="${!empty param.userId}">
    <x:set name="printPage.monthlyReport" property="userId" value="${param.userId}"/>
    <x:set name="printPage.monthlyReport" property="reportType" value="user"/>
</c:if>

<x:set name="printPage.monthlyReport" property="print" value="${true}"/>

<html>
  <head><title>Print Time Sheet</title></head>
  <style>
  .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRowNew {background-color: #DCDCDC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .folderlink {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  </style>
  <body>
    <table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <x:display name="printPage.monthlyReport"></x:display>
        </td>
    </tr>
    </table>
  </body>
</html>