<%@ include file="/common/header.jsp" %>

<c:set var="textbox" value="${widget}"/>
<c:set var="height" value="${widget.rows*15}"/>
<c:if test="${height < 300}">
    <c:set var="height" value="300"/>
</c:if>

<c:if test="${textbox.invalid}">
  !<div style="border:1 solid #de123e">
</c:if>
<div style="width:100%; border:solid 1px silver">
<textarea id="<c:out value="${textbox.absoluteName}"/>" name="<c:out value="${textbox.absoluteName}"/>" style="width:100%;height:<c:out value="${height}"/>;"><c:out value="${textbox.value}"/></textarea>
</div>
<c:if test="${textbox.invalid}">
  </div>
</c:if>

<c:forEach var="child" items="${textbox.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>

<script type="text/javascript">
  _editor_lang = "en";
  _editor_url = "<c:url value="/common/xinha/"/>";
</script>
<!-- load the main HTMLArea files -->
<script type="text/javascript" src="<c:url value="/common/xinha/htmlarea.js.jsp"/>?<c:if test="${!empty textbox.imageUrl}">&imageUrl=<c:out value="${textbox.imageUrl}"/></c:if><c:if test="${!empty textbox.linkUrl}">&linkUrl=<c:out value="${textbox.linkUrl}"/></c:if>"></script>

<script type="text/javascript">
<c:if test="${empty requestScope.initScript}">
<%--
try {
    HTMLArea.loadPlugin("TableOperations");
}
catch(e) {
  // ignore to let editor load up properly
}
try {
    HTMLArea.loadPlugin("ContextMenu");
}
catch(e) {
  // ignore to let editor load up properly
}
--%>
</c:if>

function <c:out value="${textbox.absoluteNameForJavaScript}"/>submit() {
    return true;
}

var <c:out value="${textbox.absoluteNameForJavaScript}"/>_editor;

function <c:out value="${textbox.absoluteNameForJavaScript}"/>initEditor() {
  xinha_editors = null;
    xinha_init    = null;
    xinha_config  = null;
    xinha_plugins = null;

  xinha_plugins = xinha_plugins ? xinha_plugins :
      [
<%--       'CharacterMap',--%>
       'ContextMenu',
       'FullScreen',
<%--       'ListType',--%>
<%--       'SpellChecker',--%>
<%--       'Stylist',--%>
<%--       'SuperClean',--%>
       'TableOperations'
      ];
             // THIS BIT OF JAVASCRIPT LOADS THE PLUGINS, NO TOUCHING  :)
             if(!HTMLArea.loadPlugins(xinha_plugins, <c:out value="${textbox.absoluteNameForJavaScript}"/>initEditor)) return;

      <c:out value="${textbox.absoluteNameForJavaScript}"/>_editor =
      [
        '<c:out value="${textbox.absoluteName}"/>'
      ];
       xinha_config = xinha_config ? xinha_config() : new HTMLArea.Config();

      xinha_editors   = HTMLArea.makeEditors(<c:out value="${textbox.absoluteNameForJavaScript}"/>_editor, xinha_config, xinha_plugins);

      HTMLArea.startEditors(xinha_editors);

      return false;
}

<c:set scope="request" var="initScript"><c:out value="${requestScope.initScript}"/>;setTimeout(function() {<c:out value="${textbox.absoluteNameForJavaScript}"/>initEditor();}, 0);</c:set>

function initAllEditors() {
    <c:out value="${initScript}"/>
}

window.onload=initAllEditors;
</script>

