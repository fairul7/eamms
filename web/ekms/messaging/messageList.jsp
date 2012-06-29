<%@ page import="java.util.ResourceBundle,
                 java.text.MessageFormat,
                 com.tms.collab.messaging.model.MessagingQueue,
                 com.tms.collab.messaging.ui.MessageTable,
                 com.tms.collab.messaging.model.Folder"%>
<%@include file="includes/taglib.jsp" %>

<x:config>
    <page name="messageListPage">
        <com.tms.collab.messaging.ui.MessageTable name="table"/>
		<com.tms.collab.messaging.ui.MessageTable name="tableForOutbox" />
    </page>
</x:config>

<c:choose>
<c:when test="${forward.name == 'success'}">
    <c:redirect url="messageList.jsp" />
    <% if (true) return; %>
</c:when>
<c:when test="${forward.name == 'error'}">
    <c:redirect url="messageList.jsp" />
</c:when>
<c:when test="${(!empty param.messageId) && (param.cn != 'messageListPage.tableForOutbox') }">
	<c:choose>
		<c:when test="${!empty param.index}">
			<c:set var="theUrl" value="readMessage.jsp?messageId=${param.messageId}&index=${param.index}" />
		</c:when>
		<c:otherwise>
			<c:set var="theUrl" value="readMessage.jsp?messageId=${param.messageId}" />
		</c:otherwise>
	</c:choose>
    <c:redirect url="${theUrl}" />
</c:when>
<c:when test="${(!empty param.messageId) && (param.cn == 'messageListPage.tableForOutbox') }">
	<c:redirect url="composeMessage.jsp?messageId=${param.messageId}" />
</c:when>
</c:choose>

<%
    ResourceBundle rb = Application.getInstance().getResourceBundle();
    MessageFormat mf = new MessageFormat("");
    MessagingQueue mq = MessagingQueue.getInstance();
    String colorCode = "#00DD00";

    String mqMessage = rb.getString("messaging.label.queue.idle");

    if(mq.getCheckPop3Size()>0 || mq.getDownloadPop3Size()>0 || mq.getSendSmtpSize()>0) {
        Object queueArgs[] = {
            new Integer(mq.getCheckPop3Size()),
            new Integer(mq.getDownloadPop3Size()),
            new Integer(mq.getSendSmtpSize())
        };
        colorCode = "#DDDD00";
        mf.applyPattern(rb.getString("messaging.label.queue.queued"));
        mqMessage = mf.format(queueArgs);
    }

    if(mq.getCheckPop3Current()>0 || mq.getDownloadPop3Current()>0 || mq.getSendSmtpQueueCurrent()>0) {
        Object[] busyArgs = {
            new Integer(mq.getCheckPop3Current()),
            new Integer(mq.getDownloadPop3Current()),
            new Integer(mq.getSendSmtpQueueCurrent())
        };
        colorCode = "#DD0000";
        mf.applyPattern(rb.getString("messaging.label.queue.busy"));
        mqMessage = mf.format(busyArgs);
    }

%>

<%@include file="includes/header.jsp" %>

<script type="text/javascript" src="<c:url value="/common/dragndrop/wz_dragdrop.js"/>"></script>

<c:choose>
	<c:when test="${param.folder == 'Outbox'}">
		<c:set var="messageListTableHtml">
			<x:display name="messageListPage.tableForOutbox" body="/ekms/messaging/messageListTable.jsp" />
		</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="messageListTableHtml">
			<x:display name="messageListPage.table" body="/ekms/messaging/messageListTable.jsp" />
		</c:set>
	</c:otherwise>
</c:choose>
<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
      <table cellpadding="0" cellspacing="0" width="100%"><tr><td><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;  <fmt:message key='messaging.label.messageListing'/> &gt; 
      
	  <c:choose>
      	<c:when test="${param.folder == 'Outbox'}">
      		<c:out value="${widgets['messageListPage.tableForOutbox.filterForm.selectFolder'].selectedFolderName}"/>
      	</c:when>
      	<c:otherwise>
			<c:out value="${widgets['messageListPage.table.filterForm.selectFolder'].selectedFolderName}"/>
	  	</c:otherwise>
	  </c:choose>      

      </font></b></td><td align="right"><font color="#FFCF63" class="contentTitleFont">

        <c:set var="warnThreshold" value="80" />
        <c:if test="${widgets['messageListPage.table'].currentPercent gt warnThreshold}">
        <script>
        function doWarnQuota() {
            alert("<fmt:message key='messaging.label.quotaWarningMessage'/>");
            return false;
        }
        </script>
        <a href="" onclick="return doWarnQuota()"  style="text-decoration:none"><font color="red"></c:if>
        <fmt:message key='messaging.label.quotaStatus'>
            <fmt:param><c:out value="${widgets['messageListPage.table'].currentPercent}" /></fmt:param>
            <fmt:param><fmt:formatNumber maxFractionDigits="2" value="${widgets['messageListPage.table'].currentUsage / 1024}" /></fmt:param>
            <fmt:param><fmt:formatNumber maxFractionDigits="2" value="${widgets['messageListPage.table'].userQuota / 1024}" /></fmt:param>
        </fmt:message>
        <c:if test="${widgets['messageListPage.table'].currentPercent gt warnThreshold}"></font></a></c:if>
      </font></td></tr></table>

    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;
<%--    <b><font face="Verdana, Arial" color="<%= colorCode %>"><a onclick="alert('<fmt:message key="messaging.label.queue.accountLog" />')" style="cursor:hand" title="<%= mqMessage %>"><fmt:message key="messaging.label.queue.status" /></a></font></b>--%>
    &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <c:out value="${messageListTableHtml}" escapeXml="false"/>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>




<%@include file="includes/footer.jsp" %>

<c:if test="${DRAG_ENABLED && !empty dragKeyList}">
<c:set var="formCn" value="messageListPage.table"/>
<c:set var="checkboxCn" value="messageListPage.table.sel"/>
<form name="messageList.dragDropForm" method="POST" action="<c:url value="/ekms/messaging/messageList.jsp"/>">
<input type="hidden" name="cn" value="<c:out value="${formCn}"/>">
<input type="hidden" name="et" value="<%= MessageTable.EVENT_DRAG_DROP %>">
<input type="hidden" name="targetFolder" value="">
<input type="hidden" name="id" value="">
</form>
<script type="text/javascript">
<!--
    SET_DHTML(CURSOR_HAND<c:out value="${dropFolderList}" escapeXml="false" />,<c:out value="${dragKeyList}" escapeXml="false" />);

    function my_DropFunc() {
        try {
            // find target
            for (i=0; i<dd.elements.length; i++) {
                var el = dd.elements[i];
                var src = dd.obj.name;
                var folder = el.name;
                if (folder.indexOf("folder_") == 0) {
                    if (my_DropFunc_InTarget(dd.obj, el)) {
                        if (confirm("<fmt:message key='messaging.label.moveselectedmessagestoselectedfolder'/>")) {
                            var ids = src;
                            var cbs = document.forms['<c:out value="${formCn}"/>'].elements['<c:out value="${checkboxCn}"/>'];
                            for (i=0; i<cbs.length; i++) {
                                if (cbs[i].checked && cbs[i].value != src) {
                                    ids += "," + cbs[i].value;
                                }
                            }
                            var dragDropForm = document.forms['messageList.dragDropForm'];
                            dragDropForm.elements['targetFolder'].value = folder;
                            dragDropForm.elements['id'].value = ids;
                            dragDropForm.submit();
                        }
                        break;
                    }
                }
            }
        }
        catch (e) {
        }
        // move source back to original position
        dd.obj.moveTo(dd.obj.defx, dd.obj.defy);
    }

    function my_DropFunc_InTarget(src, target) {
    //    alert (src.x + ", " + src.y + ", " + target.x + ", " + target.y + ", " + (target.x + target.w) + ", " + (target.y + target.h));
        return (src.x >= target.x && src.x <= (target.x + target.w) && src.y >= target.y && src.y <= (target.y + target.h));
    }

//-->
</script>
</c:if>
