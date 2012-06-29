<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='ad.label.bannerAds'/></span>
                <br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">

            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/ad_index.jsp?cn=page.portlet.adUi&et=listAdLocations"><fmt:message key='ad.label.adLocationListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/ad_index.jsp?cn=page.portlet.adUi.adLocationTable&et=newAdLocation"><fmt:message key='ad.label.newAdLocation'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/ad_index.jsp?cn=page.portlet.adUi&et=listAds"><fmt:message key='ad.label.adListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/ad_index.jsp?cn=page.portlet.adUi.adTable&et=newAd"><fmt:message key='ad.label.newAd'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/ad_index.jsp?cn=page.portlet.adUi&et=refreshModule"><fmt:message key='ad.label.refreshModules'/></a>

                <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>
        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
