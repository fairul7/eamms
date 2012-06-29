<!--- viewQuotation.jsp --->
<%@ page import="com.tms.quotation.ui.ViewQuotation" %>

<%@ include file="/common/header.jsp" %>
<x:config>
  <page name="viewQuotationPage">
    <com.tms.quotation.ui.ViewQuotation name="newQuotation" width="100%" sort="recdate" desc="true" />
    <com.tms.quotation.ui.ViewApprovedQuotation name="approvedQuotation" width="100%" sort="closeDate" desc="true" />
  </page>
</x:config>

<c-rt:set var="forward_add" value="<%= ViewQuotation.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ViewQuotation.FORWARD_DELETE %>"/>
<c-rt:set var="forward_approve" value="<%= ViewQuotation.FORWARD_APPROVE %>"/>
<c-rt:set var="forward_invalid" value="<%= ViewQuotation.FORWARD_INVALID %>"/>
<c-rt:set var="forward_submit" value="<%= ViewQuotation.FORWARD_SUBMIT %>"/>

<!--- permissions --->
<%--
<x:permission permission="" module=""/>
--%>
<x:permission permission="com.tms.quotation.approve" module="com.tms.quotation.model.QuotationModule">
  <x:set name="viewQuotationPage.newQuotation" property="canApprove" value="1"/>
  <x:set name="viewQuotationPage.approvedQuotation" property="canDelete" value="1"/>  
</x:permission>  
<x:permission permission="com.tms.quotation.add" module="com.tms.quotation.model.QuotationModule">
  <x:set name="viewQuotationPage.newQuotation" property="canAdd" value="1"/>
  <x:set name="viewQuotationPage.newQuotation" property="canEdit" value="1"/>
</x:permission>
<x:permission permission="com.tms.quotation.delete" module="com.tms.quotation.model.QuotationModule">
  <x:set name="viewQuotationPage.newQuotation" property="canDelete" value="1"/>
</x:permission>
<x:set name="viewQuotationPage.newQuotation" property="editUrl" value="editQuotation.jsp" />
<x:set name="viewQuotationPage.approvedQuotation" property="canApprove" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_add}">
    <%--
    <c:redirect url="newQuotation_customer.jsp"/>
    --%>
    <c:redirect url="incidentCompany.jsp"/>
  </c:when>
  <c:when test="${forward.name == forward_approve }">
  <script language="javascript">
    alert("<fmt:message key='com.tms.quotation.viewQuotation.approve'/>");
    document.location="<c:url value='viewQuotation.jsp'/>";
  </script>  
<%--
	<c:set var="quotationId" value="${widgets['viewQuotationPage.newQuotation'].quotationId}"/>
    <c:redirect url="approveQuotation.jsp">
      <c:param name="quotationId" value="${quotationId}"/>
    </c:redirect>
--%>    
  </c:when>
  <c:when test="${forward.name eq forward_submit }" >
  <script>
    alert("<fmt:message key='com.tms.quotation.viewQuotation.submit'/>");
    document.location="<c:url value='viewQuotation.jsp'/>";    
  </script>
  </c:when>
<%--  
  <c:when test="${forward.name == forward_invalid }" >
     <script language="javascript">
       alert("This Quotation is already Approved");
     </script>
  </c:when>
--%>  
</c:choose>
<c:if test="${!empty param.quotationId}">
  <script language="javascript">
    window.open("previewQuotation.jsp?quotationId=<c:out value='${param.quotationId}' />", "Preview" )
  </script>
<%-- 
  <c:choose>
    <c:when test="${param.cn eq 'viewQuotationPage.newQuotation' }">
      <script language="javascript">
        window.open("previewQuotation.jsp?quotationId=<c:out value='${param.quotationId}' />", "Preview" )
      </script>
    </c:when>
    <c:when test="${param.cn eq 'viewQuotationPage.approvedQuotation' }">
      <script language="javascript">
        window.open();
      </script>
    </c:when>
  </c:choose>
--%>  
</c:if>

<%--<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />--%>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<tr>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
    <tr>
      <td class="contentTitleFont" style="vertical-align: top;">
        <b><fmt:message key='com.tms.quotation.table.newQuotation'/></b><br>
<!--      <hr size="1" color="cccccc">        -->
      </td>        
    </tr>      
    <tr style="background-color:#E9F5FF;">
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
            <x:display name="viewQuotationPage.newQuotation"/>
          </td>
        </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="contentTitleFont" style="vertical-align: top;">
        <b><fmt:message key='com.tms.quotation.table.approvedQuotation'/></b><br>
<!--       <hr size="1" color="cccccc"> -->
      </td>
    </tr>
     <tr style="background-color:#E9F5FF;">
       <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
            <x:display name="viewQuotationPage.approvedQuotation"/>
          </td>
        </tr>
        </table>
      </td>
    </tr>
    </tbody>
    </table>
  </td>
</tr>
</tbody>
</table>

<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
