<%@ page import="com.tms.collab.formwizard.grid.G2Column,
                 kacang.Application"%>

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

                Application app = Application.getInstance();
                if(!column.isValidValue(val)) {
                    if(G2Column.VALIDATE_NUMBER.equals(column.getValidation())) {
                        out.print("<b><i>" + app.getMessage("formWizard.label.nan", "#NaN#") + "</i></b>");
                    } else if(G2Column.VALIDATE_REQUIRED.equals(column.getValidation())) {
                        out.print("<b><i>" + app.getMessage("formWizard.label.required", "#Required#") + "</i></b>");
                    } else {
                        out.print("<b><i>" + app.getMessage("formWizard.label.validationError",  "#ValidationError#") + "</i></b>");
                    }
                }                                
            %>

            <c:out value="${val}" />&nbsp;
        </td>
    </c:forEach>
    <td id="tableAction<c:out value="${rowStatus.count}" />">
        <input class="button" type="button" value="<fmt:message key="formWizard.label.edit"/>" onclick="location='?cn=<c:out value="${f.absoluteName}" />&et=editRow&editRow=<c:out value="${rowStatus.count}" />'">
        <input class="button" type="button" value="<fmt:message key="formWizard.label.delete"/>" onclick="location='?cn=<c:out value="${f.absoluteName}" />&et=deleteRow&editRow=<c:out value="${rowStatus.count}" />'">
    </td>
</tr>