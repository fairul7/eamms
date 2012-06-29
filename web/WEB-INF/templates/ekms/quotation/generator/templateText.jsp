<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="template" value="${widget}"/>
<div id="${template.textObject.templateId }" />
  <c:out value="${template.textObject.templateBody}" escapeXml="false" />
</div>