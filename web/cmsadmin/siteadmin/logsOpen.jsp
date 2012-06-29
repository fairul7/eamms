<%@ page import="kacang.services.log.ui.LogView"%>
<%@ include file="/common/header.jsp" %> 
<x:permission permission="kacang.services.log.Entry.view" module="kacang.services.log.LogService" url="noPermission.jsp" />
<x:config>
    <page name="logsOpen">
        <portlet name="portlet" text="Log Entry" width="100%" permanent="true">
            <kacang.services.log.ui.LogView name="view"/>
        </portlet>
    </page>
</x:config>
<c-rt:set var="forward_done" value="<%= LogView.FORWARD_DONE %>"/>  
<c:if test="${!empty param.entryId}">
	<x:set name="logsOpen.portlet.view" property="entryId" value="${param.entryId}"/>
</c:if>
<c:if test="${forward_done == forward.name}">
	<c:redirect url="logs.jsp"/> 
</c:if>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<jsp:include page="/cmsadmin/includes/headerSiteAdmin.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
	<tbody>
		<tr>
			<td style="vertical-align: top; width: 250px;">
				<jsp:include page="/cmsadmin/includes/sideSiteAdminLogs.jsp" flush="true"/>
      		</td>
			<td style="vertical-align: top;">
				<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
					<tbody>
						<tr>
							<td style="vertical-align: top;"><br>
								<x:display name="logsOpen.portlet"/>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>
<jsp:include page="/cmsadmin/includes/footerSiteAdmin.jsp" flush="true" />
<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>

