<%@ include file="/common/header.jsp"%>
<jsp:include page="includes/header.jsp" flush="true"/>
<TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=col_orange cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left height=22><FONT 
                        class=font_titlewhite>&nbsp;Results</FONT></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<c:forEach items="${articles}" var="article" varStatus="status">
						<TABLE class="lightgrey" width="100%" cellspacing="1" >
						    
						    
						    <tr>
						        <td align="left" width="25%" ><a href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" class="article_title"> <c:out value="${article.title}"/></a> </td>        
						        <td width="25%">
						        <td align="left" width="50%" class="font_date"><i>created by:<fmt:formatDate pattern="dd MM yyyy" value="${article.modifiedOn}"/> </i></td>
						    </tr>    
						
						    <tr>
						    	<td class="font_text">Category:<a class="font_category" href="wikiSearch?category=<c:out value='${article.categoryId}'/>"/><c:out value="${article.category}"/></a></td>
								<td width="25%">
						        <td class="font_text">Tags: <c:out value="${article.displayTags}" escapeXml="false"/></td>
						    </tr>  
						    
						     <tr>
						    	<td colspan="3" class="font_text"><p><c:out value="${article.storySummary}" escapeXml="false"/>...(more)<p></td>
						    </tr>   
						
						    <tr>
							    <td colspan="3" class="font_text">__________________________________________________________________________________________________________________________________</td>
						    </tr>
						
							</table>
						</c:forEach>
        			
        		</td></tr>
        </tbody>
  </table>

<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>     
   


