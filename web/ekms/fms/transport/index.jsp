<%@ include file="/common/header.jsp"%>
<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule">
	<c:redirect url="VehicleListing.jsp" />
</x:permission>
<x:permission permission="com.tms.fms.transport.admin" module="com.tms.fms.transport.model.TransportModule">
	<c:redirect url="Category.jsp" />
</x:permission>
