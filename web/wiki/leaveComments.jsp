<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="leaveComments">
          <com.tms.wiki.ui.LeaveComments name="form" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='success'}">
	<script>
		alert('Successfully added');
		opener.location= 'viewArticle.jsp?articleId=<c:out value="${widgets['leaveComments.form'].articleId}"/>';
		window.close();    
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}">
	<script>
		opener.location= 'viewArticle.jsp?articleId=<c:out value="${widgets['leaveComments.form'].articleId}"/>';
		window.close();    
    </script>
</c:if>

<c:choose>
<c:when test="${!empty param.articleId}">
    <x:set name="leaveComments.form" property="articleId" value="${param.articleId}"/>
</c:when>
<c:otherwise>
	<x:set name="leaveComments.form" property="articleId" value="${widgets['leaveComments.form'].articleId}"/>
</c:otherwise>

</c:choose>

<HEAD><TITLE>TMS-ekp</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK href="./styles/default.css" rel=stylesheet>
</HEAD>
<BODY>


 <TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top class=siteBodyHeader>                
				  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left><FONT 
                        class="siteBodyHeader">&nbsp;Leave Comments</font></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:display name="leaveComments.form"/>
        		</td></tr>
        </tbody>
  </table>

</body>
</html>









