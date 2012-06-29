<%@ include file="/common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
  <head>
    <title>Fullscreen HTMLArea</title>
    <script type="text/javascript">
      _editor_url = window.opener._editor_url;
      _editor_lang = window.opener._editor_lang;
      var BASE = window.opener.document.baseURI || window.opener.document.URL;
      var head = document.getElementsByTagName("head")[0];
      var base = document.createElement("base");
      base.href = BASE;
      head.appendChild(base);
    </script>

    <script type="text/javascript" src="<c:url value="/common/htmlarea/htmlarea.jsp"/>"></script>

    <script type="text/javascript">
	// load HTMLArea scripts that are present in the opener frame
	var scripts = window.opener.HTMLArea._scripts;
	for (var i = 4; i < scripts.length; ++i) {
           document.write("<scr" + "ipt type='text/javascript' src='" + scripts[i] + "'></scr" + "ipt>");
        }
    </script>

    <!-- browser takes a coffee break here -->
    <script type="text/javascript">
var parent_object  = null;
var editor	   = null;	// to be initialized later [ function init() ]
HTMLArea.loadPlugin("TableOperations");
HTMLArea.loadPlugin("ContextMenu");

/* ---------------------------------------------------------------------- *\
   Function    :
   Description :
\* ---------------------------------------------------------------------- */

function _CloseOnEsc(ev) {
}

/* ---------------------------------------------------------------------- *\
   Function    : resize_editor
   Description : resize the editor when the user resizes the popup
\* ---------------------------------------------------------------------- */

function resize_editor() {  // resize editor to fix window
	var newHeight;
	if (document.all) {
		// IE
		newHeight = document.body.offsetHeight - editor._toolbar.offsetHeight;
		if (newHeight < 0) { newHeight = 0; }
	} else {
		// Gecko
		newHeight = window.innerHeight - editor._toolbar.offsetHeight;
	}
	if (editor.config.statusBar) {
		newHeight -= editor._statusBar.offsetHeight;
	}
	editor._textArea.style.height = editor._iframe.style.height = newHeight + "px";
}

/* ---------------------------------------------------------------------- *\
   Function    : init
   Description : run this code on page load
\* ---------------------------------------------------------------------- */

function init() {
	parent_object	   = opener.HTMLArea._object;
	var config	   = HTMLArea.cloneObject( parent_object.config );
	config.width	   = "100%";
	config.height	   = "auto";

	// change maximize button to minimize button
	config.btnList["popupeditor"] = [ 'Minimize Editor', _editor_url + 'images/fullscreen_minimize.gif', true,
					  function() { window.close(); } ];

	// generate editor and resize it
	editor = new HTMLArea("editor", config);

	// register the plugins, if any
	for (var i in parent_object.plugins) {
		var plugin = parent_object.plugins[i];
		editor.registerPlugin2(plugin.name, plugin.args);
	}
	// and restore the original toolbar
        config.toolbar = parent_object.config.toolbar;
	editor.generate();
	editor._iframe.style.width = "100%";
	editor._textArea.style.width = "100%";
	resize_editor();

	editor.doctype = parent_object.doctype;

	// set child window contents and event handlers, after a small delay
	setTimeout(function() {
			   editor.setHTML(parent_object.getInnerHTML());

			   // switch mode if needed
			   if (parent_object._mode == "textmode") { editor.setMode("textmode"); }

			   // continuously update parent editor window
			   setInterval(update_parent, 500);

			   // setup event handlers
			   document.body.onkeypress = _CloseOnEsc;
			   editor._doc.body.onkeypress = _CloseOnEsc;
			   editor._textArea.onkeypress = _CloseOnEsc;
			   window.onresize = resize_editor;
		   }, 333);			 // give it some time to meet the new frame
}

/* ---------------------------------------------------------------------- *\
   Function    : update_parent
   Description : update parent window editor field with contents from child window
   \* ---------------------------------------------------------------------- */

function update_parent() {
	// use the fast version
	parent_object.setHTML(editor.getInnerHTML());
}

    </script>
    <style type="text/css"> html, body { height: 100%; margin: 0px; border: 0px; background-color: buttonface; } </style>
  </head>
  <body scroll="no" onload="setTimeout(function(){init();}, 500)" onunload="update_parent()">
    <form style="margin: 0px; border: 1px solid; border-color: threedshadow threedhighlight threedhighlight threedshadow;">
      <textarea name="editor" id="editor" style="width:100%; height:300px">&nbsp;</textarea>
    </form>
  </body>
</html>
