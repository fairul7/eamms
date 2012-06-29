<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<x:permission permission="kacang.services.log.Entry.view" module="kacang.services.log.LogService" url="noPermission.jsp" />
<x:config>
    <page name="logs">
        <portlet name="portlet" text="Logs" width="100%" permanent="true">
            <kacang.services.log.ui.LogTable name="table" sort="entryDate" desc="true"/>
        </portlet>
    </page>
</x:config>
<c:if test="${!empty param.entryId}">
	<c:redirect url="logsOpen.jsp?entryId=${param.entryId}"/> 
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
								<x:display name="logs.portlet"/>
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

