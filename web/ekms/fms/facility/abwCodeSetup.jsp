<%@ page import="com.tms.fms.facility.ui.AbwSetupForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.permission.rateCard" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="abwSetup">         
            <com.tms.fms.facility.ui.AbwSetupForm name="form" />
            <com.tms.fms.facility.ui.AbwSetupList name="table" />        
    </page>
</x:config>

<x:set name="abwSetup.form" property="mode" value="<%=null%>"/>
<x:set name="abwSetup.form" property="code" value="<%=null%>"/>

<c:if test="${!empty param.id}">
    <x:set name="abwSetup.form" property="mode" value="<%=AbwSetupForm.EDIT_MODE%>"/>
    <x:set name="abwSetup.form" property="code" value="${param.id}"/>
</c:if>

<c:if test="${forward.name == 'abwCodeDuplicated'}">
    <script>
         alert('<fmt:message key="fms.setup.alert.abwCodeDuplicated"/>');
         document.location="abwCodeSetup.jsp";
    </script>
</c:if>

<c:if test="${forward.name == 'abwCodeInUse'}">
    <script>
         alert('<fmt:message key="fms.setup.alert.abwCodeInUse"/>');
         document.location="abwCodeSetup.jsp";
    </script>
</c:if>

<c:if test="${forward.name == 'updateSuccessfully'}">
    <script>
         alert('<fmt:message key="fms.setup.alert.updateSuccessfully"/>');
         document.location="abwCodeSetup.jsp";
    </script>
</c:if>

<c:if test="${forward.name == 'deleteSuccessfully'}">
    <script>
         alert('<fmt:message key="fms.setup.alert.deleteSuccessfully"/>');
         //document.location="abwCodeSetup.jsp";
    </script>    
</c:if>

<c:if test="${forward.name == 'noRecordSelelcted'}">
    <script>
         alert('<fmt:message key="fms.setup.alert.noRecordSelelcted"/>');
         //document.location="abwCodeSetup.jsp";
    </script>
</c:if>

<c:if test="${forward.name == 'cancel_form_action'}">
    <script>
         //document.location="rateCardList.jsp";
         document.location="abwCodeSetup.jsp";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="fms.setup.form.abwCodeSetup"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="abwSetup.form" />
    <hr/>
    <x:display name="abwSetup.table" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

<c:if test="${empty param.id}">
    <script>
         var abwInput = document.getElementsByName("abwSetup.form.abwCode")[0];
         abwInput.focus();
    </script>
</c:if>



