<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ScheduledTasks" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerSiteAdmin.jsp" flush="true" />


<x:config>
    <page name="scheduledTasks">
        <portlet name="jobPortlet" text="<fmt:message key='siteadmin.label.scheduledTasks'/>" width="100%" permanent="true">
            <kacang.services.scheduling.ui.JobScheduleTable name="jobTable" width="100%"/>
        </portlet>
    </page>
</x:config>


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideSiteAdminScheduledTasks.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="scheduledTasks.jobPortlet" />

                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
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

