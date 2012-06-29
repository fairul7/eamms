<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.Mobile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerSiteAdmin.jsp" flush="true" />


<x:config>
    <page name="mobileChannelEdit">
        <portlet name="mobileChannelEditPortlet" text="<fmt:message key='siteadmin.label.editMobileChannel'/>" width="100%" permanent="true">
            <com.tms.cms.mobile.ui.MobileChannelForm name="mobileChannelForm" width="100%">
                <request_script>
                    String editId = event.getRequest().getParameter("id");
                    if (editId != null) {
                        editForm = wm.getWidget("mobileChannelEdit.mobileChannelEditPortlet.mobileChannelForm");
                        editForm.setId(event.getRequest().getParameter("id"));
                    }
                </request_script>
            </com.tms.cms.mobile.ui.MobileChannelForm>
        </portlet>
    </page>
</x:config>


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideSiteAdminMobile.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">

        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="mobileChannelEdit.mobileChannelEditPortlet" />

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

