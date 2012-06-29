<%@include file="includes/taglib.jsp" %>
<%@ page import="com.tms.collab.messaging.ui.NewFolderForm,
                 java.util.Enumeration,
                 com.tms.collab.messaging.model.MessagingModule,
                 java.util.Map,
                 java.util.List,
                 com.tms.collab.messaging.ui.ServerView,
                 com.tms.collab.messaging.ui.MessagingStatusWidget,
                 com.tms.collab.messaging.model.MessagingUserStatus,
                 com.tms.collab.messaging.model.ProgressTracker"%>

<x:config>
    <page name="serverView">
        <com.tms.collab.messaging.ui.ServerView name="form"/>
    </page>
</x:config>

<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
        Server View
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">

        <x:display name="serverView.form" body="custom">

<%--

IMPORTANT: The following text was "copy, paste 'n edited" from
http://www.firetrust.com/support/pro/faq.php?section=overview.
Please edit before production.
<p>
"Server View" works directly with your email server, exactly like your
email program does. But there is one important difference: "Server View"
allows you to delete a message at the mail server, without downloading
it.
<p>
If you check your accounts via "Server View" first, you can delete the
email you do not want. Then, when you "Download POP3 Email", it
downloads only the remaining email, those that you want to read.
<p>
"Server View" can be thought of as a "first line of defense" which weeds
out junk, large wasteful attachments and potentially harmful viruses.

// TODO: LAST STOP
// TODO: preview email header no wrap in IE?
// TODO: no email no show header row
--%>
            <table cellpadding="5" cellspacing="0" border="0" width="100%">
                <tr valign="top">
                    <td class="contentBgColor">
                        <x:display name="${widget.sbPop3Account.absoluteName}" />
                        <x:display name="${widget.btCheck.absoluteName}" />
                        <x:display name="${widget.btCheckAll.absoluteName}" />
                        <x:display name="${widget.cbPreview.absoluteName}" />
                        <hr size="1">
                        <c:if test="${!empty widget.errorMessage}">
                            <font color="red"><c:out value="${widget.errorMessage}" /></font><br>
                        </c:if>
                        <c:if test="${!widget.serverViewBusy && !empty widget.viewUpdate}">
                            This server view was updated at <b><fmt:formatDate value="${widget.viewUpdate}" pattern="${globalDatetimeLong}" />.</b><br>
                        </c:if>
                        <c:if test="${widget.serverViewBusy}">
                            Server view is busy processing emails.<br>
                        </c:if>
                    </td>
                </tr>

                <c:if test="${widget.cbPreview.checked}">
                <script>
                    function doPreview(divId) {
                        e = document.getElementById(divId);

                        myWin = open("", "previewWindow",
                            "top=0, left=0, width=600,height=500,status=no,toolbar=no,menubar=no,resize=yes");

                        myWin.document.open();
                        myWin.document.write("<html><head><title>Email Preview</title></head><body onload='focus()' onblur='window.close()'><pre>");
                        myWin.document.write(e.innerHTML);
                        myWin.document.write("</pre></body></html>");
                        myWin.document.close();

                        return false;
                    }
                </script>
                </c:if>

                <%-- show server view data if server view is not busy --%>
                <c:if test="${!widget.serverViewBusy && forward.name != 'process'}">
                    <c:forEach var="item" items="${widget.dataMap}">
                        <%
                            List messageSummaryList;
                            messageSummaryList = (List) ((Map.Entry)pageContext.getAttribute("item")).getValue();
                            int noMails = messageSummaryList.size();
                            int height = 0;

                            if(noMails>=10) {
                                height = 400;
                            } else {
                                height = 30 + (noMails*40);
                            }
                        %>
                        <tr valign="top">
                            <td class="contentBgColor">
                                <br>
                                <b>Account Name: <c:out value="${item.key.name}" /></b> (<%= noMails %> emails)
                                <br>
                                <div align="right">
                                    <input type="button" class="button" value="Delete From Server">
                                </div>
                                <div style="height:<%= height %>; width:100%; overflow: auto;">
                                    <table cellpadding="2" cellspacing="1" border="0" width="100%">
                                        <tr valign="top" bgcolor="#efefef">
                                            <td> <b>No</b> </td>
                                            <td> <b>Subject</b> </td>
                                            <td> <b>Date</b> </td>
                                            <td> <b>Size</b> </td>
                                            <td> <input type="checkbox" name="<c:out value="${item.key.id}" />" value="1"> </td>
                                        </tr>
    <c:forEach var="msg" items="${item.value}" varStatus="status" >
        <c:if test="${status.index % 2 == 0}"><c:set var="color" value="#ffffff" /></c:if>
        <c:if test="${status.index % 2 == 1}"><c:set var="color" value="#efefef" /></c:if>
        <tr valign="top" bgcolor="<c:out value="${color}" />">
            <td rowspan="2"> <c:out value="${status.index+1}" />. </td>
            <c:if test="${widget.cbPreview.checked}">
                <td> <b><a href="" onclick="return doPreview('preview<c:out value="${msg.messageId}" />')"><c:out value="${msg.subject}" default="-no subject-" /></a></b> </td>
            </c:if>
            <c:if test="${!widget.cbPreview.checked}">
                <td> <b><c:out value="${msg.subject}" default="-no subject-" /></b> </td>
            </c:if>
            <td rowspan="2" nowrap> <fmt:formatDate value="${msg.receivedDate}" pattern="${globalDatetimeLong}" />&nbsp; </td>
            <td rowspan="2">
                <c:if test="${msg.size < 256*1024}"><c:out value="${msg.size}" /></c:if>
                <c:if test="${msg.size >= 256*1024 && msg.size < 1024*1024}"><font color="#BB0000"><c:out value="${msg.size}" /></font></c:if>
                <c:if test="${msg.size >= 1024*1024}"><b><font color="#BB0000"><c:out value="${msg.size}" /></font></b></c:if>
            &nbsp; </td>
            <td rowspan="2"> <input type="checkbox" name="messageId" value="<c:out value="${msg.messageId}" />">&nbsp; </td>
        </tr>
        <tr valign="top" bgcolor="<c:out value="${color}" />">
            <td> <i>From: <c:out value="${msg.from}" default="-not specified-" />&nbsp;</i> </td>
        </tr>
        <c:if test="${widget.cbPreview.checked}">
            <div id="preview<c:out value="${msg.messageId}" />" style="display:none">
            <b>Subject: <c:out value="${msg.subject}" default="-no subject-" /></b><hr size="1">
            <c:out value="${msg.preview}" />
            </div>
        </c:if>
    </c:forEach>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>

        </x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
