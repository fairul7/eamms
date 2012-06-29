<%@ page import="kacang.runtime.*,kacang.ui.*,
                 kacang.stdui.Table" %>
<%@ page import="com.tms.crm.sales.misc.TemplateUtil" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="table" value="${widget}"/>
<c:set var="model" value="${table.model}" scope="page" />
<c:set var="showIndex" value="${table.numbering}"/>

<table border="0" cellpadding="4" cellspacing="4" style="border:1px solid gray" width="<c:out value="${table.width}"/>">
<tr>
<td>

<%-- Show Data Table --%>
<table border="0" cellpadding="4" cellspacing="1" style="border:0px solid gray" class="tableStyle" width="100%">

<%-- Show Headers --%>
<thead>
<c:set var="colspan" value="0"/>
  <tr class="tableHeader">

  <%-- Show Index Header --%>
  <c:if test="${showIndex}">
    <td>&nbsp;</td>
    <c:set var="colspan" value="${colspan + 1}"/>
  </c:if>

  <%-- Show Column Headers --%>
  <c:forEach items="${model.columnList}" var="column" varStatus="hdrStatus">
    <td>
          <c:set var="hdr" value="${column.header}"/>
          <c:set var="prop" value="${column.property}"/>
          <span class="tableHeaderStyle"><c:out value="${hdr}"/></span>
    </td>
    <c:set var="colspan" value="${colspan + 1}"/>
  </c:forEach>

  </tr>
</thead>

<%-- Show Body --%>
<tbody>
<c:set var="selectedRowMap" value="${table.selectedRowMap}"/>
<c:forEach items="${model.allTableRows}" var="row" varStatus="status">
   <tr class="tableRow">
  <%-- Determine Row Style --%>
  <%--
  <c:choose>
    <c:when test="${status.index % 2 != 0}">
      <c:set var="style" value="background: #dddddd"/>
    </c:when>
    <c:otherwise>
      <c:set var="style" value="background: white"/>
    </c:otherwise>
  </c:choose>
  -->

  <tr style="<c:out value="${style}"/>">

    <%-- Show Index Column --%>
    <c:if test="${showIndex}">
        <td valign="top">
            <c:set var="idx" value="${table.startIndex + status.index + 1}"/>
            <c:out value="${idx}"/>
        </td>
    </c:if>

    <%-- Show Data Columns --%>
    <c:forEach items="${model.columnList}" var="column">
        <td valign="top">
            <c:choose>
                <c:when test="${!empty column.property}">
<%--
                    <c:catch var="e">
                        <c:set scope="page" var="value" value="${row2[column.property]}"/>
                    </c:catch>
                    <c:if test="${!empty e}">
                        <c:set scope="page" var="value" value="${row.propertyMap[column.property]}"/>
                    </c:if>
--%>
                    <c:set var="xrow" value="${row}" scope="page"/>
                    <c:set var="xproperty" value="${column.property}" scope="page"/>
                    <%
                        // using scriptlets here, as workaround to suspected bug in oc4j
                        Object row = pageContext.findAttribute("xrow");
                        String propertyName = (String)pageContext.findAttribute("xproperty");
                        Object value = null;
                        try {
                            value = org.apache.commons.beanutils.PropertyUtils.getProperty(row, propertyName);
                        } catch (Exception e) {
                            value = org.apache.commons.beanutils.PropertyUtils.getMappedProperty(row, "propertyMap", propertyName);
                        }
                        if (value != null) {
                            pageContext.setAttribute("value", value);
                        } else {
                            pageContext.setAttribute("value", "");
                        }
                    %>
                    <c:set var="formatter" value="${column.formatMap}"/>
                    <c:set scope="page" var="value" value="${formatter[value]}"/>
                </c:when>
                <c:otherwise>
                    <c:set scope="page" var="value" value="${column.label}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${empty column.urlParam}">
                    <c:out value="${value}" escapeXml="${column.escapeXml}"/>
                </c:when>
                <c:when test="${empty column.url}">
                    <c:set var="keyValue" value="${row[column.urlParam]}"/>
                    <x:event name="${table.absoluteName}" type="sel" param="${column.urlParam}=${keyValue}">
                        <c:out value="${value}" escapeXml="${column.escapeXml}"/></x:event>
                </c:when>
                <c:otherwise>
                    <c:set var="keyValue" value="${row[column.urlParam]}"/>
                    <c:set var="xrow" value="${row}" scope="page"/>
                    <c:set var="xproperty" value="${column.urlParam}" scope="page"/>
                    <%
                        // using scriptlets here, as workaround to suspected bug in oc4j
                        Object row = pageContext.findAttribute("xrow");
                        String propertyName = (String)pageContext.findAttribute("xproperty");
                        Object value = null;
                        try {
                            value = org.apache.commons.beanutils.PropertyUtils.getProperty(row, propertyName);
                        } catch (Exception e) {
                            value = org.apache.commons.beanutils.PropertyUtils.getMappedProperty(row, "propertyMap", propertyName);
                        }
                        if (value != null) {
                            pageContext.setAttribute("keyValue", value);
                        }
                    %>
                    <x:event name="${table.absoluteName}" url="${column.url}" type="sel" param="${column.urlParam}=${keyValue}">
                        <c:out value="${value}" escapeXml="${column.escapeXml}"/></x:event>
                </c:otherwise>
            </c:choose>
        </td>
    </c:forEach>

  </tr>
</c:forEach>

</tbody>
</table>

</td>
</tr>
</form>
</table>
