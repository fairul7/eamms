



<%@ page import="kacang.ui.Widget,
				 kacang.stdui.*,
				 java.util.*,
				 java.awt.*,
                 kacang.stdui.Hidden"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<c:choose>
    <c:when test="${form.columns == 0}">
        <c:forEach var="child" items="${form.children}">
            <x:display name="${child.absoluteName}"/>
        </c:forEach>
    </c:when>
    <c:otherwise>
		<%
			Form form = (Form) pageContext.getAttribute("form");
			int column = 0;
			int row = 0;
			int limit = form.getColumns();
			Point point = null;
			Collection points = new ArrayList();
		%>
		<%-- 
		<table width="<%= form.getWidth() %>">
		--%>
		
		<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				
		
		
		
		
		
		
		
		
		
		
			<tr>
				<%
					for (Iterator i = form.getChildren().iterator(); i.hasNext();)
					{
						//Determine spans
						Widget child = (Widget) i.next();
                        if(!(child instanceof Hidden))
                        {
                            point = new Point(column, row);
                            if(points.contains(point))
                            {
                                boolean blocked = true;
                                while(blocked)
                                {
                                    column++;
                                    if(column >= limit)
                                    {
                                        column = 0;
                                        row ++;
                                        %></tr><tr><%
                                    }
                                    /*else
                                        column++;*/
                                    point = new Point(column, row);
                                    if(!(points.contains(point)))
                                        blocked = false;
                                }
                            }
						}
						//Rendering child
						%>
                        <% if(!(child instanceof Hidden)) { %>
                            <td
                            <% if ("errorMsg".equals(child.getName()) || "effectiveDays".equals(child.getName())) { %>
                            	align="left"
                            	class="classRowLabel"
                            <% } else if (child instanceof kacang.stdui.Label) { %>
                            	class="classRowLabel"
                            	align="right"
                            <% } else { %>
                            	class="classRow"
                            <% } %>
                                
                                valign="<%= child.getValign() %>"
                                colspan="<%= child.getColspan() %>"
                                rowspan="<%= child.getRowspan() %>"
                            >
                        <% } %>
							<x:display name="<%= child.getAbsoluteName() %>"/>
                        <% if(!(child instanceof Hidden)) { %>
						    </td>
                        <% } %>
						<%
						//Inserting points
                        if(!(child instanceof Hidden))
                        {
                            if(child.getColspan() > 1)
                            {
                                for(int x = 0; x < child.getColspan(); x++)
                                {
                                    Point block = new Point(column + x, row);
                                    points.add(block);
                                }
                            }
                            if(child.getRowspan() > 1)
                            {
                                for(int y = 0; y < child.getRowspan(); y++)
                                {
                                    Point block = new Point(column, row + y);
                                    points.add(block);
                                    if(child.getColspan() > 1)
                                    {
                                        for(int x = 1; x < child.getColspan(); x++)
                                        {
                                            Point colBlock = new Point(column + x, row + y);
                                            points.add(colBlock);
                                        }
                                    }
                                }
                            }
                            column+=child.getColspan();
                            if(column >= limit)
                            {
                                column = 0;
                                row ++;
                                %></tr><tr><%
                            }
                        }
					}
				%>
			</tr>
			
			




<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>








			
		<%-- 	
		</table>
		--%>
		
		
    </c:otherwise>
</c:choose>



