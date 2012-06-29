<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"><html style="width: 420px; height: 250px;" id="dlgFind"><head><!-- saved from url=(0059)http://webstationone.com/test/htmlarea/popups/cellprop.html --><!-- based on insimage.dlg --><title><fmt:message key='richTextBox.cellProperties'/></title>
<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<meta content="Yes" http-equiv="MSThemeCompatible">
<style>HTML {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
BODY {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
BUTTON {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
DIV {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
INPUT {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
SELECT {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
FIELDSET {
	FONT-SIZE: 8pt; FONT-FAMILY: MS Shell Dlg; POSITION: absolute
}
</style>

<script>
opener = window.dialogArguments;
var editor_obj = opener.document.all["_" + opener.getGlobalVar('_editor_field') + "_editor"];
var td = opener.getGlobalVar('_editor_cell');
</script>

<script defer="defer">
function _CloseOnEsc() {
  if (event.keyCode == 27) { window.close(); return; }
}
window.onerror = HandleError
function HandleError(message, url, line) {
  var std = "An error has occurred in this dialog." + "\n\n"
  + "Error: " + line + "\n" + message;
  alert(std);
  window.close();
  return true;
}
function Init() {
  // event handlers  
  document.body.onkeypress = _CloseOnEsc;
  btnOK.onclick = new Function("btnOKClick()");
  align.selectedIndex = 0;
  for (var i=0; i< align.length; i++) {
     if (align[i].value.toLowerCase() == td.align.toLowerCase()) {
        align.selectedIndex = i;
     }
  }
  vAlign.selectedIndex = 0;
  for (var i=0; i< vAlign.length; i++) {
     if (vAlign[i].value.toLowerCase() == td.vAlign.toLowerCase()) {
        vAlign.selectedIndex = i;
     }
  }
  //if (td.bgColor == ''){
     bgColor.innerText = ' ' + td.bgColor.toUpperCase();
  //}
  bgColor.style.backgroundColor = td.bgColor;
  //if (td.borderColor == ''){
     borderColor.innerText = ' ' + td.borderColor.toUpperCase();
  //}
  borderColor.style.backgroundColor = td.borderColor;
  //if (td.borderColorDark == ''){
     borderColorDark.innerText = ' ' + td.borderColorDark.toUpperCase();
  //}
  borderColorDark.style.backgroundColor = td.borderColorDark;
  //if (td.borderColorLight == ''){
     borderColorLight.innerText = ' ' + td.borderColorLight.toUpperCase();
  //}
  borderColorLight.style.backgroundColor = td.borderColorLight;
  if (td.height == null){
    height.value = '';
    heightExt.value = '';
  } else { 
     if (td.height.search(/%/) < 0) {
        height.value = td.height;
        heightExt.value = '';
     } else {
        height.value = td.height.split('%')[0]; 
        heightExt.value = '%';
     }
  }   
  if (td.width == null){
    width.value = '';
    widthExt.value = '';
  } else { 
     if (td.width.search(/%/) < 0) {
        widthExt.value = '';
        width.value = td.width;
     } else {
        width.value = td.width.split('%')[0]; 
        widthExt.value = '%';
     }
  } 
  id.value = td.id;
  title.value = td.title;
  if (td.background == null) {
     background.value = '';
  } else {
     background.value = td.background;
  }
  colSpan.value = td.colSpan;
  rowSpan.value = td.rowSpan;
  noWrap.checked = td.noWrap;
}
function radioValue(radioobject){
   for (var i=0; i < radioobject.length; i++) {
      if (direction[i].checked) {
         return radioobject[i].value;
      }
   }
} 
function _isValidNumber(txtBox) {
  if (txtBox == '') {
     return true;
  } else {  
     var val = parseInt(txtBox);
     if (isNaN(val) || val < 0 || val > 9999) { return false; }
     return true;
  }
}
function btnOKClick() {
   if (!_isValidNumber(height.value)) {
      alert("You must specify a number between 0 and 9999 for height!");
      heigth.focus();
      return;
   }
   if (height.value == ''){heightExt.value = '';}
   if (!_isValidNumber(width.value)) {
      alert("You must specify a number between 0 and 9999 for width!");
      width.focus();
      return;
   }
   if (width.value == ''){widthExt.value = '';}
   td.align = align[align.selectedIndex].value;
   td.vAlign = vAlign[vAlign.selectedIndex].value;
   td.bgColor = bgColor.style.backgroundColor;
   td.borderColor = borderColor.style.backgroundColor;
   td.borderColorDark = borderColorDark.style.backgroundColor;
   td.borderColorLight = borderColorLight.style.backgroundColor;
   td.height = height.value + heightExt.value;
   td.id = id.value ;
   if (title.value == ''){
      if (td.getAttribute('title') != null){td.removeAttribute('title');}
   } else {td.title = title.value;}
   if (width.value == ''){
      if (td.getAttribute('width') != null){td.removeAttribute('width');}
   } else {td.width = width.value + widthExt.value;}
   if (background.value == '' ){
      if (td.getAttribute('background') != null){td.removeAttribute('background');}
   } else {td.background = background.value;}
   if (!_isValidNumber(colSpan.value)) {
      alert("You must specify a number between 0 and 9999 for colSpan!");
      colSpan.focus();
      return;
   }
   if (colSpan.value == '' || colSpan.value == '0' || colSpan.value == '1'){
      if (td.getAttribute('colSpan') != null){td.removeAttribute('colSpan');}
   } else {td.colSpan = colSpan.value;}
   if (!_isValidNumber(rowSpan.value)) {
      alert("You must specify a number between 0 and 9999 for rowSpan!");
      rowSpan.focus();
      return;
   }
   if (rowSpan.value == '' || rowSpan.value == '0' || rowSpan.value == '1'){
      if (td.getAttribute('rowSpan') != null){td.removeAttribute('rowSpan');}
   } else {td.rowSpan = rowSpan.value;}
   td.noWrap = noWrap.checked;
   window.close();
   return true;
}
function setColors (but) {
   but.style.borderStyle = 'inset';
   var color=showModalDialog('set_color.html',but.style.backgroundColor,'resizable:no;help:no;status:no;scroll:no;');
   if (color == '' || color == null){
     but.innerText = '';
     but.style.backgroundColor = '';
   } else {
     but.innerText = ' #' + color.toUpperCase();
     but.style.backgroundColor =  color;
   }
   but.style.borderStyle = 'outset';
   return;
}
</script>

<meta name="GENERATOR" content="MSHTML 6.00.2800.1141"></head>

<body onload="Init()" scroll="no" style="background: buttonface none repeat scroll 0%; -moz-background-clip: initial; -moz-background-origin: initial; -moz-background-inline-policy: initial; color: windowtext;" id="bdy">
<div style="left: 1em; width: 10em; top: 1em; height: 1.9em;" id="divHeight"><fmt:message key='richTextBox.height'/>: </div><input tabindex="10" style="left: 10em; width: 3em; top: 0.8em; height: 1.9em;" id="height">
<select name="heightExt" tabindex="20" style="left: 13.2em; width: 6em; top: 0.8em; height: 1.9em;"><option value=""><fmt:message key='richTextBox.pixels'/></option><option selected="selected" value="%"><fmt:message key='richTextBox.percent'/></option></select>
<div style="left: 20em; width: 10em; top: 1em; height: 1.9em;" id="divWidth"><fmt:message key='richTextBox.width'/>:
</div><input tabindex="30" style="left: 27.9em; width: 3em; top: 0.8em; height: 1.9em;" id="width"> <select name="widthExt" tabindex="40" style="left: 31.1em; width: 6em; top: 0.8em; height: 1.9em;"><option value=""><fmt:message key='richTextBox.pixels'/></option><option selected="selected" value="%"><fmt:message key='richTextBox.percent'/></option></select>
<div style="left: 1em; width: 10em; top: 3em; height: 1.21em;" id="divId"><fmt:message key='richTextBox.id'/>:
</div><input tabindex="50" onfocus="select()" style="left: 10em; width: 27.04em; top: 2.8em; height: 1.9em;" id="id"> 
<div style="left: 1em; width: 31em; top: 5em; height: 1.21em;" id="divTitle"><fmt:message key='richTextBox.tooltip'/>: </div><input tabindex="60" onfocus="select()" style="left: 10em; width: 27.04em; top: 4.8em; height: 1.9em;" id="title">
<div style="left: 1em; width: 10em; top: 7em; height: 1.21em;" id="divBackground"><fmt:message key='richTextBox.backgroundImage'/>:
</div><input tabindex="70" onfocus="select()" style="left: 10em; width: 27.04em; top: 6.8em; height: 1.9em;" id="background"> 
<div style="left: 1em; width: 10em; top: 9em; height: 1.21em;" id="divAlign"><fmt:message key='richTextBox.horizontalAlign'/>:
</div><select name="align" tabindex="80" style="left: 10em; width: 6em; top: 8.8em; height: 1.9em;"><option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option><option value="center"><fmt:message key='richTextBox.center'/></option><option value="left"><fmt:message key='richTextBox.left'/></option><option value="right"><fmt:message key='richTextBox.right'/></option></select>
<div style="left: 20em; width: 10em; top: 9em; height: 1.21em;" id="divvAlign"><fmt:message key='richTextBox.verticalAlign'/>:
</div><select name="vAlign" tabindex="90" style="left: 31em; width: 6em; top: 8.8em; height: 1.9em;"><option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option><option value="middle"><fmt:message key='richTextBox.middle'/></option><option value="baseline"><fmt:message key='richTextBox.baseline'/></option><option value="bottom"><fmt:message key='richTextBox.bottom'/></option><option value="top"><fmt:message key='richTextBox.top'/></option></select>
<div style="left: 1em; width: 10em; top: 11em; height: 1.21em;" id="divBgColor"><fmt:message key='richTextBox.backgroundColour'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 11em; height: 1.21em;" id="bgColor"></div>
<div style="left: 20em; width: 10em; top: 11em; height: 1.21em;" id="divBorderColor"><fmt:message key='richTextBox.borderColor'/>: </div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 11em; height: 1.21em;" id="borderColor"></div>
<div style="left: 1em; width: 10em; top: 13em; height: 1.21em;" id="divBorderColorDark"><fmt:message key='richTextBox.borderColorDark'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 13em; height: 1.21em;" id="borderColorDark"></div>
<div style="left: 20em; width: 10em; top: 13em; height: 1.21em;" id="divBorderCOlorLight"><fmt:message key='richTextBox.borderColorLight'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 13em; height: 1.21em;" id="borderColorLight"></div>
<div style="left: 1em; width: 10em; top: 15em; height: 1.9em;" id="divColSpan"><fmt:message key='richTextBox.colspan'/>: </div><input tabindex="100" onfocus="select()" style="left: 10em; width: 3em; top: 14.8em; height: 1.9em;" id="colSpan">
<div style="left: 17em; width: 10em; top: 15em; height: 1.9em;" id="divrowSpan"><fmt:message key='richTextBox.rowspan'/>: </div><input tabindex="110" onfocus="select()" style="left: 23em; width: 3em; top: 14.8em; height: 1.9em;" id="rowSpan">
<div style="left: 30em; width: 10em; top: 15em; height: 1.9em;" id="divrowSpan"><fmt:message key='richTextBox.no'/>
<fmt:message key='richTextBox.wrap'/>: </div><input type="checkbox" tabindex="120" onfocus="select()" style="left: 35.6em; width: 1.5em; top: 15em; height: 1.5em;" id="noWrap"><button type="submit" tabindex="130" style="left: 10em; width: 8.5em; top: 17em; height: 2.2em;" id="btnOK"><fmt:message key='richTextBox.ok'/></button><button type="reset" tabindex="140" onclick="window.close();" style="left: 19.9em; width: 8.5em; top: 17em; height: 2.2em;" id="btnCancel"><fmt:message key='richTextBox.cancel'/></button> </body></html>