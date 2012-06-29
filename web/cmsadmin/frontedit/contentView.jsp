<%@ page import="com.tms.cms.core.ui.ContentHelper"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config>
<page name="cms">
    <%@ include file="/cmsadmin/content/contentDefinition.jsp" %>
</page>
</x:config>

<x:template type="TemplateDisplayFrontEndEdit" body="custom" />

<c:redirect url="/cmsadmin/content/contentView.jsp"/>
