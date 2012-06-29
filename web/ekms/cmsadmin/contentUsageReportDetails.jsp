<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
				 kacang.services.presence.PresenceService,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.services.security.User"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="contentUsageDetailsReportPg">
        <com.tms.report.ui.ContentUsageDetailsReport name="contentUsageDetailsReport"/>
    </page>
</x:config>

<x:permission permission="com.tms.cms.AccessReports" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<c:if test="${! empty param.sectionId }">
	<x:set name="contentUsageDetailsReportPg.contentUsageDetailsReport" property="sectionId" value="${param.sectionId }"/>
</c:if>

<c:if test="${! empty param.groupId }">
	<x:set name="contentUsageDetailsReportPg.contentUsageDetailsReport" property="groupId" value="${param.groupId}"/>
</c:if>

<x:set name="contentUsageDetailsReportPg.contentUsageDetailsReport" property="startDate" value="${widgets['contentUsageReportPg.contentUsageReport'].startDate.date}"/>
<x:set name="contentUsageDetailsReportPg.contentUsageDetailsReport" property="endDate" value="${widgets['contentUsageReportPg.contentUsageReport'].endDate.date}"/>

<html>
	<head>
		<title>Content Usage Details Report</title>
		<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
		<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
		<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
	</head>
	<body class="portletRow">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr valign="middle">
				<td height="22" bgcolor="#003366" class="contentTitleFont"><b>&nbsp;Content Usage Details Report</b></td>
				<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
			</tr>
			<tr><td colspan="2" valign="top" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
			<tr>
				<td colspan="2" valign="top">
				<x:display name="contentUsageDetailsReportPg.contentUsageDetailsReport"/>
				</td>
			</tr>
			<tr><td colspan="2" valign="top" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td></tr>
		</table>
	</body>
</html>