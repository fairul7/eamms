//
// htmlArea v2.02 - Copyright (c) 2002 interactivetools.com, inc.
// This copyright notice MUST stay intact for use (see license.txt).
//
// A free WYSIWYG editor replacement for <textarea> fields.
// For full source code and docs, visit http://www.interactivetools.com/

//  *-------------------------------------------------------------------------
//  | This version of htmlArea was put together by Pierre Renaud
//  | ezHTMLarea - http://fslactivities.sd61.bc.ca/ezHTMLarea/
//  *-------------------------------------------------------------------------
// write out styles for UI buttons
document.write('<style type="text/css">\n');
document.write('.btn     { width: 22px; height: 22px; border: 1px solid buttonface; margin: 0; padding: 0; }\n');
document.write('.btnOver { width: 22px; height: 22px; border: 1px outset; }\n');
document.write('.btnDown { width: 22px; height: 22px; border: 1px inset; background-color: buttonhighlight; }\n');
document.write('.btnNA   { width: 22px; height: 22px; border: 1px solid buttonface; filter: alpha(opacity=25); }\n');
document.write('.cMenu     { background-color: threedface; color: menutext; cursor: Default; font-family: MS Sans Serif; font-size: 8pt; padding: 2 12 2 16; }');
document.write('.cMenuOver { background-color: highlight; color: highlighttext; cursor: Default; font-family: MS Sans Serif; font-size: 8pt; padding: 2 12 2 16; }');
document.write('.cMenuDivOuter { background-color: threedface; height: 9 }');
document.write('.cMenuDivInner { margin: 0 4 0 4; border-width: 1; border-style: solid; border-color: threedshadow threedhighlight threedhighlight threedshadow; }');
document.write('</style>\n');
/*document.write('<SCR' + 'IPT LANGUAGE=VBScript\> \n');
document.write('on error resume next \n');
document.write('spellEnabled = (IsObject(CreateObject("Word.Application"))) \n');
document.write('</SCR' + 'IPT\> \n');*/

/* ---------------------------------------------------------------------- *\
  Function    : editor_defaultConfig
  Description : default configuration settings for wysiwyg editor
\* ---------------------------------------------------------------------- */

function editor_defaultConfig(objname) {

this.version = "2.03.1"

this.width =  "auto";
this.height = "auto";
this.bodyStyle = 'background-color: white; font-size: x-small; border-color: #D8D8D8';
this.bordersStyle = 'border:1px dotted #ff0000;height:22px';
this.imgURL = _editor_url + 'images/';
this.debug  = 0;

this.replaceNextlines = 0; // replace nextlines from spaces (on output)
this.plaintextInput = 0;   // replace nextlines with breaks (on input)

this.toolbar = [
//Row 1: Font Related Tools
    ['fontname'],
	['separator'],
	['fontsize'],
	['separator'],
	['fontstyle'],
	['separator'],
	['formatblock'],
	['rtl','ltr','template','liveresize','spell','colorpicker','plaintext'],
	['linebreak'],
//File
	['refresh','openfile','save','ftp','openword','preview','print'],

	//Persistent Buttons
		//Tables
	['separator'],
	['inserttable','showborder','TableProperties',
	//Table Rows
	'RowProperties','InsertRowBefore','InsertRowAfter','DeleteRow','SplitRow',
	//Table Columns
	'InsertColumnBefore','InsertColumnAfter','DeleteColumn',
	//Table Cells
	'CellProperties','InsertCellBefore','InsertCellAfter','DeleteCell','SplitCell','MergeCells','separator',
	'line','InputForm','specchar','today','marquee'],
	['separator'],
	['help','htmlmode','popupeditor'],
	['linebreak'],
	//Edit
    ['separator'],
	['selectall','cut','copy','paste','delete','remove','undo','redo','find','changecase','separator',
	//Format Text Style
	'bold','italic','underline','strikethrough','subscript','superscript',
	// Format Text Alignment
	'justifyleft','justifycenter','justifyright','justifyfull','justifynone', 'separator',
	'forecolor','backcolor',
	//Format Text Block
	'OrderedList','UnOrderedList','Outdent','Indent','separator',

	//Row 3: Insert/Modify Tools
	//Hyperlinks and Images
	'insertlink','unlink','anchor','InsertImage','multipleselect'],
    ];

this.fontnames = {
    "Arial":           "arial, helvetica, sans-serif",
	//"Aharoni":          "Aharoni",
    "Courier New":     "courier new, courier, mono",
    "Georgia":         "Georgia, Times New Roman, Times, Serif",
    "Tahoma":          "Tahoma, Arial, Helvetica, sans-serif",
    "Times New Roman": "times new roman, times, serif",
    "Verdana":         "Verdana, Arial, Helvetica, sans-serif",
	//"David":           "David",
    "impact":          "impact",
	"WingDings":       "WingDings"};

this.fontsizes = {
    "1 (8 pt)":  "1",
    "2 (10 pt)": "2",
    "3 (12 pt)": "3",
    "4 (14 pt)": "4",
    "5 (18 pt)": "5",
    "6 (24 pt)": "6",
    "7 (36 pt)": "7"
  };
// inserted by lvn
this.formatblocks = [
  {tag: "",   		 formatblocklangs: [{lang: "en", name: "Normal"},
                                        {lang: "nl", name: "Normaal"}]},
  {tag: "<address>", formatblocklangs: [{lang: "en", name: "Address"},
                                        {lang: "nl", name: "Adres"}]},
  {tag: "<dd>",      formatblocklangs: [{lang: "en", name: "Definition"},
                                        {lang: "nl", name: "Definitie"}]},
  {tag: "<dt>",      formatblocklangs: [{lang: "en", name: "Definition Term"},
                                        {lang: "nl", name: "Definitieterm"}]},
  {tag: "<ol>",      formatblocklangs: [{lang: "en", name: "Numbered List"},
                                        {lang: "nl", name: "Genummerde lijst"}]},
  {tag: "<dir>",     formatblocklangs: [{lang: "en", name: "Directory List"},
                                        {lang: "nl", name: "Inhoud"}]},
  {tag: "<h1>",      formatblocklangs: [{lang: "en", name: "Heading 1"},
                                        {lang: "nl", name: "Kop 1"}]},
  {tag: "<h2>",      formatblocklangs: [{lang: "en", name: "Heading 2"},
                                        {lang: "nl", name: "Kop 3"}]},
  {tag: "<h3>",      formatblocklangs: [{lang: "en", name: "Heading 3"},
                                        {lang: "nl", name: "Kop 3"}]},
  {tag: "<h4>",      formatblocklangs: [{lang: "en", name: "Heading 4"},
                                        {lang: "nl", name: "Kop 4"}]},
  {tag: "<h5>",      formatblocklangs: [{lang: "en", name: "Heading 5"},
                                        {lang: "nl", name: "Kop 5"}]},
  {tag: "<ul>",      formatblocklangs: [{lang: "en", name: "Bulleted List"},
                                        {lang: "nl", name: "Lijst met opsommingstekens"}]},
  {tag: "<menu>",    formatblocklangs: [{lang: "en", name: "Menu List"},
                                        {lang: "nl", name: "Menulijst"}]},
  {tag: "<pre>",     formatblocklangs: [{lang: "en", name: "Formatted"},
                                        {lang: "nl", name: "Met opmaak"}]}
];
this.systemLang= navigator.systemLanguage.split("-");
this.userLang = navigator.userLanguage.split("-");
this.browserLang = navigator.userLanguage.split("-");
this.showborders = true;
// end insert by lvn
//this.stylesheet = "http://fslactivities.sd61.bc.ca/ezHTMLarea/wsone.css"; // full URL to stylesheet

this.fontstyles = [     // make sure these exist in the header of page the content is being display as well in or they won't work!
   { name: "headline",     className: "headline",  classStyle: "font-family: arial black, arial; font-size: 28px; letter-spacing: -2px;" },
   { name: "arial red",    className: "headline2", classStyle: "font-family: arial; font-size: 12pt; color:red; font-weight:bold;" },
   { name: "verdana blue", className: "headline4", classStyle: "font-family: verdana; font-size: 18px; letter-spacing: -2px; color:blue" },
   { name: "Remove Style", className: "remove", classStyle: "" },
];

this.btnList = {
//	 buttonName:    		commandID,               title,               												 onclick,                  image,
//Row 1: Font Related Tools
//Persistent Buttons
	"openfile":  		  ['OpenFile',           'Open a Webfile From Your Local Disk',      'editor_action(this.id)',   'openfold.gif'],
    "openword":  		  ['OpenWord',           'Open a Word Document',             	'editor_action(this.id)',   'ed_word.gif'],
    "removefont":  		  ['RemoveFont',           'Remove all Fonts',             		'editor_action(this.id)',   'ed_removefont.gif'],
    "template":    	   	  ['Template',             'Select a Template',             	'editor_action(this.id)',   'ed_template.gif'],
    "save":				  ['save', 	 			   'Save This page on Your Computer',   'editor_action(this.id)',   'ed_save.gif'],
	"htmlmode":    		  ['HtmlMode',             'View HTML Source',   				'editor_setmode(\''+objname+'\')', 'ed_html.gif'],
    "popupeditor": 	      ['popupeditor',          'Enlarge Editor',     				'editor_action(this.id)',  'fullscreen_maximize.gif'],
    "about":       		  ['about',                'About this editor',  				'editor_about(\''+objname+'\')',  'ed_about.gif'],
    "colorpicker":        		  ['colorpicker',         	   'Web-Safe Colour Picker',  			 				'editor_action(this.id)',  'color_picker.gif'],
    "ftp":        		  ['ftp',         	   'FTP Applet',  			 				'editor_action(this.id)',  'ftp.gif'],
    "help":        		  ['showhelp',         	   'Help',  			 				'editor_action(this.id)',  'ed_help.gif'],
"plaintext": ['plaintext', 'Plain Text', 'editor_action(this.id)', 'plaintext.gif'],
//Row 2: File, Edit, Format Tools
//File
	"refresh":         	  ['Refresh',              'Clear Contents',                				'editor_action(this.id)',   'ed_refresh.gif'],
    "preview":   	   	  ['Preview',              'Preview',            				'editor_action(this.id)',   'ed_preview.gif'],
    "print":    	   	  ['Print',                'Print - CTRL+P',              				'editor_action(this.id)',   'ed_print.gif'],
//Edit
	"selectall": 	   	  ['selectall',            'Select All - CTRL+A', 	     				'editor_action(this.id)',   'ed_selectall.gif'],
    "cut":       	   	  ['Cut',                  'Cut - CTRL+X',            					'editor_action(this.id)',   'ed_cut.gif'],
    "copy":     	   	  ['Copy',                 'Copy - CTRL+C',               				'editor_action(this.id)',   'ed_copy.gif'],
    "paste":     	   	  ['Paste',                'Paste - CTRL+V',              				'editor_action(this.id)',   'ed_paste.gif'],
    "delete":    	   	  ['Delete',               'Delete Selection',   				'editor_action(this.id)',   'ed_delete.gif'],
    "remove":          	  ['RemoveFormat',         'Remove Format in selected text', 	'editor_action(this.id)',   'ed_remove.gif'],
    "undo":            	  ['Undo',                 'Undo - CTRL+Z',  						'editor_action(this.id)',   'ed_undo.gif'],
    "redo":            	  ['Redo',                 'Redo - CTRL+Y',               		'editor_action(this.id)',   'ed_redo.gif'],
    "find":      	   	  ['Find',                 'Search and Replace - CTRL+S',               				'editor_action(this.id)',  'ed_find.gif'],
	"changecase":    	  ['ChangeCase',           'Change Case',             			'editor_action(this.id)',   'ed_changecase.gif'],
    //Format Text Style
	"bold":            	  ['Bold',                 'Bold - CTRL+B',               				'editor_action(this.id)',  'ed_format_bold.gif'],
    "italic":          	  ['Italic',               'Italic - CTRL+I',             				'editor_action(this.id)',  'ed_format_italic.gif'],
    "underline":       	  ['Underline',            'Underline - CTRL+U',          				'editor_action(this.id)',  'ed_format_underline.gif'],
    "strikethrough":   	  ['StrikeThrough',        'Strikethrough',     				'editor_action(this.id)',  'ed_format_strike.gif'],
    "subscript":       	  ['SubScript',            'Subscript',          				'editor_action(this.id)',  'ed_format_sub.gif'],
    "superscript":     	  ['SuperScript',          'Superscript',        				'editor_action(this.id)',  'ed_format_sup.gif'],
// Format Text Alignment
	"justifyleft":     	  ['JustifyLeft',          'Justify Left - ALT+L',       				'editor_action(this.id)',  'ed_align_left.gif'],
    "justifycenter":   	  ['JustifyCenter',        'Justify Center - ALT+C',     				'editor_action(this.id)',  'ed_align_center.gif'],
    "justifyright":    	  ['JustifyRight',         'Justify Right - ALT+R',      				'editor_action(this.id)',  'ed_align_right.gif'],
    "justifyfull":     	  ['JustifyFull', 		   'Justify Full - ALT+J', 						'editor_action(this.id)', 'ed_align_justify.gif'],
	"justifynone":     	  ['JustifyNone', 		   'Remove Alignment', 					'editor_action(this.id)', 'ed_align_none.gif'],
//Format Text Block
	"orderedlist":     	  ['InsertOrderedList',    'Ordered List - CTRL+N',       				'editor_action(this.id)',  'ed_list_num.gif'],
    "unorderedlist":   	  ['InsertUnorderedList',  'Bulleted List - CTRL+L',      				'editor_action(this.id)',  'ed_list_bullet.gif'],
    "outdent":         	  ['Outdent',              'Decrease Indent',    				'editor_action(this.id)',  'ed_indent_less.gif'],
    "indent":          	  ['Indent',               'Increase Indent',    				'editor_action(this.id)',  'ed_indent_more.gif'],
    "forecolor":       	  ['ForeColor',            'Font Color',         				'editor_action(this.id)',  'ed_color_fg.gif'],
    "backcolor":       	  ['BackColor',            'Background Color',   				'editor_action(this.id)',  'ed_color_bg.gif'],

//#Row 3: Insert/Modify Tools
	"explorer":           ['explorer',             'File explorer',   					'editor_action(this.id)',   'ed_explorer.gif'],
	"paragraph":   	   	  ['InsertParagraph',      'New paragraph at insertion point',  'editor_action(this.id)', 	'ed_paragraph.gif'],
    "marquee":   	   	  ['marquee',              'Marquee',            				'editor_action(this.id)', 	'ed_marquee.gif'],
    "line":        	      ['line',                 'Horizontal Rule',    				'editor_action(this.id)',  'ed_line.gif'],
    "specchar":	  	   	  ['SpecChar',             'Insert Special Characters', 		'editor_action(this.id)',  	'ed_spec_char.gif'],
//Hyperlinks and Images
	"insertlink":      	  ['InsertLink',           'Hyperlink',    						'editor_action(this.id)',  'ed_link.gif'],
    "unlink":    	  	  ['Unlink',               'Remove Link',        				'editor_action(this.id)',   'ed_unlink.gif'],
    "anchor":    	   	  ['anchor',               'Anchor',             				'editor_action(this.id)',   'ed_anchor.gif'],
    "insertimage":    	  ['InsertImage',          'Image Manager',       				'editor_action(this.id)',  'ed_image.gif'],
    "multipleselect":	  ['MultipleSelection',    'Select Multiple Obejcts (Shift or CTRL)',  'editor_action(this.id)',  	'ed_multipleselect.gif'],
	"liveresize":	      ['LiveResize',     	   'Live Resize',        			    'editor_action(this.id)',  	'ed_live.gif'],
	"today":			  ['Today',                'Insert Today\'s Date',  			'editor_action(this.id)',  'ed_date.gif'],
//Tables
	"inserttable":    	  ['InsertTable',          'Insert Table',       				'editor_action(this.id)',  'insert_table.gif'],
	"tableproperties":    ['TableProperties',      'Table Properties',  				'editor_action(this.id)',  'ed_tableprop.gif'],
    "showborder":	   	  ['ShowBorder',           'Show 0 borders',     				'editor_action(this.id)',  'ed_show_border.gif'],
// Table Properties inserted by lvn
//Table Rows
	"rowproperties":      ['RowProperties',        'Row Properties',    				'editor_action(this.id)',  'ed_rowprop.gif'],
    "insertrowbefore":    ['InsertRowBefore',      'Insert Row Before', 				'editor_action(this.id)',  'ed_insabove.gif'],
    "insertrowafter":     ['InsertRowAfter',       'Insert Row After',  				'editor_action(this.id)',  'ed_insunder.gif'],
    "deleterow":          ['DeleteRow',            'Delete Row',        				'editor_action(this.id)',  'ed_delrow.gif'],
	"splitrow":           ['SplitRow',             'Split row',        					'editor_action(this.id)',  'ed_splitrow.gif'],
    //"mergerows":          ['MergeRows',            'Merge rows',        				'editor_action(this.id)',  'ed_mergerows.gif'],
//Table Columns
	"insertcolumnbefore": ['InsertColumnBefore',   'Insert Column Before',  			'editor_action(this.id)',  'ed_insleft.gif'],
    "insertcolumnafter":  ['InsertColumnAfter',    'Insert Column Afer',				'editor_action(this.id)',  'ed_insright.gif'],
    "deletecolumn":       ['DeleteColumn',         'Delete Column',  					'editor_action(this.id)',  'ed_delcol.gif'],
//Table Cells
	"cellproperties":     ['CellProperties',       'Cell Properties',   				'editor_action(this.id)',  'ed_cellprop.gif'],
    "insertcellbefore":   ['InsertCellBefore',     'Insert Cell Before',				'editor_action(this.id)',  'ed_inscellft.gif'],
    "insertcellafter":    ['InsertCellAfter',      'Insert Cell After', 				'editor_action(this.id)',  'ed_inscelrgt.gif'],
    "deletecell":         ['DeleteCell',           'Delete Cell',       				'editor_action(this.id)',  'ed_delcel.gif'],
    "splitcell":          ['SplitCell',            'Split Cell',        				'editor_action(this.id)',  'ed_splitcel.gif'],
    "mergecells":         ['MergeCells',           'Merge Cells',       				'editor_action(this.id)',  'ed_mergecels.gif'],
// end insert by lvn
//Forms
	"inputform":	  	  ['InputForm',            'Form',               				'editor_action(this.id)',  'ed_form.gif'],
"spell": ['Spell', 'Spellcheck with SpellOnLine - ALT+S', 'editor_action(this.id);', 'ed_spellcheck.gif'],
	// Add custom buttons here:
	"rtl":                ['BlockDirRTL',          'Right to Left',  					'editor_action(this.id)',  'ed_rtl.gif'],
    "ltr":                ['BlockDirLTR',          'Left to Right',  					'editor_action(this.id)',  'ed_ltr.gif']

// end: custom buttons

    };

// insert by lvn : check editor changes
this.checkChanges = 0;
}


/* ---------------------------------------------------------------------- *\
  Function    : editor_generate
  Description : replace textarea with wysiwyg editor
  Usage       : editor_generate("textarea_id",[height],[width]);
  Arguments   : objname - ID of textarea to replace
                w       - width of wysiwyg editor
                h       - height of wysiwyg editor
\* ---------------------------------------------------------------------- */


function editor_generate(objname,userConfig) {

  // Default Settings
  var config = new editor_defaultConfig(objname);
  if (userConfig) {
    for (var thisName in userConfig) {
      if (userConfig[thisName]) { config[thisName] = userConfig[thisName]; }
    }
  }
  document.all[objname].config = config;                  // store config settings

  // set size to specified size or size of original object
  var obj    = document.all[objname];
  if (!config.width || config.width == "auto") {
    if      (obj.style.width) { config.width = obj.style.width; }      // use css style
    else if (obj.cols)        { config.width = (obj.cols * 8) + 22; }  // col width + toolbar
    else                      { config.width = '100%'; }               // default
  }
  if (!config.height || config.height == "auto") {
    if      (obj.style.height) { config.height = obj.style.height; }   // use css style
    else if (obj.rows)         { config.height = obj.rows * 17 }       // row height
    else                       { config.height = '200'; }              // default
  }

  var tblOpen  = '<table border=0 cellspacing=0 cellpadding=0 style="float: left;"  unselectable="on"><tr><td style="border: none; padding: 1 0 0 0"><nobr>';
  var tblClose = '</nobr></td></tr></table>\n';

  // build button toolbar

  var toolbar = '';
  var btnGroup, btnItem, aboutEditor;
  for (var btnGroup in config.toolbar) {

    // linebreak
    if (config.toolbar[btnGroup].length == 1 &&
        config.toolbar[btnGroup][0].toLowerCase() == "linebreak") {
      toolbar += '<br clear="all">';
      continue;
    }

    toolbar += tblOpen;
    for (var btnItem in config.toolbar[btnGroup]) {
      var btnName = config.toolbar[btnGroup][btnItem].toLowerCase();

      // fontname
      if (btnName == "fontname") {
        toolbar += '<select id="_' +objname+ '_FontName" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 2; font-size: 12px;">';
        for (var fontname in config.fontnames) {
          toolbar += '<option value="' +config.fontnames[fontname]+ '">' +fontname+ '</option>'
        }
        toolbar += '</select>';
        continue;
      }

      // fontsize
      if (btnName == "fontsize") {
        toolbar += '<select id="_' +objname+ '_FontSize" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;">';
        for (var fontsize in config.fontsizes) {
          toolbar += '<option value="' +config.fontsizes[fontsize]+ '">' +fontsize+ '</option>'
        }
        toolbar += '</select>\n';
        continue;
      }

      // font style
      if (btnName == "fontstyle") {
        toolbar += '<select id="_' +objname+ '_FontStyle" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;">';
        + '<option value="">Font Style</option>';
        for (var i in config.fontstyles) {
          var fontstyle = config.fontstyles[i];
          toolbar += '<option value="' +fontstyle.className+ '">' +fontstyle.name+ '</option>'
        }
        toolbar += '</select>';
        continue;
      }

      // separator
      if (btnName == "separator") {
        toolbar += '<span style="border: 1px inset; width: 1px; font-size: 16px; height: 16px; margin: 0 3 0 3"></span>';
        continue;
      }

      // buttons
      var btnObj = config.btnList[btnName];
      if (btnName == 'linebreak') { alert("htmlArea error: 'linebreak' must be in a subgroup by itself, not with other buttons.\n\nhtmlArea wysiwyg editor not created."); return; }
      if (!btnObj) { alert("htmlArea error: button '" +btnName+ "' not found in button list when creating the wysiwyg editor for '"+objname+"'.\nPlease make sure you entered the button name correctly.\n\nhtmlArea wysiwyg editor not created."); return; }
      var btnCmdID   = btnObj[0];
      var btnTitle   = btnObj[1];
      var btnOnClick = btnObj[2];
      var btnImage   = btnObj[3];
      toolbar += '<button title="' +btnTitle+ '" id="_' +objname+ '_' +btnCmdID+ '" class="btn" onClick="' +btnOnClick+ '" onmouseover="if(this.className==\'btn\'){this.className=\'btnOver\'}" onmouseout="if(this.className==\'btnOver\'){this.className=\'btn\'}" unselectable="on"><img src="' +config.imgURL + btnImage+ '" border=0 unselectable="on"></button>';


    } // end of button sub-group
    toolbar += tblClose;
  } // end of entire button set

  // build editor

  var editor = '<span id="_editor_toolbar"><table border=0 cellspacing=0 cellpadding=0 bgcolor="buttonface" style="padding: 1 0 0 2" width=' + config.width + ' unselectable="on"><tr><td>\n'
  + toolbar
  + '</td></tr></table>\n'
  + '</td></tr></table></span>\n'
  + '<textarea ID="_' +objname + '_editor" style="width:' +config.width+ '; height:' +config.height+ '; margin-top: -1px; margin-bottom: -1px;" wrap=soft></textarea>';

  // add context menu
  editor += '<div id="_' +objname + '_cMenu" style="position: absolute; visibility: hidden;"></div>';

  //  hide original textarea and insert htmlarea after it
  if (!config.debug) { document.all[objname].style.display = "none"; }

  if (config.plaintextInput) {     // replace nextlines with breaks
    var contents = document.all[objname].value;
    contents = contents.replace(/\r\n/g, '<br>');
    contents = contents.replace(/\n/g, '<br>');
    contents = contents.replace(/\r/g, '<br>');
    document.all[objname].value = contents;
  }

  // insert wysiwyg
  document.all[objname].insertAdjacentHTML('afterEnd', editor)

  // convert htmlarea from textarea to wysiwyg editor
  editor_setmode(objname, 'init');

  // call filterOutput when user submits form
  for (var idx=0; idx < document.forms.length; idx++) {
    var r = document.forms[idx].attachEvent('onsubmit', function() { editor_filterOutput(objname); });
    if (!r) { alert("Error attaching event to form!"); }
  }

return true;

}

/* ---------------------------------------------------------------------- *\
  Function    : editor_generate
  Description : replace textarea with wysiwyg editor
  Usage       : editor_generate("textarea_id",[height],[width]);
  Arguments   : objname - ID of textarea to replace
                w       - width of wysiwyg editor
                h       - height of wysiwyg editor
\* ---------------------------------------------------------------------- */
/*
function editor_generate(objname,userConfig) {
// Default Settings
	var config = new editor_defaultConfig(objname);
	if (userConfig) {
	for (var thisName in userConfig) {
	if (userConfig[thisName]) { config[thisName] = userConfig[thisName]; }
	}
}
document.all[objname].config = config; // store config settings

// set size to specified size or size of original object
	var obj    = document.all[objname];
	if (!config.width || config.width == "auto") {
	if  (obj.style.width) { config.width = obj.style.width; } // use css style
	else if (obj.cols) { config.width = (obj.cols * 8) + 22; } // col width + toolbar
	else { config.width = '100%'; } // default
	}

	if (!config.height || config.height == "auto") {
	if (obj.style.height) { config.height = obj.style.height; } // use css style
	else if (obj.rows) { config.height = obj.rows * 17 } // row height
	else { config.height = '200'; } // default
	}

	var tblOpen  = '<table border=1 cellspacing=0 cellpadding=0 style="float: left;"  unselectable="on"><tr><td style="border: none; padding: 1 0 0 0; font-family: MS Shell Dlg;"><nobr>';
	var tblClose = '</nobr></td></tr></table>\n';

// build button toolbar
	var toolbar = '';
	var btnGroup, btnItem, aboutEditor;
	for (var btnGroup in config.toolbar){

// linebreak
	if (config.toolbar[btnGroup].length == 1 &&	config.toolbar[btnGroup][0].toLowerCase() == "linebreak") {
	toolbar += '<br clear="all">';
	continue;
	}

	toolbar += tblOpen;
	for (var btnItem in config.toolbar[btnGroup]) {
	var btnName = config.toolbar[btnGroup][btnItem].toLowerCase();

// formatblock inserted by lvn
	if (btnName == "formatblock") {
	toolbar += '&nbsp;&nbsp;Format <select id="_' +objname+ '_FormatBlock" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 2; font-size: 12px;">';
	for (var i in config.formatblocks) {
	var fbObj = config.formatblocks[i];
	var fbvalue = "";
	var fbname  = "";
	for (var j in fbObj.formatblocklangs) {
	var fblangObj = fbObj.formatblocklangs[j];
	if (fblangObj.lang == config.systemLang[0]) {fbvalue = fblangObj.name;}
	if (fblangObj.lang == config.browserLang[0]) {fbname = fblangObj.name;}
	}
	toolbar += '<option value="' +fbvalue+ '">' + fbname + '</option>';
	}
	toolbar += '</select>'; continue;
	}
// end insert by lvn
// fontname
	if (btnName == "fontname") {
	toolbar += '&nbsp;&nbsp;Font Name <select id="_' +objname+ '_FontName" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 2; font-size: 12px;">';
	for (var fontname in config.fontnames) {
	toolbar += '<option value="' +config.fontnames[fontname]+ '">' +fontname+ '</option>'
	}
	toolbar += '</select>';
	continue;
	}

// fontsize
	if (btnName == "fontsize") {
	toolbar += '&nbsp;&nbsp;Font Size <select id="_' +objname+ '_FontSize" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;">';
	for (var fontsize in config.fontsizes) {
	toolbar += '<option value="' +config.fontsizes[fontsize]+ '">' +fontsize+ '</option>'
	}
	toolbar += '</select>\n';
	continue;
	}

// font style
	if (btnName == "fontstyle") {
	toolbar += '<select id="_' +objname+ '_FontStyle" onChange="editor_action(this.id)" unselectable="on" style="margin: 1 2 0 0; font-size: 12px;">';
	toolbar += '<option value="">Font Style</option>';
	for (var i in config.fontstyles) {
	var fontstyle = config.fontstyles[i];
	toolbar += '<option value="' +fontstyle.className+ '">' +fontstyle.name+ '</option>'
	}
		toolbar += '</select>';
	continue;
	}
// separator
	if (btnName == "separator") {
	toolbar += '<span style="border: 1px inset; width: 1px; font-size: 16px; height: 16px; margin: 0 2 0 2"></span>';
	continue;
	}
// buttons
	var btnObj = config.btnList[btnName];
	if (btnName == 'linebreak') { alert("htmlArea error: 'linebreak' must be in a subgroup by itself, not with other buttons.\n\nhtmlArea wysiwyg editor not created."); return; }
	if (!btnObj) { alert("htmlArea error: button '" +btnName+ "' not found in button list when creating the wysiwyg editor for '"+objname+"'.\nPlease make sure you entered the button name correctly.\n\nhtmlArea wysiwyg editor not created."); return; }
	var btnCmdID   = btnObj[0];
	var btnTitle   = btnObj[1];
	var btnOnClick = btnObj[2];
	var btnImage   = btnObj[3];
	toolbar += '<button title="' +btnTitle+ '" id="_' +objname+ '_' +btnCmdID+ '" class="btn" onClick="' +btnOnClick+ '" onmouseover="if(this.className==\'btn\'){this.className=\'btnOver\'}" onmouseout="if(this.className==\'btnOver\'){this.className=\'btn\'}" unselectable="on"><img src="' +config.imgURL + btnImage+ '" border=0 unselectable="on"></button>';
	} // end of button sub-group
	toolbar += tblClose;
	} // end of entire button set

// build editor
var editor = '<span id="_editor_toolbar"><table border=1 cellspacing=0 cellpadding=0 bgcolor="buttonface" style="padding: 1 0 0 2" width=' + config.width + ' unselectable="on"><tr><td>\n'
+ toolbar
+ '</td></tr></table>\n'
+ '</td></tr></table></span>\n'
+'<table width='+config.width+'><tr><td BACKGROUND="'+'images/ruler.gif">&nbsp;</td></tr></table>'
+ '<textarea ID="_' +objname + '_editor" style="width:' +config.width+ '; height:' +config.height+ '; margin-top: -1px; margin-bottom: -1px;" wrap=soft></textarea>';

//  hide original textarea and insert htmlarea after it
	if (!config.debug) { document.all[objname].style.display = "none"; }

	if (config.plaintextInput) {     // replace nextlines with breaks
	var contents = document.all[objname].value;
	contents = contents.replace(/\r\n/g, '<br>');
	contents = contents.replace(/\n/g, '<br>');
	contents = contents.replace(/\r/g, '<br>');
	document.all[objname].value = contents;
	}

// insert wysiwyg
  document.all[objname].insertAdjacentHTML('afterEnd', editor)

// convert htmlarea from textarea to wysiwyg editor
  editor_setmode(objname, 'init');

// call filterOutput when user submits form
	for (var idx=0; idx < document.forms.length; idx++) {
	var r = document.forms[idx].attachEvent('onsubmit', function() { editor_filterOutput(objname); });
	if (!r) { alert("Error attaching event to form!"); }
	}
	return true;
	}
*/

/* ---------------------------------------------------------------------- *\
  Function    : editor_action
  Description : perform an editor command on selected editor content
  Usage       :
  Arguments   : button_id - button id string with editor and action name
\* ---------------------------------------------------------------------- */

function editor_action(button_id) {
// split up button name into "editorID" and "cmdID"
	var BtnParts = Array();
	BtnParts = button_id.split("_");
	var objname    = button_id.replace(/^_(.*)_[^_]*$/, '$1');
	var cmdID      = BtnParts[ BtnParts.length-1 ];
	var button_obj = document.all[button_id];
	var editor_obj = document.all["_" +objname + "_editor"];
	var config     = document.all[objname].config;

  // help popup
if (cmdID == 'showhelp') {
window.open(_editor_url + "popups/editor_help.jsp",'Help','Width=450px,left=50,top=20, Height=300px,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no');
return false;}

  // Colour picker
if (cmdID == 'colorpicker') {
window.open(_editor_url + "popups/select_color.jsp",'colorpicker','Width=420px,left=50,top=20, Height=200px,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes');
return false;}

// FTP Applet
	if (cmdID == 'ftp') {
	showModalDialog(_editor_url + "popups/ftpapplet/index.html",myTitle,"resizable: yes; help: no; status: no; scroll: no; ");
	return false;}

	if (cmdID == 'explorer') {
		window.open(_editor_url + "popups/explorer.php", 'explorer','Width=500px,left=100,top=20, Height=510px,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no');
		return;
		}

  // popup editor
  if (cmdID == 'popupeditor') {
    window.open(_editor_url + "popups/fullscreen.jsp?"+objname,
                'FullScreen',
                'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes');
    return;
  }

// inserted by lvn
// show 0 borders
	if (cmdID.toLowerCase() == 'showborder'){
	var btnObj = document.all["_" +objname+ "_ShowBorder"];
	if (config.showborders){ // toggle is on : put borders off
		nullBorders(editor_obj.contentWindow.document,'hide');
		btnObj.className = 'btn';
		config.showborders = false;
	}
	else {
		nullBorders(editor_obj.contentWindow.document,'show');
		btnObj.className = 'btnDown';
		config.showborders = true;
	}
	return;
	}



// check editor mode (don't perform actions in textedit mode)
	if (editor_obj.tagName.toLowerCase() == 'textarea') { return; }
	var editdoc = editor_obj.contentWindow.document;

if (cmdID == 'save') {
var str = editdoc.body.createTextRange().htmlText;
str = cleanHTML(str);
if (str == '') {
       alert('\nEditor is empty.\n\nNothing to save!');
        return;
		}

else{
var re1 = /BORDER-RIGHT: #c0c0c0 1px dotted; BORDER-TOP: #c0c0c0 1px dotted; BORDER-LEFT: #c0c0c0 1px dotted; BORDER-BOTTOM: #c0c0c0 1px dotted/g;
var re2 = / style=""/g;
	str = str.replace(re1,"");
	str = str.replace(re2,"");
cDialog.CancelError=true;
  	try{
  		cDialog.Filter="HTML (*.html)|*.html|HTM (*.htm)|*.html|Include Files (*.inc)|*.inc|Text Files (*.txt)|*.txt"
		cDialog.DialogTitle="HTMLArea SaveAs Dialog"
  		cDialog.ShowSave();
  		var fso = new ActiveXObject("Scripting.FileSystemObject");
  		var f = fso.CreateTextFile(cDialog.filename,true);
		f.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">');
		f.write('<html>\n');
		f.write('<head>\n');
		f.write('<meta name="GENERATOR" content="ezHTMLarea - http://fslactivities.sd61.bc.ca/ezHTMLarea/ ">\n');
		f.write('<title>mydocument</title>\n');
		f.write('\n');
		f.write('<!*************************************************************************************>\n');
		f.write('<!*********************** This page was created with ezHTMLarea ***********************>\n');
		f.write('<!************* ezHTMLarea - http://fslactivities.sd61.bc.ca/ezHTMLarea/ **************>\n');
		f.write('<!*************************************************************************************>\n');
		f.write('\n');
		f.write('<!------------------------------------------------------------------------------------->\n');
		f.write('<!-------- Follow these steps to change the color or picture in the background -------->\n');
		f.write('<!------------------------------------------------------------------------------------->\n');
		f.write('<!----------------------------- S T E P  1 -------------------------------------------->\n');
		f.write('<!---- Change the color WHITE below by another color or a Hex value.  i.e. #CCCC99 ---->\n');
		f.write('<!----------------------------- S T E P  2 -------------------------------------------->\n');
		f.write('<!--- Place a picture in the background by changing the word PICTURE below by a URL --->\n');
		f.write('<!----------------------------- S T E P  3 -------------------------------------------->\n');
		f.write('<!---- Save this file and use the Refresh button in your browser to see the changes --->\n');
		f.write('<!------------------------------------------------------------------------------------->\n');
		f.write('\n');
		f.write('<style>\n');
		f.write('body {background-color: ');
		f.write('WHITE');
		f.write('; background-image:url(PICTURE); font-family: "Arial"; font-size: x-small;}\n');
		f.write('.headline {font-family: arial black, arial; font-size: 28px; letter-spacing: -2px;}\n');
		f.write('.headline2 {font-family: arial black, arial; font-size: 12px; letter-spacing: -2px; color:red}\n');
		f.write('.headline4 {font-family: verdana; font-size: 18px; letter-spacing: -2px; color:blue}\n');
		f.write('</style>\n');
		f.write('</head>\n');
		f.write('<body>\n');
  		f.write(str);
		f.write('\n');
		f.write('<!--Footer-->\n');
		f.write('<P align=center><FONT face="arial, helvetica, sans-serif" size=1>This web page was created with </FONT><A href="http://fslactivities.sd61.bc.ca/ezHTMLarea/"><FONT face="arial, helvetica, sans-serif" size=1>ezHTMLarea</FONT></A><FONT face="arial, helvetica, sans-serif" size=1>.</FONT>\n');
		f.write('<!--End of Footer-->\n');
		f.write('</body>\n');
		f.write('</html>\n');
  		f.Close();
  		sPersistValue=str}
  	catch(e){
  		var sCancel="true";
  		return sCancel;}
	document.focus();
}
		}
// Plain Text
else if (cmdID == 'plaintext') {
if (editdoc.body.createTextRange().htmlText != "") {
window.clipboardData.clearData();
editdoc.execCommand('SelectAll');
editdoc.execCommand('Copy');
window.open(_editor_url + "../editez/index.html",'plaintext','Width=768px,left=0,top=0, Height=510px,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,resizable=yes');
}
else {alert('\nEditor is empty.\n\nNo HTML tags to remove!');}
}


// inserted by lvn : preview
  if (cmdID == 'Preview'){
     var predoc = editdoc.body.createTextRange().htmlText;
     if (config.showborders) {
        var re1 = /BORDER-RIGHT: #c0c0c0 1px dotted; BORDER-TOP: #c0c0c0 1px dotted; BORDER-LEFT: #c0c0c0 1px dotted; BORDER-BOTTOM: #c0c0c0 1px dotted/g;
        var re2 = / style=""/g;
        predoc = predoc.replace(re1,"");
        predoc = predoc.replace(re2,"");
     };
     win = window.open('','preview','toolbar=yes,location=yes,menubar=yes,status=yes,scrollbars=yes,resizable=yes');
     doc=win.document.open();
     doc.writeln('<html>\n<head>\n<title>Preview</title>');
     if (config.stylesheet) {
        doc.writeln('<link rel="stylesheet" href="' + config.stylesheet +'" type="text/css">');
     }
     doc.writeln('<style>');
     doc.writeln('body {' +config.bodyStyle+ '} ');
     for (var i in config.fontstyles) {
       var fontstyle = config.fontstyles[i];
       if (fontstyle.classStyle) {
         doc.writeln('.' +fontstyle.className+ ' {' +fontstyle.classStyle+ '}');
       }
     }

     doc.writeln('</style>');
     doc.writeln('</head>\n<body>');
     doc.writeln(predoc);
	 doc.writeln('</body>\n</html>\n');
	 doc=win.document.close();
     win.focus();
     return;
  }


	if (cmdID.toLowerCase() == 'find') {
	if (editdoc.body.createTextRange().htmlText != "") {
		setGlobalVar ("_editor_field",objname);
		var TxtRange = editor_obj.contentWindow.document.body.createTextRange();
		showModelessDialog(_editor_url + "popups/find.html",window, "resizable: no; help: no; status: no; scroll: no; ");
		return;
		}
		else {alert('\nEditor is empty.\n\nNothing to find or replace.!');}
}

	editor_focus(editor_obj);
// get index and value for pulldowns
	var idx = button_obj.selectedIndex;
	var val = (idx != null) ? button_obj[ idx ].value : null;
	if (0) {}   // use else if for easy cutting and pasting

	else if (cmdID == 'Today') {  // insert some text from a popup window
		var myTitle = "Insert Today's Date";
		var myText = showModalDialog(_editor_url + "popups/today.html",
		myTitle, "resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, myText); }
		}

// empty htmlarea content (refresh / new)
else if (cmdID.toLowerCase() == 'refresh') {
	editdoc.body.innerHTML="";
		}

// Spellcheck with SpellOnLine
else if (cmdID.toLowerCase()  == 'spell') {
editor_updateOutput(objname);

str = editdoc.body.createTextRange().htmlText;
mySpellWin = window.open("", "mySpellWin", "width=100,height=100");
mySpellWin.document.open();
mySpellWin.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">');
mySpellWin.document.write('<html>\n');
mySpellWin.document.write('<head>\n');
mySpellWin.document.write('\n');
mySpellWin.document.write('</head>\n');
mySpellWin.document.write('<body>\n');
mySpellWin.document.write('<P><TEXTAREA NAME="spell" style="width:450; height:300;">\n');
mySpellWin.document.write(str);
mySpellWin.document.write('\n');
mySpellWin.document.write('</TEXTAREA><\P>\n');
mySpellWin.spell.focus();
mySpellWin.document.write('</body>\n');
mySpellWin.document.write('</html>\n');
mySpellWin.document.execCommand("SelectAll");
mySpellWin.document.execCommand("Copy");
mySpellWin.document.close();
mySpellWin.close();
mySpellWin= open("popups/spellcheck.html",'spell','Width=782px,Height=540px,left=0,top=0, toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes');
editor_setmode(objname, 'init');
}

else if (cmdID.toLowerCase() == 'paste') {
	editdoc.execCommand('Paste');
	var str=editdoc.body.createTextRange().htmlText;
	if (str.indexOf("; mso-")>=0 ||str.indexOf("<v:")>=0 ||str.indexOf("class=Mso")>=0){
	//alert("It's Word");
	myclean(editdoc);
	}
	editdoc.body.innerHTML = cleanHTML(editdoc.body.innerHTML);
	}

	else if (cmdID == 'RemoveFont') {
	oTags = editdoc.all.tags("FONT"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {oTags[i].outerHTML = oTags[i].innerHTML;}}
	}

//FUNCTION OPEN WORD:
else if (cmdID.toLowerCase() == 'openword') {
		window.clipboardData.clearData();
		editdoc.execCommand('SelectAll');
		editdoc.execCommand('cut');
		var myTitle = "";
		var myText = showModalDialog(_editor_url + "popups/openword.html",myTitle,"resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );
		}
		else {editdoc.execCommand('paste');}
		myclean(editdoc);
		editdoc.body.innerHTML = cleanHTML(editdoc.body.innerHTML);
		}

	else if (cmdID.toLowerCase() == 'changecase') {
	window.clipboardData.clearData();
	if (editdoc.selection.createRange().htmlText != "") {
	var highlightedText = editdoc.selection.createRange().htmlText;
	editdoc.execCommand('copy');
	editdoc.execCommand('FormatBlock','','Normal')
	editdoc.execCommand('RemoveFormat');
	editdoc.execCommand('unlink');
	var myText = showModalDialog(_editor_url + "popups/changecase.html",
		highlightedText, "resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );}
		else {editdoc.execCommand('paste');}
		window.clipboardData.clearData();
		}
	else {alert('\nYou need to select some text first.');}
 }

// inserted by lvn : table operations
	else if ( cmdID.toLowerCase()  == 'tableproperties'||cmdID.toLowerCase()  == 'rowproperties'||cmdID.toLowerCase() == 'insertrowbefore'||cmdID.toLowerCase()  == 'insertrowafter'||cmdID.toLowerCase()  == 'deleterow'||cmdID.toLowerCase()  == 'insertcolumnbefore'||cmdID.toLowerCase()  == 'insertcolumnafter'||cmdID.toLowerCase()  == 'deletecolumn'||cmdID.toLowerCase()  == 'cellproperties'||cmdID.toLowerCase()  == 'insertcellbefore'||cmdID.toLowerCase()  == 'insertcellafter'||cmdID.toLowerCase()  == 'splitcell'||cmdID.toLowerCase()  == 'mergerows'||cmdID.toLowerCase()  == 'splitrow'||cmdID.toLowerCase()  == 'mergecells'||cmdID.toLowerCase()  == 'deletecell' ||cmdID.toLowerCase()  == 'createcaption')
	{
// table operations
	var table_src_element = editdoc.selection.createRange().parentElement();
	while (table_src_element != null && table_src_element.tagName != 'TD' && table_src_element.tagName != 'TH'){
	table_src_element = table_src_element.parentElement;
	}
	if (table_src_element == null) {alert('Table operations not allowed here');}
	else {tables_action(button_id,table_src_element);}
	}
// end insert by lvn

// FontName
	else if (cmdID == 'FontName' && val) {
		editdoc.execCommand(cmdID,0,val);
		}

// inserted by lvn
  // Formatblock
	else if (cmdID == 'FormatBlock' && val) {
		editdoc.execCommand(cmdID,0,val);
		}

// special characters
	else if (cmdID == 'SpecChar') {
		var newchar = showModalDialog(_editor_url + "popups/insert_char.html", '', "dialogWidth:630px; dialogHeight: 180px; resizable: no; help: no; status: no; scroll: no;");
		if (newchar == '') {return;}
		else {editor_insertHTML(objname,newchar);}
		}
// end insert by lvn

// FontSize
	else if (cmdID == 'FontSize' && val) {editdoc.execCommand(cmdID,0,val);}

// FontStyle (change CSS className)
	else if (cmdID == 'FontStyle' && val) {
		if(val!="remove"){
		editdoc.execCommand('RemoveFormat');
		editdoc.execCommand('FontName',0,'636c6173734e616d6520706c616365686f6c646572');
		var fontArray = editdoc.all.tags("FONT");
		for (i=0; i<fontArray.length; i++) {
		if (fontArray[i].face == '636c6173734e616d6520706c616365686f6c646572') {
		fontArray[i].face = "";
		fontArray[i].className = val;
		fontArray[i].outerHTML = fontArray[i].outerHTML.replace(/face=['"]+/, "");
		}
		}
		button_obj.selectedIndex =0;
		}
		else{editdoc.execCommand('RemoveFormat');}
		}

// fgColor and bgColor
	else if (cmdID == 'ForeColor' || cmdID == 'BackColor') {
		var oldcolor = _dec_to_rgb(editdoc.queryCommandValue(cmdID));
		var newcolor = showModalDialog(_editor_url + "popups/select_color.jsp", oldcolor, "resizable: no; help: no; status: no; scroll: no;");
		if (newcolor != null) { editdoc.execCommand(cmdID, false, "#"+newcolor); }
		}

// execute command for buttons - if we didn't catch the cmdID by here we'll assume it's a
// commandID and pass it to execCommand().   See http://msdn.microsoft.com/workshop/author/dhtml/reference/commandids.asp
	else {
// subscript & superscript, disable one before enabling the other
		if (cmdID.toLowerCase() == 'subscript' && editdoc.queryCommandState('superscript')) { editdoc.execCommand('superscript'); }
		if (cmdID.toLowerCase() == 'superscript' && editdoc.queryCommandState('subscript')) { editdoc.execCommand('subscript'); }

// insert link (modified)
	if (cmdID.toLowerCase() == 'insertlink') {
		var theRange = editdoc.selection.createRange();
		var highlightedText = "";
		var linkText = '';
		var href_attribute = '';
		var tar_attribute = '';
		var elmSelectedImage;
		var htmlSelectionControl = "Control";

	if (editdoc.selection.type == htmlSelectionControl) {
// actully we have an image.
		elmSelectedImage = theRange.item(0);
		highlightedText = elmSelectedImage.outerHTML;

//convert the ControlRange to a TextRange
		theRange = editdoc.body.createTextRange();
		theRange.moveToElementText(elmSelectedImage);
		theRange.select();
		fullElement = theRange.htmlText;
		}

	else {
		highlightedText = theRange.htmlText;
		fullElement = theRange.parentElement().outerHTML;
		}

//in case we happen to select the link itself!
	if (highlightedText.search(/^\<[A|a]/) != -1) {fullElement = highlightedText;}

//extrect attributes from HTML
	if (fullElement.search(/^\<[A|a]/) != -1) {
		fullElement = fullElement.replace(/\"/g, "");
		fullElement = fullElement.replace(/\'/g, "");

// here, we have an <a> tag. Now let's extract...
// 1. the href attribute
		var href_value = fullElement.split(/href=/);
		href_value2 = href_value[1].split(/\s|>/);
		href_attribute = href_value2[0];

// 2. the target attribute
		if (fullElement.search(/target=/) != -1) {
		var tar = fullElement.split(/target=/);
		tar2 = tar[1].split(/\s|>/);
		tar_attribute = tar2[0];
		}

// 3. the link text (more robust as includes all html code aswell)
		pos1 = fullElement.indexOf(">");
		pos2 = fullElement.lastIndexOf("<");
		linkText = fullElement.substring(pos1+1,pos2);
		}

		var myValues = new Object();
		myValues.highlightedText = highlightedText;
		myValues.tar_attribute = tar_attribute;
		myValues.href_attribute = href_attribute;
		myValues.linkText = linkText;

		var myText = showModalDialog(_editor_url + "popups/insert_link.html", myValues, "status=no; scroll=no");

	if (linkText != '') {
	if (myText) {
		theRange.parentElement().outerHTML = '';
		editor_insertHTML(objname, unescape( myText) );
		}
		}

	else {
	if (myText) {
		editor_insertHTML(objname, unescape(myText) ); // this function ALWAYS puts in an absolute link
		}
		}
		}

// insert image
	else if (cmdID.toLowerCase() == 'insertimage'){
		showModalDialog(_editor_url + "popups/insert_image.jsp", editdoc, "resizable: no; help: no; status: no; scroll: no; ");
editdoc.body.innerHTML = cleanHTML(editdoc.body.innerHTML);
		}

		// insert image
		else if (cmdID.toLowerCase() == 'upload'){
		showModalDialog(_editor_url + "imgupload.php", editdoc, "resizable: no; help: no; status: no; scroll: no; ");
		}
// insert form
	else if (cmdID.toLowerCase() == 'inputform') {
		var myText = showModalDialog(_editor_url + "popups/mpc.html","","resizable: no; help: no; status: no; scroll: no; ");
		if (myText) {editor_insertHTML(objname, unescape( myText) );}
		}

// insert formelement
	else if (cmdID.toLowerCase() == 'inputformelement') {
		if (editdoc.selection.createRange().text != "") {
		var highlightedText = editdoc.selection.createRange().text;}
		else {var highlightedText = "";}
		var myText = showModalDialog(_editor_url + "popups/insert_formelement.html",highlightedText,"resizable: no; help: no; status: no; scroll: no; ");
		if (myText) {editor_insertHTML(objname, unescape( myText) );}
		}

// insert table
	else if (cmdID.toLowerCase() == 'inserttable'){
		setGlobalVar('_editor_field',objname);
		showModalDialog(_editor_url + "popups/insert_table.jsp?"+objname,window,"resizable: yes; help: no; status: no; scroll: no; ");
		if (config.showborders) { nullBorders(editdoc,'show')};
		}

// insert line
	else if (cmdID == 'line') {  // insert horizontal rule
		var myText = showModalDialog(_editor_url + "popups/insert_line.html",window,"resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );}
		}

// insert marquee
else if (cmdID == 'marquee') {
var myText = showModalDialog(_editor_url + "popups/insert_marquee.html",editdoc,"resizable: yes; help: no; status: no; scroll: no; ");
if (myText) { editor_insertHTML(objname, unescape( myText) );}
}


// Insert anchor
	else if (cmdID == 'anchor') {
		var myTitle = "";
		var myText = showModalDialog(_editor_url + "popups/insert_anchor.html",myTitle,"resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );}
		}
// Insert Template
	else if (cmdID.toLowerCase() == 'template') {
		window.clipboardData.clearData();
		editdoc.execCommand('SelectAll');
		editdoc.execCommand('cut');
		var myTitle = "";
		var myText = showModalDialog(_editor_url + "popups/insert_template.html",myTitle,"resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );
		nullBorders(editor_obj.contentWindow.document,'show');
		config.showborders = true;
		}
		else editdoc.execCommand('paste');
		}

	else if (cmdID.toLowerCase() == 'openfile') {
		window.clipboardData.clearData();
		editdoc.execCommand('SelectAll');
		editdoc.execCommand('cut');
		var myTitle = "";
		var myText = showModalDialog(_editor_url + "popups/openpage.html",myTitle,"dialogWidth:350px; dialogHeight: 170px; resizable: yes; help: no; status: no; scroll: no; ");
		if (myText) { editor_insertHTML(objname, unescape( myText) );
		}
		else editdoc.execCommand('paste');
		}



    // all other commands microsoft Command Identifiers
    else {
	editdoc.execCommand(cmdID.toLowerCase());
	//stoperror();
	}
  }

  editor_event(objname);
}

/* ---------------------------------------------------------------------- *\
	Function    : tables_action
	Description : perform an action on selected table
	Usage       :
	Arguments   : table_action - objectname + action to execute td - startpoint cell
	inserted by lvn : table operations
\* ---------------------------------------------------------------------- */

	function tables_action(table_action,td) { // operations only valid on table cells
	if (td.tagName == 'TD' || td.tagName == 'TH' ) {
		var TableParts = table_action.split("_");
		var objname    = table_action.replace(/^_(.*)_[^_]*$/, '$1');
		var cmdID      = TableParts[ TableParts.length-1 ];
		var editor_obj = document.all["_" +objname + "_editor"];
		var config     = document.all[objname].config;
		var tr,td,tbody,table,newtr;
		// get the table object model
		tr = td.parentNode;
		while(tr != null && tr.tagName != 'TR'){tr = tr.parentNode;}
		if (tr != null) {
		var tbody = tr.parentNode;
		while(tbody != null && tbody.tagName != 'TBODY' && tbody.tagName != 'THEAD' && tbody.tagName != 'TFOOT'){tbody = tbody.parentNode;}
		if (tbody != null) {
		table = tbody.parentNode;
		while(table!= null && table.tagName != 'TABLE'){table = table.parentNode;}
		}
		}
	// only execute commands if table object model is complete
	if (table != null) {
	// local functions to insert rowdetails and columns

	function insertRowDetails(tr,newtr) {
		for (var i=0;i < tr.cells.length;i++) {newtr.insertCell(-1);}
		}

	function insertColumn(tbody,where) {
		for (var i=0;i < tbody.rows.length;i++) {
		tr = tbody.rows(i);
		if (where > tr.cells.length){tr.insertCell();}
		else {tr.insertCell(where);}
		}
		}
	function deleteColumn(tbody,where) {
		for (var i=0;i <  tbody.rows.length;i++) {
		var tr = tbody.rows(i);
		if (tr.cells.length - 1 < where){tr.deleteCell(tr.cells.length - 1);}
		else {tr.deleteCell(where);}
		tr = tbody.rows(i);
		if (tr.cells.length == 0){tbody.deleteRow(i);}
		}
		}

	function splitCell(tbody,currTr,currTd){
	// rowspan > 1 just insert cell and decrease colspan
		if (currTd.colSpan > 1) {
		currTd.colSpan = currTd.colSpan - 1;
		currTr.insertCell(currTd.cellIndex + 1);
		}
		else {
	// rowspan = 1 increase colspan for all other rows and insert cell in current row
		for (var i=0;i <  tbody.rows.length;i++) {
		var tr = tbody.rows(i);
		var td = tr.cells(currTd.cellIndex);
		if (i == currTr.rowIndex) {tr.insertCell(currTd.cellIndex + 1);}
		else {td.colSpan = td.colSpan + 1;}
		}
		}
		}

	function mergeCells(tbody,currTr,currTd){
	//first check if there are cells to the right
		if (currTd.cellIndex < currTr.cells.length-1) {
	//get current colspan and cell to be merged's colspan add the two together to get the new one, move the content and delete the right one
		var currColSpan = currTd.colSpan ;
		var mergeCellColSpan = currTr.cells(currTd.cellIndex+1).colSpan;
		var mergeCell = currTr.cells(currTd.cellIndex+1);
		currTd.innerHTML = currTd.innerHTML + mergeCell.innerHTML;
		currTr.deleteCell(currTd.cellIndex + 1);
		currTd.colSpan = currColSpan+mergeCellColSpan ;
		}
		else {alert('Select the leftmost cell of the split to merge.');}
		}

	function splitRow(tbody,currTr,currTd){
	// check rowspan on other cells
		if (currTd.rowSpan > 1){
		currTd.rowSpan = currTd.rowSpan - 1;
		var tr = tbody.rows(currTr.rowIndex + 1);
		var where = 0;
		for (var i=0;i <  currTr.cells.length;i++) {
		if (i < currTd.cellIndex){
		if (currTr.cells(i).rowSpan < 2){where++;}
		}
		}
		tr.insertCell(where);
		}
		else {
		for (var i=0;i <  currTr.cells.length;i++) {
		var td = currTr.cells(i);
		if (i == currTd.cellIndex) {
		tr = tbody.insertRow(currTr.rowIndex + 1);
		tr.insertCell(0);
		}
		else {td.rowSpan = td.rowSpan + 1;}
		}
		}
		}
	function mergeRows(tbody,currTr,currTd){
	// check if topmost of cells to merge
		var top = false;
		if (currTd.rowSpan < 2){
		for (var i=0;i <  currTr.cells.length;i++) {
		if (i !== currTd.cellIndex) {
		if (currTr.cells(i).rowSpan > 1){top = true; break;}
		}
		}
		}
		if (top){return;}
		else {alert('Select the topmost row of the split to merge.');}
		}

	// execute the operation depending on the given command
	switch(cmdID.toLowerCase()) {
		case 'createcaption'      : table.createCaption();break;
		case 'deletecaption'      : table.deleteCaption();break;
		case 'createthead'        : table.createTHead();break;
		case 'deletethead'        : table.deleteTHead();break;
		case 'createtfoot'        : table.createTFoot();break;
		case 'deletetfoot'        : table.deleteTFoot();break;
		case 'insertrowtop'       : newtr = tbody.insertRow(0);insertRowDetails(tr,newtr);break;
		case 'insertrowbottom'    : newtr = tbody.insertRow(-1);insertRowDetails(tr,newtr);break;
		case 'insertrowbefore'    : newtr = tbody.insertRow(tr.rowIndex);insertRowDetails(tr,newtr);break;
		case 'insertrowafter'     : newtr = tbody.insertRow(tr.rowIndex+1);insertRowDetails(tr,newtr);break;
		case 'insertrowstart'     : newtr = tbody.insertRow(0);insertRowDetails(tr,newtr);break;
		case 'deleterow'          : tbody.deleteRow(tr.rowIndex);break;
		case 'insertcolumnleft'   : insertColumn(tbody,0);break;
		case 'insertcolumnright'  : insertColumn(tbody,-1);break;
		case 'insertcolumnbefore' : insertColumn(tbody,td.cellIndex);break;
		case 'insertcolumnafter'  : insertColumn(tbody,td.cellIndex+1);break;
		case 'deletecolumn'       : deleteColumn(tbody,td.cellIndex);break;
		case 'insertcellleft'     : tr.insertCell(0);break;
		case 'insertcellright'    : tr.insertCell(-1);break;
		case 'insertcellbefore'   : tr.insertCell(td.cellIndex);break;
		case 'insertcellafter'    : tr.insertCell(td.cellIndex+1);break;
		case 'insertcellstart'    : tr.insertCell(0);break;
		case 'deletecell'         : tr.deleteCell(td.cellIndex);break;
		case 'splitcell'          : splitCell(tbody,tr,td);break;
		case 'mergecells'         : mergeCells(tbody,tr,td);break;
		case 'splitrow'           : splitRow(tbody,tr,td);break;
		case 'mergerows'          : mergeRows(tbody,tr,td);break;
// inserted by lvn : property pallettes
        case 'tableproperties'    : nullBorders(editor_obj.contentWindow.document,'hide');
                                       setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_table',table);
                                       showModalDialog(_editor_url + "popups/tableprop.jsp?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
           case 'rowproperties'      : setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_row',tr);
                                       showModalDialog(_editor_url + "popups/rowprop.jsp?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
           case 'cellproperties'     : setGlobalVar('_editor_field',objname);
                                       setGlobalVar('_editor_cell',td);
                                       showModalDialog(_editor_url + "popups/cellprop.jsp?"+objname,
                                                       window,
                                                       "resizable: yes; help: no; status: no; scroll: no; ");
                                       td.focus();
                                       break;
// end insert lvn property pallettes
           default                   : break;
        }
// if 0 table borders and the switch to show them is on: show them
	// toggle is on : show null borders
	if (config.showborders){nullBorders(editor_obj.contentWindow.document,'show');}
		}
		}
		return;
		}
// end insert by lvn

/* ---------------------------------------------------------------------- *\
  Function    : editor_event
  Description : called everytime an editor event occurs
  Usage       : editor_event(objname, runDelay, eventName)
  Arguments   : objname - ID of textarea to replace
                runDelay: -1 = run now, no matter what
                          0  = run now, if allowed
                        1000 = run in 1 sec, if allowed at that point
\* ---------------------------------------------------------------------- */

	function editor_event(objname,runDelay) {
		var config = document.all[objname].config;
		var editor_obj  = document.all["_" +objname+  "_editor"];// html editor object
		if (runDelay == null) { runDelay = 0; }
		var editdoc;
		var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;

// catch keypress events
	if (editEvent && editEvent.keyCode) {
	    var editdoc = editor_obj.contentWindow.document;
		var ord       = editEvent.keyCode;    // ascii order of key pressed
		var ctrlKey   = editEvent.ctrlKey;
		var altKey    = editEvent.altKey;
		var shiftKey  = editEvent.shiftKey;

	if (ord == 16) { return; }  // ignore shift key by itself
	if (ord == 17) { return; }  // ignore ctrl key by itself
	if (ord == 18) { return; }  // ignore alt key by itself

	//if (ctrlKey && (ord == 122 || ord == 90)){ return;}	// catch ctrl-z (UNDO)
	//if (ctrlKey && (ord == 121 || ord == 89)){return;}	// catch ctrl-y (REDO)

	/*Default Shortcuts built-in in IE (*cannot be cancelled).
	When called, these events will fire being the cursor anywhere in the page
	CTRL+f	=	FIND (Browser/Editor)*
	CTRL+o	=	IE OPEN (Browser)*			F1		=	IE BROWSER HELP*		CTRL+i	=	IE FAVORITES (Browser)*

	Default Shortcuts built-in in IE we don't want cancelled.
	DEL		= 	DELETES SELECTION			HOME	=	GO TO TOP OF PAGE			END	=	GO TO BOTTOM OF PAGE

	CTRL+a	=	SELECT ALL (Editor)			CTRL+b	=	BOLD (Editor)				CTRL+c	=	COPY (Editor)
	CTRL+i	=	ITALICS (Editor)			CTRL+k	=	IE INSERT LINK (Editor)		CTRL+m	=	INSERT PARAGRAPH (Editor)
	CTRL+p	=	PRINT (Editor)				CTRL+u	=	UNDERLINE (Editor)			CTRL+v	=	PASTE (Editor)
	CTRL+y	=	REDO (Editor)				CTRL+z	=	UNDO (Editor)
	*/

	//When called, these events will fire only if the editor is focused, otherwise
	//the browser's default will do. (if exists)

	if (ctrlKey && (ord == 87) && editEvent.type == 'keydown') {     // Cancels CTRL+w (Close Browser Window)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	}
	//Default Shortcuts built-in in IE we want to assign a new function.
	if (ctrlKey && (ord == 82) && editEvent.type == 'keydown') {     // Cancels CTRL+r (Refresh Browser Window)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;   // And sets it for Editor's Refresh
	editdoc.execCommand("SelectAll");  editdoc.execCommand("Delete");
	}

	if (ctrlKey && (ord == 72) && editEvent.type == 'keydown') {     // Cancels CTRL-h (IE Browser History)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;   //And sets it for Editor Help
	window.showHelp(_editor_url + "popups/editor_help.htm"); return false;
	}
	//Now, we build our custom shorcuts
	if (ctrlKey && (ord == 83) && editEvent.type == 'keydown') {     // CTRL+s (Search and Replace)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	showModelessDialog(_editor_url + "popups/find.html",window, "resizable: no; help: no; status: no; scroll: no; ");return;
	}

	if (ctrlKey && (ord == 76) && editEvent.type == 'keydown') {     // CTRL+l(Bulleted List)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("InsertUnorderedList");
	}

	if (ctrlKey && (ord == 78) && editEvent.type == 'keydown') {     // CTRL+n(Numbered List)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("InsertOrderedList");
	}

	if (altKey && (ord == 83) && editEvent.type == 'keydown') { 	//ALT+s (Spell Check)
		editdoc.execCommand('copy');
		if (editdoc.selection.createRange().text != "") {CheckDocument(); return true;}
		else {alert('Nothing to spellcheck. Please select the text you want HTMLArea to check.\nTo avoid unexpected results, we recommend you to select complete paragraphs.');}
	}

	if (altKey && (ord == 88) && editEvent.type == 'keydown') {     // ALT+x (Remove Format)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("RemoveFormat");
	}

	if (altKey && (ord == 76) && editEvent.type == 'keydown') {     // ALT+l (Align Left)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("JustifyLeft");
	}

	if (altKey && (ord == 67) && editEvent.type == 'keydown') {     // ALT+c (Align Center)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("JustifyCenter");
	}

	if (altKey && (ord == 82) && editEvent.type == 'keydown') {     // ALT+r (Align Right)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("JustifyRight");
	}

	if (altKey && (ord == 74) && editEvent.type == 'keydown') {     // ALT+j (Full Justification)
	editEvent.returnValue = false;  editEvent.cancelBubble = true;
	editdoc.execCommand("JustifyFull");
	}

/* cancel ENTER key and insert <BR> instead (Example to insert code onkey event)
	if (ord == 13 && editEvent.type == 'keypress') {
		editEvent.returnValue = false;
		editor_insertHTML(objname, "<br>"); return;
	}
*/
	}

// setup timer for delayed updates (some events take time to complete)
	if (runDelay > 0) { return setTimeout(function(){ editor_event(objname); }, runDelay); }

// don't execute more than 3 times a second (eg: too soon after last execution)
	if (this.tooSoon == 1 && runDelay >= 0) { this.queue = 1; return; } // queue all but urgent events
	this.tooSoon = 1;
	setTimeout(function(){
	this.tooSoon = 0;
	if (this.queue) { editor_event(objname,-1); };
	this.queue = 0;
	}, 333);  // 1/3 second

  //editor_updateOutput(objname);
	editor_updateToolbar(objname);
	}

/* ---------------------------------------------------------------------- *\


/* ---------------------------------------------------------------------- *\
  Function    : editor_updateToolbar
  Description : update toolbar state
  Usage       :
  Arguments   : objname - ID of textarea to replace
                action  - enable, disable, or update (default action)
\* ---------------------------------------------------------------------- */

	function editor_updateToolbar(objname,action) {
		var config = document.all[objname].config;
		var editor_obj  = document.all["_" +objname+  "_editor"];

// disable or enable toolbar
		if (action == "enable" || action == "disable") {

//var tbItems = new Array('FontName','FontSize','FontStyle');
	    var tbItems = new Array('FontName','FontSize','FontStyle','FormatBlock');                           // add pulldowns

// set _editor_disabled to close the open modeless dialogs
		if (action == "disable") {setGlobalVar("_editor_field","_editor_disabled");}
		else {setGlobalVar("_editor_field",objname);}

		for (var btnName in config.btnList) { tbItems.push(config.btnList[btnName][0]); } // add buttons
		for (var idxN in tbItems) {
		var cmdID = tbItems[idxN].toLowerCase();
		var tbObj = document.all["_" +objname+ "_" +tbItems[idxN]];
		if (cmdID == "htmlmode" ||  cmdID == "showhelp" || cmdID == "about" || cmdID == "popupeditor") { continue; } // don't change these buttons
		if (tbObj == null) { continue; }
		var isBtn = (tbObj.tagName.toLowerCase() == "button") ? true : false;

		if (action == "enable")  { tbObj.disabled = false; if (isBtn) { tbObj.className = 'btn' }}
		if (action == "disable") { tbObj.disabled = true;  if (isBtn) { tbObj.className = 'btnNA' }}
		}
		return;
		}

// update toolbar state
		if (editor_obj.tagName.toLowerCase() == 'textarea') { return; }   // don't update state in textedit mode
		var editdoc = editor_obj.contentWindow.document;

// Set FontName pulldown
		var fontname_obj = document.all["_" +objname+ "_FontName"];
		if (fontname_obj) {
		var fontname = editdoc.queryCommandValue('FontName');
		if (fontname == null) { fontname_obj.value = null; }
		else {
		var found = 0;
		for (i=0; i<fontname_obj.length; i++) {
		if (fontname.toLowerCase() == fontname_obj[i].text.toLowerCase()) {
		fontname_obj.selectedIndex = i;
		found = 1;
		}}
		if (found != 1) { fontname_obj.value = null; }// for fonts not in list
		}}

// Set Formatblock pulldown inserted by lvn
		var formatblock_obj = document.all["_" +objname+ "_FormatBlock"];
		if (formatblock_obj) {
		var formatblock = editdoc.queryCommandValue('FormatBlock');
		if (formatblock == null) { formatblock_obj.value = null; }
		else {
		var found = 0;
		for (i=0; i<formatblock_obj.length; i++) {
		if (formatblock == formatblock_obj[i].value) {
		formatblock_obj.selectedIndex = i;
		found = 1;
		}}
		if (found != 1) { formatblock_obj.value = null; }// for formatblocks not in list
		}}
// end insert by lvn

// Set FontSize pulldown
		var fontsize_obj = document.all["_" +objname+ "_FontSize"];
		if (fontsize_obj) {
		var fontsize = editdoc.queryCommandValue('FontSize');
		if (fontsize == null) { fontsize_obj.value = null; }
		else {
		var found = 0;
		for (i=0; i<fontsize_obj.length; i++) {
		if (fontsize == fontsize_obj[i].value) { fontsize_obj.selectedIndex = i; found=1; }
		}
		if (found != 1) { fontsize_obj.value = null; }// for sizes not in list
		}}

// Set FontStyle pulldown
		var classname_obj = document.all["_" +objname+ "_FontStyle"];
		if (classname_obj) {
		var curRange = editdoc.selection.createRange();
	// check element and element parents for class names
	    var pElement;
	    if (curRange.length) { pElement = curRange[0]; }// control tange
	    else                 { pElement = curRange.parentElement(); }// text range
	    while (pElement && !pElement.className) { pElement = pElement.parentElement; }// keep going up
		var thisClass = pElement ? pElement.className.toLowerCase() : "";
		if (!thisClass && classname_obj.value) { classname_obj.value = null; }
		else {
		var found = 0;
		for (i=0; i<classname_obj.length; i++) {
		if (thisClass == classname_obj[i].value.toLowerCase()) {
		classname_obj.selectedIndex = i;
		found=1;
		}}
		if (found != 1) { classname_obj.value = null; }// for classes not in list
		}}

// update button states
		var IDList = Array('Bold','Italic','Underline','StrikeThrough','SubScript','SuperScript','JustifyLeft','JustifyCenter','JustifyRight','JustifyFull','JustifyNone','InsertOrderedList','InsertUnorderedList','BlockDirLTR','BlockDirRTL','MultipleSelection','LiveResize');
		for (i=0; i<IDList.length; i++) {
		var btnObj = document.all["_" +objname+ "_" +IDList[i]];
		if (btnObj == null) { continue; }
		var cmdActive = editdoc.queryCommandState( IDList[i] );

	// option is OK
		if (!cmdActive)  {
		if (btnObj.className != 'btn') { btnObj.className = 'btn'; }
		if (btnObj.disabled  != false) { btnObj.disabled = false; }
		}
	// option already applied or mixed content
		else if (cmdActive)  {
		if (btnObj.className != 'btnDown') { btnObj.className = 'btnDown';}
		if (btnObj.disabled  != false)   { btnObj.disabled = false;}
		}
			}

// inserted by lvn: table operations
	// disable table handling buttons when not in a table cell
		var table_src_element = null;
	// only works on non-control ranges
		if (editdoc.selection.type != 'Control'){
		table_src_element = editdoc.selection.createRange().parentElement();
		while (table_src_element != null && table_src_element.tagName != 'TD' && table_src_element.tagName != 'TH'){
		table_src_element = table_src_element.parentElement;
		}
			}
  // check if buttons are set in the config
		var IDList = Array('TableProperties','RowProperties','InsertRowBefore','InsertRowAfter','DeleteRow','InsertColumnBefore','InsertColumnAfter','DeleteColumn','CellProperties','InsertCellBefore','InsertCellAfter','DeleteCell','SplitCell','MergeCells','SplitRow','MergeRows');
		for (var i=0; i<IDList.length; i++) {
		var found = false;
		for (var j=0;j<config.toolbar.length;j++){
		if(config.toolbar[j]) {
		for (var k=0;k<config.toolbar[j].length;k++){
		if ( IDList[i] ==  config.toolbar[j][k]){found = true;}
		}
			}
				}
// if in cell enable buttons, else disable them
		if (found) {
		var btnObj = document.all["_" +objname+ "_" +IDList[i]];
		if (table_src_element == null) {btnObj.disabled = true; btnObj.className = 'btnNA';}
		else {btnObj.disabled = false;btnObj.className = 'btn';}
		}}
// end insert by lvn
		}

/* ---------------------------------------------------------------------- *\
  Function    : editor_updateOutput
  Description : update hidden output field with data from wysiwg
\* ---------------------------------------------------------------------- */

	function editor_updateOutput(objname) {
		var config = document.all[objname].config;
		var editor_obj  = document.all["_" +objname+  "_editor"];// html editor object
		var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;
		var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');
		var editdoc = isTextarea ? null : editor_obj.contentWindow.document;

	// get contents of edit field
		var contents;
		if (isTextarea) { contents = editor_obj.value; }
		else{ contents = editdoc.body.innerHTML; }
		contents = cleanHTML(contents);

	// check if contents has changed since the last time we ran this routine
		if (config.lastUpdateOutput && config.lastUpdateOutput == contents) { return; }
		else { config.lastUpdateOutput = contents; }
	// update hidden output field
		document.all[objname].value = contents;
		}

/* ---------------------------------------------------------------------- *\
  Function    : editor_filterOutput
  Description :
\* ---------------------------------------------------------------------- */

	function editor_filterOutput(objname) {
		editor_updateOutput(objname);
		var contents = document.all[objname].value;
		var config   = document.all[objname].config;

	// ignore blank contents
		if (contents.toLowerCase() == '<p>&nbsp;</p>') { contents = ""; }

	// filter tag - this code is run for each HTML tag matched
		var filterTag = function(tagBody,tagName,tagAttr) {
		tagName = tagName.toLowerCase();
		var closingTag = (tagBody.match(/^<\//)) ? true : false;

	// fix placeholder URLS - remove absolute paths that IE adds
		if (tagName == 'img') { tagBody = tagBody.replace(/(src\s*=\s*.)[^*]*(\*\*\*)/, "$1$2"); }
		if (tagName == 'a')   { tagBody = tagBody.replace(/(href\s*=\s*.)[^*]*(\*\*\*)/, "$1$2"); }

	// add additional tag filtering here

    // convert to vbCode
//    if      (tagName == 'b' || tagName == 'strong') {
//      if (closingTag) { tagBody = "[/b]"; } else { tagBody = "[b]"; }
//    }
//    else if (tagName == 'i' || tagName == 'em') {
//      if (closingTag) { tagBody = "[/i]"; } else { tagBody = "[i]"; }
//    }
//    else if (tagName == 'u') {
//      if (closingTag) { tagBody = "[/u]"; } else { tagBody = "[u]"; }
//    }
//    else {
//      tagBody = ""; // disallow all other tags!
//    }

	return tagBody;
	};

	// match tags and call filterTag
		RegExp.lastIndex = 0;
		var matchTag = /<\/?(\w+)((?:[^'">]*|'[^']*'|"[^"]*")*)>/g;   // this will match tags, but still doesn't handle container tags (textarea, comments, etc)
		contents = contents.replace(matchTag, filterTag);
		contents = contents.replace(/class=borders /gi, '');
		var re1 = /BORDER-RIGHT: #c0c0c0 1px dotted; BORDER-TOP: #c0c0c0 1px dotted; BORDER-LEFT: #c0c0c0 1px dotted; BORDER-BOTTOM: #c0c0c0 1px dotted/g;
		var re2 = / style=""/g;
		contents = contents.replace(re1,"");
		contents = contents.replace(re2,"");
		// remove nextlines from output (if requested)
	if (config.replaceNextlines) {
		contents = contents.replace(/\r\n/g, ' ');
		contents = contents.replace(/\n/g, ' ');
		contents = contents.replace(/\r/g, ' ');
	}

  // update output with filtered content
  document.all[objname].value = contents;
		}

// inserted by lvn
/* ---------------------------------------------------------------------- *\
  Function    : nullBorders
  Description : show 'dotted' borders for tables with border=0
  Usage       : nullBorders(doc,status);
  Arguments   : doc - document object in wich the borders must be shown
                status - show or hide
\* ---------------------------------------------------------------------- */

	function nullBorders(doc,status) {
	// show table borders
		var edit_Tables = doc.body.getElementsByTagName("TABLE");
		for (i=0; i < edit_Tables.length; i++) {
			if (edit_Tables[i].border == '' || edit_Tables[i].border == '0' ) {
				if (status == 'show' ) {edit_Tables[i].style.border = "1px dotted #C0C0C0";}
				else {edit_Tables[i].removeAttribute("style");}
		}

	edit_Rows = edit_Tables[i].rows;
		for (j=0; j < edit_Rows.length; j++) {
		edit_Cells = edit_Rows[j].cells;
		for (k=0; k < edit_Cells.length; k++) {
		if (edit_Tables[i].border == '' || edit_Tables[i].border == '0' ) {
		if (!edit_Cells[k].border || edit_Cells[k].border == '' || edit_Cells[k].border == '0' ) {
		if (status == 'show' ) {edit_Cells[k].style.border = "1px dotted #C0C0C0";}
		else {edit_Cells[k].removeAttribute("style");}
		}}
		else {
		if ( edit_Cells[k].border == '0' ) {
		if (status == 'show' ) {edit_Cells[k].style.border = "1px dotted #C0C0C0";}
		else {edit_Cells[k].removeAttribute("style");}
		}}
		}}
		}}

// end insert by lvn

/* ---------------------------------------------------------------------- *\
  Function    : editor_setmode
  Description : change mode between WYSIWYG and HTML editor
  Usage       : editor_setmode(objname, mode);
  Arguments   : objname - button id string with editor and action name
                mode      - init, textedit, or wysiwyg
\* ---------------------------------------------------------------------- */

function editor_setmode(objname, mode) {
  var config     = document.all[objname].config;
  var editor_obj = document.all["_" +objname + "_editor"];

  // wait until document is fully loaded
  if (document.readyState != 'complete') {
    setTimeout(function() { editor_setmode(objname,mode) }, 25);
    return;
  }

  // define different editors
  var TextEdit   = '<textarea ID="_' +objname + '_editor" style="width:' +editor_obj.style.width+ '; height:' +editor_obj.style.height+ '; margin-top: -1px; margin-bottom: -1px;" TABINDEX=2></textarea>';
  var RichEdit   = '<iframe ID="_' +objname+ '_editor"    style="width:' +editor_obj.style.width+ '; height:' +editor_obj.style.height+ ';" TABINDEX=2></iframe>';

 // src="' +_editor_url+ 'popups/blank.html"

  //
  // Switch to TEXTEDIT mode
  //

  if (mode == "textedit" || editor_obj.tagName.toLowerCase() == 'iframe') {
    config.mode = "textedit";
    var editdoc = editor_obj.contentWindow.document;
    // inserted by lvn
    // show table borders
    nullBorders(editdoc,'hide');
    // end insert by lvn
    var contents = cleanHTML(editdoc.body.createTextRange().htmlText);
    editor_obj.outerHTML = TextEdit;
    editor_obj = document.all["_" +objname + "_editor"];
    editor_obj.value = contents;
    editor_event(objname);
    // inserted by lvn
    if (config.showborders) {
      editor_updateToolbar(objname, "disable");
      config.showborders =  true;
    } else {
    // end insert by lvn
    editor_updateToolbar(objname, "disable");  // disable toolbar items
    // insert by lvn
    }
    // end insert by lvn

// set event handlers
    editor_obj.onkeydown   = function() { editor_event(objname); }
    editor_obj.onkeypress  = function() { editor_event(objname); }
    editor_obj.onkeyup     = function() { editor_event(objname); }
    editor_obj.onmouseup   = function() { editor_event(objname); }
    editor_obj.ondrop      = function() { editor_event(objname, 100); }     // these events fire before they occur
    editor_obj.oncut       = function() { editor_event(objname, 100); }
    editor_obj.onpaste     = function() { editor_event(objname, 100); }
    editor_obj.onblur      = function() { editor_event(objname, -1); }

    editor_updateOutput(objname);
    editor_focus(editor_obj);
  }

  //
  // Switch to WYSIWYG mode
  //

  else {
    config.mode = "wysiwyg";
    var contents = editor_obj.value;

    if (mode == 'init') { contents = document.all[objname].value; } // on init use original textarea content

    // create editor
    editor_obj.outerHTML = RichEdit;
    editor_obj = document.all["_" +objname + "_editor"];

    // get iframe document object

    // create editor contents (and default styles for editor)
    var html = "";
    html += '<html><head>\n';
    if (config.stylesheet) {
      html += '<link href="' +config.stylesheet+ '" rel="stylesheet" type="text/css">\n';
    }
    html += '<style>\n';
    html += 'body {' +config.bodyStyle+ '} \n';
	html += 'TD {' +config.TDStyle+ '} \n';
	html += 'FORM {' +config.bordersStyle+ '} \n';
	html += 'MARQUEE {' +config.marqueeStyle+ '} \n';
    for (var i in config.fontstyles) {
      var fontstyle = config.fontstyles[i];
      if (fontstyle.classStyle) {
        html += '.' +fontstyle.className+ ' {' +fontstyle.classStyle+ '}\n';
      }
    }
    html += '</style>\n'
      + '</head>\n'
      + '<body contenteditable="true" topmargin=1 leftmargin=1'

// still working on this
// updated by lvn: table actions (uncommented next line to show in popupmenu)
	 //+ ' oncontextmenu="return false;"'
      + ' oncontextmenu="parent.displayMenu(window,\'' +objname+ '\');return false;"'
      + ' onDblClick="parent.DblClick(window,\'' +objname+ '\');return false;"'
      +'>'
      + contents
      + '</body>\n'
      + '</html>\n';

    // write to editor window
    var editdoc = editor_obj.contentWindow.document;

    editdoc.open();
    editdoc.write(html);
    editdoc.close();

	editor_updateToolbar(objname, "enable");  // enable toolbar items

    // store objname under editdoc
    editdoc.objname = objname;

    // set event handlers
    editdoc.onkeydown      = function() { editor_event(objname); }
    editdoc.onkeypress     = function() { editor_event(objname); }
    editdoc.onkeyup        = function() { editor_event(objname); }
    editdoc.onmouseup      = function() { editor_event(objname); }
    editdoc.body.ondrop    = function() { editor_event(objname, 100); }     // these events fire before they occur
    editdoc.body.oncut     = function() { editor_event(objname, 100); }
    editdoc.body.onpaste   = function() { editor_event(objname, 100); }
    editdoc.body.onblur    = function() { editor_event(objname, -1); }

    // inserted by lvn
    // show table borders

	// show table borders
	if (config.showborders) {
	nullBorders(editdoc,'show');
	var btnObj = document.all["_" +objname+ "_ShowBorder"];
	if(btnObj) { btnObj.className = 'btnDown'; }
	}

    // end insert by lvn

// bring focus to editor.
	//Don't focus on page load, only on mode switch
	if (mode != 'init') {editor_focus(editor_obj);}
	// insert by lvn : check editor changes)
	else {
	if (config.checkChanges == 1) {
	var localVar = getGlobalVar("objnames");
	if (localVar == null){setGlobalVar("objnames",objname);}
	else {	localVar = localVar + ',' + objname; setGlobalVar("objnames",localVar);}

	setGlobalVar("_" +objname + "_initialText",editdoc.body.innerHTML);
	if (window.onbeforeunload == null){window.onbeforeunload = function() {discardOnExit();}}
	}
// end insert by lvn
	}
	}

  // Call update UI
  // don't update UI on page load, only on mode switch
	if (mode != 'init') {editor_event(objname);}
	}
//endfunction editor_setmode

/* ---------------------------------------------------------------------- *\
  Function    : editor_focus
  Description : bring focus to the editor
  Usage       : editor_focus(editor_obj);
  Arguments   : editor_obj - editor object
\* ---------------------------------------------------------------------- */

	function editor_focus(editor_obj) {
		// check editor mode
		if (editor_obj.tagName.toLowerCase() == 'textarea') {// textarea
			var myfunc = function() {
			    editor_obj.focus();
                try {
                    // user customizable js function
                    user_editor_focus();
                } catch(e) {
                }
            };
			setTimeout(myfunc,100); // doesn't work all the time without delay


		}

		else {// wysiwyg
			var editdoc = editor_obj.contentWindow.document; 	// get iframe editor document object
			var editorRange = editdoc.body.createTextRange();	// editor range
			var curRange    = editdoc.selection.createRange();	// selection range

		if (curRange.length == null && 			// make sure it's not a controlRange
			!editorRange.inRange(curRange)) { 	// is selection in editor range
			editorRange.collapse(); 			// move to start of range
			editorRange.select(); 				// select
			curRange = editorRange;
			}
			}


		}
//end Function editor_focus
/* ---------------------------------------------------------------------- *\
  Function    : editor_about
  Description : display "about this editor" popup
\* ---------------------------------------------------------------------- */

function editor_about(objname) {
  showModalDialog(_editor_url + "popups/about.html", window, "resizable: yes; help: no; status: no; scroll: no; ");
}

/* ---------------------------------------------------------------------- *\
  Function    : _dec_to_rgb
  Description : convert dec color value to rgb hex
  Usage       : var hex = _dec_to_rgb('65535');   // returns FFFF00
  Arguments   : value   - dec value
\* ---------------------------------------------------------------------- */

	function _dec_to_rgb(value) {
		var hex_string = "";
		for (var hexpair = 0; hexpair < 3; hexpair++) {
		var myByte = value & 0xFF;			// get low byte
		value >>= 8;						// drop low byte
		var nybble2 = myByte & 0x0F;		// get low nybble (4 bits)
		var nybble1 = (myByte >> 4) & 0x0F;	// get high nybble
		hex_string += nybble1.toString(16); // convert nybble to hex
		hex_string += nybble2.toString(16);	// convert nybble to hex
		}
		return hex_string.toUpperCase();
		}

/* ---------------------------------------------------------------------- *\
  Function    : editor_insertHTML
  Description : insert string at current cursor position in editor.
  				If two strings are specifed, surround selected text with them.
  Usage       : editor_insertHTML(objname, str1, [str2], reqSelection)
  Arguments   : objname - ID of textarea
                str1 - HTML or text to insert
                str2 - HTML or text to insert (optional argument)
                reqSelection - (1 or 0) give error if no text selected
\* ---------------------------------------------------------------------- */
	function editor_insertHTML(objname, str1,str2, reqSel) {
		var config     = document.all[objname].config;
		var editor_obj = document.all["_" +objname + "_editor"];    // editor object
		if (str1 == null) { str1 = ''; }
		if (str2 == null) { str2 = ''; }

// for non-wysiwyg capable browsers just add to end of textbox
	if (document.all[objname] && editor_obj == null) {
		document.all[objname].focus();
		document.all[objname].value = document.all[objname].value + str1 + str2;
		return;
		}

// error checking
	if (editor_obj == null) { return alert("Unable to insert HTML.  Invalid object name '" +objname+ "'."); }
		editor_focus(editor_obj);
		var tagname = editor_obj.tagName.toLowerCase();
		var sRange;

 // insertHTML for wysiwyg iframe
	if (tagname == 'iframe') {
		var editdoc = editor_obj.contentWindow.document;
		sRange  = editdoc.selection.createRange();
		var sHtml   = sRange.htmlText;

// check for control ranges
	if (sRange.length) { return alert("Unable to insert HTML.  Try highlighting content instead of selecting it."); }

// insert HTML
		var oldHandler = window.onerror;
		window.onerror = function() { alert("Unable to insert HTML for current selection."); return true; } // partial table selections cause errors
		if (sHtml.length) {									// if content selected
		if (str2) { sRange.pasteHTML(str1 +sHtml+ str2) }	// surround
		else { sRange.pasteHTML(str1); }					// overwrite
		}
		else {												// if insertion point only
		if (reqSel) { return alert("Unable to insert HTML.  You must select something first."); }
		sRange.pasteHTML(str1 + str2);                    	// insert strings
		}
		window.onerror = oldHandler;
		}

	// insertHTML for plaintext textarea
		else if (tagname == 'textarea') {
			editor_obj.focus();
			sRange  = document.selection.createRange();
			var sText   = sRange.text;

	// insert HTML
		if (sText.length) {								// if content selected
		if (str2) { sRange.text = str1 +sText+ str2; }	// surround
		else { sRange.text = str1; }					// overwrite
		}
		else {											// if insertion point only
		if (reqSel) { return alert("Unable to insert HTML.  You must select something first."); }
			sRange.text = str1 + str2;					// insert strings
		}
		}
		else { alert("Unable to insert HTML.  Unknown object tag type '" +tagname+ "'."); }

	// move to end of new content
		  sRange.collapse(false);						// move to end of range
		  sRange.select();								// re-select
	}
//end function editor_insertHTML

/* ---------------------------------------------------------------------- *\
  Function    : editor_getHTML
  Description : return HTML contents of editor (in either wywisyg or html mode)
  Usage       : var myHTML = editor_getHTML('objname');
\* ---------------------------------------------------------------------- */

	function editor_getHTML(objname) {
		var editor_obj = document.all["_" +objname + "_editor"];
		var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

		if (isTextarea) { return editor_obj.value; }
		else { return editor_obj.contentWindow.document.body.innerHTML; }
		}
//end function editor_getHTML

/* ---------------------------------------------------------------------- *\
  Function    : editor_setHTML
  Description : set HTML contents of editor (in either wywisyg or html mode)
  Usage       : editor_setHTML('objname',"<b>html</b> <u>here</u>");
\* ---------------------------------------------------------------------- */

	function editor_setHTML(objname, html) {
		var editor_obj = document.all["_" +objname + "_editor"];
		var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

		if (isTextarea) { editor_obj.value = html; }
		else { editor_obj.contentWindow.document.body.innerHTML = html; }
		}
//end function editor_setHTML

/* ---------------------------------------------------------------------- *\
  Function    : editor_appendHTML
  Description : append HTML contents to editor (in either wywisyg or html mode)
  Usage       : editor_appendHTML('objname',"<b>html</b> <u>here</u>");
\* ---------------------------------------------------------------------- */

	function editor_appendHTML(objname, html) {
		var editor_obj = document.all["_" +objname + "_editor"];
		var isTextarea = (editor_obj.tagName.toLowerCase() == 'textarea');

		if (isTextarea) { editor_obj.value += html; }
		else { editor_obj.contentWindow.document.body.innerHTML += html; }
		}
//end function editor_appendHTML
/* ---------------------------------------------------------------------- *\
  Function    : setGlobalVar
  Description : set a variable with a global scope
  Usage       : setGlobalVar(varName, value);
  Arguments   : varName - name of the global variable to set
                value - value of the global variable to set
\* ---------------------------------------------------------------------- */
function setGlobalVar(varName, value) {
   if (this.cache == null) {this.cache = new Object();}
   this.cache[varName] = value;
}
/* ---------------------------------------------------------------------- *\
  Function    : getGlobalVar
  Description : get a variable in a global scope
  Usage       : value = getGlobalVar(varName);
  Arguments   : varName - name of the global variable to get
                value - value of the global variable to get
\* ---------------------------------------------------------------------- */
function getGlobalVar(varName, value) {
   if (this.cache == null) {
     return null;
   } else {
     return this.cache[varName];
   }
}
// insert by lvn : check editor changes
/* ---------------------------------------------------------------------- *\
  Function    : discardOnExit
  Description : check if contents have been changed and ask user confirmation
                to discard changes
  Usage       : discardOnExit();
\* ---------------------------------------------------------------------- */
function discardOnExit(){
   var objNames = getGlobalVar("objnames").split(",");
   for (var i=0;i < objNames.length;i++){
       if (document.all["_" +objNames[i] + "_editor"].contentWindow.document.body.innerHTML
           != getGlobalVar("_" + objNames[i] + "_initialText")) {
          event.returnValue = "Your document has been changed. Discard changes?";
       }
   }
}
// end insert by lvn

// WME: MS-Word clean-up (begin)
/* ---------------------------------------------------------------------- *\
  Function    : MS-Word clean-up
  Description : replace textarea with wysiwyg editor
  Usage       : editor_generate("textarea_id",[height],[width]);
  Arguments   : objname - ID of textarea to replace
                w       - width of wysiwyg editor
                h       - height of wysiwyg editor
\* ---------------------------------------------------------------------- */


function cleanEmptyTag(oElem) {
	if (oElem.hasChildNodes) {
	var tmp = oElem
	for (var k = tmp.children.length; k >= 0; k--) {
	if (tmp.children[k] != null) {cleanEmptyTag(tmp.children[k]);}
	}
	}

	var oAttribs = oElem.attributes;
	if (oAttribs != null) {
	for (var j = oAttribs.length - 1; j >=0; j--) {
	var oAttrib = oAttribs[j];
	if (oAttrib.nodeValue != null) {
			oAttribs.removeNamedItem('style')
			oAttribs.removeNamedItem('title')
			oAttribs.removeNamedItem('class')
	}
	}}

	if (oElem.style) oElem.style.cssText = '';
	if (oElem.innerHTML == '' || oElem.innerHTML == '&nbsp;')
	if (oElem.outerHTML != '<BR>') oElem.outerHTML = '';
	}

function cleanTable(oElem) {
	oElem.style.cssText = '';
	var oAttribs = oElem.attributes;
	if (oAttribs != null) {for (var j = oAttribs.length - 1; j >=0; j--) {var oAttrib = oAttribs[j];
	if (oAttrib.nodeValue != null) {
			oAttribs.removeNamedItem('class')
			oAttribs.removeNamedItem('style')
			}
		}
	}
	var oTR = oElem.rows;
	if (oTR != null) {for (var r = oTR.length - 1; r >= 0; r--) {oTR[r].style.cssText = '';}}
	var oTD = oElem.cells;
	if (oTD != null) {for (var t = oTD.length - 1; t >= 0; t--) {oTD[t].style.cssText = '';}}
}

function CheckDocument()
{
oShell= new
ActiveXObject("WScript.Shell");
oShell.SendKeys( "^c" ); // copy
oWord = new ActiveXObject("Word.Application");
oWord.Documents.Add();
oWord.Selection.Paste();
oWord.ActiveDocument.CheckSpelling();
oWord.Selection.WholeStory();
oWord.Selection.Copy();
oWord.ActiveDocument.Close(0);
oWord.Quit();
var nRet= oShell.Popup( "HTMLArea finished checking your document.\nApply changes? Click OK to replace the corrected words.",0,"Spell Check Complete",33 );
if ( nRet == 1 ) {oShell.SendKeys( "^v" );}// paste
}

function cleanHTML(unclean)
{
		//unclean = unclean.replace(/\t/g, " ");
	//The next line removes the footer inserted with the Save function
                unclean = unclean.replace(/<!--Footer-->((.|\s)+?)+/gi, "");
		unclean = unclean.replace(/<(\/)?strong>/ig, '<$1B> '); //replaces <STRONG> with <B>
		unclean = unclean.replace(/<(\/)?em>/ig, '<$1I> '); //replaces <EM> with <I>
		unclean = unclean.replace(/&nbsp;/gi, " ");
		unclean = unclean.replace(/[ ]+/g, " ");
		unclean = unclean.replace(/<\/TR>/gi, '\n<\/TR>');
		unclean = unclean.replace(/<\/FORM>/gi, '\n<\/FORM>');
		unclean = unclean.replace(/<\/TBODY>/gi, '\n<\/TBODY>');
		unclean = unclean.replace(/<\/TABLE>/gi, '\n<\/TABLE>\n');
		unclean = unclean.replace(/<BR[^>]*>/gi, "\n<BR>");
		unclean = unclean.replace(/<\/UL>/gi, '\n<\/UL>');
		unclean = unclean.replace(/<\/OL>/gi, '\n<\/OL>\r');
		unclean = unclean.replace(/<\/DL>/gi, '\n<\/DL>');
		unclean = unclean.replace(/<\/P>/gi, '');
		unclean = unclean.replace(/<SELECT/gi, '\n<SELECT');
		unclean = unclean.replace(/<OPTION/gi, '\n<OPTION');
		unclean = unclean.replace(/<INPUT/gi, '\n<INPUT');
		//unclean = unclean.replace(/<!--\s+/gi, '<!--\r');

	//And Finally, Remove Absolute references to the editor's URL (important when using #anchorlinks)
	//This works only if you are using one HTMLArea per editor.js
	//Otherwise, comment it out and remove it on server-side

                unclean = unclean.replace(/http\:\/\/fslactivities\.sd61\.bc\.ca\/ezHTMLarea\/index2\.html/gi, '');
                unclean = unclean.replace(/http\:\/\/fslactivities\.sd61\.bc\.ca\/ezHTMLarea\/index3\.html/gi, '');


		return unclean;
}

function myclean(editdoc) {
var 	oTags = editdoc.all.tags("SPAN"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {oTags[i].outerHTML = oTags[i].innerHTML;}}
		oTags = editdoc.all.tags("DIV"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {oTags[i].outerHTML = oTags[i].innerHTML;}}
		oTags = editdoc.all.tags("FONT"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {oTags[i].outerHTML = oTags[i].innerHTML;}}
		oTags = editdoc.all.tags("P"); 			if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("B"); 			if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("I"); 			if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("U"); 			if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H1"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H2"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H3"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H4"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H5"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("H6"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("BLOCKQUOTE"); if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("OL"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("UL"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanEmptyTag(oTags[i]);}}
		oTags = editdoc.all.tags("TABLE"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanTable(oTags[i]);}}
		oTags = editdoc.all.tags("TR"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanTable(oTags[i]);}}
		oTags = editdoc.all.tags("TD"); 		if (oTags != null) {for (var i = oTags.length - 1; i >= 0; i--) {cleanTable(oTags[i]);}}
}


/* ---------------------------------------------------------------------- *\
  Function    :displayMenu();
  Description : Context Menus
  Usage       : Displays Menus on right click
\* ---------------------------------------------------------------------- */

var oPopup = window.createPopup();

function displayMenu(editorWin,objname){
var parentWin = window;
var src_object = document.all["_" +objname + "_editor"];
var src_element = src_object.contentWindow.event.srcElement;
var table_object      = document.all["_" +objname + "_editor"];
var table_src_element = table_object.contentWindow.event.srcElement;
//alert(table_src_element.tagName);

var editor_obj  = document.all["_" +objname+  "_editor"];       // html editor object
var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;
var editdoc = editor_obj.contentWindow.document;

// get contents of edit field
var contents = editdoc.body.innerHTML;
var oPopupBody = oPopup.document.body;
var obj=document.getElementById(objname);
var screenX = editorWin.event.screenX;

//Decide which Context Menu to show. Order of these routines is important. Don't alter it.
//Routine 1
if (src_element.tagName == 'IMG'){oPopupBody.innerHTML = Image_CMenu.innerHTML;
oPopup.show(screenX, 300, 150, 134, objname.body);
}
//Routine 2
else if (table_src_element.tagName == 'TABLE'){
var menu = document.getElementById(objname + 'Tables_CMenu');
oPopupBody.innerHTML = menu.innerHTML;
oPopup.show(screenX, 300, 150, 134, objname.body);
}
//Routine 3
else if (src_element.tagName == 'BODY' && contents ==""){
var menu = document.getElementById(objname + 'InsertOptions_CMenu');
oPopupBody.innerHTML = menu.innerHTML;
oPopup.show(screenX, 300, 150, 225, objname.body);
}
//Routine 4
else if (editdoc.selection.createRange().text != ""){
var menu = document.getElementById(objname + 'Format_CMenu');
oPopupBody.innerHTML = menu.innerHTML;
oPopup.show(screenX, 300, 150, 160, objname.body);
}
//Routine 5
else if (table_src_element.tagName == 'TD'){
var menu = document.getElementById(objname + 'TableOperations_CMenu');
oPopupBody.innerHTML = menu.innerHTML;
oPopup.show(screenX, 300, 170, 380, objname.body);
}
//Last Routine
else if(contents !==""){
var menu = document.getElementById(objname + 'Contents_CMenu');
oPopupBody.innerHTML = menu.innerHTML;
oPopup.show(screenX, 300, 150, 260, objname.body);
}
//Builds the Context Menu popup
	oPopupBody.style.backgroundColor = "threedface";
    oPopupBody.style.border = "outset 2px";
    oPopupBody.style.fontFamily = "MS sans serif";
    oPopupBody.style.fontSize = "11px";
	oPopupBody.style.cursor = 'default';
	oPopupBody.onclick = oPopup.hide;
	oPopup.document.body.oncontextmenu = oPopup.hide;
	}

//Adds Context Menu Items to the popup
function WriteMenuItems(objname){

//Setup Variables for Re-usable elements.
var CMDIdCode= 	'"unselectable="on" onselectstart="return false;" oncontextmenu="return false;" onclick="parent.editor_action(this.id);" STYLE="background: threedface; height:18px; padding:2 0 0 2; color:menutext" onMouseOver= "this.style.background = \'highlight\'\; this.style.color=\'highlighttext\'" onMouseOut= "this.style.background = \'threedface\'\; this.style.color=\'menutext\'"';
var AboutCode= 	'"unselectable="on" onselectstart="return false;" oncontextmenu="return false;" onclick="parent.goContext()";" STYLE="background: threedface; height:18px; padding:2 0 0 2; color:menutext" onMouseOver= "this.style.background = \'highlight\'\; this.style.color=\'highlighttext\'" onMouseOut= "this.style.background = \'threedface\'\; this.style.color=\'menutext\'"';
var separator= 	'<hr width="98%">';

var bold=		'<div id="_'		+ objname +	'_bold" '			+CMDIdCode+'>&nbsp;&nbsp;Bold</div> ';
var italic=		'<div id="_'		+ objname +	'_italic" '			+CMDIdCode+'>&nbsp;&nbsp;Italics</div> ';
var underline=	'<div id="_'		+ objname +	'_underline" '		+CMDIdCode+'>&nbsp;&nbsp;Underline</div> ';
var cut=		'<div id="_'		+ objname +	'_cut" '			+CMDIdCode+'>&nbsp;&nbsp;Cut</div> ';
var copy=		'<div id="_'		+ objname +	'_copy" '			+CMDIdCode+'>&nbsp;&nbsp;Copy</div> ';
var deletion=	'<div id="_'		+ objname +	'_delete" '			+CMDIdCode+'>&nbsp;&nbsp;Delete Selection</div> ';
var remove=		'<div id="_'		+ objname +	'_removeformat" '	+CMDIdCode+'>&nbsp;&nbsp;Remove Format</div> ';
var image=		'<div id="_'		+ objname +	'_insertimage"'		+CMDIdCode+'>&nbsp;&nbsp;Insert or Edit an Image</div> ';
var paste=		'<div id="_'		+ objname +	'_paste" '			+CMDIdCode+'>&nbsp;&nbsp;Paste</div> ';
var selectall=	'<div id="_'		+ objname +	'_SelectAll" '		+CMDIdCode+'>&nbsp;&nbsp;Select All</div> ';
var clearall=	'<div id="_'		+ objname +	'_refresh" '		+CMDIdCode+'>&nbsp;&nbsp;Clear All</div> ';
var find=   	'<div id="_'		+ objname +	'_find" '			+CMDIdCode+'>&nbsp;&nbsp;Find and Replace</div> ';
var insertform= '<div id="_'		+ objname +	'_inputform" '		+CMDIdCode+'>&nbsp;&nbsp;Insert a Form</div> ';
var inserttable='<div id="_'		+ objname +	'_inserttable" '	+CMDIdCode+'>&nbsp;&nbsp;Insert a Table</div> ';
var inserttemplate='<div id="_'		+ objname +	'_template" '		+CMDIdCode+'>&nbsp;&nbsp;Select a Template</div> ';
var helpfile=   '<div id="_'		+ objname +	'_showhelp" '		+CMDIdCode+'>&nbsp;&nbsp;Help</div> ';
var about=   	'<div id="_'		+ objname +	'_about" '			+AboutCode+'>&nbsp;&nbsp;About This Editor</div> ';
var openfile=	'<div id="_'		+ objname +	'_openfile" '		+CMDIdCode+'>&nbsp;&nbsp;Open a Web File</div> ';
var openword=	'<div id="_'		+ objname +	'_openword" '		+CMDIdCode+'>&nbsp;&nbsp;Open a MS-Word File</div> ';
var print=		'<div id="_'		+ objname +	'_print" '			+CMDIdCode+'>&nbsp;&nbsp;Print</div> ';
var preview=	'<div id="_'		+ objname +	'_preview" '		+CMDIdCode+'>&nbsp;&nbsp;Preview</div> ';
var hyperlink=	'<div id="_'		+ objname +	'_insertlink" '		+CMDIdCode+'>&nbsp;&nbsp;Insert or Edit Hyperlink</div> ';
var removelink=	'<div id="_'		+ objname +	'_unlink" '			+CMDIdCode+'>&nbsp;&nbsp;Remove Hyperlink</div> ';
var changecase=	'<div id="_'		+ objname +	'_changecase" '		+CMDIdCode+'>&nbsp;&nbsp;Change Case</div> ';
var spellcheck=	'<div id="_'		+ objname +	'_spell" '			+CMDIdCode+'>&nbsp;&nbsp;Spell Check</div> ';

var insertcaption=		'<div id="_'	+ objname +	'_createcaption" '		+CMDIdCode+'>&nbsp;&nbsp;Insert Caption</div> ';
var insertrowbefore=	'<div id="_'	+ objname +	'_insertrowbefore" '	+CMDIdCode+'>&nbsp;&nbsp;Insert Row Before</div> ';
var insertrowafter=		'<div id="_'	+ objname +	'_insertrowafter" '		+CMDIdCode+'>&nbsp;&nbsp;Insert Row After</div> ';
var deleterow=			'<div id="_'	+ objname +	'_deleterow" '			+CMDIdCode+'>&nbsp;&nbsp;Delete Row</div> ';
var insertcolumnbefore=	'<div id="_'	+ objname +	'_insertcolumnbefore" '	+CMDIdCode+'>&nbsp;&nbsp;Insert Column Before</div> ';
var insertcolumnafter=	'<div id="_'	+ objname +	'_insertcolumnafter" '	+CMDIdCode+'>&nbsp;&nbsp;Insert Column After</div> ';
var deletecolumn=		'<div id="_'	+ objname +	'_deletecolumn" '		+CMDIdCode+'>&nbsp;&nbsp;Delete Column</div> ';
var insertcellbefore=	'<div id="_'	+ objname +	'_insertcellbefore" '	+CMDIdCode+'>&nbsp;&nbsp;Insert Cell Before</div> ';
var insertcellafter=	'<div id="_'	+ objname +	'_insertcellafter" '	+CMDIdCode+'>&nbsp;&nbsp;Insert Cell After</div> ';
var deletecell=			'<div id="_'	+ objname +	'_deletecell" '			+CMDIdCode+'>&nbsp;&nbsp;Delete Cell</div> ';
var tableproperties=	'<div id="_'	+ objname +	'_tableproperties" '	+CMDIdCode+'>&nbsp;&nbsp;Table Properties</div> ';
var rowproperties=		'<div id="_'	+ objname +	'_rowproperties" '		+CMDIdCode+'>&nbsp;&nbsp;Row Properties</div> ';
var cellproperties=		'<div id="_'	+ objname +	'_cellproperties" '		+CMDIdCode+'>&nbsp;&nbsp;Cell Properties</div> ';
var splitrow=			'<div id="_'	+ objname +	'_splitrow" '			+CMDIdCode+'>&nbsp;&nbsp;Split Row</div> ';
//var mergerow=			'<div id="_'	+ objname +	'_mergerows" '			+CMDIdCode+'>&nbsp;&nbsp;Merge Row</div> ';
var splitcell=			'<div id="_'	+ objname +	'_splitcell" '			+CMDIdCode+'>&nbsp;&nbsp;Split Cell</div> ';
var mergecell=			'<div id="_'	+ objname +	'_mergecells" '			+CMDIdCode+'>&nbsp;&nbsp;Merge Cell</div> ';
var showborder=			'<div id="_'	+ objname +	'_showborder" '			+CMDIdCode+'>&nbsp;&nbsp;Show/Hide Table Borders</div> ';
var upload=				'<div id="_'	+ objname +	'_upload" '				+CMDIdCode+'>&nbsp;&nbsp;Upload a New Image</div> ';

//InsertOptions_CMenu
/*
document.write('<div id=InsertOptions_CMenu style="display:none;"> ');
document.write(openfile);
document.write(openword);
document.write(paste);
document.write(separator);
document.write(image);
document.write(upload);
document.write(inserttable);
document.write(insertform);
document.write(inserttemplate);
document.write(separator);
document.write(helpfile);
document.write(about);
document.write('</div>');
*/

//Contents_CMenu
/*
document.write('<div id=Contents_CMenu style="display:none;"> ');
document.write(selectall);
document.write(clearall);
document.write(paste);
document.write(find);
document.write(separator);
document.write(image);
document.write(inserttable);
document.write(insertform);
document.write(separator);
document.write(print);
document.write(preview);
document.write(separator);
document.write(helpfile);
document.write(about);
document.write('</div>');
*/

//Image_CMenu
/*
document.write('<div id=Image_CMenu style="display:none;"> ');
document.write(image);
document.write(upload);
document.write(cut);
document.write(copy);
document.write(separator);
document.write(hyperlink);
document.write('</div>');
*/

//Format_CMenu
document.write('<div id=' + objname + 'Format_CMenu style="display:none;"> ');
document.write(bold);
document.write(italic);
document.write(underline);
document.write(separator);
document.write(copy);
document.write(cut);
document.write(deletion);
document.write(remove);
//document.write(separator);
//document.write(hyperlink);
//document.write(removelink);
//document.write(changecase);
//document.write(spellcheck);
document.write('</div>');

//Tables_CMenu
document.write('<div id=' + objname + 'TableOperations_CMenu style="display:none;"> ');
document.write(inserttable);
//document.write(insertcaption);
document.write(showborder);
document.write(separator);
document.write(tableproperties);
document.write(rowproperties);
document.write(cellproperties);
document.write(separator);
document.write(insertrowbefore);
document.write(insertrowafter);
document.write(deleterow);
//document.write(splitrow);
//document.write(mergerow);
document.write(separator);
document.write(insertcolumnbefore);
document.write(insertcolumnafter);
document.write(deletecolumn);
document.write(separator);
document.write(insertcellbefore);
document.write(insertcellafter);
document.write(deletecell);
document.write(splitcell);
document.write(mergecell);
//document.write(separator);
//document.write(paste);
//document.write(image);
//document.write(insertform);
document.write('</div>');

//Tables_CMenu
document.write('<div id=' + objname + 'Tables_CMenu style="display:none;"> ');
document.write(cut);
document.write(copy);
document.write(deletion);
document.write('</div>');

}



var oPopup = window.createPopup()

function goContext()
{
  var oPopupBody = oPopup.document.body;

  oPopupBody.innerHTML = oContext.innerHTML;
  oPopup.show(195, 200, 404, 174, document.body);
  //document.body.onmousedown = oPopup.hide;
}

function stoperror(){
return true
}
window.onerror=stoperror

function DblClick(editorWin,objname){
var parentWin = window;
var src_object = document.all["_" +objname + "_editor"];
var src_element = src_object.contentWindow.event.srcElement;
var table_object = document.all["_" +objname + "_editor"];
var table_src_element = table_object.contentWindow.event.srcElement;

var editor_obj = document.all["_" +objname+ "_editor"]; // html editor object
var editEvent = editor_obj.contentWindow ? editor_obj.contentWindow.event : event;
var editdoc = editor_obj.contentWindow.document;
// get contents of edit field
var contents = editdoc.body.innerHTML;

if (src_element.tagName == 'IMG'){
editor_action('_' + objname + '_' + 'insertimage');
}

else if (src_element.tagName == 'HR'){ // insert horizontal rule
editor_action('_' + objname + '_' + 'line');
}

else if (table_src_element.tagName == 'TD'){
displayMenu(editorWin,objname);
}

else if (table_src_element.tagName == 'TABLE'){
alert('Table operations not allowed here\n To display the Table Properties Dialogs, double click or right click inside a cell.');
}

}


/* ---------------------------------------------------------------------- */

function custom_image(objName, url) {
  var arr = showModalDialog( url,
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:60em; dialogHeight:40em" );
  if (arr != null) {
    if (!arr[0]) {
      var html = '<img src="' + arr + '" border="0">';
      editor_insertHTML(objName, html);
    }
    else {
      var html = '<table align="left"><tr><td style="font-size:7pt; text-align:center; padding:5; margin:5"><img src="' + arr[0] + '" border="0"><br>' + arr[1] + '</td></tr></table>';
      editor_insertHTML(objName, html);
    }
  }
}

function custom_hyperlink(objName, url) {
  var arr = showModalDialog( url,
                             "",
                             "font-family:Verdana; font-size:12; dialogWidth:60em; dialogHeight:40em" );
  if (arr != null) {
/*
    var open = '<a href="' + arr + '">';
    var close = '</a>';
    editor_insertHTML(objName, open, close, 1);
*/
    var editor_obj;
    var editdoc;
    try {
      editor_obj = document.all["_" +objName + "_editor"];    // editor object
      editdoc = editor_obj.contentWindow.document;
    }
    catch(e) {
      objName = 'editor';
      editor_obj = document.all["_" +objName + "_editor"];    // popup editor object
      editdoc = editor_obj.contentWindow.document;
    }
    editor_focus(editor_obj);
    editdoc.execCommand("Unlink", 0);
    editdoc.execCommand("CreateLink", 0, arr);
  }
}

