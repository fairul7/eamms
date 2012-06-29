<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="customer" value="${widget }" />
<c:set var="info" value="${customer.customerInfo}" />
<p>
<strong><c:out value="${info.companyName}" /></strong><br/>
<c:out value="${customer.formattedAddress}" escapeXml="false" /><br/>

<%-- %>
<c:out value="${customer.address2}" /><br/>
<c:out value="${customer.postcode}" /> <c:out value="${customer.address3}" /><br/>
<c:out value="${customer.state}" /><br/>
<c:out value="${customer.country}"/><br/>
--%>
</p>
<br/>
<p>
<strong>ATT:</strong>
<c:out value="${info.salutation}"/>
<c:out value="${info.contactFirstName }"/> <c:out value="${info.contactLastName }" /><br/>
</p>
