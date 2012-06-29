<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentSearch">
    <tabbedpanel name="contentSearchPanel" text="<fmt:message key='cms.label.contentFullTextSearch'/>" width="100%" permanent="true">
        <panel name="searchPanel" text="<fmt:message key='cms.label.simpleSearch'/>">
            <com.tms.cms.core.ui.ContentSearchTable name="contentSearchTable" width="100%">
                <forward name="selection" url="contentView.jsp"/>
            </com.tms.cms.core.ui.ContentSearchTable>
        </panel>
        <panel name="advancedSearchPanel" text="<fmt:message key='cms.label.advancedSearch'/>">
            <com.tms.cms.core.ui.ContentSearchPanel name="contentSearchPanel" width="100%">
                <forward name="selection" url="contentView.jsp"/>
            </com.tms.cms.core.ui.ContentSearchPanel>
        </panel>
    </tabbedpanel>
</page>
</x:config>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">
        <jsp:include page="/cmsadmin/includes/sideContentModules.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                  <%--Content Search Results--%>
                  <x:display name="contentSearch.contentSearchPanel"/>
                  <br>
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
