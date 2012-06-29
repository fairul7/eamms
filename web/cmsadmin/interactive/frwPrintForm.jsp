<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="printFormPage">
    		<com.tms.collab.formwizard.ui.PrintForm name="printForm"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name="printFormPage.printForm" property="id" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="printFormPage.printForm" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.showHidden}">    
    <x:set name="printFormPage.printForm" property="showHidden" value="${param.showHidden}"/>
</c:if>

<script>
<!--
    window.print();
//-->    
</script>


<tr>
    <td>
        <x:display name="printFormPage.printForm" ></x:display>

        
    </td>
</tr>



