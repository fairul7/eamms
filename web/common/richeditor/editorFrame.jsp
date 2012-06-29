<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!--TOOLBAR_START--><!--TOOLBAR_EXEMPT--><!--TOOLBAR_END-->
<!-- Copyright 2000 Microsoft Corporation. All rights reserved. -->
<!-- Author: Steve Isaac, Microsoft Corporation -->
<!--This demo shows how to host the DHTML Editing widget on a Web page.-->
<html>
<head>
<meta HTTP-EQUIV="Expires" CONTENT="Fri, 30 Oct 1998 14:19:41 GMT">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" VALUE="no-cache, no-store, must-revalidate">
<meta NAME="GENERATOR" CONTENT="Microsoft Visual Studio 6.0">
<title>DHTML Editor</title>

<!-- Styles -->
<link REL="stylesheet" TYPE="text/css" HREF="./toolbars/toolbars.css">


<!-- Script Functions and Event Handlers -->
<script LANGUAGE="JavaScript" SRC="./inc/dhtmled.js">
</script>


<script ID="clientEventHandlersJS" LANGUAGE="javascript">
<!--

//
// Constants
//
var MENU_SEPARATOR = ""; // Context menu separator

//
// Globals
//
var docComplete = false;
var initialDocComplete = false;

var QueryStatusToolbarButtons = new Array();
var QueryStatusEditMenu = new Array();
var QueryStatusFormatMenu = new Array();
var QueryStatusHTMLMenu = new Array();
var QueryStatusTableMenu = new Array();
var QueryStatusZOrderMenu = new Array();
var ContextMenu = new Array();
var GeneralContextMenu = new Array();
var TableContextMenu = new Array();
var AbsPosContextMenu = new Array();

//
// Utility functions
//

// Constructor for custom object that represents an item on the context menu
function ContextMenuItem(string, cmdId) {
  this.string = string;
  this.cmdId = cmdId;
}

// Constructor for custom object that represents a QueryStatus command and
// corresponding toolbar element.
function QueryStatusItem(command, element) {
  this.command = command;
  this.element = element;
}

//
// Event handlers
//
function window_onload() {

  if (!document.tbContentElement) {
    return;
  }

  // Initialze QueryStatus tables. These tables associate a command id with the
  // corresponding button object. Must be done on window load, 'cause the buttons must exist.
  QueryStatusToolbarButtons[0] = new QueryStatusItem(DECMD_BOLD, document.body.all["DECMD_BOLD"]);
  QueryStatusToolbarButtons[1] = new QueryStatusItem(DECMD_COPY, document.body.all["DECMD_COPY"]);
  QueryStatusToolbarButtons[2] = new QueryStatusItem(DECMD_CUT, document.body.all["DECMD_CUT"]);
  QueryStatusToolbarButtons[3] = new QueryStatusItem(DECMD_HYPERLINK, document.body.all["DECMD_HYPERLINK"]);
  QueryStatusToolbarButtons[4] = new QueryStatusItem(DECMD_ITALIC, document.body.all["DECMD_ITALIC"]);
  QueryStatusToolbarButtons[5] = new QueryStatusItem(DECMD_JUSTIFYLEFT, document.body.all["DECMD_JUSTIFYLEFT"]);
  QueryStatusToolbarButtons[6] = new QueryStatusItem(DECMD_JUSTIFYCENTER, document.body.all["DECMD_JUSTIFYCENTER"]);
  QueryStatusToolbarButtons[7] = new QueryStatusItem(DECMD_JUSTIFYRIGHT, document.body.all["DECMD_JUSTIFYRIGHT"]);
  QueryStatusToolbarButtons[8] = new QueryStatusItem(DECMD_ORDERLIST, document.body.all["DECMD_ORDERLIST"]);
  QueryStatusToolbarButtons[9] = new QueryStatusItem(DECMD_PASTE, document.body.all["DECMD_PASTE"]);
  QueryStatusToolbarButtons[10] = new QueryStatusItem(DECMD_REDO, document.body.all["DECMD_REDO"]);
  QueryStatusToolbarButtons[11] = new QueryStatusItem(DECMD_UNDERLINE, document.body.all["DECMD_UNDERLINE"]);
  QueryStatusToolbarButtons[12] = new QueryStatusItem(DECMD_UNDO, document.body.all["DECMD_UNDO"]);
  QueryStatusToolbarButtons[13] = new QueryStatusItem(DECMD_UNORDERLIST, document.body.all["DECMD_UNORDERLIST"]);
  QueryStatusToolbarButtons[14] = new QueryStatusItem(DECMD_INSERTTABLE, document.body.all["DECMD_INSERTTABLE"]);
  QueryStatusEditMenu[0] = new QueryStatusItem(DECMD_UNDO, document.body.all["EDIT_UNDO"]);
  QueryStatusEditMenu[1] = new QueryStatusItem(DECMD_REDO, document.body.all["EDIT_REDO"]);
  QueryStatusEditMenu[2] = new QueryStatusItem(DECMD_CUT, document.body.all["EDIT_CUT"]);
  QueryStatusEditMenu[3] = new QueryStatusItem(DECMD_COPY, document.body.all["EDIT_COPY"]);
  QueryStatusEditMenu[4] = new QueryStatusItem(DECMD_PASTE, document.body.all["EDIT_PASTE"]);
  QueryStatusEditMenu[5] = new QueryStatusItem(DECMD_DELETE, document.body.all["EDIT_DELETE"]);
  QueryStatusHTMLMenu[0] = new QueryStatusItem(DECMD_HYPERLINK, document.body.all["HTML_HYPERLINK"]);
  QueryStatusHTMLMenu[1] = new QueryStatusItem(DECMD_IMAGE, document.body.all["HTML_IMAGE"]);
  QueryStatusFormatMenu[0] = new QueryStatusItem(DECMD_FONT, document.body.all["FORMAT_FONT"]);
  QueryStatusFormatMenu[1] = new QueryStatusItem(DECMD_BOLD, document.body.all["FORMAT_BOLD"]);
  QueryStatusFormatMenu[2] = new QueryStatusItem(DECMD_ITALIC, document.body.all["FORMAT_ITALIC"]);
  QueryStatusFormatMenu[3] = new QueryStatusItem(DECMD_UNDERLINE, document.body.all["FORMAT_UNDERLINE"]);
  QueryStatusFormatMenu[4] = new QueryStatusItem(DECMD_JUSTIFYLEFT, document.body.all["FORMAT_JUSTIFYLEFT"]);
  QueryStatusFormatMenu[5] = new QueryStatusItem(DECMD_JUSTIFYCENTER, document.body.all["FORMAT_JUSTIFYCENTER"]);
  QueryStatusFormatMenu[6] = new QueryStatusItem(DECMD_JUSTIFYRIGHT, document.body.all["FORMAT_JUSTIFYRIGHT"]);
  QueryStatusFormatMenu[7] = new QueryStatusItem(DECMD_SETBACKCOLOR, document.body.all["FORMAT_SETBACKCOLOR"]);
  QueryStatusTableMenu[0] = new QueryStatusItem(DECMD_INSERTTABLE, document.body.all["TABLE_INSERTTABLE"]);
  QueryStatusTableMenu[1] = new QueryStatusItem(DECMD_INSERTROW, document.body.all["TABLE_INSERTROW"]);
  QueryStatusTableMenu[2] = new QueryStatusItem(DECMD_DELETEROWS, document.body.all["TABLE_DELETEROW"]);
  QueryStatusTableMenu[3] = new QueryStatusItem(DECMD_INSERTCOL, document.body.all["TABLE_INSERTCOL"]);
  QueryStatusTableMenu[4] = new QueryStatusItem(DECMD_DELETECOLS, document.body.all["TABLE_DELETECOL"]);
  QueryStatusTableMenu[5] = new QueryStatusItem(DECMD_INSERTCELL, document.body.all["TABLE_INSERTCELL"]);
  QueryStatusTableMenu[6] = new QueryStatusItem(DECMD_DELETECELLS, document.body.all["TABLE_DELETECELL"]);
  QueryStatusTableMenu[7] = new QueryStatusItem(DECMD_MERGECELLS, document.body.all["TABLE_MERGECELL"]);
  QueryStatusTableMenu[8] = new QueryStatusItem(DECMD_SPLITCELL, document.body.all["TABLE_SPLITCELL"]);
  QueryStatusZOrderMenu[0] = new QueryStatusItem(DECMD_SEND_TO_BACK, document.body.all["ZORDER_SENDBACK"]);
  QueryStatusZOrderMenu[1] = new QueryStatusItem(DECMD_BRING_TO_FRONT, document.body.all["ZORDER_BRINGFRONT"]);
  QueryStatusZOrderMenu[2] = new QueryStatusItem(DECMD_SEND_BACKWARD, document.body.all["ZORDER_SENDBACKWARD"]);
  QueryStatusZOrderMenu[3] = new QueryStatusItem(DECMD_BRING_FORWARD, document.body.all["ZORDER_BRINGFORWARD"]);
  QueryStatusZOrderMenu[4] = new QueryStatusItem(DECMD_SEND_BELOW_TEXT, document.body.all["ZORDER_BELOWTEXT"]);
  QueryStatusZOrderMenu[5] = new QueryStatusItem(DECMD_BRING_ABOVE_TEXT, document.body.all["ZORDER_ABOVETEXT"]);

  // Initialize the context menu arrays.
  GeneralContextMenu[0] = new ContextMenuItem("Cut", DECMD_CUT);
  GeneralContextMenu[1] = new ContextMenuItem("Copy", DECMD_COPY);
  GeneralContextMenu[2] = new ContextMenuItem("Paste", DECMD_PASTE);
  TableContextMenu[0] = new ContextMenuItem(MENU_SEPARATOR, 0);
  TableContextMenu[1] = new ContextMenuItem("Insert Row", DECMD_INSERTROW);
  TableContextMenu[2] = new ContextMenuItem("Delete Rows", DECMD_DELETEROWS);
  TableContextMenu[3] = new ContextMenuItem(MENU_SEPARATOR, 0);
  TableContextMenu[4] = new ContextMenuItem("Insert Column", DECMD_INSERTCOL);
  TableContextMenu[5] = new ContextMenuItem("Delete Columns", DECMD_DELETECOLS);
  TableContextMenu[6] = new ContextMenuItem(MENU_SEPARATOR, 0);
  TableContextMenu[7] = new ContextMenuItem("Insert Cell", DECMD_INSERTCELL);
  TableContextMenu[8] = new ContextMenuItem("Delete Cells", DECMD_DELETECELLS);
  TableContextMenu[9] = new ContextMenuItem("Merge Cells", DECMD_MERGECELLS);
  TableContextMenu[10] = new ContextMenuItem("Split Cell", DECMD_SPLITCELL);
  AbsPosContextMenu[0] = new ContextMenuItem(MENU_SEPARATOR, 0);
  AbsPosContextMenu[1] = new ContextMenuItem("Send To Back", DECMD_SEND_TO_BACK);
  AbsPosContextMenu[2] = new ContextMenuItem("Bring To Front", DECMD_BRING_TO_FRONT);
  AbsPosContextMenu[3] = new ContextMenuItem(MENU_SEPARATOR, 0);
  AbsPosContextMenu[4] = new ContextMenuItem("Send Backward", DECMD_SEND_BACKWARD);
  AbsPosContextMenu[5] = new ContextMenuItem("Bring Forward", DECMD_BRING_FORWARD);
  AbsPosContextMenu[6] = new ContextMenuItem(MENU_SEPARATOR, 0);
  AbsPosContextMenu[7] = new ContextMenuItem("Send Below Text", DECMD_SEND_BELOW_TEXT);
  AbsPosContextMenu[8] = new ContextMenuItem("Bring Above Text", DECMD_BRING_ABOVE_TEXT);
}

function InsertTable() {
  var pVar = ObjTableInfo;
  var args = new Array();
  var arr = null;

  // Display table information dialog
  args["NumRows"] = ObjTableInfo.NumRows;
  args["NumCols"] = ObjTableInfo.NumCols;
  args["TableAttrs"] = ObjTableInfo.TableAttrs;
  args["CellAttrs"] = ObjTableInfo.CellAttrs;
  args["Caption"] = ObjTableInfo.Caption;

  arr = null;

  arr = showModalDialog( "./inc/instable.html",
                             args,
                             "font-family:Verdana; font-size:12; dialogWidth:36em; dialogHeight:35em");
  if (arr != null) {

    // Initialize table object
    for ( elem in arr ) {
      if ("NumRows" == elem && arr["NumRows"] != null) {
        ObjTableInfo.NumRows = arr["NumRows"];
      } else if ("NumCols" == elem && arr["NumCols"] != null) {
        ObjTableInfo.NumCols = arr["NumCols"];
      } else if ("TableAttrs" == elem) {
        ObjTableInfo.TableAttrs = arr["TableAttrs"];
      } else if ("CellAttrs" == elem) {
        ObjTableInfo.CellAttrs = arr["CellAttrs"];
      } else if ("Caption" == elem) {
        ObjTableInfo.Caption = arr["Caption"];
      }
    }

    tbContentElement.ExecCommand(DECMD_INSERTTABLE,OLECMDEXECOPT_DODEFAULT, pVar);
  }
}

function tbContentElement_ShowContextMenu() {

  if (!document.tbContentElement) {
    return;
  }

  var menuStrings = new Array();
  var menuStates = new Array();
  var state;
  var i
  var idx = 0;

  // Rebuild the context menu.
  ContextMenu.length = 0;

  // Always show general menu
  for (i=0; i<GeneralContextMenu.length; i++) {
    ContextMenu[idx++] = GeneralContextMenu[i];
  }

  // Is the selection inside a table? Add table menu if so
  if (tbContentElement.QueryStatus(DECMD_INSERTROW) != DECMDF_DISABLED) {
    for (i=0; i<TableContextMenu.length; i++) {
      ContextMenu[idx++] = TableContextMenu[i];
    }
  }

  // Is the selection on an absolutely positioned element? Add z-index commands if so
  if (tbContentElement.QueryStatus(DECMD_LOCK_ELEMENT) != DECMDF_DISABLED) {
    for (i=0; i<AbsPosContextMenu.length; i++) {
      ContextMenu[idx++] = AbsPosContextMenu[i];
    }
  }

  // Set up the actual arrays that get passed to SetContextMenu
  for (i=0; i<ContextMenu.length; i++) {
    menuStrings[i] = ContextMenu[i].string;
    if (menuStrings[i] != MENU_SEPARATOR) {
      state = tbContentElement.QueryStatus(ContextMenu[i].cmdId);
    } else {
      state = DECMDF_ENABLED;
    }
    if (state == DECMDF_DISABLED || state == DECMDF_NOTSUPPORTED) {
      menuStates[i] = OLE_TRISTATE_GRAY;
    } else if (state == DECMDF_ENABLED || state == DECMDF_NINCHED) {
      menuStates[i] = OLE_TRISTATE_UNCHECKED;
    } else { // DECMDF_LATCHED
      menuStates[i] = OLE_TRISTATE_CHECKED;
    }
  }

  // Set the context menu
  tbContentElement.SetContextMenu(menuStrings, menuStates);
}

function tbContentElement_ContextMenuAction(itemIndex) {

  if (!document.tbContentElement) {
    return;
  }

  if (ContextMenu[itemIndex].cmdId == DECMD_INSERTTABLE) {
    InsertTable();
  } else {
    tbContentElement.ExecCommand(ContextMenu[itemIndex].cmdId, OLECMDEXECOPT_DODEFAULT);
  }
}

// DisplayChanged handler. Very time-critical routine; this is called
// every time a character is typed. QueryStatus those toolbar buttons that need
// to be in synch with the current state of the document and update.
function tbContentElement_DisplayChanged() {
  var i, s;

  for (i=0; i<QueryStatusToolbarButtons.length; i++) {
    s = tbContentElement.QueryStatus(QueryStatusToolbarButtons[i].command);
    if (s == DECMDF_DISABLED || s == DECMDF_NOTSUPPORTED) {
      TBSetState(QueryStatusToolbarButtons[i].element, "gray");
    } else if (s == DECMDF_ENABLED  || s == DECMDF_NINCHED) {
       TBSetState(QueryStatusToolbarButtons[i].element, "unchecked");
    } else { // DECMDF_LATCHED
       TBSetState(QueryStatusToolbarButtons[i].element, "checked");
    }
  }

  /*s = tbContentElement.QueryStatus(DECMD_GETBLOCKFMT);
  if (s == DECMDF_DISABLED || s == DECMDF_NOTSUPPORTED) {
    ParagraphStyle.disabled = true;
  } else {
    ParagraphStyle.disabled = false;
    ParagraphStyle.value = tbContentElement.ExecCommand(DECMD_GETBLOCKFMT, OLECMDEXECOPT_DODEFAULT);
  }
  s = tbContentElement.QueryStatus(DECMD_GETFONTNAME);
  if (s == DECMDF_DISABLED || s == DECMDF_NOTSUPPORTED) {
    FontName.disabled = true;
  } else {
    FontName.disabled = false;
    FontName.value = tbContentElement.ExecCommand(DECMD_GETFONTNAME, OLECMDEXECOPT_DODEFAULT);
  }
  if (s == DECMDF_DISABLED || s == DECMDF_NOTSUPPORTED) {
    FontSize.disabled = true;
  } else {
    FontSize.disabled = false;
    FontSize.value = tbContentElement.ExecCommand(DECMD_GETFONTSIZE, OLECMDEXECOPT_DODEFAULT);
  }*/
}

function tbContentElement_DocumentComplete() {

    if (!document.tbContentElement) {
    return;
    }

    if (initialDocComplete) {
        if (tbContentElement.CurrentDocumentPath == "") {
        URL_VALUE.value = "http://";
    }
    else {
      URL_VALUE.value = tbContentElement.CurrentDocumentPath;
    }
  }

  initialDocComplete = true;
  docComplete = true;
}

function DECMD_VISIBLEBORDERS_onclick() {
  tbContentElement.ShowBorders = !tbContentElement.ShowBorders;
  tbContentElement.focus();
}

function DECMD_UNORDERLIST_onclick() {
  tbContentElement.ExecCommand(DECMD_UNORDERLIST,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_UNDO_onclick() {
  tbContentElement.ExecCommand(DECMD_UNDO,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_UNDERLINE_onclick() {
  tbContentElement.ExecCommand(DECMD_UNDERLINE,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_SNAPTOGRID_onclick() {
  tbContentElement.SnapToGrid = !tbContentElement.SnapToGrid;
  tbContentElement.focus();
}

function DECMD_SHOWDETAILS_onclick() {
  tbContentElement.ShowDetails = !tbContentElement.ShowDetails;
  tbContentElement.focus();
}


function DECMD_SETFORECOLOR_onclick() {
  var arr = showModalDialog( "./inc/selcolor.html",
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:30em; dialogHeight:30em" );

  if (arr != null) {
    tbContentElement.ExecCommand(DECMD_SETFORECOLOR,OLECMDEXECOPT_DODEFAULT, arr);
  }
}

function DECMD_SETBACKCOLOR_onclick() {
  var arr = showModalDialog( "./inc/selcolor.html",
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:30em; dialogHeight:30em" );

  if (arr != null) {
    tbContentElement.ExecCommand(DECMD_SETBACKCOLOR,OLECMDEXECOPT_DODEFAULT, arr);
  }
  tbContentElement.focus();
}

function DECMD_SELECTALL_onclick() {
  tbContentElement.ExecCommand(DECMD_SELECTALL,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_REDO_onclick() {
  tbContentElement.ExecCommand(DECMD_REDO,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_PASTE_onclick() {
  tbContentElement.ExecCommand(DECMD_PASTE,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_OUTDENT_onclick() {
  tbContentElement.ExecCommand(DECMD_OUTDENT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_ORDERLIST_onclick() {
  tbContentElement.ExecCommand(DECMD_ORDERLIST,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_MAKE_ABSOLUTE_onclick() {
  tbContentElement.ExecCommand(DECMD_MAKE_ABSOLUTE,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_LOCK_ELEMENT_onclick() {
  tbContentElement.ExecCommand(DECMD_LOCK_ELEMENT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_JUSTIFYRIGHT_onclick() {
  tbContentElement.ExecCommand(DECMD_JUSTIFYRIGHT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_JUSTIFYLEFT_onclick() {
  tbContentElement.ExecCommand(DECMD_JUSTIFYLEFT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_JUSTIFYCENTER_onclick() {
  tbContentElement.ExecCommand(DECMD_JUSTIFYCENTER,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_ITALIC_onclick() {
  tbContentElement.ExecCommand(DECMD_ITALIC,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_INDENT_onclick() {
  tbContentElement.ExecCommand(DECMD_INDENT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_IMAGE_onclick() {
<c:if test="${param.imageUrl == null || param.imageUrl == ''}">
  var arr = showModalDialog( "./inc/selimage.html",
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:30em; dialogHeight:20em" );
</c:if>
<c:if test="${param.imageUrl != null && param.imageUrl != ''}">
  var arr = showModalDialog( "<c:out value="${param.imageUrl}"/>",
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:60em; dialogHeight:40em" );
</c:if>
  if (arr != null) {
    tbContentElement.ExecCommand(DECMD_IMAGE, OLECMDEXECOPT_DONTPROMPTUSER, arr);
  }
}

function DECMD_HYPERLINK_onclick() {
<c:if test="${param.linkUrl == null || param.linkUrl == ''}">
  tbContentElement.ExecCommand(DECMD_HYPERLINK,OLECMDEXECOPT_PROMPTUSER);
  tbContentElement.focus();
</c:if>
<c:if test="${param.linkUrl != null && param.linkUrl != ''}">
  var arr = showModalDialog( "<c:out value="${param.linkUrl}"/>",
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:60em; dialogHeight:40em" );
  if (arr != null) {
    tbContentElement.ExecCommand(DECMD_HYPERLINK, OLECMDEXECOPT_DONTPROMPTUSER, arr);
  }
</c:if>
}

function DECMD_FINDTEXT_onclick() {
  tbContentElement.ExecCommand(DECMD_FINDTEXT,OLECMDEXECOPT_PROMPTUSER);
  tbContentElement.focus();
}

function DECMD_DELETE_onclick() {
  tbContentElement.ExecCommand(DECMD_DELETE,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_CUT_onclick() {
  tbContentElement.ExecCommand(DECMD_CUT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_COPY_onclick() {
  tbContentElement.ExecCommand(DECMD_COPY,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function DECMD_BOLD_onclick() {
  tbContentElement.ExecCommand(DECMD_BOLD,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function OnMenuShow(QueryStatusArray) {
  var i, s;

  for (i=0; i<QueryStatusArray.length; i++) {
  s = tbContentElement.QueryStatus(QueryStatusArray[i].command);
  if (s == DECMDF_DISABLED || s == DECMDF_NOTSUPPORTED) {
      TBSetState(QueryStatusArray[i].element, "gray");
    } else if (s == DECMDF_ENABLED  || s == DECMDF_NINCHED) {
       TBSetState(QueryStatusArray[i].element, "unchecked");
    } else { // DECMDF_LATCHED
       TBSetState(QueryStatusArray[i].element, "checked");
    }
  }
  tbContentElement.focus();
}

function INTRINSICS_onclick(html) {
  var selection;

  selection = tbContentElement.DOM.selection.createRange();
  selection.pasteHTML(html);
  tbContentElement.focus();
}

function TABLE_DELETECELL_onclick() {
  tbContentElement.ExecCommand(DECMD_DELETECELLS,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_DELETECOL_onclick() {
  tbContentElement.ExecCommand(DECMD_DELETECOLS,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_DELETEROW_onclick() {
  tbContentElement.ExecCommand(DECMD_DELETEROWS,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_INSERTCELL_onclick() {
  tbContentElement.ExecCommand(DECMD_INSERTCELL,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_INSERTCOL_onclick() {
  tbContentElement.ExecCommand(DECMD_INSERTCOL,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_INSERTROW_onclick() {
  tbContentElement.ExecCommand(DECMD_INSERTROW,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_INSERTTABLE_onclick() {
  InsertTable();
  tbContentElement.focus();
}

function TABLE_MERGECELL_onclick() {
  tbContentElement.ExecCommand(DECMD_MERGECELLS,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TABLE_SPLITCELL_onclick() {
  tbContentElement.ExecCommand(DECMD_SPLITCELL,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function FORMAT_FONT_onclick() {
  tbContentElement.ExecCommand(DECMD_FONT,OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function URL_VALUE_onkeypress() {
  if (event.keyCode == 13) {

  /**
    NOTE: The user is not prompted to save the the current
    document before the call to LoadURL. Therefore the
    user will lose any edits he has made to the current document
    after the call to LoadURL. The purpose of the sample is
    to provide a basic demonstration of how to use the DHTMLEdit
    control. A complete implementation would check if there were
    unsaved edits to the current document by testing the IsDirty
    property on the control. If the IsDirty property is true, the
    user should be given an opporunity to save his edits first.

    See the implementation of MENU_FILE_NEW_onclick() in this sample
    for a demonstration how to do this.
  **/

    docComplete = false;
    tbContentElement.LoadURL(URL_VALUE.value);
    tbContentElement.focus();
  }
}

function ZORDER_ABOVETEXT_onclick() {
  tbContentElement.ExecCommand(DECMD_BRING_ABOVE_TEXT, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function ZORDER_BELOWTEXT_onclick() {
  tbContentElement.ExecCommand(DECMD_SEND_BELOW_TEXT, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function ZORDER_BRINGFORWARD_onclick() {
  tbContentElement.ExecCommand(DECMD_BRING_FORWARD, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function ZORDER_BRINGFRONT_onclick() {
  tbContentElement.ExecCommand(DECMD_BRING_TO_FRONT, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function ZORDER_SENDBACK_onclick() {
  tbContentElement.ExecCommand(DECMD_SEND_TO_BACK, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function ZORDER_SENDBACKWARD_onclick() {
  tbContentElement.ExecCommand(DECMD_SEND_BACKWARD, OLECMDEXECOPT_DODEFAULT);
  tbContentElement.focus();
}

function TOOLBARS_onclick(toolbar) {
  if (toolbar.TBSTATE == "hidden") {
    TBSetState(toolbar, "dockedTop");
  } else {
    TBSetState(toolbar, "hidden");
  }
  tbContentElement.focus();
}

function ParagraphStyle_onchange() {
  tbContentElement.ExecCommand(DECMD_SETBLOCKFMT, OLECMDEXECOPT_DODEFAULT, ParagraphStyle.value);
  tbContentElement.focus();
}

function FontName_onchange() {
  tbContentElement.ExecCommand(DECMD_SETFONTNAME, OLECMDEXECOPT_DODEFAULT, FontName.value);
  tbContentElement.focus();
}

function FontSize_onchange() {
  tbContentElement.ExecCommand(DECMD_SETFONTSIZE, OLECMDEXECOPT_DODEFAULT, parseInt(FontSize.value));
  tbContentElement.focus();
}


function DECMD_SHOWCODE_onclick() {
    //popup HTML source window
    htmlwin = window.open("inc/edithtml.html","htmlwin","width=620,height=480");
}

function Editor_onkeypress() {
/*    var cur;
    //onkeypress event listener
    if(tbContentElement.DOM.parentWindow.event.keyCode == 13 && (tbContentElement.QueryStatus(DECMD_UNORDERLIST)!=DECMDF_LATCHED || tbContentElement.QueryStatus(DECMD_ORDERLIST)!=DECMDF_LATCHED)) {
        //insert <br> instead of <p> and ctrl-Enter will insert <p>
        tbContentElement.DOM.parentWindow.event.returnValue = false;
        cur = tbContentElement.DOM.selection.createRange();
        cur.pasteHTML("<BR>");
        cur.collapse(false);
        cur.select();
        }
    else if(tbContentElement.DOM.parentWindow.event.keyCode == 10) {
        tbContentElement.DOM.parentWindow.event.keyCode = 13;
        }*/
}
//-->
</script>

<script LANGUAGE="javascript" FOR="tbContentElement" EVENT="DisplayChanged">
<!--
return tbContentElement_DisplayChanged()
//-->
</script>

<script LANGUAGE="javascript" FOR="tbContentElement" EVENT="DocumentComplete">
<!--
return tbContentElement_DocumentComplete()
//-->
</script>

<script LANGUAGE="javascript" FOR="tbContentElement" EVENT="ShowContextMenu">
<!--
return tbContentElement_ShowContextMenu()
//-->
</script>

<script LANGUAGE="javascript" FOR="tbContentElement" EVENT="ContextMenuAction(itemIndex)">
<!--
  if (document.tbContentElement) {
    return tbContentElement_ContextMenuAction(itemIndex)
  }
//-->
</script>

<script LANGUAGE="javascript" FOR="tbContentElement" EVENT="onkeypress">
<!--
return Editor_onkeypress()
//-->
</script>

</head>
<body LANGUAGE="javascript" onload="return window_onload()">


<!-- Toolbars -->

<div class="tbToolbar" ID="StandardToolbar1">

  <div class="tbButton" ID="DECMD_CUT" TITLE="Cut" LANGUAGE="javascript" onclick="return DECMD_CUT_onclick()">
    <img class="tbIcon" src="./images/cut.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_COPY" TITLE="Copy" LANGUAGE="javascript" onclick="return DECMD_COPY_onclick()">
    <img class="tbIcon" src="./images/copy.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_PASTE" TITLE="Paste" LANGUAGE="javascript" onclick="return DECMD_PASTE_onclick()">
    <img class="tbIcon" src="./images/paste.gif" WIDTH="23" HEIGHT="22">
  </div>

  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_UNDO" TITLE="Undo" LANGUAGE="javascript" onclick="return DECMD_UNDO_onclick()">
    <img class="tbIcon" src="./images/undo.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_REDO" TITLE="Redo" LANGUAGE="javascript" onclick="return DECMD_REDO_onclick()">
    <img class="tbIcon" src="./images/redo.gif" WIDTH="23" HEIGHT="22">
  </div>

<%--
  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_FINDTEXT" TITLE="Find" LANGUAGE="javascript" onclick="return DECMD_FINDTEXT_onclick()">
    <img class="tbIcon" src="./images/find.gif" WIDTH="23" HEIGHT="22">
  </div>

--%>
  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_BOLD" TITLE="Bold" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_BOLD_onclick()">
    <img class="tbIcon" src="./images/bold.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_ITALIC" TITLE="Italic" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_ITALIC_onclick()">
    <img class="tbIcon" src="./images/italic.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_UNDERLINE" TITLE="Underline" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_UNDERLINE_onclick()">
    <img class="tbIcon" src="./images/under.gif" WIDTH="23" HEIGHT="22">
  </div>

<%--  <div class="tbSeparator"></div>--%>

  <div class="tbButton" ID="DECMD_FONT" TITLE="Font Format" LANGUAGE="javascript" onclick="return FORMAT_FONT_onclick()">
    <img class="tbIcon" src="./images/fgcolor.gif" WIDTH="23" HEIGHT="22">
  </div>
<%--
  <div class="tbButton" ID="DECMD_SETBACKCOLOR" TITLE="Background Color" LANGUAGE="javascript" onclick="return DECMD_SETBACKCOLOR_onclick()">
    <img class="tbIcon" src="./images/bgcolor.gif" WIDTH="23" HEIGHT="22">
  </div>
--%>

<%--
</div>

<div class="tbToolbar" ID="StandardToolbar2">
--%>

  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_HYPERLINK" TITLE="Link" LANGUAGE="javascript" onclick="return DECMD_HYPERLINK_onclick()">
    <img class="tbIcon" src="./images/link.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_IMAGE" TITLE="Insert Image" LANGUAGE="javascript" onclick="return DECMD_IMAGE_onclick()">
    <img class="tbIcon" src="./images/image.gif" WIDTH="23" HEIGHT="22">
  </div>

  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_VISIBLEBORDERS" TITLE="Visible Borders" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_VISIBLEBORDERS_onclick()">
    <img class="tbIcon" src="./images/borders.gif" WIDTH="23" HEIGHT="22">
  </div>

  <div class="tbButton" ID="DECMD_INSERTTABLE" TITLE="Insert Table" LANGUAGE="javascript" onclick="return TABLE_INSERTTABLE_onclick()">
    <img class="tbIcon" src="./images/instable.gif" WIDTH="23" HEIGHT="22">
  </div>

  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_JUSTIFYLEFT" TITLE="Align Left" TBTYPE="toggle" NAME="Justify" LANGUAGE="javascript" onclick="return DECMD_JUSTIFYLEFT_onclick()">
    <img class="tbIcon" src="./images/left.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_JUSTIFYCENTER" TITLE="Center" TBTYPE="toggle" NAME="Justify" LANGUAGE="javascript" onclick="return DECMD_JUSTIFYCENTER_onclick()">
    <img class="tbIcon" src="./images/center.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_JUSTIFYRIGHT" TITLE="Align Right" TBTYPE="toggle" NAME="Justify" LANGUAGE="javascript" onclick="return DECMD_JUSTIFYRIGHT_onclick()">
    <img class="tbIcon" src="./images/right.gif" WIDTH="23" HEIGHT="22">
  </div>

  <div class="tbSeparator"></div>

  <div class="tbButton" ID="DECMD_ORDERLIST" TITLE="Numbered List" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_ORDERLIST_onclick()">
    <img class="tbIcon" src="./images/numlist.gif" WIDTH="23" HEIGHT="22">
  </div>
  <div class="tbButton" ID="DECMD_UNORDERLIST" TITLE="Bulletted List" TBTYPE="toggle" LANGUAGE="javascript" onclick="return DECMD_UNORDERLIST_onclick()">
    <img class="tbIcon" src="./images/bullist.gif" WIDTH="23" HEIGHT="22">
  </div>

</div>

<!-- DHTML Editing control Object. This will be the body object for the toolbars. -->
<object ID="tbContentElement" CLASS="tbContentElement" CLASSID="clsid:2D360201-FFF5-11d1-8D03-00A0C959BC0A" VIEWASTEXT>
    <param name=Scrollbars value=true>
</object>

<!-- DEInsertTableParam Object -->
<object ID="ObjTableInfo" CLASSID="clsid:47B0DFC7-B7A3-11D1-ADC5-006008A5848C" VIEWASTEXT>
</object>

<!-- DEGetBlockFmtNamesParam Object -->
<object ID="ObjBlockFormatInfo" CLASSID="clsid:8D91090E-B955-11D1-ADC5-006008A5848C" VIEWASTEXT>
</object>


<!-- Toolbar Code File. Note: This must always be the last thing on the page -->

<%--
<script LANGUAGE="Javascript" SRC="./toolbars/toolbars.js">
</script>
--%>

<script LANGUAGE="Javascript">
  try {
    document.write('<SCRIPT LANGUAGE="JavaScript" SRC="./toolbars/toolbars.js"></scrip' +'t>');
  }
  catch(e) {
  }
</script>



</body>
</html>
