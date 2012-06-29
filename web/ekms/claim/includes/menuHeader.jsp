<c:set var="title"><c:out value="${pageScope.title}" default="!Missing param.title!" /></c:set>

<%-- BEGIN: Header --%>
<TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
<tr>
    <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
        <table cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td class="menuHeader" width="5%"><img src="/ekms/images/system/blank.gif" width="1" height="1"></td>
                <td class="menuHeader" > <c:out value="${title}" /></td>
                <td class="menuHeader" width="18">&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>
<%-- END: Header --%>