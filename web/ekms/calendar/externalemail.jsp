<%@ page import="com.tms.collab.messaging.ui.ComposeMessageForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="jsp_email">
        <com.tms.collab.emeeting.ui.MessageForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardSuccess" value="<%= ComposeMessageForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
    alert('<fmt:message key='messaging.label.emailhasbeensentsuccessfully'/>!');
    document.location = 'calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${widgets['jsp_email.form'].meetingId}" />&instanceId=0';
    //--></script>
</c:if>

<c:if test="${! empty param.meetingId}">
    <x:set name="jsp_email.form" property="meetingId" value="${param.meetingId}" />
</c:if>

<c:if test="${invalidAddress}">
    <script><!--
    alert('<fmt:message key='messaging.label.invalidemailaddressdetected'/>');
    //--></script>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<script>
    <!--
    function selectRecipients(){
        window.open('<c:url value="/ekms/addressbook/messagingContacts.jsp"/>?formId=jsp_email.form',
                'messagingContacts','scrollbars=yes,resizable=yes,width=700,height=500')
        return false;
    }
    //-->
</script>

<script><!--
function doAllowHtml() {
}
function updateEditor() {
    try {
        f = document.forms['jsp_email.form'];
        cb = f.elements['jsp_email.form.allowHtml'];

        if(!cb.checked) {
            editor_setmode('jsp_email.form.body');
        }

    } catch(e) {
    }
}
updateEditor();

//--></script>

<script>
    var set = false;
    function user_editor_focus() {
        if(!set) {
            document.forms['jsp_email.form'].elements['jsp_email.form.to'].focus();
            set = true;
        }
    }
</script>

<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader">Email</td></tr>

    <tr><td >
        <x:display name="jsp_email.form" />
    </td></tr>
</table>
</table>

<%@include file="/ekms/includes/footer.jsp"%>
