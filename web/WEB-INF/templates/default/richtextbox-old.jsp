<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="textbox" value="${widget}"/>
<c:set var="prefix" value="${textbox.absoluteNameForJavaScript}"/>
<c:set var="property" value="${textbox.absoluteName}"/>
<c:set var="form" value="${textbox.rootForm.absoluteName}"/>
<c:set var="path" value="${param.path}"/>
<c:if test="${path == null}">
    <c:set var="path" value="../../"/>
</c:if>
<c:set var="imageUrl" value="${textbox.imageUrl}"/>
<c:set var="linkUrl" value="${textbox.linkUrl}"/>

<script language="JavaScript">
<!--
    var <c:out value="${prefix}"/>html=true;
    var showAlert;

    function <c:out value="${prefix}"/>init() {
        if (!document.all || !document.all.<c:out value="${prefix}"/>editor) {
        }
        else {
            document.all.<c:out value="${prefix}"/>editor.focus();
            document.all.<c:out value="${prefix}"/>editor.document.onload=<c:out value="${prefix}"/>IEinit();
        }
    }

    function <c:out value="${prefix}"/>nonIEinit() {
        if (!showAlert) {
            alert('Rich-Text Editor Requires Internet Explorer');
            showAlert = true;
        }

        <c:out value="${prefix}"/>hideVisual();
        <c:out value="${prefix}"/>showSource();
        <c:out value="${prefix}"/>hideToggleButton();
    }

    function <c:out value="${prefix}"/>IEinit() {
        document.all.<c:out value="${prefix}"/>editor.focus();
        var myTimer=setTimeout("<c:out value="${prefix}"/>copySourceToVisual()", 250);
        try {
            var i=0;
            while (document.forms['<c:out value="${textbox.rootForm.absoluteName}"/>'].elements[i].type == 'hidden') {
                i++;
            }
            document.forms['<c:out value="${textbox.rootForm.absoluteName}"/>'].elements[i].focus();
        }
        catch(e) {
        };
    }

    function <c:out value="${prefix}"/>toggle() {
        if (<c:out value="${prefix}"/>html) {
            <c:out value="${prefix}"/>html = false;
            <c:out value="${prefix}"/>setToggleButtonLabel('To Rich Editor');
            <c:out value="${prefix}"/>copyVisualToSource();
            <c:out value="${prefix}"/>hideVisual();
            <c:out value="${prefix}"/>showSource();
        }
        else if (document.frames.<c:out value="${prefix}"/>editor.tbContentElement.DOM.body) {
            <c:out value="${prefix}"/>html = true;
            <c:out value="${prefix}"/>setToggleButtonLabel('To Text Mode');
            <c:out value="${prefix}"/>copySourceToVisual();
            <c:out value="${prefix}"/>hideSource();
            <c:out value="${prefix}"/>showVisual();
        }
    }

    function <c:out value="${prefix}"/>hideToggleButton() {
        document.getElementById('<c:out value="${prefix}"/>toggleButton').style.display = "none";
    }

    function <c:out value="${prefix}"/>setToggleButtonLabel(label) {
        document.getElementById('<c:out value="${prefix}"/>toggleButton').value=label;
    }

    function <c:out value="${prefix}"/>copySourceToVisual() {
        document.frames.<c:out value="${prefix}"/>editor.tbContentElement.DOM.body.innerHTML=document.getElementById('<c:out value="${property}"/>').value;
    }

    function <c:out value="${prefix}"/>copyVisualToSource() {
        document.getElementById('<c:out value="${property}"/>').value=document.frames.<c:out value="${prefix}"/>editor.tbContentElement.DOM.body.innerHTML;
    }

    function <c:out value="${prefix}"/>showVisual() {
        var visualEditor = document.getElementById('<c:out value="${prefix}"/>editorDiv');
        visualEditor.style.display='block';
        //visualEditor.focus();
    }

    function <c:out value="${prefix}"/>hideVisual() {
        var visualEditor = document.getElementById('<c:out value="${prefix}"/>editorDiv');
        visualEditor.style.display='none';
    }

    function <c:out value="${prefix}"/>showSource() {
        var sourceEditor = document.getElementById('<c:out value="${property}"/>');
        sourceEditor.style.display='block'; 
        //sourceEditor.focus();
    }

    function <c:out value="${prefix}"/>hideSource() {
        var sourceEditor = document.getElementById('<c:out value="${property}"/>');
        sourceEditor.style.display='none'; 
    }

    function <c:out value="${prefix}"/>submit() {
        <c:out value="${prefix}"/>copyVisualToSource();
        return true;
    }
//-->
</script>

<c:if test="${textbox.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

<input id="<c:out value="${prefix}"/>toggleButton" name="<c:out value="${prefix}"/>toggleButton" onclick="<c:out value="${prefix}"/>toggle()" type="button" class="button" value="To Text Mode">

<!-- WYSIWYG Editor -->
<div id="<c:out value="${prefix}"/>editorDiv" style="display: block;">
<iframe id="<c:out value="${prefix}"/>editor" onload="<c:out value="${prefix}"/>init()" src="<%= request.getContextPath() %>/common/richeditor/editorFrame.jsp?imageUrl=<c:out value="${imageUrl}"/>&linkUrl=<c:out value="${linkUrl}"/>" width="<c:out value="${textbox.width}"/>" height="300" marginheight="0" marginwidth="0"></iframe>
</div>
<!-- WYSIWYG Editor -->

<!-- Source Code Editor -->
<textarea style="display: none;" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" cols="<c:out value="${textbox.cols}"/>" rows="<c:out value="${textbox.rows}"/>" onblur="<c:out value="${prefix}"/>copySourceToVisual()"><c:out value="${textbox.value}"/></textarea>
<!-- Source Code Editor -->

<c:if test="${textbox.invalid}">
  </span>
</c:if>
<c:forEach var="child" items="${textbox.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>

<!-- Initialize -->
<script language="Javascript">
<!--
    if (!document.all || !document.frames.<c:out value="${prefix}"/>editor) {
        <c:out value="${prefix}"/>nonIEinit();
    }
//-->
</script>
