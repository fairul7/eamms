<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"><html style="width: 420px; height: 290px;" id="dlgFind"><head><!-- saved from url=(0060)http://webstationone.com/test/htmlarea/popups/tableprop.html --><!-- based on insimage.dlg --><title>Table Properties</title>
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
var table = opener.getGlobalVar('_editor_table');
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
     if (align[i].value.toLowerCase() == table.align.toLowerCase()) {
        align.selectedIndex = i;
     }
  }
  bgColor.innerText = ' ' + table.bgColor.toUpperCase();
  bgColor.style.backgroundColor = table.bgColor;
  border.value = table.border;
  borderColor.innerText = ' ' + table.borderColor.toUpperCase();
  borderColor.style.backgroundColor = table.borderColor;
  borderColorDark.innerText = ' ' + table.borderColorDark.toUpperCase();
  borderColorDark.style.backgroundColor = table.borderColorDark;
  borderColorLight.innerText = ' ' + table.borderColorLight.toUpperCase();
  borderColorLight.style.backgroundColor = table.borderColorLight;
  if (table.height == null){
    height.value = '';
    heightExt.value = '';
  } else { 
     if (table.height.search(/%/) < 0) {
        height.value = table.height;
        heightExt.value = '';
     } else {
        height.value = table.height.split('%')[0]; 
        heightExt.value = '%';
     }
  }   
  if (table.width == null){
    width.value = '';
    widthExt.value = '';
  } else { 
     if (table.width.search(/%/) < 0) {
        widthExt.value = '';
        width.value = table.width;
     } else {
        width.value = table.width.split('%')[0]; 
        widthExt.value = '%';
     }
  } 
  id.value = table.id;
  title.value = table.title;
  if (table.background == null) {
     background.value = '';
  } else {
     background.value = table.background;
  }
  cellPadding.value = table.cellPadding;
  cellSpacing.value = table.cellSpacing;
  if (table.caption) {
     caption.value = table.caption.innerText;
  }
  rules.selectedIndex = 0;
  for (var i=0; i< rules.length; i++) {
     if (rules[i].value.toLowerCase() == table.rules.toLowerCase()) {
        rules.selectedIndex = i;
     }
  }
  frame.selectedIndex = 0;
  for (var i=0; i< frame.length; i++) {
     if (frame[i].value.toLowerCase() == table.frame.toLowerCase()) {
        frame.selectedIndex = i;
     }
  }
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
      alert("<fmt:message key='richTextBox.error.height'/>");
      heigth.focus();
      return;
   }
   if (height.value == ''){heightExt.value = '';}
   if (!_isValidNumber(width.value)) {
      alert("<fmt:message key='richTextBox.error.width'/>");
      width.focus();
      return;
   }
   if (!_isValidNumber(border.value)) {
      alert("<fmt:message key='richTextBox.error.border'/>");
      border.focus();
      return;
   }
   if (width.value == ''){widthExt.value = '';}
   table.align = align[align.selectedIndex].value;
   table.bgColor = bgColor.style.backgroundColor;
   table.borderColor = borderColor.style.backgroundColor;
   table.borderColorDark = borderColorDark.style.backgroundColor;
   table.borderColorLight = borderColorLight.style.backgroundColor;
   table.height = height.value + heightExt.value;
   table.id = id.value ;
   if (title.value == ''){
      if (table.getAttribute('title') != null){table.removeAttribute('title');}
   } else {table.title = title.value;}
   if (width.value == ''){
      if (table.getAttribute('width') != null){table.removeAttribute('width');}
   } else {table.width = width.value + widthExt.value;}
   if (background.value == '' ){
      if (table.getAttribute('background') != null){table.removeAttribute('background');}
   } else {table.background = background.value;}
   if (!_isValidNumber(cellPadding.value)) {
      alert("<fmt:message key='richTextBox.error.cellPadding'/>");
      cellPadding.focus();
      return;
   }
   if (cellPadding.value == ''){
      if (table.getAttribute('cellPadding') != null){table.removeAttribute('cellPadding');}
   } else {table.cellPadding = cellPadding.value;}
   if (!_isValidNumber(cellSpacing.value)) {
      alert("<fmt:message key='richTextBox.error.cellSpacing'/>");
      cellSpacing.focus();
      return;
   }
   if (cellSpacing.value == ''){
      if (table.getAttribute('cellSpacing') != null){table.removeAttribute('cellSpacing');}
   } else {table.cellSpacing = cellSpacing.value;}
   if (table.caption) {
      if (caption.value == '') {
         table.deleteCaption();
      } else {
         table.caption.innerText = caption.value ;
      }
   } else {
      if (caption.value != '') {
         table.createCaption();
         table.caption.innerText = caption.value;
      }
   }
   table.frame = frame[frame.selectedIndex].value;
   table.rules = rules[rules.selectedIndex].value;
   if (border.value == '' || border.value == 0){
      if (table.getAttribute('border') != null){table.removeAttribute('border');}
   } else {table.border = border.value;}
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
<div style="left: 19.4em; width: 10em; top: 1em; height: 1.9em;" id="divWidth"><fmt:message key='richTextBox.width'/>: </div><input tabindex="30" style="left: 22.7em; width: 3em; top: 0.8em; height: 1.9em;" id="width"> <select name="widthExt" tabindex="40" style="left: 25.8em; width: 6em; top: 0.8em; height: 1.9em;"><option value=""><fmt:message key='richTextBox.pixels'/></option><option selected="selected" value="%"><fmt:message key='richTextBox.percent'/></option></select>
<div style="left: 32em; width: 10em; top: 1em; height: 1.9em;" id="divBorder"><fmt:message key='richTextBox.border'/>: </div><input tabindex="50" onfocus="select()" style="left: 35.5em; width: 1.5em; top: 0.8em; height: 1.9em;" id="border">
<div style="left: 1em; width: 10em; top: 3em; height: 1.21em;" id="divId"><fmt:message key='richTextBox.id'/>:
</div><input tabindex="60" onfocus="select()" style="left: 10em; width: 27.04em; top: 2.8em; height: 1.9em;" id="id"> 
<div style="left: 1em; width: 10em; top: 5em; height: 1.21em;" id="divCaption"><fmt:message key='richTextBox.caption'/>: </div><input tabindex="70" onfocus="select()" style="left: 10em; width: 27.04em; top: 4.8em; height: 1.9em;" id="caption">
<div style="left: 1em; width: 31em; top: 7em; height: 1.21em;" id="divTitle"><fmt:message key='richTextBox.tooltip'/>: </div><input tabindex="80" onfocus="select()" style="left: 10em; width: 27.04em; top: 6.8em; height: 1.9em;" id="title">
<div style="left: 1em; width: 10em; top: 9em; height: 1.21em;" id="divBackground"><fmt:message key='richTextBox.backgroundImage'/>:
</div><input tabindex="90" onfocus="select()" style="left: 10em; width: 27.04em; top: 8.8em; height: 1.9em;" id="background"> 
<div style="left: 1em; width: 10em; top: 11em; height: 1.21em;" id="divFrame"><fmt:message key='richTextBox.frame'/>: </div>
<select tabindex="100" style="left: 10em; width: 27.04em; top: 10.8em; height: 1.9em;" id="frame">
<option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option>
<option value="above"><fmt:message key='richTextBox.borderTopSide'/></option>
<option value="below"><fmt:message key='richTextBox.borderBottomSide'/></option>
<option value="border"><fmt:message key='richTextBox.borderAllSides'/></option>
<option value="box"><fmt:message key='richTextBox.borderAllSides'/></option>
<option value="hsides"><fmt:message key='richTextBox.borderTopBottomSides'/></option>
<option value="lhs"><fmt:message key='richTextBox.borderLeftSide'/></option>
<option value="rhs"><fmt:message key='richTextBox.borderRightSide'/></option>
<option value="vsides"><fmt:message key='richTextBox.borderLeftRightSides'/></option>
</select> 
<div style="left: 1em; width: 10em; top: 13em; height: 1.21em;" id="divRules"><fmt:message key='richTextBox.rules'/>: </div>
<select tabindex="110" style="left: 10em; width: 27.04em; top: 12.8em; height: 1.9em;" id="rules">
<option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option>
<option value="all"><fmt:message key='richTextBox.borderAllRowsColumns'/></option>
<option value="cols"><fmt:message key='richTextBox.borderBetweenAllColumns'/></option>
<option value="none"><fmt:message key='richTextBox.borderInteriorNone'/></option>
<option value="rows"><fmt:message key='richTextBox.borderBetweenAllRows'/></option>
</select> 
<div style="left: 1em; width: 10em; top: 15em; height: 1.21em;" id="divAlign"><fmt:message key='richTextBox.horizontalAlign'/>:
</div><select name="align" tabindex="120" style="left: 10em; width: 6em; top: 14.8em; height: 1.9em;"><option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option><option value="center"><fmt:message key='richTextBox.center'/></option><option value="left"><fmt:message key='richTextBox.left'/></option><option value="right"><fmt:message key='richTextBox.right'/></option></select>
<div style="left: 17em; width: 10em; top: 15em; height: 1.9em;" id="divcellPadding"><fmt:message key='richTextBox.cellPadding'/>:
</div><input tabindex="130" onfocus="select()" style="left: 23.5em; width: 3em; top: 14.8em; height: 1.9em;" id="cellPadding"> 
<div style="left: 27.5em; width: 10em; top: 15em; height: 1.9em;" id="divcellSpacing"><fmt:message key='richTextBox.cellSpacing'/>:
</div><input tabindex="140" onfocus="select()" style="left: 34em; width: 3em; top: 14.8em; height: 1.9em;" id="cellSpacing"> 
<div style="left: 1em; width: 10em; top: 17em; height: 1.21em;" id="divBgColor"><fmt:message key='richTextBox.backgroundColour'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 17em; height: 1.21em;" id="bgColor"></div>
<div style="left: 20em; width: 10em; top: 17em; height: 1.21em;" id="divBorderColor"><fmt:message key='richTextBox.borderColor'/>: </div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 17em; height: 1.21em;" id="borderColor"></div>
<div style="left: 1em; width: 10em; top: 19em; height: 1.21em;" id="divBorderColorDark"><fmt:message key='richTextBox.borderColorDark'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 19em; height: 1.21em;" id="borderColorDark"></div>
<div style="left: 20em; width: 10em; top: 19em; height: 1.21em;" id="divBorderCOlorLight"><fmt:message key='richTextBox.borderColorLight'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 19em; height: 1.21em;" id="borderColorLight"></div><button type="submit" tabindex="150" style="left: 10em; width: 8.5em; top: 21em; height: 2.2em;" id="btnOK"><fmt:message key='richTextBox.ok'/></button><button type="reset" tabindex="160" onclick="window.close();" style="left: 19.9em; width: 8.5em; top: 21em; height: 2.2em;" id="btnCancel"><fmt:message key='richTextBox.cancel'/></button> </body></html>