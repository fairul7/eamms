<%@include file="includes/taglib.jsp" %>
 <%@ page import="com.tms.collab.messaging.ui.ForwardMessageForm"%>

<x:config>
    <page name="forwardMessagePage">
        <com.tms.collab.messaging.ui.ForwardMessageForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardError" value="<%= ForwardMessageForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>

<c-rt:set var="forwardSuccess" value="<%= ForwardMessageForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
        alert('<fmt:message key='messaging.label.emailhasbeensentsuccessfully'/>!');
        document.location = 'messageList.jsp';
    //--></script>
</c:if>

<c:if test="${forward.name ne forwardSuccess}">
    <c-rt:set var="forwardDraft" value="<%= ForwardMessageForm.FORWARD_DRAFT_SAVED %>" />
    <c:if test="${forward.name eq forwardDraft}">
    <script><!--
        alert('<fmt:message key='messaging.label.acopyofthisemailhasbeensavedtotheDraftfolder'/>');
    //--></script>
    </c:if>

    <c:if test="${invalidAddress}">
        <script><!--
            alert('<fmt:message key='messaging.label.invalidemailaddressdetected'/>');
        //--></script>
    </c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.forwardMessage'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
<script>
<!--
    function selectRecipients(){
        window.open('<c:url value="/ekms/addressbook/messagingContacts.jsp"/>?formId=forwardMessagePage.form',
        'messagingContacts','scrollbars=yes,resizable=yes,width=700,height=500')
        return false;
    }
//-->
</script>

<x:template type="com.tms.collab.messaging.ui.QuotaCheck" />
<c:if test="${!exceedQuota}">
        <x:display name="forwardMessagePage.form" />
        <c:if test="${!widgets['forwardMessagePage.form'].htmlEmail}">
            <script language="JavaScript">
            <!--
                function forwardMessagePage_form_bodyonLoad(editor) {
                    editor._editMode = "textmode";
                    editor.setMode("textmode");
                }
            //-->
            </script>
        </c:if>
</c:if>


    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
</c:if>


<%@include file="includes/footer.jsp" %>
