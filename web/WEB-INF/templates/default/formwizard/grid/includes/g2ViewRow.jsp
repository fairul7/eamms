<%@ page import="com.tms.collab.formwizard.grid.G2Column"%>
<%--<%@include file="/common/header.jsp"%>--%>
<tr>
    <td align="right"><c:out value="${rowStatus.count}" /></td>
    <c:forEach var="colList" items="${rowList}" varStatus="colStatus">
        <c:set var="column" value="${f.columnList[colStatus.index]}" />
        <c:set var="val" value="${f.dataList[rowStatus.index][colStatus.index]}" />
        <%
                G2Column column = (G2Column) pageContext.getAttribute("column");
        %>
          <c-rt:set var="g2ColumnValidateNumber" value="<%= G2Column.VALIDATE_NUMBER  %>" />
          <c-rt:set var="g2ColumnTypeFormula" value="<%= G2Column.TYPE_FORMULA  %>" />
          <c:set var="align" value="left" />
          <c:if test="${g2ColumnValidateNumber == column.validation || g2ColumnTypeFormula == column.type}">
            <c:set var="align" value="right" />
          </c:if>
          <td align="<c:out value="${align}"/>" >
            <%
                String val = (String) pageContext.getAttribute("val");

                if(!column.isValidValue(val)) {
                    if(G2Column.VALIDATE_NUMBER.equals(column.getValidation())) {
                        out.print("<i>#NaN#</i>");
                    } else if(G2Column.VALIDATE_REQUIRED.equals(column.getValidation())) {
                        out.print("<i>#Required#</i>");
                    } else {
                        out.print("<i>#ValidationError#</i>");
                    }
                }
            %>
            <c:out value="${val}" />&nbsp;
        </td>
    </c:forEach>
</tr>