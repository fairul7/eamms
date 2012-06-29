<%@ page import="com.tms.collab.messaging.ui.ComposeMessageForm,
                 java.util.Enumeration"%>
<%@include file="includes/taglib.jsp" %>

<x:config>
    <page name="composeMessagePage">
    	<c:choose>
    		<c:when test="${!empty param.messageId}">
    			<com.tms.collab.messaging.ui.EditComposeMessageForm name="form" />
    		</c:when>
    		<c:otherwise>
        		<com.tms.collab.messaging.ui.ComposeMessageForm name="form"/>
        	</c:otherwise>
       	</c:choose>
    </page>
</x:config>

<c-rt:set var="forwardError" value="<%= ComposeMessageForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>

<c-rt:set var="forwardSuccess" value="<%= ComposeMessageForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
        alert('<fmt:message key='messaging.label.emailhasbeensentsuccessfully'/>!');
        document.location = 'messageList.jsp';
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
          &nbsp;
          <c:choose>
          	<c:when test="${! empty param.messageId}">
	          	<fmt:message key='messaging.label.editComposeMessage' />
          	</c:when>
          	<c:otherwise>
    			<fmt:message key='general.label.messaging'/> > <fmt:message key='messaging.label.composeMessage' />     	
          	</c:otherwise>
          </c:choose>
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

<x:template type="com.tms.collab.messaging.ui.QuotaCheck" />
<c:if test="${!exceedQuota}">
    <x:display name="composeMessagePage.form" />
    <c:if test="${!widgets['composeMessagePage.form'].htmlEmail}">
        <script language="JavaScript">
        <!--
            function composeMessagePage_form_bodyonLoad(editor) {
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