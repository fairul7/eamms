<%@ include file="/common/header.jsp" %>

<%@ include file="/cmsadmin/includes/headerCommon.jsp" %>

<TABLE width="100%" cellpadding="0" height="35" bgcolor="#336699" border="0" cellspacing="0">
    <TR>
      <TD valign="middle"><img width="5" height="1" src="../images/clear.gif"></TD>
      <TD valign="middle">
        <TABLE cellpadding="2" bgcolor="#4779AB" border="0" cellspacing="0" width="100%">
          <TR>
            <TD valign="middle" width="100"> | <a href="ad_index.jsp?cn=page.portlet.adUi&et=listAdLocations" class="<%= (startsWith(request, "/cmsadmin/interactive/ad")) ? "submenulink2" : "submenulink" %>"><fmt:message key='general.label.bannerAds'/></a></TD>
            <TD valign="middle" width="100"> | <a href="forums.jsp?cn=forumAdminPage.forumAdminPortlet.adminForumPanel&et=adminForumTable&location=Forums Listing"" class="<%= (startsWith(request, "/cmsadmin/interactive/forum")) ? "submenulink2" : "submenulink" %>"><fmt:message key='general.label.forums'/></a></TD>
            <TD valign="middle" width="100"> | <a href="pollAdmin.jsp?event=view" class="<%= (startsWith(request, "/cmsadmin/interactive/poll")) ? "submenulink2" : "submenulink" %>"><fmt:message key='general.label.votes'/></a></TD>
            <TD valign="middle" width="100"> | <a href="eventList.jsp" class="<%= (startsWith(request, "/cmsadmin/interactive/event")) ? "submenulink2" : "submenulink" %>"><fmt:message key='general.label.events'/></a></TD>
            <TD valign="middle" width="100"> | <a href="frwFormsView.jsp" class="<%= (startsWith(request, "/cmsadmin/interactive/frw")) ? "submenulink2" : "submenulink" %>"><fmt:message key='general.label.formWizard'/></a></TD>
            <TD valign="middle" align="right">&nbsp;</TD>
          </TR>
        </TABLE>
      </TD>
      <TD align="right" valign="middle"><TABLE cellpadding="2" border="0" cellspacing="0">
          <TR>
            <TD valign="middle" colspan="5">


            </TD>
          </TR>
        </TABLE></TD>
    </TR>
</TABLE>
