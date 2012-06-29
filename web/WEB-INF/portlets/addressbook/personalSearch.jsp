<%@ include file="/common/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="forumRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="forumRow">
                    <x:cache key="personalSearchPortlet" scope="application" time="300">
                        <x:template type="com.tms.collab.directory.ui.ContactSearchPortlet" properties="personal=true&intranet=false&business=false" />
                    </x:cache>
                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="forumRow">&nbsp;</td></tr>
    <tr><td class="forumFooter"><img src="images/blank.gif" width="1" height="15">&nbsp;
    <input type="button" class="button" value="New Contact" onclick="document.location='<c:url value="/ekms/addressbook/abNewContact.jsp" />'">
    </td></tr>
</table>
