<%@ page import="kacang.runtime.*,kacang.ui.*, kacang.stdui.Table" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%-- Style Definitions --%>
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
<table border="1" cellpadding="0" cellspacing="0" width="<c:out value="${table.width}"/>">
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
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
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
                    <tr class="tableHeader">
                        <%-- Show Index Header --%>
                        <c:if test="${showIndex}">
                            <td>&nbsp;</td>
                            <c:set var="colspan" value="${colspan + 1}"/>
                        </c:if>
                        <%-- Show Column Headers --%>
                        <c:forEach items="${model.columnList}" var="column" varStatus="hdrStatus">
                            <td height="25">
                                <c:set var="hdr" value="${column.header}"/>
                                <c:set var="prop" value="${column.property}"/>
                                <c:choose>
                                    <c:when test="${!table.sortable || !column.sortable || empty column.property}">
                                        <c:out value="${hdr}" escapeXml="false"/>
                                    </c:when>
                                    <c:when test="${table.desc}">
                                        <x:event name="${table.absoluteName}" type="sort" param="sort=${prop}" html="class=\"tableHeader\""><c:out value="${hdr}" escapeXml="false" /></x:event>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${hdr}" escapeXml="false" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:set var="colspan" value="${colspan + 1}"/>
                        </c:forEach>

                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    <c:set var="selectedRowMap" value="${table.selectedRowMap}"/>
                    <c:forEach items="${model.tableRows}" var="row" varStatus="status">
                        <tr class="tableRow">
                            <%-- Show Index Column --%>
                            <c:if test="${showIndex}">
                                <td valign="top" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                                    <c:set var="idx" value="${table.startIndex + status.index + 1}"/>
                                    <c:out value="${idx}"/>
                                </td>
                            </c:if>
                            <%-- Show Data Columns --%>
                            <c:forEach items="${model.columnList}" var="column">
                                <td valign="top" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                                    <c:choose>
                                        <c:when test="${!empty column.property}">
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
                                            
                                                <c:out value="${value}" escapeXml="${column.escapeXml}"/>
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
    <c:if test="${table == table.rootForm}">
    </form>
    </c:if>
</table>
