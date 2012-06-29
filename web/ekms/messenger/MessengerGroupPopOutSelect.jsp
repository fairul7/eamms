<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.collab.messenger.ui.MessengerGroupPopOut"%>


<x:config>
	<page name="msgGroupPopup">
		<com.tms.collab.messenger.ui.MessengerGroupForm name="form"/>
	</page>
</x:config>


<div>
	<x:display name="msgGroupPopup.form" />
</div>
