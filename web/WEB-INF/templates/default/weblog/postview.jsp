<%@ page import="org.apache.commons.lang.StringUtils,
                 java.util.regex.Pattern"%>
<%@include file="/common/header.jsp"%>
<style>
.storyBox
{
  border-bottom: 1px solid #B39169;
}
.storyDateBox
{
  font-family: Times New Roman, serif;
  float: left;
  padding:0px 5px 5px 0px;
  margin: 0px 10px 4px 0px;
  border-right: 1px solid #B39169;
  width: 70px;
  position: relative;
  /*background: #f7f7f7;*/
}
.storyDateBox h2
{
  font-weight: 400;
  margin: 0px;
  padding: 0px;
}

.storyDateBox span
{
  font-size: 11px;
}
.blockDisplay
{
  display: block;
}
.noDisplay
{
  display: none;
}
</style>
<c:set var="post" value="${widget.post}"  />
<a href="blogview.jsp?blogId=<c:out value="${post.blogId}" />"style="text-decoration: none;">
	<span class="contentName"><c:out value="${widget.blog.title}" /></span>
</a>
<table>
	<tr>
		<td>
			<br><br>
			<div class="storyBox">
				<div class="storyDateBox">
					<h2>
						<a href="blogview.jsp?blogId=<c:out value="${post.blogId}" />&date=<fmt:formatDate value="${post.publishTime}" pattern="${globalDateLong}"  />"style="text-decoration: none;">
							<fmt:formatDate value="${post.publishTime}" pattern="d"  />
						</a>
						<span><fmt:formatDate value="${post.publishTime}" pattern="MMM"  /></span>
						<span class="blockDisplay">
							<span class="noDisplay">&middot;</span>
							<fmt:formatDate value="${post.publishTime}" pattern="EEE yyyy"  />
						</span>
					</h2>
				</div>
				<div>
					<strong><c:out value="${post.title}" /></strong><br>
					<c:set var="content" value="${post.contents}"/>
					<%
						String translated = StringUtils.replace((String)pageContext.getAttribute("content"), "\n", "<br>");
						pageContext.setAttribute("content", translated);
					%>
					<c:out value="${content}" escapeXml="false" ></c:out>
				</div>
				<br>
				<em>@ <fmt:formatDate value="${post.publishTime}" pattern="h:mm a"/></em>
				&nbsp;&nbsp;&nbsp; <fmt:message key='weblog.label.comments'/>[<font color="#FF0000"><c:out value="${post.totalComments}" /></font>]
				<br><br>
			</div>
			<c:choose>
				<c:when test="${widget.preview}" >
					<c:set var="comment" value="${widget.temp_comment}" />
					<br><strong><fmt:message key='weblog.label.preview'/>:</strong><br><br>
					<c:set var="comments" value="${comment.comment}" />
					<%
						String comment = (String)pageContext.getAttribute("comments");/*[Tt][fF][oO][oO][Tt][Ee][Rr]*/
						Pattern patter = Pattern.compile("<footer>|<tbody>|<theader>|<tr>|<td>|<table>|<script>",Pattern.CASE_INSENSITIVE);
						String comments =patter.matcher(comment).replaceAll("");
						comments = StringUtils.replace(comments,"\n","<br>");
						pageContext.setAttribute("comments",comments);
					%>
					<c:out value="${comments}" escapeXml="true" />
					<br><br>
					<font size="-2">
						<fmt:message key='weblog.label.postby'/> <c:if test="${!empty widget.email}" ><a href="mailto:<c:out value="${widget.email}" />"></c:if><c:out value="${comment.userName}" /><c:if test="${!empty widget.email}" ></a></c:if> <fmt:message key='weblog.label.on'/> <fmt:formatDate value="${comment.date}" pattern="${globalDatetimeLong}"/>
						<c:if test="${!empty widget.url}" >
							<br><fmt:message key='weblog.label.website'/>: <a href="<c:out value="${widget.url}" />"><c:out value="${widget.url}" /></a>
						</c:if>
					</font>
				</c:when>
				<c:otherwise>
					<c:forEach var="comment" items="${widget.post.comments}" varStatus="status" >
						<c:if test="${status.first}" ><a name=comment><br><strong><fmt:message key='weblog.label.comments'/>:</strong></a></c:if>
						<br><br><br>
						<li>
							<c:set var="comments" value="${comment.comment}" />
							<%
								String comment= StringUtils.replace((String)pageContext.getAttribute("comments"), "\n", "<br>");
								pageContext.setAttribute("comments", comment);
							%>
							<c:out value="${comments}" escapeXml="false" />
						</li>
						<br><br>
						<font size="-2">
							<fmt:message key='weblog.label.postby'/> <strong><c:if test="${!empty comment.email}" ><a href="mailto:<c:out value="${comment.email}" />"></c:if><c:out value="${comment.userName}" /><c:if test="${!empty comment.email}" ></a></c:if></strong> <fmt:message key='weblog.label.on'/> <fmt:formatDate value="${comment.date}" pattern="${globalDatetimeLong}"/>
							<c:if test="${!empty comment.url}" >
								<br><fmt:message key='weblog.label.website'/>: <a href="<c:out value="${comment.url}" />"><c:out value="${comment.url}" /></a>
							</c:if>
						</font>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<br><br><br>
			<strong><fmt:message key='weblog.label.postaComment'/>:</strong>
			<br>
			<form method="POST" action="">
				<table>
					<tr>
						<td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.name'/>:</td>
						<td><input id="nametf" type=text name=name value="<c:out value="${widget.name}"/>" size=32 maxlength=80></td>
					</tr>
					<tr>
						<td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.email'/>:</td>
						<td><input type=text name=email value="<c:out value="${widget.email}"/>" size=32 maxlength=80></td>
					</tr>
					<tr>
						<td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.url'/>:</td>
						<td><input type=text name=url value="<c:out value="${widget.url}"/>"size=32 maxlength=255></td>
					</tr>
					<tr>
						<td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.comments'/>:</td>
						<td>
							<c:if test="${widget.commentEmpty}" >!<span style="border:1 solid #de123e"></c:if>
							<textarea name=comment cols=30 rows=5><c:out value="${widget.comment}"/></textarea>
							<c:if test="${widget.commentEmpty}" ></span></c:if>
							<br>
							<fmt:message key='weblog.label.hTMLSyntax'/>:<em> <fmt:message key='weblog.label.enabled'/></em>
						</td>
					</tr>
					<tr>
						<td valign="top" align="right" class="blogLabel"></td>
						<td><input type=submit value="Preview" name="submit" class="button"> <input type=submit value="Post" name="submit" class="button"></td>
					</tr>
				</table>
			</form>
		</td>
		<td valign="top" width="30%">
			<div class="sidebarbox">
				<x:template name="calendar" type="com.tms.collab.weblog.ui.BlogCalendar" properties="blogUrl=blogview.jsp" ></x:template>
			</div>
			<br>
			<c:if test="${!empty widget.blog.links}" >
				<div style="border-bottom: 1px solid #B39169;text-alignment:justify"><strong><fmt:message key='weblog.label.links'/></strong></div>
				<table>
					<c:forEach var="link" items="${widget.blog.links}" >
						<tr><td><a href="//<c:out value="${link.url}" />" target="_blank"><c:out value="${link.name}" /></a><br></td></tr>
					</c:forEach>
				</table>
			</c:if>
		</td>
	</tr>
</table>
<script>
   window.onload = setFocus;
    function setFocus()
	{
    	document.getElementById("nametf").focus();
    }
</script>


