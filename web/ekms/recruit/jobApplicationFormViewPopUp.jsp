<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config >
	<page name="jobApplicationForm">
		<tabbedpanel name="tab1" width="100%">
			<panel name="panel1" text="Personal Details">
				<com.tms.hr.recruit.ui.JobAppPersonalView name="view"/>
			</panel>
			<panel name="panel2" text="Education Details">
				<com.tms.hr.recruit.ui.JobAppEduListView name="list" width="100%" />
			</panel>
			<panel name="panel3" text="Working Experience">
				<com.tms.hr.recruit.ui.JobAppWorkingExpTypeView name="type" width="100%" />
				<%-- <c:if test="${!widgets['jobApplicationForm.tab1.panel3.type'].hasWorkingExp}"> --%>
				<com.tms.hr.recruit.ui.JobAppWorkingExpListView name="list" width="100%" />
			</panel>
			<panel name="panel4" text="Skill">
				<com.tms.hr.recruit.ui.JobAppSkillListView name="List" width="100%" />
			</panel>
			<panel name="panel5" text="language">
				<com.tms.hr.recruit.ui.JobAppLanguageListView name="List" width="100%" />
			</panel>
			<panel name="panel6" text="Additional Info">
				<com.tms.hr.recruit.ui.JobAppAdditionalView name="Form"/>
			</panel>
		</tabbedpanel>
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.applicantId}">
    <x:set name="jobApplicationForm.tab1.panel1.view" property="applicantId" value="${param.applicantId}"/>  
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<c:set var="type" value="${param.type}" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key="recruit.menu.label.recruit"/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
     <x:display name="jobApplicationForm.tab1"/>    
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>
