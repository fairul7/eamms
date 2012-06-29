<%@ page import="kacang.stdui.Portlet,
                 kacang.ui.WidgetManager,
                 com.tms.cms.ad.ui.AdUi"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ad.ManageAds" module="com.tms.cms.ad.model.AdModule" url="noPermission.jsp" />

<x:config>
    <page name="page">
        <portlet name="portlet" text="<fmt:message key='ad.label.bannerAds'/>" width="100%" permanent="true">
            <com.tms.cms.ad.ui.AdUi name="adUi" showMenu="false" />
        </portlet>
    </page>
</x:config>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />

<%
    WidgetManager wm;
    Portlet portlet;
    AdUi adUi;

    wm = WidgetManager.getWidgetManager(request);
    portlet = (Portlet) wm.getWidget("page.portlet");
    adUi = (AdUi) wm.getWidget("page.portlet.adUi");
    portlet.setText(adUi.getTitle());
%>

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveAd.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="page.portlet" />

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













<jsp:include page="/cmsadmin/includes/footerInteractive.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
