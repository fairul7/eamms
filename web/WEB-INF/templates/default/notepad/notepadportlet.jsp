<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<div id="notepad_<c:out value="${widget.absoluteName}" />" width="100%">
    <table cellspacing="0" cellpadding="0" width="100%">
        <jsp:include page="../form_header.jsp" flush="true"/>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="10"></td></tr>
        <tr>
            <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor" align="center">
                <table cellspacing="0" cellpadding="0" width="90%">
                    <tr BGCOLOR="#EFEFEF" CLASS="contentBgColor" align="center">
                        <td id="td" colspan="2">
                            <c:if test="${widget.newPage}"><span style="border: solid 1px"></c:if>
                                <x:display name="${widget.notepad.absoluteName}" />
                            <c:if test="${widget.newPage}"></span></c:if>
                        </td>
                    </tr>
                    <tr bgcolor="#EFEFEF"><td colspan="2" valign="TOP" class="contentBgColor">&nbsp;</td></tr>
                    <tr BGCOLOR="#EFEFEF" CLASS="contentBgColor" align="center">
                        <td align="left" ><c:if test="${! widget.newPage}">Page <c:out value="${widget.currentPageNum}"/></c:if></td>
                        <td align="center">
                            <x:display name="${widget.saveButton.absoluteName}" />
                            <x:display name="${widget.clearButton.absoluteName}" />
                            <x:display name="${widget.deleteButton.absoluteName}" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr bgcolor="#EFEFEF"><td colspan="2" valign="TOP" class="contentBgColor">&nbsp;</td></tr>
        <tr>
            <td CLASS="portletFooter" colspan="2" align="left">
                &nbsp;
                <x:display name="${widget.newPageButton.absoluteName}" />
                <c:forEach items="${widget.pages}"var="button" >
                    <x:display name="${button.absoluteName}" />
                </c:forEach>
            </td>
        </tr>
        <jsp:include page="../form_footer.jsp" flush="true"/>
    </table>
</div>
<%--<script>
<!--
    function initNotepad() {
        var notepad = document.getElementById("notepad_<c:out value="${widget.absoluteName}" />");
        var width = notepad.offsetWidth-40;
        if (width > 400) {
            width = 400;
        }
        if (width > 0) {
            var notewidget = document.forms['<c:out value="${widget.absoluteName}" />'].elements['<c:out value="${widget.notepad.absoluteName}" />'];
            notewidget.style.width = width;
        }
    }
    window.onload=initNotepad;
//-->
</script>--%>

