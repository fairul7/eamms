<%@ page import="kacang.runtime.*,kacang.ui.*,
                 kacang.stdui.Table" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="table" value="${widget}"/>
<c:set var="model" value="${table.model}" scope="page" />
<c:set var="showCheckbox" value="${!empty model.tableRowKey && !empty model.actionList[0]}"/>
<c:choose>
  <c:when test="${table.multipleSelect}">
    <c:set scope="page" var="selectType" value="checkbox"/>
  </c:when>
  <c:otherwise>
    <c:set scope="page" var="selectType" value="radio"/>
  </c:otherwise>
</c:choose>
<c:set var="showIndex" value="${table.numbering}"/>

<table border="0" cellpadding="4" cellspacing="4" style="border:1px solid gray" width="<c:out value="${table.width}"/>">

<%-- Show Form Header --%>
<c:if test="${table == table.rootForm}">
<form name="<c:out value="${table.absoluteName}"/>"
      action="?"
      method="POST"
      target="<c:out value="${table.target}"/>"
      <c:if test="${!empty table.enctype}">
          enctype="<c:out value="${table.enctype}"/>"
      </c:if>
      onSubmit="<c:out value="${table.attributeMap['onSubmit']}"/>"
      onReset="<c:out value="${table.attributeMap['onReset']}"/>"
>
<input type="hidden" name="<%= Event.PARAMETER_KEY_WIDGET_NAME %>" value="<c:out value="${table.absoluteName}"/>" />
</c:if>
<input type="hidden" name="<%= Event.PARAMETER_KEY_EVENT_TYPE %>" value="<%= Table.PARAMETER_KEY_ACTION %>" />
<tr>
<td>

<%-- Show Filters --%>
<c:if test="${table.showPageSize || !empty model.filterList[0]}">
<table border="0" cellpadding="4" cellspacing="4" style="border:0px solid gray" width="100%">
  <tbody>
    <tr>
      <td align="right">
        <x:display name="${table.filterForm.absoluteName}"/>
      </td>
    </tr>
  </tbody>
</table>
</c:if>

<%-- Show Action Buttons --%>
<c:if test="${!empty model.actionList[0]}">
<table border="0" cellpadding="4" cellspacing="4" style="border:0px solid gray" width="100%">
  <tbody>
    <tr>
      <td align="right">
        <c:forEach items="${model.actionList}" var="action" varStatus="hdrStatus">
            <c:if test="${!empty action.message}">
                <c:set var="onclick">return confirm('<c:out value="${action.message}"/>')</c:set>
            </c:if>
            <c:if test="${empty action.message}">
                <c:set var="onclick" value=""/>
            </c:if>
            <input type="submit" class="button" name="<c:out value="${table.tableActionPrefix}"/><c:out value="${table.absoluteName}"/>.<c:out value="${action.action}"/>" value="<c:out value="${action.label}"/>" onclick="<c:out value="${onclick}"/>" />
        </c:forEach>
      </td>
    </tr>
  </tbody>
</table>
</c:if>

<%-- Show Data Table --%>
<table border="0" cellpadding="4" cellspacing="4" style="border:0px solid gray" width="100%">

<c:if test="${showCheckbox && table.multipleSelect}">
<script>
<!--
    function <c:out value="${table.name}"/>_toggle(obj) {
        if (obj.checked) {
            <c:out value="${table.name}"/>_selectAll(document.forms['<c:out value="${table.rootForm.absoluteName}"/>']['<c:out value="${table.tableRowName}"/>']);
        }
        else {
            <c:out value="${table.name}"/>_deselectAll(document.forms['<c:out value="${table.rootForm.absoluteName}"/>']['<c:out value="${table.tableRowName}"/>']);
        }
    }
    function <c:out value="${table.name}"/>_selectAll(obj) {
        if (!obj) {
            return;
        }
        if (!obj.length) {
            obj.checked = true;
        }
        for(i=0; i<obj.length; i++) {
            obj[i].checked = true;
        }
    }
    function <c:out value="${table.name}"/>_deselectAll(obj) {
        if (!obj) {
            return;
        }
        if (!obj.length) {
            obj.checked = false;
        }
        for(i=0; i<obj.length; i++) {
            obj[i].checked = false;
        }
    }
//-->
</script>
</c:if>

<%-- Show Headers --%>
<thead>
<c:set var="colspan" value="0"/>
  <tr style="background: #4779AB">

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
          <c:choose>
            <c:when test="${!table.sortable || !column.sortable || empty column.property}">
              <span style="color:white; font-weight:bold"><c:out value="${hdr}"/></span>
            </c:when>
            <c:when test="${table.desc}">
              <x:event name="${table.absoluteName}" type="sort" param="sort=${prop}"><span style="color:white; font-weight:bold"><c:out value="${hdr}"/></span></x:event>
            </c:when>
            <c:otherwise>
              <x:event name="${table.absoluteName}" type="sort" param="sort=${prop}&desc=true"><span style="color:white; font-weight:bold"><c:out value="${hdr}"/></span></x:event>
            </c:otherwise>
          </c:choose>
          <c:if test="${!empty table.sort && table.sort == prop}">
              <c:choose>
                <c:when test="${table.desc}">
                    <img src="<%= request.getContextPath() %>/common/table/sortup.gif">
                </c:when>
                <c:otherwise>
                    <img src="<%= request.getContextPath() %>/common/table/sortdown.gif">
                </c:otherwise>
              </c:choose>
          </c:if>
    </td>
    <c:set var="colspan" value="${colspan + 1}"/>
  </c:forEach>

  <%-- Show Check Box Header --%>
  <c:if test="${showCheckbox}">
    <td>
        <c:choose>
          <c:when test="${table.multipleSelect}">
            <input type="checkbox" onClick="<c:out value="${table.name}"/>_toggle(this)" />
          </c:when>
          <c:otherwise>
            &nbsp;
          </c:otherwise>
        </c:choose>
    </td>
    <c:set var="colspan" value="${colspan + 1}"/>
  </c:if>

  </tr>
</thead>

<%-- Show Body --%>
<tbody>
<c:set var="selectedRowMap" value="${table.selectedRowMap}"/>
<c:forEach items="${model.tableRows}" var="row" varStatus="status">

  <%-- Determine Row Style --%>
  <c:choose>
    <c:when test="${status.index % 2 != 0}">
      <c:set var="style" value="background: #dddddd"/>
    </c:when>
    <c:otherwise>
      <c:set var="style" value="background: white"/>
    </c:otherwise>
  </c:choose>

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
                <c:otherwise>
                    <%-- <c:set var="keyValue" value="${row[column.urlParam]}"/> --%>
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
                    <c:choose>
	                <c:when test="${empty column.url}">
	                    <x:event name="${table.absoluteName}" type="sel" param="${column.urlParam}=${keyValue}">
	                        <c:out value="${value}" escapeXml="${column.escapeXml}"/></x:event>
	                </c:when>
	                <c:otherwise>
	                    <x:event name="${table.absoluteName}" url="${column.url}" type="sel" param="${column.urlParam}=${keyValue}">
	                        <c:out value="${value}" escapeXml="${column.escapeXml}"/></x:event>
	                </c:otherwise>
	                </c:choose>
                </c:otherwise>
            </c:choose>
        </td>
    </c:forEach>

    <%-- Show CheckBox Column --%>
    <c:if test="${showCheckbox}">
        <td valign="top">
            <c:set var="key" value="${row[model.tableRowKey]}"/>
            <input
                type="<c:out value="${selectType}"/>"
                name="<c:out value="${table.tableRowName}"/>"
                value="<c:out value="${key}"/>"
                <c:if test="${!empty selectedRowMap[key]}">checked</c:if>
            />
        </td>
    </c:if>

  </tr>
</c:forEach>
</tbody>

<%-- Show Paging --%>
<tfooter>
  <tr>
    <td colspan="<c:out value="${colspan}"/>" align="right">
        <c:set var="pageCount" value="${table.pageCount}"/>

        <c:if test="${table.currentPage > 1}">
        [<x:event name="${table.absoluteName}" type="page" param="page=${table.currentPage - 1}">&lt;</x:event>]
        </c:if>

        <fmt:message key="table.label.page" /> <!-- Page -->
        <select id="<c:out value='${table.absoluteName}page'/>" name="<c:out value='${table.absoluteName}page'/>" onchange="goToPage('<c:out value='${table.absoluteName}'/>')">
        <c:set var="end" value="${pageCount}" />
            <c:forEach begin="1" end="${end}" var="pg">
              <option<c:if test='${pg == table.currentPage}'> selected</c:if>><c:out value="${pg}" /></option>
            </c:forEach>
        </select>
        <script>
        <!--
            function goToPage(id) {
                var selectbox = document.getElementById(id + 'page');
                var page = selectbox.options[selectbox.selectedIndex].text;
                location.href='?cn=' + id + '&et=page&page=' + page;
            }
        //-->
        </script>

        <c:if test="${table.currentPage < pageCount}">
        [<x:event name="${table.absoluteName}" type="page" param="page=${table.currentPage + 1}">&gt;</x:event>]
        </c:if>
    </td>
  </tr>
</tfooter>
</table>

</td>
</tr>
<c:if test="${table == table.rootForm}">
</form>
</c:if>
</table>
