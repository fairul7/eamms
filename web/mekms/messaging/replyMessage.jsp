<%@include file="includes/taglib.jsp" %>
<%@ page import="com.tms.collab.messaging.ui.ReplyMessageForm"%>

<x:config>
    <page name="replyMessagePage">
        <com.tms.collab.messaging.ui.ReplyMessageForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardError" value="<%= ReplyMessageForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>

<c-rt:set var="forwardSuccess" value="<%= ReplyMessageForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
        alert('<fmt:message key='messaging.label.emailhasbeensentsuccessfully'/>!');
        document.location = 'messageList.jsp';
    //--></script>
</c:if>

<c:if test="${forward.name ne forwardSuccess}">
    <c-rt:set var="forwardDraft" value="<%= ReplyMessageForm.FORWARD_DRAFT_SAVED %>" />
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
      &nbsp;<fmt:message key='messaging.label.replyMessage'/>
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
        window.open('<c:url value="/ekms/addressbook/messagingContacts.jsp"/>?formId=replyMessagePage.form',
        'messagingContacts','scrollbars=yes,resizable=yes,width=700,height=500')
        return false;
    }
//-->
</script>
        <x:display name="replyMessagePage.form" />
        <textarea style="display:none" id="tohide" rows="1" cols="1"><c:out value="${widgets['replyMessagePage.form.body'].value}"/></textarea>
<script><!--
    function doAllowHtml() {
    }

    function updateEditor() {
        try {
              // wait until document is fully loaded
              if (document.readyState != 'complete') {
                setTimeout(function() { updateEditor() }, 25);
                return;
              }

            f = document.forms['replyMessagePage.form'];
            cb = f.elements['replyMessagePage.form.allowHtml'];

            if(!cb.checked) {
                editor_setmode('replyMessagePage.form.body');
                document.getElementById("_replyMessagePage.form.body_editor").value=document.getElementById("tohide").value;
            }
        } catch(e) {
        }
    }

    updateEditor();
//--></script>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


</c:if>



<%@include file="includes/footer.jsp" %>
