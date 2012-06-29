<%@ page import="kacang.runtime.*,kacang.ui.*, kacang.stdui.Table" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%-- Style Definitions --%>
<head>

<style type="text/css">
	
	td  {
	color:#333333;
	font-family:Verdana,Arial,Helvetica,sans-serif;
	font-size:11px; 
	}
	
	a  {
	color:#333333;
	font-family:Verdana,Arial,Helvetica,sans-serif;
	font-size:11px;
	}
	
	.headerBgColor  {
	background-color:#e9eccd;
	font-family:Arial,Verdana,Helvetica,sans-serif;
	font-size:8.5pt;
	}
	
	.tableHeader {
	background-color:#e9eccd;
	color:black;
	font-family:Verdana,Arial,Helvetica,sans-serif;;
	font-size:11px;
	font-weight:bold;
	text-decoration:none;
	}
	
</style>


</head>

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

<table border="0" cellpadding="4" cellspacing="4" width="<c:out value="${table.width}"/>">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
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
			                <table border="0" cellpadding="4" cellspacing="4" width="100%">
			                    <tr><td align="right"><x:display name="${table.filterForm.absoluteName}"/></td></tr>
			                </table>
			            </c:if>
			            <%-- Show Action Buttons --%>
			            <c:if test="${!empty model.actionList[0]}">
			                <table border="0" cellpadding="2" cellspacing="4" class="tableBackground" width="100%">
			                    <tr>
			                        <td align="right">
			                            <c:forEach items="${model.actionList}" var="action" varStatus="hdrStatus">
			                                <c:if test="${!empty action.message}">
			                                    <c:set var="onclick">return confirm('<c:out value="${action.message}"/>')</c:set>
			                                </c:if>
			                                <c:if test="${empty action.message}">
			                                    <c:set var="onclick" value=""/>
			                                </c:if>
			                                <input type="submit" class="tableButton" name="<c:out value="${table.tableActionPrefix}"/><c:out value="${table.absoluteName}"/>.<c:out value="${action.action}"/>" value="<c:out value="${action.label}"/>" onclick="<c:out value="${onclick}"/>" />
			                            </c:forEach>
			                        </td>
			                    </tr>
			                </table>
			            </c:if>
			            
			            </td>
			        </tr>
			</table>
			
		</td>
		</tr>
		<!-- *********************************************************************************************************************** -->
		<tr>
		<td>
			<%-- Show Data Table --%>
        <table bgcolor="white" >
        <tr><td>
            <table border="1" style="border:1px; background-color:white" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr>
                        <%-- Show Index Header --%>
                        
                        <%-- Show Column Headers --%>
                        <c:forEach items="${model.columnList}" var="column" varStatus="hdrStatus">
                            <td height="25" class="headerBgColor" align="center" >
                            	<strong>
                                <c:set var="hdr" value="${column.header}"/>
                                <c:set var="prop" value="${column.property}"/>
                                <span class="tableHeader"> <c:out value="${hdr}" escapeXml="false"/></span>
                                </strong>
                            </td>
                            <c:set var="colspan" value="${colspan + 1}"/>
                        </c:forEach>
                      
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    <c:set var="selectedRowMap" value="${table.selectedRowMap}"/>
                    <c:forEach items="${model.tableRows}" var="row" varStatus="status">
                        <tr bgcolor="white" >
                            <%-- Show Index Column --%>
                           
                            <%-- Show Data Columns --%>
                            <c:forEach items="${model.columnList}" var="column">
                                <td  bgcolor="white" align="center" height="25" class="table_border_bottom" >
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
						                	<c:if test="${!empty value}">
						                	<c:out value="${value}" escapeXml="${column.escapeXml}"/>
						                	</c:if>
						                    <c:if test="${empty value}">
						                	&nbsp;
						                	</c:if>
						                </c:when>
						                <c:otherwise>
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
                            
                        </tr>
                    </c:forEach>
                </tbody>
                
                </table>
                </td>
                </tr>
        </table>
        
		</td>
		</tr>
		
</table>
                
        


