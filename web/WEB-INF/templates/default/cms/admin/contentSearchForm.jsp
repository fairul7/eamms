<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" scope="request" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true" />
<c:if test="${!(empty form.message)}">
    <script>alert("<c:out value="${form.message}"/>");</script>
</c:if>

<table cellpadding="2" cellspacing="2" border="0" style="width: 100%; text-align: left;">


<tbody>
	<tr>
		<td width="30%" class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key='cms.label.enterSearchWords'/> 
		</td>
		<td>
			<x:display name="${form.childMap.searchField.absoluteName}"/><br/>
			<fmt:message key='cms.label.useQuotesAroundPhrases'/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top; ">
			<fmt:message key='cms.label.appearingIn'/>
		</td>
		<td>
			<x:display name="${form.childMap.archiveBox.absoluteName}"/><br/>
			<x:display name="${form.childMap.andRadio.absoluteName}"/><fmt:message key='cms.label.and'/>
        	<x:display name="${form.childMap.orRadio.absoluteName}"/><fmt:message key='cms.label.or'/>
        	<x:display name="${form.childMap.notRadio.absoluteName}"/><fmt:message key='cms.label.not'/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key='cms.label.additionalWordsOrPhrases'/>
		</td>
		<td>
			<x:display name="${form.childMap.searchField2.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key="cms.label_com.tms.cms.section.Section"/>
		</td>
		<td>
			<x:display name="${form.childMap.sectionSelectBox.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key="cms.label.contentName"/>
		</td>
		<td>
			<x:display name="${form.childMap.contentName.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key="cms.label.contentAuthor"/>
		</td>
		<td>
			<x:display name="${form.childMap.contentAuthor.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key="cms.label.fileType"/>
		</td>
		<td>
			<x:display name="${form.childMap.fileType.absoluteName}"/> 
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key='cms.label.selectADateRange'/>
		</td>
		<td>
			<x:display name="${form.childMap.dateRadio.absoluteName}"/>
        	<fmt:message key='cms.label.onThisSpecificDay'/>:<br>
        	<x:display name="${form.childMap.dateField.absoluteName}"/>
        	<br/>
        	<x:display name="${form.childMap.dateRangeRadio.absoluteName}"/>
        	<fmt:message key='cms.label.inADateRangeFrom'/><br>
        	<x:display name="${form.childMap.startDateField.absoluteName}"/> 
        	<fmt:message key='cms.label.to'/> 
        	<x:display name="${form.childMap.endDateField.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top;">
			<fmt:message key='cms.label.sortResultsBy'/>
		</td>
		<td>
			<x:display name="${form.childMap.sortBox.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" style="vertical-align: top">
			<fmt:message key='cms.label.resultsPerPage'/>
		</td>
		<td>
			<x:display name="${form.childMap.pageSizeSelectBox.absoluteName}"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><x:display name="${form.childMap.submitButton.absoluteName}"/></td>
	</tr>
</tbody>

<%-- 
<tbody>
    <tr>
      <td style="vertical-align: top;">
      <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
        <thead><tr valign="top">
          <td style="width: 50%;">
          <p align="left"><b><fmt:message key='cms.label.enterSearchWords'/></b> 
          <fmt:message key='cms.label.useQuotesAroundPhrases'/><br>
          <x:display name="${form.childMap.searchField.absoluteName}"/>
				</p>
			</td>
          <td width="50%">
          <p align="left"><b><fmt:message key='cms.label.appearingIn'/></b>
            <br>
            <x:display name="${form.childMap.archiveBox.absoluteName}"/>
				</p>
			</td>
        </tr>
        </thead>
      </table>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;">
        <x:display name="${form.childMap.andRadio.absoluteName}"/><fmt:message key='cms.label.and'/>
        <x:display name="${form.childMap.orRadio.absoluteName}"/><fmt:message key='cms.label.or'/>
        <x:display name="${form.childMap.notRadio.absoluteName}"/><fmt:message key='cms.label.not'/><br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;">
      <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
        <thead><tr valign="top">
          <td style="width: 50%;">
          <p align="left"><b><fmt:message key='cms.label.additionalWordsOrPhrases'/></b><br>
          <x:display name="${form.childMap.searchField2.absoluteName}"/>
				</p>
			</td>
          <td width="50%">
          <p align="left">
          <br>
				</p>
			</td>
        </tr>
        </thead>
      </table>
      </td>
&nbsp;&nbsp;&nbsp; <br>
    </tr>
    
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label_com.tms.cms.section.Section"/></b>
            <br>
            <x:display name="${form.childMap.sectionSelectBox.absoluteName}"/>
            
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.contentName"/></b>
            <br>
            <x:display name="${form.childMap.contentName.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.contentAuthor"/></b>
            <br>
            <x:display name="${form.childMap.contentAuthor.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.fileType"/></b>
            <br>
            <x:display name="${form.childMap.fileType.absoluteName}"/>         
          </td>
        </tr>
    <tr>
    <tr>
      <td style="vertical-align: top;"><b><fmt:message key='cms.label.selectADateRange'/></b><br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;">
        <x:display name="${form.childMap.dateRadio.absoluteName}"/>
        <fmt:message key='cms.label.onThisSpecificDay'/>:<br>
        <x:display name="${form.childMap.dateField.absoluteName}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;">
        <x:display name="${form.childMap.dateRangeRadio.absoluteName}"/>
        <fmt:message key='cms.label.inADateRangeFrom'/><br>
        <x:display name="${form.childMap.startDateField.absoluteName}"/> <fmt:message key='cms.label.to'/> <x:display name="${form.childMap.endDateField.absoluteName}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><br>
      <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
        <thead><tr valign="top">
          <td><b><fmt:message key='cms.label.sortResultsBy'/></b><br>
          <x:display name="${form.childMap.sortBox.absoluteName}"/>
          </td>
          <td><b><fmt:message key='cms.label.resultsPerPage'/></b><br>
          <x:display name="${form.childMap.pageSizeSelectBox.absoluteName}"/>
          </td>
          <td style="vertical-align: top;">
            <br>
            <x:display name="${form.childMap.submitButton.absoluteName}"/>
          </td>
        </tr>
        </thead>
      </table>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><br>
      </td>
    </tr>
&nbsp;&nbsp;&nbsp; <br>
--%>


</tbody>

</table>

<jsp:include page="../../form_footer.jsp" flush="true" />
