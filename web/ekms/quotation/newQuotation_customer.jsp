<!--- newQuotation_customer.jsp --->
<%@ page import="com.tms.quotation.ui.CustomerTable" %>

<%@ include file="/common/header.jsp" %>
<%-- <x:permission permission="" module="" /> --%>
<x:config>
  <page name="newQtnCustomer">
<%--  
    <tabbedpanel name="tab" width="100%" >
      <panel name="formPanel" text="<fmt:message key='com.tms.quotation.quotation.newCustomer'/>">
        <com.tms.quotation.ui.AddCustomer name="addCustomer" width="100%"/>
      </panel>
      <panel name="tablePanel" text="<fmt:message key='com.tms.quotation.quotation.oldCustomer'/>">
        <com.tms.quotation.ui.CustomerTable name="customerTable" width="100%"/>
      </panel>
    </tabbedpanel>
--%>
    <com.tms.quotation.ui.AddCustomer name="addCustomer" width="100%"/>
    <com.tms.quotation.ui.CustomerTable name="customerTable" width="100%"/>
  </page>
</x:config>

<x:permission permission="com.tms.quotation.add" module="com.tms.quotation.model.QuotationModule" var="hasPermission"/>
<c:if test="${false==hasPermission }">
  <script language="javascript">
    alert("<fmt:message key='com.tms.quotation.quotation.noPermission'/>");
    document.location = "<c:url value='viewQuotation.jsp'/>";
  </script>
</c:if>
<x:set name="newQtnCustomer.customerTable" property="nextUrl" value="addQuotation.jsp"/>
<x:set name="newQtnCustomer.addCustomer" property="canAdd" value="1"/>
<x:set name="newQtnCustomer.addCustomer" property="resetUrl" value="newQuotation_customer.jsp"/>
<x:set name="newQtnCustomer.addCustomer" property="cancelUrl" value="viewQuotation.jsp"/>
<%-- Handle events --%>
<c:if test="${forward.name == 'AddCustomer.success'}">
  <c:set var="customerId" value="${widgets['newQtnCustomer.addCustomer'].customerId }"/>
  <c:redirect url="addQuotation.jsp?customerId=${customerId}" />
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<tr>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
    <tr>
<%--    
    <td style="vertical-align: top;">
      <x:display name="newQtnCustomer.tab"/>
    </td>
--%>
      <td style="vertical-align: top;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
            <table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
            <tr>
              <td style="vertical-align: top;">
                <fmt:message key='com.tms.quotation.table.customer'/>
              </td>
            </tr>
            </table>
          </td>
       </tr>
       <tr>
         <td>
           <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
           <tr style="background-color:#E9F5FF;">
             <td style="vertical-align: top;">
               <x:display name="newQtnCustomer.customerTable"/>
             </td>
           </tr>
           </table>
         </td>
       </tr>
       <tr>
          <td style="vertical-align: top;">
            <x:display name="newQtnCustomer.addCustomer"/>
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
