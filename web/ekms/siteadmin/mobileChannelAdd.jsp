<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.Mobile" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="mobileChannelAdd">
        <%-- portlet name="mobileChannelAddPortlet" text="<fmt:message key='siteadmin.label.newMobileChannel'/>" width="100%" permanent="true" --%>
            <com.tms.cms.mobile.ui.MobileChannelForm name="mobileChannelForm" width="100%">
                <forward name="saved" url="mobileChannelList.jsp" />
            </com.tms.cms.mobile.ui.MobileChannelForm>
        <%-- /portlet --%>
    </page>
</x:config>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="siteadmin.label.mobile"/> > <fmt:message key="siteadmin.label.addNewChannel"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

	<%-- 
    <x:display name="mobileChannelAdd.mobileChannelAddPortlet" />
	--%>
	<x:display name="mobileChannelAdd.mobileChannelForm" />
	
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
