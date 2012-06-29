<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentAdd">

	<%-- 
    <portlet name="addContentPortlet" text="<fmt:message key='general.label.addNewContent'/>" width="100%" permanent="true">
    --%>
    <%-- 
    <portlet name="addContentPortlet" text="" width="100%" permanent="true">
    --%>
        <com.tms.cms.core.ui.AddContentObjectPanel name="addContentPanel" width="100%">
            <forward name="save" url="contentView.jsp" redirect="true"/>
            <forward name="submit" url="contentView.jsp" redirect="true"/>
            <forward name="approve" url="contentView.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.AddContentObjectPanel>
    <%--    
    </portlet>
    --%>
</page>
</x:config>

<c:if test="${forward.name=='failed'}">
    <script>
        alert("Unable to perform the operation at this time. Please try again later.");
        document.location="<c:url value="/ekms/cmsadmin/contentView.jsp"/>";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader_withoutspace.jsp" flush="true"/>
	<table width="100%">
	<tr>
	<td  class="contentBgColor">
                <%--Content Path--%>
                <x:display name="cms.contentPath"/> 
   	</td>
   	</tr>
   	<tr><td>&nbsp;</td></tr>             
	<tr>
        <td height="22" class="contentTitleFont">
        		<font class="contentTitleFont">&nbsp;
           		<fmt:message key='general.label.addNewContent'/>
           		</font>
        </td>
    </tr>    
    	<td class="contentBgColor">
            <table width="100%" cellspacing="0">
       		<tr><td>
                  <%-- Content Tree --%>
                  <%-- 
                  <x:display name="contentAdd.addContentPortlet"/>
                  --%>
                  <x:display name="contentAdd.addContentPanel" />
			</td></tr>
			</table>
	 	</td>
    </tr>
    </table>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
