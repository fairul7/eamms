<%@ include file="/common/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>

<td valign=top align=left width="60%">
<TABLE style="TEXT-ALIGN: center" cellSpacing=2 cellPadding=2 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left><FONT 
                        class="contentName">&nbsp;View Article</FONT></TD>
                      </TR>                     
                      </TBODY>                      
				</TABLE>
			  </TD></TR>			  
              <TR>
                <TD style="VERTICAL-ALIGN: top" > 	
        			<x:template type="com.tms.wiki.ui.ViewArticle" /> 
        	  </td></tr>        	  
        	  
        	  <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left><FONT 
                        class="siteBodyHeader">&nbsp;Comments</FONT></TD>
                     </TR>
                     </TBODY>                      
				</TABLE>
			  </TD></TR>
			  <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:template type="com.tms.wiki.ui.ViewArticleComments" name="articleComments"/> 
        	  </td></tr>		
        	</tbody>
  </table>
</td>
<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>
<c:if test="${not empty articleComments.message}">
	<script>
		alert('<c:out value="${articleComments.message}"/>');
		document.location = 'viewArticle.jsp?articleId=<c:out value="${articleComments.articleId}"/>';
	</script>
</c:if>

