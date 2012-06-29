<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ taglib uri="modules.tld" prefix="modules" %>
<%@ page import="com.tms.collab.messaging.model.MessagingModule,
                 kacang.Application,
                 com.tms.collab.messaging.model.Message,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 java.util.Map,
                 com.tms.collab.messaging.ui.MailEncoding,
                 java.util.Iterator,
                 org.apache.commons.lang.StringUtils"
%>
<%
    String encoding = request.getParameter("encoding");
    if(encoding == null || "".equals(encoding))
        encoding = "UTF-8";

    String messageId = request.getParameter("messageId");
    MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
    Message message = null;
    if(!(messageId == null || "".equals(messageId)))
        message = module.getMessageByMessageId(messageId);
    if(message != null)
    {
        request.setAttribute("message", message);
        String newBody = "";
        String newSubject = "";
        try
        {
            newBody = new String(message.getBody().getBytes(), encoding);
            newBody = StringUtils.replace(newBody, "\n", "<br>");
            newSubject = new String(message.getSubject().getBytes(), encoding);
        }
        catch(Exception e) {}
%>
<script>
function showEncoding()
{
    selection = frmEncoding.selEncoding.options[frmEncoding.selEncoding.selectedIndex].value;
    if(selection != '-1')
        document.location="?messageId=<%= messageId %>&encoding=" + selection;
}
</script>
<style>
.header {background:#CCCCCC;text-decoration:none;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;color:#000000;font-weight:bold}
.row {background:#EFEFEF;text-decoration:none;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;color:#000000;font-weight:normal}
.background {background:#EFEFEF}
.outline {background:#999999}
</style>
<html>
    <head>
        <title><%= newSubject %></title>
    </head>
    <body>
        <table cellpadding="1" cellspacing="0" width="100%">
            <tr>
                <td class="outline">
                    <table cellpadding="3" cellspacing="1" width="100%" bgcolor="FFFFFF">
                        <tr>
                            <form name="frmEncoding" method="post">
                                <td align="right" class="header" colspan="2">
                                    <select name="selEncoding" onChange="showEncoding()" class="WCHhider" style="border-width:1pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt; font-weight:normal">
                                        <%
                                            Map map = MailEncoding.getInstance().getEncodings();
                                            for (Iterator i = map.keySet().iterator(); i.hasNext();)
                                            {
                                                String key = (String) i.next();
                                                String value = (String) map.get(key);
                                                if(value.equals("-------------"))
                                                    key = "-1";
                                        %>
                                            <option value="<%= key %>" <%= (encoding.equals(key)) ? "SELECTED" : "" %>><%= value %></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </td>
                            </form>
                        </tr>
                        <tr><td align="left" class="row" colspan="2">&nbsp;</td></tr>
                        <tr>
                            <td class="row" width="25%"><b><fmt:message key='messaging.message.date'/></b></td>
                            <td class="row" width="75%"><fmt:formatDate pattern="${globalDatetimeLong}" value="${message.date}" /></td>
                        </tr>
                        <tr>
                            <td class="row" width="25%"><b><fmt:message key='messaging.message.from'/></b></td>
                            <td class="row" width="75%">
                                <c:if test="${!empty message.toIntranetList}">
                                    <c:out value="${message.toIntranetList}" /><br>
                                </c:if>
                                <c:if test="${!empty message.toList}">
                                    <c:out value="${message.toList}" />
                                </c:if>
                            </td>
                        </tr>
                        <c:if test="${(!empty message.ccIntranetList) || (!empty message.ccList)}">
                            <tr>
                                <td class="row" width="25%"><b><fmt:message key='messaging.message.cc'/></b></td>
                                <td class="row" width="75%">
                                    <c:if test="${!empty message.ccIntranetList}">
                                        <c:out value="${message.ccIntranetList}" /><br>
                                    </c:if>
                                    <c:if test="${!empty message.ccList}">
                                        <c:out value="${message.ccList}" />
                                    </c:if>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="row" width="25%"><b><fmt:message key='messaging.message.subject'/></b></td>
                            <td class="row" width="75%"><%= newSubject %></td>
                        </tr>
                        <tr><td class="row" colspan="2"><b><fmt:message key='messaging.label.messageBody'/></b></td></tr>
                        <tr><td align="left" bgcolor="EFEFEF" colspan="2"><%= newBody %></td></tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>
<%
    }
%>
