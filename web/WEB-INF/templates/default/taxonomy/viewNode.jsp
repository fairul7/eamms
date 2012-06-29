<%@ page import="com.tms.cms.taxonomy.ui.ViewNode,
				 org.apache.commons.collections.SequencedHashMap,
				 java.util.Iterator,
				 java.util.Collection,
				 com.tms.cms.taxonomy.model.TaxonomyNode"%>
<%@ include file="/common/header.jsp"%>
<c:set var="w" value="${widget}"/>
<table width="100%" cellpadding="0" Cellspacing="0">
<tr>
<td>
			<table width="100%"  border="0" cellpadding="0" cellspacing="5" >
				<tr>
					<td align="left" valign="middle">
						<%
 							SequencedHashMap title = ((ViewNode) pageContext.getAttribute("w")).getTitle();
							String header = "";
                            for (Iterator i = title.keySet().iterator(); i.hasNext();)
							{
								String key = (String) i.next();
								if(!("".equals(header.toString())))
									header = " &gt; " + header;
                                if("-1".equals(key))
									header = "<span class=\"contentPathLink\"><b>" + title.get(key) + "</b></span>" + header;
								else if("".equals(key))
									header = "<a href=\"/ekms/content/taxonomyTree.jsp\" class=\"contentPathLink\">" + title.get(key) + "</a>" + header;
                                else
									header = "<a href=\"/ekms/content/taxonomyTree.jsp?id=" + key + "\" class=\"contentPathLink\">" + title.get(key) + "</a>" + header;
							}
 						%>
						 <span class="indication"><%= header.toString() %></span><br>
					</td>
				</tr>
			</table>
</td>
</tr>			
	
<tr>
<td>	<br>
				<table width="95%"  border="0" cellspacing="0" cellpadding="3" align="center">
					<tr><td colspan="2" align="left" valign="top"  class="contentHeader">
					<span class="contentName"><c:out value="${widget.selectedNodeTitle}"/></span>
					</td></tr>
				</table>
<c:if test="${!empty widget.nodeList}">				
				<table width="95%" align="center" border="0" cellspacing="0" cellpadding="3" class="contentBgColor">
					<%
						Collection children = ((ViewNode) pageContext.getAttribute("w")).getNodeList();
						boolean right = false;
						for (Iterator i = children.iterator(); i.hasNext();)
						{
							TaxonomyNode node = (TaxonomyNode) i.next();
							if(!right)
							{ %><tr><td width="40%" align="left" valign="top" class="tsArticleMore"><% }
							else
							{ %><td width="56%" align="left" valign="top" class="tsArticleMore"><% }
                            %>
                        <table width="100%" cellpadding="5" cellspacing="0">
                        <tr>
                        <td width="10" valign="top" class="contentBgColor" width="50%">
                        <li>
                        <a href="/ekms/content/taxonomyTree.jsp?id=<%= node.getTaxonomyId() %>"><%= node.getTaxonomyName() %></a>
                        </li>
                        </td></tr></table>
                        <%-- <li><a href="/cmsadmin/content/taxonomyTree.jsp?id=<%= node.getTaxonomyId() %>" class="mainsection2link"><%= node.getName() %></a></li>--%>
                            <%
                            if(!right)
							{ %></td><% }
							else
							{ %></td></tr><% }
							if(right)
								right = false;
							else
								right = true;
						}
						if(right) { %><td>&nbsp;</td></tr><% }
					%>
				</table>
</c:if>				
</td>
</tr>				
	
</table>	
