<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='general.label.forums'/></span>
                <br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/forums.jsp?cn=forumAdminPage.forumAdminPortlet.adminForumPanel&et=adminForumTable&location=Forums Listing"><fmt:message key='forum.label.forumsListing'/></a>
            <li><a class="option" href="<%= request.getContextPath() %>/cmsadmin/interactive/forums.jsp?cn=forumAdminPage.forumAdminPortlet.adminForumPanel&et=createForumForm&location=Add New Forum"><fmt:message key='forum.label.newForum'/></a>

                <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>
        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
