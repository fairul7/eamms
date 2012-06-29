<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%--
<c:set var="co" value="${widget.quotationObject}"/>
<c:set var="templateList" value="${widget.templateList }" />

<c:forEach var="tmp" items="${templateList }">
  <div>
    <c:out value="${tmp.templateBody}" escapeXml="false" />
  </div>
</c:forEach>
--%>

<c:set var="quotation" value="${widget}"/>
<c:set var="contentList" value="${quotation.contentList}" />
<c:set var="canEdit" value="${widget.canEdit}"/>

<%-- <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_LetterHead"/>  --%>
  <c:forEach var="content" items="${contentList}">

    <c:choose>
    
      <c:when test="${content.contentType eq 'com.tms.quotation.generator.Subject'}" >
      <div>
        <strong style="text-decoration:underline;">RE: <c:out value="${quotation.quotationObject.subject }"/></strong>
      </div>
      <br/>
      </c:when>
      
      <c:when test="${content.contentType eq 'com.tms.quotation.generator.Date'}">
        <div>
        <fmt:formatDate value="${quotation.quotationObject.recdate}" pattern="dd MMMM yyyy"/>
        </div>
      </c:when>
      
      <c:otherwise>
        <x:template type="${content.contentType}" properties="id=${content.contentId}&canEdit=${canEdit }"/>
      </c:otherwise>
      
    </c:choose>

  </c:forEach>

<%--
<table>
<tr>
  <td>
    <x:template type="com.tms.quotation.ui.PreviewQuotationCustomer" name="customerInfo" properties="quotationId=${param.quotationId}"/>
  </td>
</tr>
<tr>
  <td>
    <strong style="text-decoration:underline;">RE: <c:out value="${co.subject }" /></strong>
  </td>
</tr>
<tr>
  <td>
    <x:template type="com.tms.quotation.ui.PreviewQuotationItem" name="quotationTable"    properties="quotationId=${param.quotationId}"></x:template>
  </td>
</tr>
</table>
--%>
