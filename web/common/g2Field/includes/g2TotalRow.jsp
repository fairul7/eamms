<%@ page import="com.tms.collab.formwizard.grid.G2Field,
                 com.tms.collab.formwizard.grid.G2Column,
                 java.util.List"%>

<c:forEach var="column" items="${f.columnList}" varStatus="colStatus">
    <c:if test="${column.columnTotal}">
        <c:set var="needTotal" value="true" />
    </c:if>
</c:forEach>
<c:if test="${needTotal}">
<tr>
    <td>&nbsp;</td>
    <c:forEach var="column" items="${f.columnList}" varStatus="colStatus">
        <c:set var="columnIndex" value="${colStatus.index}" />
        <td align="right">
            <%
                G2Field field = (G2Field) request.getAttribute("widget");
                G2Column column = (G2Column) pageContext.getAttribute("column");
                int colNo = Integer.parseInt(pageContext.getAttribute("columnIndex").toString());

                pageContext.setAttribute("rowCount", new Integer(field.getDataList().size()));

                if(column.isColumnTotal()) {
                    // required total column
                    double total = 0;
                    boolean error = false;
                    String val;
                    try {
                        for(int rowNo=0; rowNo<field.getDataList().size(); rowNo++) {
                            val = (String) ((List)field.getDataList().get(rowNo)).get(colNo);
                            total += Double.parseDouble(val);
                        }
                    } catch(Exception e) {
                        error = true;
                    }

                    if(error) {
                        out.print("<b><i>#ERROR#</i></b>");
                    } else {
                        val = G2Field.df.format(total);
                        out.print("<b>" + val + "</b>");
                    }
                }

            %>
            &nbsp;
        </td>
    </c:forEach>
    <td id="tableAction<c:out value="${rowCount+1}" />">
        &nbsp;
    </td>
</tr>
</c:if>