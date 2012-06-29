<%@ page import="org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="reply" value="${widget}"/>
<c:if test="${!empty reply.originalMessage}">
    <table cellspacing="0" cellpadding="0" width="100%">
        <jsp:include page="../form_header.jsp" flush="true"/>
        <tr>
            </td>
                <table cellspacing="1" cellpadding="3" width="100%">
                    <tr><td class="qMessagingRowHeader" colspan="2"><fmt:message key='qmessaging.message.originalMessage'/></td></tr>
                    <tr>
                        <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="top"><fmt:message key='qmessaging.message.date'/> </td>
                        <td class="qMessagingRow" width="80%"><fmt:formatDate value="${reply.originalMessage.date}" pattern="${globalDatetimeLong}"/></td>
                    </tr>
                    <c:set var="messageContent" value="${reply.originalMessage.body}" ></c:set>
                    <%
                        String translated = StringUtils.replace((String)pageContext.getAttribute("messageContent"), "\n", "<br>");
                        pageContext.setAttribute("translated", translated);
                    %>
                    <tr>
                        <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="top"><fmt:message key='qmessaging.message.content'/> </td>
                        <td class="qMessagingRow" width="80%">
                            <c:out value="${translated}" escapeXml="false"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            </td>
                <x:display name="${reply.form.absoluteName}"/>
            </td>
        </tr>
        <jsp:include page="../form_footer.jsp" flush="true"/>
    </table>
</c:if>
