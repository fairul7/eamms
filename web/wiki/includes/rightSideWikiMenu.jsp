<%@ page import="kacang.services.security.SecurityService,
               kacang.Application,
               kacang.services.security.User"%>
<td valign=top align=left width=16><IMG SRC="images/clear.gif" height=1 width=16 border=0></td>
<td valign=top align=left>
<%
  SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
  User user = security.getCurrentUser(request); 
  boolean isWikiManager = security.hasPermission(user.getId(), "com.tms.wiki.ManageWiki", null, null);
%>
	<table cellpadding=4 cellspacing=2 border=0 width="100%">
		<TR>
   			<TD height=20 align=left colspan="2" bgcolor="#D7D7D7"><font size=2 face=Arial, Verdana color="333333">&nbsp;Wiki Menu</font></TD>
      	</TR>
		<TR>
          	<TD vAlign=center><A class="wikiMenu" href="index.jsp">Home</A></TD>
         </TR>
         <% if(user.getUsername().equals("anonymous")){ %>
         <TR>
			<TD vAlign=center><A class="wikiMenu" href="login.jsp">Login</A></TD>
		 </TR>
		 <% } %>
		 <TR>
          	<TD vAlign=center><A class="wikiMenu" href="latestArticles.jsp">Latest Articles</A></TD>
         </TR>
         <TR>
          	<TD vAlign=center><A class="wikiMenu" href="recentChanges.jsp">Recent Changes</A></TD>
         </TR>
         <% if(!user.getUsername().equals("anonymous")){ %>
         <TR>
          	<TD vAlign=center width=90 height=18 colSpan=2><A class="wikiMenu" href="createArticle.jsp">New Article</A></TD>
         </TR>
         <TR>
          	<TD vAlign=center width=90 height=18 colSpan=2><A class="wikiMenu" href="myArticles.jsp">My Articles</A></TD>
         </TR>                  
         <% } 
         	if(isWikiManager) { %>
         <TR>
          	<TD vAlign=center width=90 height=18 colSpan=2><A class="wikiMenu" href="viewModules.jsp">Setup</A></TD>
         </TR>
         <TR>
          	<TD vAlign=center width=90 height=18 colSpan=2><A class="wikiMenu" href="lockedArticles.jsp">Locked Articles</A></TD>
         </TR>
         <% } %>
         <TR>
          	<TD vAlign=center width=90 height=18 colSpan=2 class="wikiMenu"><A class="wikiMenu" href="indexPage.jsp?alphabet=A">Index</A></TD>
         </TR>
	</table>

	<table><td>&nbsp;</td></table>	
	<script>
		function goSearch(){
			var title =  document.searchForm.keyword.value;
			document.location = 'wikiSearch?title='+title;
		}
		function articleSearch() {
			var keyword =  document.searchForm.keyword.value;
			document.location = 'wikiSearch.jsp?keyword='+keyword;
		}
		function advanceSearch() {						
			document.location = 'advanceSearchResults.jsp';
		}
	</script>
	<form name="searchForm" method="get" action='wikiSearch'>
	
		<table cellpadding=4 cellspacing=2 border=0 width="100%">

		<TR>
   			<TD height=20 align=left colspan="2" bgcolor="#D7D7D7"><font size=2 face=Arial, Verdana color="333333">&nbsp;Wiki Search</font></TD>
      	</TR>
		<TR>
      	<td align=center nowrap><input name=hl type=hidden value=en>
			<input type="text" name="keyword" value="" size="30"/><br>
			<input class="button" type="submit" value="Go"/>&nbsp;
			<input class="button" type="button" value="Search" onClick="articleSearch();"/>&nbsp;
			<input class="button" type="button" value="Advance Search" onClick="advanceSearch();"/>
			</td>
		</tr>
		</table>
	</form>
</td>