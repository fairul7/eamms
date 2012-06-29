<%@include file="/common/header.jsp" %>

<x:config>
    <page name="spellCheck">
        <com.tms.portlet.portlets.spellcheck.SpellCheckForm name="form" />
    </page>
</x:config>



<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="forumRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="forumRow">
                    <x:display name="spellCheck.form" />
                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="forumRow">&nbsp;</td></tr>
    <tr><td class="forumFooter"><img src="images/blank.gif" width="1" height="15">&nbsp;
    </td></tr>
</table>
