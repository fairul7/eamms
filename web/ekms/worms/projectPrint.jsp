<%@ include file="/common/header.jsp" %>
<html>
<head>
<title><fmt:message key='project.label.printingProjectSchedule'/></title>
</head>
<style>
.projectTableHeader {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; font-weight:bold}
.projectTableRow {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.projectClassRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.projectClassRowLabel {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
.projectClassRowFaded {font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.classText {font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8pt; font-weight:normal}
.classTextSmall {font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 7.5pt; font-weight:normal}
.projectOpenRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.projectCloseRow {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.projectSchedule {font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; color: #000000; font-weight:bold}
.chartTableRow {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
.chartClassRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
</style>
<body>
    <x:template type="com.tms.collab.project.ui.ProjectChart" properties="projectId=${param.projectId}&hideDetails=false&type=${widgets['wormsProject.personalProject'].type}"/>
</body>
<input type="button" class="button" value="<fmt:message key='project.label.print'/>" onClick="window.print()"/>
</html>

<c:if test="${param.print != 'false'}">
    <script>print();</script>
</c:if>
