<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageKeywords" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentKeywords">
    <portlet name="contentKeywordsPortlet" text="<fmt:message key='cms.label.contentKeywords'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentKeywordsPanel name="contentKeywordsPanel" width="100%" />
        <com.tms.cms.core.ui.ContentKeywordsTable name="contentKeywordsTable" width="100%">
            <forward name="deleted" url="contentKeywords.jsp"/>
        </com.tms.cms.core.ui.ContentKeywordsTable>
    </portlet>
</page>
</x:config>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentModules.jsp" flush="true" />

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>

                  <%-- Content Listing --%>
                  <x:display name="contentKeywords.contentKeywordsPortlet"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table></td>
    </tr>
  </tbody>
</table>


<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
