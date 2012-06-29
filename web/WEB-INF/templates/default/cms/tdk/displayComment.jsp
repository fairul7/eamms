<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<x:template
    name="displayCommentary"
    type="com.tms.cms.tdk.DisplayContentObject"
    properties="id=${co.parentId}" />
