<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.Mobile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="mobileChannelEdit">
        <portlet name="mobileChannelEditPortlet" text="<fmt:message key='siteadmin.label.editMobileChannel'/>" width="100%" permanent="true">
            <com.tms.cms.mobile.ui.MobileChannelForm name="mobileChannelForm" width="100%">
                <request_script>
                    String editId = event.getRequest().getParameter("id");
                    if (editId != null) {
                        editForm = wm.getWidget("mobileChannelEdit.mobileChannelEditPortlet.mobileChannelForm");
                        editForm.setId(event.getRequest().getParameter("id"));
                    }
                </request_script>
            </com.tms.cms.mobile.ui.MobileChannelForm>
        </portlet>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="siteadmin.label.mobile"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="mobileChannelEdit.mobileChannelEditPortlet" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
