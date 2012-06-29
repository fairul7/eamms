<%@ include file="/common/header.jsp" %>
// I18N constants

// LANG: "en", ENCODING: UTF-8 | ISO-8859-1
// Author: Mihai Bazon, http://dynarch.com/mishoo

// FOR TRANSLATORS:
//
//   1. PLEASE PUT YOUR CONTACT INFO IN THE ABOVE LINE
//      (at least a valid email address)
//
//   2. PLEASE TRY TO USE UTF-8 FOR ENCODING;
//      (if this is not possible, please include a comment
//       that states what encoding is necessary.)

HTMLArea.I18N = {

	// the following should be the filename without .js extension
	// it will be used for automatically load plugin language.
	lang: "en",

	tooltips: {
		bold:           "<fmt:message key="richTextBox.bold"/>",
		italic:         "<fmt:message key="richTextBox.italic"/>",
		underline:      "<fmt:message key="richTextBox.underline"/>",
		strikethrough:  "<fmt:message key="richTextBox.strikethrough"/>",
		subscript:      "<fmt:message key="richTextBox.subscript"/>",
		superscript:    "<fmt:message key="richTextBox.superscript"/>",
		justifyleft:    "<fmt:message key="richTextBox.justifyLeft"/>",
		justifycenter:  "<fmt:message key="richTextBox.justifyCenter"/>",
		justifyright:   "<fmt:message key="richTextBox.justifyRight"/>",
		justifyfull:    "<fmt:message key="richTextBox.justifyFull"/>",
		orderedlist:    "<fmt:message key="richTextBox.orderedList"/>",
		unorderedlist:  "<fmt:message key="richTextBox.bulletedList"/>",
		outdent:        "<fmt:message key="richTextBox.decreaseIndent"/>",
		indent:         "<fmt:message key="richTextBox.increaseIndent"/>",
		forecolor:      "<fmt:message key="richTextBox.fontColor"/>",
		hilitecolor:    "<fmt:message key="richTextBox.backgroundColor"/>",
		horizontalrule: "<fmt:message key="richTextBox.horizontalRule"/>",
		createlink:     "<fmt:message key="richTextBox.hyperlink"/>",
		insertimage:    "<fmt:message key="richTextBox.imageManager"/>",
		inserttable:    "<fmt:message key="richTextBox.insertTable"/>",
		htmlmode:       "<fmt:message key="richTextBox.htmlSource"/>",
		popupeditor:    "<fmt:message key="richTextBox.popupEditor"/>",
		about:          "About this editor",
		showhelp:       "Help using editor",
		textindicator:  "Current style",
		undo:           "<fmt:message key="richTextBox.undo"/>",
		redo:           "<fmt:message key="richTextBox.redo"/>",
		cut:            "<fmt:message key="richTextBox.cut"/>",
		copy:           "<fmt:message key="richTextBox.copy"/>",
		paste:          "<fmt:message key="richTextBox.paste"/>",
		lefttoright:    "<fmt:message key="richTextBox.leftToRight"/>",
		righttoleft:    "<fmt:message key="richTextBox.rightToLeft"/>"
	},

	buttons: {
		"ok":           "<fmt:message key="richTextBox.ok"/>",
		"cancel":       "<fmt:message key="richTextBox.cancel"/>"
	},

	msg: {
		"Path":         "Path",
		"TEXT_MODE":    "<fmt:message key="richTextBox.textMode"/>",

		"IE-sucks-full-screen" :
		// translate here
		"The full screen mode is known to cause problems with Internet Explorer, " +
		"due to browser bugs that we weren't able to workaround.  You might experience garbage " +
		"display, lack of editor functions and/or random browser crashes.  If your system is Windows 9x " +
		"it's very likely that you'll get a 'General Protection Fault' and need to reboot.\n\n" +
		"You have been warned.  Please press OK if you still want to try the full screen editor."
	},

	dialogs: {
		"Cancel"                                            : "<fmt:message key="richTextBox.cancel"/>",
		"Insert/Modify Link"                                : "<fmt:message key="richTextBox.hyperlink"/>",
		"New window (_blank)"                               : "<fmt:message key="richTextBox.newWindow"/>",
		"None (use implicit)"                               : "<fmt:message key="richTextBox.none"/>",
		"OK"                                                : "<fmt:message key="richTextBox.ok"/>",
		"Other"                                             : "<fmt:message key="richTextBox.other"/>",
		"Same frame (_self)"                                : "<fmt:message key="richTextBox.sameFrame"/>",
		"Target:"                                           : "<fmt:message key="richTextBox.target"/>:",
		"Title (tooltip):"                                  : "<fmt:message key="richTextBox.title"/>:",
		"Top frame (_top)"                                  : "<fmt:message key="richTextBox.topFrame"/>",
		"URL:"                                              : "<fmt:message key="richTextBox.url"/>:",
		"You must enter the URL where this link points to"  : "<fmt:message key="richTextBox.linkUrlRequired"/>"
	}
};

ContextMenu.I18N = {
	// Items that appear in menu.  Please note that an underscore (_)
	// character in the translation (right column) will cause the following
	// letter to become underlined and be shortcut for that menu option.

	"Cut"                                                   : "<fmt:message key="richTextBox.cut"/>",
	"Copy"                                                  : "<fmt:message key="richTextBox.copy"/>",
	"Paste"                                                 : "<fmt:message key="richTextBox.paste"/>",
	"Image Properties"                                      : "<fmt:message key="richTextBox.modifyImage"/>",
	"Modify Link"                                           : "<fmt:message key="richTextBox.modifyLink"/>",
	"Check Link"                                            : "<fmt:message key="richTextBox.checkLink"/>",
	"Remove Link"                                           : "<fmt:message key="richTextBox.removeLink"/>",
	"Cell Properties"                                       : "<fmt:message key="richTextBox.cellProperties"/>",
	"Row Properties"                                        : "<fmt:message key="richTextBox.rowProperties"/>",
	"Insert Row Before"                                     : "<fmt:message key="richTextBox.insertRowBefore"/>",
	"Insert Row After"                                      : "<fmt:message key="richTextBox.insertRowAfter"/>",
	"Delete Row"                                            : "<fmt:message key="richTextBox.deleteRow"/>",
	"Table Properties"                                      : "<fmt:message key="richTextBox.tableProperties"/>",
	"Insert Column Before"                                  : "<fmt:message key="richTextBox.insertColumnBefore"/>",
	"Insert Column After"                                   : "<fmt:message key="richTextBox.insertColumnAfter"/>",
	"Delete Column"                                         : "<fmt:message key="richTextBox.deleteColumn"/>",
	"Justify Left"                                          : "<fmt:message key="richTextBox.justifyLeft"/>",
	"Justify Center"                                        : "<fmt:message key="richTextBox.justifyCenter"/>",
	"Justify Right"                                         : "<fmt:message key="richTextBox.justifyRight"/>",
	"Justify Full"                                          : "<fmt:message key="richTextBox.justifyFull"/>",
	"Make link"                                             : "<fmt:message key="richTextBox.hyperlink"/>",
	"Remove the"                                            : "<fmt:message key="richTextBox.removeThe"/>",
	"Element"                                               : "<fmt:message key="richTextBox.element"/>...",

	// Other labels (tooltips and alert/confirm box messages)

	"Please confirm that you want to remove this element:"  : "<fmt:message key="richTextBox.confirmRemoveElement"/>:",
	"Remove this node from the document"                    : "<fmt:message key="richTextBox.removeNode"/>",
	"How did you get here? (Please report!)"                : "How did you get here? (Please report!)",
	"Show the image properties dialog"                      : "<fmt:message key="richTextBox.showImageProperties"/>",
	"Modify URL"                                            : "<fmt:message key="richTextBox.modifyUrl"/>",
	"Current URL is"                                        : "<fmt:message key="richTextBox.currentUrl"/>",
	"Opens this link in a new window"                       : "<fmt:message key="richTextBox.openLinkInNewWindow"/>",
	"Please confirm that you want to unlink this element."  : "<fmt:message key="richTextBox.confirmUnlink"/>",
	"Link points to:"                                       : "<fmt:message key="richTextBox.linkPointsTo"/>:",
	"Unlink the current element"                            : "<fmt:message key="richTextBox.unlink"/>",
	"Show the Table Cell Properties dialog"                 : "<fmt:message key="richTextBox.cellProperties"/>",
	"Show the Table Row Properties dialog"                  : "<fmt:message key="richTextBox.rowProperties"/>",
	"Insert a new row before the current one"               : "<fmt:message key="richTextBox.insertRowBefore"/>",
	"Insert a new row after the current one"                : "<fmt:message key="richTextBox.insertRowAfter"/>",
	"Delete the current row"                                : "<fmt:message key="richTextBox.deleteRow"/>",
	"Show the Table Properties dialog"                      : "<fmt:message key="richTextBox.tableProperties"/>",
	"Insert a new column before the current one"            : "<fmt:message key="richTextBox.insertColumnBefore"/>",
	"Insert a new column after the current one"             : "<fmt:message key="richTextBox.insertColumnAfter"/>",
	"Delete the current column"                             : "<fmt:message key="richTextBox.deleteColumn"/>",
	"Create a link"                                         : "<fmt:message key="richTextBox.hyperlink"/>"
};

TableOperations.I18N = {
	"Align"                                                 : "<fmt:message key="richTextBox.alignment"/>",
	"All four sides"                                        : "<fmt:message key="richTextBox.borderAllSides"/>",
	"Background"                                            : "<fmt:message key="richTextBox.backgroundColor"/>",
	"Baseline"                                              : "<fmt:message key="richTextBox.baseline"/>",
	"Border"                                                : "<fmt:message key="richTextBox.border"/>",
	"Borders"                                               : "<fmt:message key="richTextBox.borders"/>",
	"Bottom"                                                : "<fmt:message key="richTextBox.bottom"/>",
	"CSS Style"                                             : "<fmt:message key="richTextBox.cssStyle"/>",
	"Caption"                                               : "<fmt:message key="richTextBox.caption"/>",
	"Cell Properties"                                       : "<fmt:message key="richTextBox.cellProperties"/>",
	"Center"                                                : "<fmt:message key="richTextBox.center"/>",
	"Char"                                                  : "Char",
	"Collapsed borders"                                     : "Collapsed borders",
	"Color"                                                 : "<fmt:message key="richTextBox.color"/>",
    "Description"                                           : "<fmt:message key="richTextBox.description"/>",
	"FG Color"                                              : "<fmt:message key="richTextBox.color"/>",
	"Float"                                                 : "<fmt:message key="richTextBox.float"/>",
	"Frames"                                                : "<fmt:message key="richTextBox.frames"/>",
	"Height"                                                : "<fmt:message key="richTextBox.height"/>",
	"How many columns would you like to merge?"             : "How many columns would you like to merge?",
	"How many rows would you like to merge?"                : "How many rows would you like to merge?",
	"Image URL"                                             : "<fmt:message key="richTextBox.url"/>",
	"Justify"                                               : "<fmt:message key="richTextBox.justify"/>",
	"Layout"                                                : "<fmt:message key="richTextBox.layout"/>",
	"Left"                                                  : "<fmt:message key="richTextBox.left"/>",
	"Margin"                                                : "<fmt:message key="richTextBox.margin"/>",
	"Middle"                                                : "<fmt:message key="richTextBox.middle"/>",
	"No rules"                                              : "<fmt:message key="richTextBox.noRules"/>",
	"No sides"                                              : "<fmt:message key="richTextBox.noSides"/>",
    "None"                                                  : "<fmt:message key="richTextBox.none"/>",
	"Padding"                                               : "<fmt:message key="richTextBox.padding"/>",
	"Please click into some cell"                           : "Please click into some cell",
	"Right"                                                 : "<fmt:message key="richTextBox.right"/>",
	"Row Properties"                                        : "<fmt:message key="richTextBox.rowProperties"/>",
	"Rules will appear between all rows and columns"        : "Rules will appear between all rows and columns",
	"Rules will appear between columns only"                : "Rules will appear between columns only",
	"Rules will appear between rows only"                   : "Rules will appear between rows only",
	"Rules"                                                 : "<fmt:message key="richTextBox.rules"/>",
	"Spacing and padding"                                   : "<fmt:message key="richTextBox.spacingAndPadding"/>",
	"Spacing"                                               : "<fmt:message key="richTextBox.spacing"/>",
	"Summary"                                               : "Summary",
	"TO-cell-delete"                                        : "<fmt:message key="richTextBox.deleteCell"/>",
	"TO-cell-insert-after"                                  : "<fmt:message key="richTextBox.insertCellAfter"/>",
	"TO-cell-insert-before"                                 : "<fmt:message key="richTextBox.insertCellBefore"/>",
	"TO-cell-merge"                                         : "<fmt:message key="richTextBox.mergeCells"/>",
	"TO-cell-prop"                                          : "<fmt:message key="richTextBox.cellProperties"/>",
	"TO-cell-split"                                         : "<fmt:message key="richTextBox.splitCell"/>",
	"TO-col-delete"                                         : "<fmt:message key="richTextBox.deleteColumn"/>",
    "TO-col-insert-after"                                   : "<fmt:message key="richTextBox.insertColumnAfter"/>",
	"TO-col-insert-before"                                  : "<fmt:message key="richTextBox.insertColumnAfter"/>",
	"TO-col-split"                                          : "<fmt:message key="richTextBox.splitColumn"/>",
	"TO-row-delete"                                         : "<fmt:message key="richTextBox.deleteRow"/>",
	"TO-row-insert-above"                                   : "<fmt:message key="richTextBox.insertRowBefore"/>",
	"TO-row-insert-under"                                   : "<fmt:message key="richTextBox.insertRowAfter"/>",
	"TO-row-prop"                                           : "<fmt:message key="richTextBox.rowProperties"/>",
	"TO-row-split"                                          : "<fmt:message key="richTextBox.splitRow"/>",
	"TO-table-prop"                                         : "<fmt:message key="richTextBox.tableProperties"/>",
	"Table Properties"                                      : "<fmt:message key="richTextBox.tableProperties"/>",
	"Text align"                                            : "<fmt:message key="richTextBox.textAlign"/>",
	"The bottom side only"                                  : "<fmt:message key="richTextBox.bottomSide"/>",
	"The left-hand side only"                               : "<fmt:message key="richTextBox.leftSide"/>",
	"The right and left sides only"                         : "<fmt:message key="richTextBox.rightLeftSides"/>",
	"The right-hand side only"                              : "<fmt:message key="richTextBox.rightSide"/>",
	"The top and bottom sides only"                         : "<fmt:message key="richTextBox.topBottomSides"/>",
	"The top side only"                                     : "<fmt:message key="richTextBox.topSide"/>",
	"Top"                                                   : "<fmt:message key="richTextBox.top"/>",
	"Unset color"                                           : "<fmt:message key="richTextBox.unsetColor"/>",
	"Vertical align"                                        : "<fmt:message key="richTextBox.verticalAlign"/>",
	"Width"                                                 : "<fmt:message key="richTextBox.width"/>",
	"not-del-last-cell"                                     : "HTMLArea refuses to delete the last cell in row.",
	"not-del-last-col"                                      : "HTMLArea refuses to delete the last column in table.",
	"not-del-last-row"                                      : "HTMLArea refuses to delete the last row in table.",
	"percent"                                               : "<fmt:message key="richTextBox.percent"/>",
	"pixels"                                                : "<fmt:message key="richTextBox.pixels"/>"
};
