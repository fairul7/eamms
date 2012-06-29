<%@ include file="/common/header.jsp"%>
<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.transport.model.TransportModule">
	<c:redirect url="FacilityListing.jsp" />
</x:permission>
<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.transport.model.TransportModule">
	<c:redirect url="CategoryListing.jsp" />
</x:permission>