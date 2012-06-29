<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.SystemSettings" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerSiteAdmin.jsp" flush="true" />


<x:config>
    <page name="systemSettings">
        <portlet name="systemSettingsPortlet" text="<fmt:message key='siteadmin.label.systemSettings'/>" width="100%" permanent="true">
            <panel name="systemSettingsPanel" />
            <com.tms.ekms.setup.ui.SetupForm name="setupForm" />
        </portlet>
    </page>
</x:config>


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideSiteAdminSystemSettings.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

              <x:display name="systemSettings.systemSettingsPortlet" body="custom">
                <x:display name="systemSettings.systemSettingsPortlet.systemSettingsPanel" body="custom">

                <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
                  <tbody>


                    <tr>
                      <td style="vertical-align: top;" width="200"><fmt:message key='siteadmin.label.jvmMemoryAvailable'/><br>
                      </td>
                      <td style="vertical-align: top; text-align:left">
                        <%= (Runtime.getRuntime().freeMemory() / 1000) %> / <%= (Runtime.getRuntime().totalMemory() / 1000) %> K
                      </td>
                    </tr>

                    <tr>
                      <td style="vertical-align: top;" colspan="2">
                        <hr>
                        <span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='siteadmin.label.systemProperties'/></span>
                      </td>
                    </tr>

                    <tr>
                      <td style="vertical-align: top;" colspan="2">
                        <x:display name="systemSettings.systemSettingsPortlet.setupForm" />
                      </td>
                    </tr>

                    <tr>
                      <td style="vertical-align: top;" colspan="2">
                      <hr size="1">
                      </td>
                    </tr>

                    <tr>
                      <td style="vertical-align: top;"><fmt:message key='siteadmin.label.siteCache'/><br>
                      </td>
                      <td style="vertical-align: top;">
                        <input type="button" class="button" onclick="location.href='systemSettingsClearCache.jsp?clearCache=true'" value="<fmt:message key='siteadmin.label.clearCache'/>">
                      </td>
                    </tr>

                    <tr>
                      <td style="vertical-align: top;"><fmt:message key='siteadmin.label.searchIndex'/><br>
                      </td>
                      <td style="vertical-align: top;">
                        <input type="button" class="button" onclick="location.href='systemSettingsOptimizeIndex.jsp?optimizeIndex=true'" value="<fmt:message key='siteadmin.label.optimizeIndex'/>">
                      </td>
                    </tr>

                  </tbody>
                </table>

                </x:display>
              </x:display>

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

