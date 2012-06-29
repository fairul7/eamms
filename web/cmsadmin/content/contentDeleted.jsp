<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%--<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />--%>
<x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.AccessRecycleBin" />
<c:if test="${!accessRecycleBin}">
    <x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageRecycleBin" />
</c:if>
<c:choose>
<c:when test="${accessRecycleBin}">

<x:config>
<page name="contentDeleted">
    <portlet name="contentDeletedPortlet" text="<fmt:message key='cms.label.deletedContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentDeletedTable name="contentDeletedTable" width="100%">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
        </com.tms.cms.core.ui.ContentDeletedTable>
    </portlet>
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
                <br>
                  <%--Content Deleted Portlet--%>
                  <x:display name="contentDeleted.contentDeletedPortlet"/>
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

</c:when>
<c:otherwise>
    <c:redirect url="noPermission.jsp" />
</c:otherwise>
</c:choose>