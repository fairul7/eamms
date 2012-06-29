<%@ page import="com.tms.collab.directory.ui.AddContactMessagingForm"%>
<%@include file="includes/taglib.jsp" %>


<x:config>
    <page name="saveContactsPage">
        <com.tms.collab.directory.ui.AddContactMessagingForm name="form"/>
    </page>
</x:config>


<c-rt:set var="forwardSuccess" value="<%= AddContactMessagingForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script>
    <!--
        alert("<fmt:message key='messaging.label.contactsSaved'/>");
        location.href='<c:url value="readMessage.jsp?messageId=${widgets['saveContactsPage.form'].messageId}&index=${widgets['saveContactsPage.form'].theIndex}" />';
    //-->
    </script>
    <%-- c:redirect url="readMessage.jsp?messageId=${widgets['saveContactsPage.form'].messageId}&index=${widgets['saveContactsPage.form'].theIndex}" /--%>
</c:if>

<c-rt:set var="forwardCancel" value="<%= AddContactMessagingForm.CANCEL_FORM_ACTION %>"/>
<c:if test="${forward.name eq forwardCancel}">
    <c:redirect url="readMessage.jsp?messageId=${widgets['saveContactsPage.form'].messageId}&index=${widgets['saveContactsPage.form'].theIndex}" />
</c:if>

<x:set name="saveContactsPage.form" property="messageId" value="${param.messageId}"/>
<x:set name="saveContactsPage.form" property="theIndex" value="${param.index}"/>

<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.saveContacts'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="saveContactsPage.form" body="custom">

            <fmt:message key="messaging.label.saveContactsMessage"/>
            &nbsp;
            <x:display name="${widget.childMap.folderSelectBox.absoluteName}"/>

            <hr size="1" style="border:dotted 1px silver">

            <c:choose>
            <c:when test="${empty widget.contactFormList[0]}">
                <fmt:message key="messaging.label.noContacts"/>
                <p>
                <x:display name="${widget.childMap.buttonPanel.childMap.cancel_form_action.absoluteName}"/>
                </p>
            </c:when>
            <c:otherwise>
                <table>
                <c:forEach items="${widget.contactFormList}" var="subform">
                    <tr>
                    <td>
                        <x:display name="${subform.absoluteName}"/>
                        <hr size="1" style="border:dotted 1px silver">
                    </td>
                    </tr>
                </c:forEach>
                </table>
                <p>
                <x:display name="${widget.childMap.buttonPanel.absoluteName}"/>
                </p>
            </c:otherwise>
            </c:choose>

        </x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
