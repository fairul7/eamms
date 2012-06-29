<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.RateCardTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.permission.rateCard" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="fms">         
            <com.tms.fms.facility.ui.RateCardTable name="table" />        
    </page>
</x:config>

<c:if test="${!empty param.id}">
	<c:redirect url="rateCardView.jsp?id=${param.id}"/> 
</c:if>

<c-rt:set var="forward_listing_add" value="<%= RateCardTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= RateCardTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_inactive" value="<%= RateCardTable.FORWARD_LISTING_INACTIVE %>"/>
<c-rt:set var="forward_listing_invalid" value="<%= RateCardTable.FORWARD_LISTING_INVALID %>"/>
<c-rt:set var="forward_listing_cannot_inactivate" value="<%= RateCardTable.FORWARD_LISTING_CANNOT_INACTIVATE %>"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="rateCardSetup.jsp"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_inactive}">
		<script>alert("Status Inactive.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_invalid}">
		<script>alert("Cannot set active, rate card is invalid.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_cannot_inactivate}">
		<script>alert("Cannot set inactivate, rate card is used in future requests.");</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="fms.label.rateCardListing"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="fms" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
