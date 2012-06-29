<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" scope="request" value="${widget}"/>

<%--Display Form--%>
<x:display name="${panel.childMap.containerForm.absoluteName}" body="custom">
    <x:permission module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageRecycleBin">
        <x:display name="${panel.childMap.containerForm.childMap.destroyBox.absoluteName}" />
    </x:permission>
    <br>
    <br>
    <x:display name="${panel.childMap.containerForm.childMap.deleteButton.absoluteName}" />
    <x:display name="${panel.childMap.containerForm.childMap.cancel_form_action.absoluteName}" />
    <hr size="1">
</x:display>

<%--Display Preview--%>
<x:display name="${panel.childMap.contentObjectView.absoluteName}" />
