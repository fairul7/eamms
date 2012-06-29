<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="createArticle">
          <com.tms.wiki.ui.CreateArticle name="form" width="100%" />
    </page>
</x:config>

<c:choose>
    <c:when test="${!empty param.categoryId}">
        <x:set name="createArticle.form" property="categoryId" value="${param.categoryId}"/>
    </c:when>
    <c:otherwise>
        <x:set name="createArticle.form" property="categoryId" value="${widgets['createArticle.form'].categoryId}"/>
    </c:otherwise>
</c:choose>

<c:if test="${forward.name=='success'}">
	<script>
		alert('Successfully added');
		window.location= 'latestArticles.jsp';		
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}">
    <script>
    	window.location= 'latestArticles.jsp';
    </script>
</c:if>

<c:if test="${forward.name=='preview'}">
    <script>
        window.open('showPreview.jsp', 'mywindow2', 'status=yes,resizable=yes,scrollbars=yes,width=600, height=500');
    </script>
</c:if>
<jsp:include page="includes/header.jsp" flush="true"/>
<html>
<HEAD><TITLE>TMS-ekp</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK href="./images/default.css" rel=stylesheet>
</HEAD>
<BODY><LINK href="./images/default.css" rel=stylesheet>
<td valign=top align=left width="60%">
 <TABLE style="TEXT-ALIGN: center" cellSpacing=2 cellPadding=2 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing=2 cellPadding=2 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left><FONT 
                        class=contentName>&nbsp;New Article</FONT></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:display name="createArticle.form"/>
        		</td></tr>
        </tbody>
  </table>
  </td>
  </body>
</html>

<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>



