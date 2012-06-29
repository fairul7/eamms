<%@ page import="kacang.stdui.Portlet, kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.collab.forum.ManageForums" module="com.tms.collab.forum.model.ForumModule" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />
    <c:choose>
        <c:when test="${!(empty requestScope.location)}">
            <c:set value="${requestScope.location}" var="title" />
        </c:when>
        <c:when test="${!(empty param.location)}">
            <c:set value="${param.location}" var="title" />
        </c:when>
        <c:otherwise>
            <c:set value="Forums Listing" var="title" />
        </c:otherwise>
    </c:choose>
<x:config>
<page name="forumAdminPage">
    <forumLocationIndicator name="forumLocationIndicator" />
    <portlet name="forumAdminPortlet" text="${title}" width="100%" permanent="true">
        <adminForumPanel name="adminForumPanel" />
    </portlet>
</page>
</x:config>
<%
    String title = (String)request.getAttribute("location");
    if(title==null)
        title = request.getParameter("location");
    if(title==null || title.equalsIgnoreCase("null"))
        title=Application.getInstance().getMessage("interactive.label.forumsListing", "Forums Listing");

    Portlet forumAdminPortlet = (Portlet)Application.getInstance().getWidget(request, "forumAdminPage.forumAdminPortlet");
    forumAdminPortlet.setText(title);
%>

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveForums.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
                <td><br>
                  <x:display name="forumAdminPage.forumLocationIndicator"/>
                </td>
            </tr>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="forumAdminPage.forumAdminPortlet"/>
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

