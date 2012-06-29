<%@ include file="/common/header.jsp" %>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

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
    <com.tms.collab.forum.ui.ForumLocationIndicator name="forumLocationIndicator" />
    <portlet name="forumAdminPortlet" text="" width="100%" permanent="true">
        <com.tms.collab.forum.ui.AdminForumPanel name="adminForumPanel" />
    </portlet>
</page>
</x:config>

        <table class="classBackground" cellpadding="4" cellspacing="0" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
                <td class="contentTitleFont">
                  <fmt:message key='forum.label.options'/> > <fmt:message key='com.tms.collab.forum.ManageForums'/>
                </td>
            </tr>
            <tr>
                <td class="classRowLabel"><br>
                  <x:display name="forumAdminPage.forumLocationIndicator"/>
                </td>
            </tr>
            <tr>
              <td class="classRowLabel" style="vertical-align: top;">
                  <x:display name="forumAdminPage.forumAdminPortlet.adminForumPanel"/>
                  <br>
              </td>
            </tr>
            <tr><td class="classRow">&nbsp;</td></tr>
          </tbody>
        </table>

<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>
