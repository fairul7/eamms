<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.services.security.SecurityService,
               kacang.Application,
               kacang.services.security.User"%>

<jsp:include page="includes/header.jsp" flush="true" />



	<!--- 2Columns --->
		
	<table cellpadding=0 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=top align=left colspan=3><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width="60%">
		
			<script>
				function goSearch(){
					var title =  document.searchForm.keyword.value;
					document.location = 'wikiSearch?title='+title;
				}
				function searchArticle() {
					var keyword =  document.searchForm.keyword.value;
					document.location = 'wikiSearch.jsp?keyword='+keyword;
				}
			</script>
		    <form name="searchForm" method="get" action="wikiSearch">    
		    <table width="100%" cellpadding=0 cellspacing=0 align="center">
				<tr valign=top align="center">		
					<td align=center nowrap><input name=hl type=hidden value=en>
					<input maxlength=2048 name="keyword" size=55 title="Go" value=""><br>
					<input class="button" type="submit" name="go" value="Go" >&nbsp;
					<input class="button" type="button" name="search" value="Search" onClick="searchArticle();">&nbsp;
					<input class="button" type="button" name="advSearch" value="Advanced Search" onClick="document.location='advanceSearchResults.jsp';">
					<input class="button" type="button" name="login" value="Login" onClick="document.location='login.jsp';">
					</td>
				</tr>
			</table> 				
			</form>

		
	</td>
	
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
	
	<table cellpadding=0 cellspacing=0 border=0 width=100%>
		<tr>
		<td valign=top align=left colspan=2>
        &nbsp;
        <IMG SRC="images/hor_dotline.gif" height=5 width="90%" border=0>
        <br>
        <strong>Hot Wiki Topics</strong>
        </td>
		</tr>
		<tr bgcolor="DF9A40">
			<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>		
		<tr bgcolor="DF9A40">
		<td valign=top align=center>
		<x:template type="com.tms.wiki.ui.HotTopicsPortlet" reload="true" name = "hotTopicsPortlet" body="custom"> 
		<c:forEach items="${hotTopicsPortlet.articles}" var="article" varStatus="status">
			<TABLE bgColor=#f4cc98 width="100%" cellspacing="1">
			    <tr>
			    <td align="left" width="25%" ><li><a class="tablefontLink" href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" 
			        class="article_title"> <c:out value="${article.title}"/></a> </td>        
			        <td width="25%">        
			    </tr>             
			    
			    <tr>
				    <td align="left" width="50%" class="tsTextSmall">created on:<fmt:formatDate pattern="dd MM yyyy" value="${article.createdOn}"/> </i></td>
			    </tr>
			          
			    </table>
			</c:forEach>							        				
		</x:template>
		</td>
		</tr>
		
		<tr bgcolor="DF9A40">
			<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>
	</table>
		<!--- Close In The News --->

		<br>
		<!--- Latest --->
		<table cellpadding=0 cellspacing=0 border=0 width=100%>
		<tr>
		<td valign=top align=left colspan=2>
        &nbsp;
        <IMG SRC="images/hor_dotline.gif" height=5 width="90%" border=0>
        <br>
        <strong>Latest Wiki Articles</strong>
        </td>
		</tr>
		<tr>
		<td valign=top align=left>
            &nbsp;
        </td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=center>
        
		<x:template type="com.tms.wiki.ui.LatestArticlesPortlet" reload="true" name = "articlesPortlet" body="custom"> 
			<c:forEach items="${articlesPortlet.articles}" var="article" varStatus="status">
				<TABLE bgColor=#f4cc98 width="100%" cellspacing="1">
				   <tr>
				    <td align="left" width="25%" ><li><a class="tablefontLink" href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" 
				        class="article_title"> <c:out value="${article.title}"/></a> </td>        
				        <td width="25%">        
				    </tr>             
				    
				    <tr>
					    <td align="left" width="50%" class="tsTextSmall">created on:<fmt:formatDate pattern="dd/MM/yyyy" value="${article.createdOn}"/> </i></td>
				    </tr>
				          
				    </table>
				</c:forEach>
		</x:template>      

		</td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>
		</table>
		<!--- Close Documents --->

	</td>
	</tr>
	</table>
	<!--- Close 2Columns --->


	<!--- dotline --->
	<table cellpadding=0 cellspacing=0 border=0 width=100%>
	<tr>
	<td valign=top align=left colspan=3><img src="images/clear.gif" height=10 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left colspan=3 background="images/bg_dotline.gif"><img src="images/clear.gif" height=7 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left colspan=3><img src="images/clear.gif" height=5 width=1 border=0></td>
	</tr>
	</table>
	<!--- Close dotlines --->


        <%--
	<!--- Forum --->
	<table cellpadding=1 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=top align=left colspan=2>
        <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_ForumSpot" />

        <x:template type="TemplateDisplayForumList" />
        </td>
	</tr>
	</table>
	<!--- Close forum --->
	--%>

<jsp:include page="includes/footer.jsp" flush="true" />
