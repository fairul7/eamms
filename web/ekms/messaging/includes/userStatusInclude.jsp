<%@include file="taglib.jsp" %>

<x:config>
    <page name="userStatus">
        <com.tms.collab.messaging.ui.MessagingStatusWidget name="status" />
    </page>
</x:config>

<c:set var="bodyContents">
    <x:display name="userStatus.status" body="custom">
    <c:set var="mus" value="${widget.mus}" />
    <c:set var="trackerCheck" value="${mus.trackerCheck}" />
    <c:set var="trackerDownload" value="${mus.trackerDownload}" />
    <c:set var="trackerSend" value="${mus.trackerSend}" />
    <c:set var="trackerServerView" value="${mus.trackerServerView}" />

    <c:if test="${widget.mus.pop3Busy || widget.mus.sendBusy || trackerDownload.status == 'pending'}">
        <script language="JavaScript">
        function setTimer() {
            setTimeout('move()', 1000);
        }
        </script>
    </c:if>
    <c:if test="${!(widget.mus.pop3Busy || widget.mus.sendBusy)}">
        <script language="JavaScript">
        function setTimer() {
            setTimeout('move()', 3000);
        }
        </script>
    </c:if>
    <script language="JavaScript">
        function move() {
            dhtmlLoadScript('userStatus.js.jsp');
        }
        function dhtmlLoadScript(url)
        {
           var e = document.createElement("script");
           e.src = url;
           e.type="text/javascript";
           document.getElementsByTagName("body")[0].appendChild(e);
        }
    </script>
    <table border="0" cellpadding="2" cellspacing="0">
    <c:if test="${trackerDownload.status ne 'notStarted'}">
        <c:set var="activity" value="${true}"/>
        <tr>
            <td valign="top">
            <fmt:message key="messaging.label.statusDownloadPop3"/>
            </td>
            <td>
            <c:if test="${trackerDownload.status eq 'processing'}">
                <table width="300" cellspacing="0" cellpadding="0">
                <tr><td height="10" width="<c:out value="${trackerDownload.progressPercentage}" />%" bgcolor="blue"></td><td bgcolor="#eeeeee">&nbsp;</td></tr>
                </table>
            </c:if>
            <c:out value="${trackerDownload.message}" />
            <c:if test="${trackerDownload.status ne 'pending' && trackerDownload.status ne 'processing' }">
                - <a href="userStatus.jsp?clear=download"><fmt:message key="messaging.label.clear"/></a>
            </c:if>
            </td>
        </tr>
    </c:if>
<%--
    <c:if test="${trackerCheck.status ne 'notStarted'}">
        <tr>
            <td valign="top">
            <fmt:message key="messaging.label.statusCheckPop3"/>
            </td>
            <td>
            <c:if test="${trackerCheck.status eq 'processing'}">
                <table width="300" cellspacing="0" cellpadding="0">
                <tr><td height="5" width="<c:out value="${trackerCheck.progressPercentage}" />%" bgcolor="blue"></td><td bgcolor="#eeeeee">&nbsp;</td></tr>
                </table>
            </c:if>
            <c:out value="${trackerCheck.message}" />
            <c:if test="${trackerCheck.status ne 'processing'}">
                - <a href="userStatus.jsp?clear=check"><fmt:message key="messaging.label.close"/></a>
            </c:if>
            </td>
        </tr>
    </c:if>
    <c:if test="${trackerServerView.status ne 'notStarted'}">
        <tr>
            <td valign="top">
            <fmt:message key="messaging.label.statusServerView"/>
            </td>
            <td>
            <c:if test="${trackerServerView.status eq 'processing'}">
                [<c:out value="${trackerServerView.status}" /> <c:out value="${trackerServerView.progressPercentage}" />%]
            </c:if>
            <c:if test="${trackerServerView.status ne 'processing'}">
                [<c:out value="${trackerServerView.status}" />]
            </c:if>
            <c:out value="${trackerServerView.message}" />
            <c:if test="${trackerServerView.status ne 'processing'}">
                - <a href="userStatus.jsp?clear=serverView"><fmt:message key="messaging.label.close"/></a>
            </c:if>
            </td>
        </tr>
    </c:if>
--%>
    <c:if test="${trackerSend.status ne 'notStarted'}">
        <c:set var="activity" value="${true}"/>
        <tr>
            <td valign="top">
            <fmt:message key="messaging.label.statusSend"/>
            </td>
            <td>
            <c:if test="${trackerSend.status eq 'processing'}">
                <table width="300" cellspacing="0" cellpadding="0">
                <tr><td height="5" width="<c:out value="${trackerSend.progressPercentage}" />%" bgcolor="blue"></td><td bgcolor="#eeeeee"></td></tr>
                </table>
            </c:if>
            <c:out value="${trackerSend.message}" />
            <c:if test="${trackerSend.status ne 'processing'}">
                - <a href="userStatus.jsp?clear=send"><fmt:message key="messaging.label.clear"/></a>
            </c:if>
            </td>
        </tr>
    </c:if>

    <c:if test="${!activity}">
        <tr>
            <td valign="top" colspan="2">
            <fmt:message key="messaging.label.noActivity"/>
            </td>
        </tr>
    </c:if>

    </table>

    </x:display>
    <script language="JavaScript">
        setTimer();
    </script>
</c:set>