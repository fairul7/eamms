<%@ include file="/common/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="bookmarkRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="bookmarkRow">

                    <x:template name="documents" type="TemplateDisplayLatestContent" properties="types=com.tms.cms.document.Document&pageSize=10&hideSummary=true&page=1&pageSize=10" body="custom">
                        <table width="100%">
                        <c:forEach var="co" items="${documents.children}">
                            <tr>
                                <td valign="top" width="10"><img src="<c:url value='/ekms/images/document.gif'/>" border="0"></td>
                                <td valign="top">
                                    <a href="<c:url value='/ekms/content/content.jsp'/>?id=<c:out value="${co.id}"/>" class="tablefontLink"><c:out value="${co.name}"/></a>
                                    <br>
                                    <c:out value="${co.author}"/> <fmt:formatDate pattern="${globalDateLong}" value="${co.date}" />
                                </td>
                            </tr>
                        </c:forEach>
                        </table>
                    </x:template>

                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="bookmarkRow">&nbsp;</td></tr>
    <tr><td class="portletFooter"><img src="images/blank.gif" width="1" height="15">&nbsp;
    </td></tr>
</table>


