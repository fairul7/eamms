<%@ page import="com.tms.collab.directory.ui.ContactMessagingTable,
                 com.tms.collab.directory.ui.Util,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="messagingContacts">
    <tabbedpanel name="tab1" width="95%">
    <panel name="panel1" text="Intranet Users">
        <com.tms.collab.directory.ui.UserMessagingTable name="table2" sort="username" />
    </panel>
    <panel name="panel2" text="Business Directory">
        <com.tms.collab.directory.ui.DirectoryContactMessagingTable name="table3" sort="firstName" />
    </panel>
    <panel name="panel3" text="Personal Contacts">
        <com.tms.collab.directory.ui.ContactMessagingTable name="table1" sort="firstName" />
    </panel>
    </tabbedpanel>
    </page>
</x:config>

<c:set var="show" value="${false}"/>
<c:if test="${!empty param.formId}">
    <c:set var="formId" value="${param.formId}" scope="session" />
</c:if>
<c:set var="toId" value="${formId}.to"/>
<c:set var="ccId" value="${formId}.cc"/>
<c:set var="bccId" value="${formId}.bcc"/>

<c-rt:set var="toAttribute" value="<%= Util.TO_ATTRIBUTE %>"/>
<c-rt:set var="ccAttribute" value="<%= Util.CC_ATTRIBUTE %>"/>
<c-rt:set var="bccAttribute" value="<%= Util.BCC_ATTRIBUTE %>"/>

<c-rt:set var="forwardTo" value="<%= ContactMessagingTable.FORWARD_TO %>"/>
<c-rt:set var="forwardCc" value="<%= ContactMessagingTable.FORWARD_CC %>"/>
<c-rt:set var="forwardBcc" value="<%= ContactMessagingTable.FORWARD_BCC %>"/>
<c-rt:set var="forwardError" value="<%= ContactMessagingTable.FORWARD_ERROR %>"/>

<script>
<!--
    function appendEmails(obj, emails) {
        var val = obj.value;
        val.trim;
        var len = val.length;
        var lastIndex = val.lastIndexOf(",", len);

        if (lastIndex < (len-1)) {
            obj.value += ",";
        }
        obj.value += emails;
    }
//-->
</script>
<c:if test="${forward.name eq forwardTo}">
    <%
        String value = (String) session.getAttribute("toAttribute");
        String escaped = com.tms.collab.messaging.model.Util.escapeJavaScript(value);
        pageContext.setAttribute("escaped", escaped);
    %>
    <script>
    <!--
        var obj = window.opener.document.forms['<c:out value="${formId}" />'].elements['<c:out value="${toId}"/>'];
        appendEmails(obj, '<c:out value="${pageScope.escaped}" escapeXml="false" />');
        alert("<fmt:message key='addressbook.label.contactsaddedtoTOfield'/>");
    //-->
    </script>
    <%
    session.removeAttribute(Util.TO_ATTRIBUTE);
    %>
</c:if>
<c:if test="${forward.name eq forwardCc}">
    <script>
    <!--
        var obj = window.opener.document.forms['<c:out value="${formId}"/>'].elements['<c:out value="${ccId}"/>'];
        appendEmails(obj, '<c:out value="${sessionScope[ccAttribute]}" escapeXml="false" />');
        alert("<fmt:message key='addressbook.label.contactsaddedtoCCfield'/>");
    //-->
    </script>
    <%
        session.removeAttribute(Util.CC_ATTRIBUTE);
    %>
</c:if>
<c:if test="${forward.name eq forwardBcc}">
    <script>
    <!--
        var obj = window.opener.document.forms['<c:out value="${formId}"/>'].elements['<c:out value="${bccId}"/>'];
        appendEmails(obj, '<c:out value="${sessionScope[bccAttribute]}" escapeXml="false" />');
        alert("<fmt:message key='addressbook.label.contactsaddedtoBCCfield'/>");
    //-->
    </script>
    <%
        session.removeAttribute(Util.BCC_ATTRIBUTE);
    %>
</c:if>
<c:if test="${forward.name eq forwardError}">
    <script>
    <!--
        alert("Error adding contacts: <c:out value='${sessionScope.error.message}'/>");
    //-->
    </script>
    <%
        session.removeAttribute(Util.CC_ATTRIBUTE);
    %>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.selectRecipients'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <c:if test="${show}">
            <ul>
                <li>
                <fmt:message key='addressbook.label.to'/>:
                <c:out value="${sessionScope[toAttribute]}"/>
                </li>

                <li>
                <fmt:message key='addressbook.label.cC'/>:
                <c:out value="${sessionScope[ccAttribute]}"/>
                </li>

                <li>
                <fmt:message key='addressbook.label.bCC'/>:
                <c:out value="${sessionScope[bccAttribute]}"/>
                </li>
            </ul>
        </c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor" align="center">
        <x:display name="messagingContacts.tab1" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
</body>
</html>
