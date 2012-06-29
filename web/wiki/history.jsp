<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"/>
<td valign=top align=left width="60%">
 <TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left height=22><FONT 
                        class=contentName>&nbsp;History</FONT></TD>
                      </TR></TBODY>
				  </TABLE>
			  	</TD></TR>
              		<TR>
                	<TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:template type="com.tms.wiki.ui.ArticleHistory"  name="articleHistory"/>         			
        			</td></tr>
        </tbody>
  </table>
  </td>
           					
<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>     
<c:if test="${not empty articleHistory.message}">
	<script>
		alert('<c:out value="${articleHistory.message}"/>');
		document.location = 'viewArticle.jsp?articleId=<c:out value="${articleHistory.articleId}"/>';
	</script>
</c:if>






