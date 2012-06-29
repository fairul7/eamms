<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="editArticle">
          <com.tms.wiki.ui.EditArticle name="form" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='success' || forward.name=='cancel'}">
    <c:redirect url="viewArticle.jsp?articleId=${widgets['editArticle.form'].articleId}"/>
</c:if>

<c:choose>
	<c:when test="${!empty param.articleId}">
	    <x:set name="editArticle.form" property="articleId" value="${param.articleId}"/>
	</c:when>
	<c:otherwise>
		<x:set name="editArticle.form" property="articleId" value="${widgets['editArticle.form'].articleId}"/>
	</c:otherwise>
</c:choose>

<c:if test="${forward.name=='preview'}">
    <script>    	
        window.open('showPreview.jsp', 'mywindow', 'status=yes,resizable=yes,scrollbars=yes,width=600, height=500');
    </script>
</c:if>

<jsp:include page="includes/header.jsp" flush="true"/>
 <td valign=top align=left width="60%">
 <TABLE style="TEXT-ALIGN: center" cellSpacing=4 cellPadding=4 width="70%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left height=22><FONT 
                        class=contentName>&nbsp;Edit Article</FONT></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:display name="editArticle.form"/>
        		</td></tr>
        </tbody>
  </table>
  </td>

<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>     







