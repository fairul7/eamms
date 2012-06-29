<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.Mobile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="mobileChannelList">
        <%--ortlet name="mobileChannelListPortlet" text="<fmt:message key='siteadmin.label.mobileChannelList'/>" width="100%" permanent="true" --%>
            <com.tms.cms.mobile.ui.MobileChannelTable name="mobileChannelTable" width="100%">
                <forward name="add" url="mobileChannelAdd.jsp" />
                <listener_script>
                    if (event.getRequest().getParameter("title") != null) {
                        return new Forward(null, "mobileChannelEdit.jsp?id=" + event.getRequest().getParameter("title"), true);
                    }
                </listener_script>
            </com.tms.cms.mobile.ui.MobileChannelTable>
        <%-- /portlet --%>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="siteadmin.label.mobile"/> > <fmt:message key="siteadmin.label.channelListing"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <%-- x:display name="mobileChannelList.mobileChannelListPortlet" /--%>
    <x:display name="mobileChannelList.mobileChannelTable" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
