<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="advanceSearch">
          <com.tms.wiki.ui.AdvanceSearch name="form" width="100%"/>
    </page>
</x:config>

<%@include file="./includes/header.jsp" %>

<TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=col_orange cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left height=22><FONT 
                        class=font_titlewhite>&nbsp;Advance Search</FONT></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	        			
					<form name="advSearchForm" method="post" action="advanceSearchResults.jsp">    
        			<TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>

				        <TBODY>
				        	<tr>
				        		<TD valign="top" align="left" class=lightgrey>Advance Search: 
								<input type="text" name="keyword" value="Enter Keyword">
								</td>
						   	</tr>		              
				
					        <tr>
					            <TD valign="top" align="left" class=lightgrey >	
					            All Catogories: &nbsp;&nbsp; 
					            <input type="checkbox" name="category" value="category">Category &nbsp;&nbsp; 
					            <input type="checkbox" name="tags" value="tags"> Tags&nbsp;&nbsp;
					            <input type="checkbox" name="title" value="title"> Title &nbsp;&nbsp;
					            <input type="checkbox" name="article" value="article"> Article &nbsp;&nbsp;
					            </td>
					        </tr>
					        
					        <tr>
						        <td valign="top" align="left" class=lightgrey>
						        <input class="box2" type="submit" name="adv search" value="Advance Search">
						        </td>
					        </tr>				        
					        						                
				                   
		           </TBODY>
				   </TABLE>					   			   
				   </form>
        		</td></tr>
        </tbody>     
        </table>


  
             					
<%@include file="/wiki/includes/footer.jsp" %>


