<%@ page import="com.tms.collab.messaging.ui.ComposeMessageForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="includes/taglib.jsp" %>

<x:config>
    <page name="composeMessagePage">
        <com.tms.collab.messaging.ui.MComposeMessageForm name="form2"/>
    </page>
</x:config>

<c-rt:set var="forwardError" value="<%= ComposeMessageForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>


<jsp:include page="../includes/mheader.jsp"/>
 <jsp:include page="/ekms/init.jsp"/>
 <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
 <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
 <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">




<c-rt:set var="forwardSuccess" value="<%= ComposeMessageForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
        alert('<fmt:message key='messaging.label.emailhasbeensentsuccessfully'/>!');
        document.location = '../home.jsp';
    //--></script>
</c:if>

<c:if test="${forward.name ne forwardSuccess}">
    <c-rt:set var="forwardDraft" value="<%= ComposeMessageForm.FORWARD_DRAFT_SAVED %>" />
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

    <table id="messageForm" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr valign="MIDDLE">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
          &nbsp;<fmt:message key='messaging.label.composeMessage'/>
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
        window.open('<c:url value="/ekms/addressbook/messagingContacts.jsp"/>?formId=composeMessagePage.form',
        'messagingContacts','scrollbars=yes,resizable=yes,width=700,height=500')
        return false;
    }
//-->
</script>
            <x:display name="composeMessagePage.form2" />

            <script><!--
                function doAllowHtml() {
                }

                function updateEditor() {
                    try {
                        f = document.forms['composeMessagePage.form'];
                        cb = f.elements['composeMessagePage.form.allowHtml'];

                        if(!cb.checked) {
                            editor_setmode('composeMessagePage.form.body');
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

<script>
    var set = false;
    function user_editor_focus() {
        if(!set) {
            document.forms['composeMessagePage.form'].elements['composeMessagePage.form.to'].focus();
            set = true;
        }
    }
</script>


<jsp:include page="../includes/mfooter.jsp"/>

<%--
   body.onload = toFocus();
   function toFocus() {
    document.forms['composeMessagePage.form'].elements['composeMessagePage.form.to'].focus();
  }
--%>
