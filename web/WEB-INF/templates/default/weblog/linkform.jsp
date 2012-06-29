<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"  />

<jsp:include page="../form_header.jsp" flush="true"/>
<table>
    <tr>
        <Td align="right">
            <fmt:message key='weblog.label.name'/>
        </td>
        <Td>
            <x:display name="${form.nameTF.absoluteName}" />
        </td>
    </tr>

    <tr>
        <Td align="right">
            <fmt:message key='weblog.label.url'/>
        </td>
        <Td>
        <x:display name="${form.url.absoluteName}" />
        </td>
    </tr>

    <tr>
        <Td align="right">
        </td>
        <Td>
           <x:display name="${form.saveButton.absoluteName}" /> <x:display name="${form.cancelButton.absoluteName}" />        </td>
    </tr>


</table>

<x:display name="${form.linkTable.absoluteName}" ></x:display>
<jsp:include page="../form_footer.jsp" flush="true"/>



