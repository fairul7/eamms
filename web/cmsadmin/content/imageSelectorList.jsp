<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config>
    <page name="imageSelector">
       <tabbedpanel name="tab1" width="100%">
         <c:if test="${!param.new}">
         <panel name="tree" text="<fmt:message key='general.label.related'/>">
           <com.tms.cms.image.ui.ImageSelectorList name="imageList" />
         </panel>
         </c:if>
         <panel name="list" text="<fmt:message key='general.label.other'/>">
           <com.tms.cms.image.ui.ImageSelectorTree name="imageTree" />
         </panel>
       </tabbedpanel>
    </page>
</x:config>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
td {
    font-family: Arial;
    font-size: 8pt;
    color: black;
}
</style>
</head>
<body>
   <x:display name="imageSelector"/>
</body>
</html>