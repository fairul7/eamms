<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="massMappingPage">
        <com.tms.cms.taxonomy.ui.TaxonomyMassTagging name="massMapping"/>
    </page>
</x:config>

<c:if test="${forward.name=='errorContent'}">
    <script>
    alert("<fmt:message key="message.massMapping.select"/>");
    </script>
</c:if>
<c:if test="${forward.name=='errorTaxonomy'}">
    <script>
    alert("<fmt:message key="message.massMapping.selectNode"/>");
    </script>
</c:if>
<c:if test="${forward.name=='added'}">
    <script>
    alert("<fmt:message key="message.massMapping.added"/>");
    </script>
    <c:redirect url="contentMassMapping.jsp"></c:redirect>
</c:if>
<c:if test="${forward.name=='removed'}">
    <script>
    alert("<fmt:message key="message.massMapping.remove"/>");
    </script>
    <c:redirect url="contentMassMapping.jsp"></c:redirect>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><%-- fmt:message key="general.label.contentManagement"/--%><fmt:message key="com.tms.cms.MassMapping"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <%-- x:display name="cms.contentPath"/> <p> --%>
<%--b><fmt:message key="txy.label.massmapping"/></b><br--%>
                  <%-- Content Tree --%>
                  <x:display name="massMappingPage.massMapping"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
