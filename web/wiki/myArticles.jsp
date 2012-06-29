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
             	  <TD vAlign=center align=left height=22><FONT class=contentName>&nbsp;My Articles</FONT></TD>
     			</TR>             
     			</TBODY>
				</TABLE>
			</td></tr>
			<TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	
        			<x:template type="com.tms.wiki.ui.MyArticles" /> 
        	  </td></tr>
</TBODY></TABLE>		
</td>			

<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>    