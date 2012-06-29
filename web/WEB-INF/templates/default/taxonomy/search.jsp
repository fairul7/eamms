<%@ page import="com.tms.cms.taxonomy.ui.TaxonomySearch,
				 com.tms.cms.taxonomy.model.TaxonomyMap,
				 com.tms.cms.core.model.ContentObject,
				 java.util.Collection,
				 java.util.Iterator,
				 org.apache.commons.collections.SequencedHashMap" %>
<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="nodesList" value="${widget.selectedNodes}"/>
<c:set var="results" value="${widget.results}"/>

<script>
	function addNode(){
		document.location = "<%=request.getServletPath()%>" + "?cn=<c:out value='${w.absoluteName}'/>&et=addNode";
	}
	
	function clearNode(){
		document.location = "<%=request.getServletPath()%>" + "?cn=<c:out value='${w.absoluteName}'/>&et=clearNode";
	}
</script>

<table width="95%">

<c:if test="${! w.selectNoteAction}">
<tr>
	<td>
		<table width="100%" class="borderlightwhole">
			<tr>
				<td class="textsmall2">
					<b><fmt:message key="taxonomy.label.selectedNode"/></b>
				</td>
			</tr>
			<%
				Collection nodesList = (Collection)pageContext.getAttribute("nodesList");
				
				if(nodesList != null && nodesList.size() > 0){
					
					for(Iterator i=nodesList.iterator(); i.hasNext(); ){
						SequencedHashMap path = (SequencedHashMap)i.next();
						String header = "";
						for (Iterator j = path.keySet().iterator(); j.hasNext();){
							String key = (String) j.next();
							if(!("".equals(header.toString())))
								header = " &gt; " + header;
                            if("-1".equals(key))
								header = "<b>" + path.get(key) + "</b>" + header;
							else if("".equals(key))
								header = path.get(key) + header;
                            else
								header = path.get(key) + header;
						}
			%>
					<tr>
						<td class="tableRow"><%=header%></td>
					</tr>
			<%
						
					}
				}
			%>
			
			<tr><td>&nbsp;</td></tr>
		</table>
	</td>
</tr>
<tr>
	<td align=left>
		<input class=button type="button" value="Select Nodes" onclick="addNode()"/> 
<!-- 		<input class=button type="button" value="Clear Nodes" onclick="clearNode()"/>  -->

	</td>
</tr>
</c:if>

<tr>
	<td>
		<x:display name="${w.mapping.absoluteName}"/>
	</td>
</tr>

<tr>
	<td>
		&nbsp;
	</td>
</tr>

<c:if test="${! w.selectNoteAction}">
<tr>
	<td>
		<table width="100%"  border="0" cellspacing="5" cellpadding="5" class="borderlightwhole">
			<tr class="tableTitle">
				<td colspan="3"><hr>
				<%-- 
					<span class="middlehd">
						Result(s)
					</span>
					--%>
				</td>
			</tr>
				<%
					Collection nodesList = (Collection)pageContext.getAttribute("results");

					if(nodesList != null && nodesList.size() > 0){
						int counter = 0; // to make sure the no content found onli display once
						for(Iterator i=nodesList.iterator(); i.hasNext(); ){
							counter++;
							TaxonomyMap results[] = (TaxonomyMap[])i.next();
							if(results != null){
								for(int j=0; j<results.length; j++){
									ContentObject content = results[j].getContentObject();
									request.setAttribute("co", content);
									
									if((j+1)%2 != 0)
										request.setAttribute("style", "background-color:#FFFFFF");
									else
										request.setAttribute("style", "background-color:no");
									
									String cnt = ""+j;
									request.setAttribute("cnt",cnt);
				%>
								
								<tr align="left" valign="top" style="<c:out value='${style}'/>">
									<%--<td  align="left" valign="middle" >&nbsp;</td>
									<td align="left" width="25"> 
										<c:choose>
											<c:when test="${co.className == 'com.tms.cms.article.Article'}">
												<fmt:message key="cms.label.iconLabel_com.tms.cms.article.Article" />
											</c:when>
											<c:when test="${co.className == 'com.tms.cms.document.Document'}">
												<fmt:message key="cms.label.iconLabel_com.tms.cms.document.Document" />
											</c:when>
											<c:when test="${co.className == 'com.tms.cms.image.Image'}">
												<fmt:message key="cms.label.iconLabel_com.tms.cms.image.Image" />
											</c:when>
										</c:choose>
									</td> --%>
									<td align="left" valign="middle" class="contentdate2" colspan="3">
                <c:set var="wName" value="displayPath_${cnt}"/>
                <x:template name="${wName}" type="TemplateDisplayPath" properties="id=${co.id}&rootId=com.tms.cms.section.Section_Sections" body="custom">
                    <c:set var="pathToContentObject" value="${pageScope[wName].pathToContentObject}"/>

                    <c:if test="${!empty pathToContentObject[0]}">
                        <a class="contentPath" href="index.jsp"><fmt:message key='general.label.home'/></a> <span class="separator">&gt;</span>
                    </c:if>

                    <c:forEach items="${pathToContentObject}" var="pathObject" varStatus="ps">
                        <c:if test="${!ps.last}">
                            <a title="<c:out value="${pathObject.name}"/>"
                               class="contentPath"
                               href="<c:url value="content.jsp?id=${pathObject.id}"/>">
                                    <c:out value="${pathObject.name}"/></a>
                            <span class="separator">&gt;</span>
                        </c:if>
                    </c:forEach>
                </x:template>
                <br>
									
										<a href="/ekms/content/content.jsp?id=<%=content.getId()%>" class="contentSubheader"><c:out value="${co.name}"/></a>
										<br>
										<span class="contentAuthor"><fmt:formatDate value="${co.date}" pattern="MMMM d, yyyy"/></span>	
									</td>
									<%--
									<td align="left" valign="middle">
										<fmt:formatDate value="${co.date}" pattern="${globalDateLong}"/>
									</td>
									 --%>
								</tr>
						
				<%
								}
							}else{
								if(counter == nodesList.size()){
				%>
								<tr align="left" valign="top"><td colspan="3" align="left" class="contentdate2">No contents found</td></tr>
				<%
								}
							}
						}
					}else{
				%>
						<tr align="left" valign="top"><td colspan="3" align="left" class="contentdate2">No contents found</td></tr>
				<%
					}
				%>
				
				<tr>
			        <td align="right" valign="top" class="eventNormal" colspan="3">
			            <% int i=0; %>
			            <c:forEach var="page" items="${widget.pageMap}">
			                <% if (i==0) { %>[ Page <% } else { %> | <% } i++;%>
			                <c:if test="${widget.page == page}">
			                    <span class="contentPageLink"><b><c:out value="${page}"/></b></span>
			                </c:if>
			                <c:if test="${widget.page != page}">
			                <span>
			                <a href="taxonomySearch.jsp?page=<c:out value="${page}"/>" class="rw_boldLink"><c:out value="${page}"/></a>  </span>
			                </c:if>
			            </c:forEach>
			            <% if (i>0) { %>]<% } %>
			        </td>
    			</tr>
		</table>
	</td>
</tr>
</c:if>
</table>


