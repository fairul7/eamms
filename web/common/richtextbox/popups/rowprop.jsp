<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"><html style="width: 420px; height: 200px;" id="dlgFind"><head><!-- saved from url=(0058)http://webstationone.com/test/htmlarea/popups/rowprop.html --><!-- based on insimage.dlg --><title><fmt:message key='richTextBox.rowProperties'/></title>
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
var tr = opener.getGlobalVar('_editor_row');
</script>

<script defer="defer">
function _CloseOnEsc() {
  if (event.keyCode == 27) { window.close(); return; }
}
window.onerror = HandleError
function HandleError(message, url, line) {
  var str = "An error has occurred in this dialog." + "\n\n"
  + "Error: " + line + "\n" + message;
  alert(str);
  window.close();
  return true;
}
function Init() {
  // event handlers  
  document.body.onkeypress = _CloseOnEsc;
  btnOK.onclick = new Function("btnOKClick()");
  align.selectedIndex = 0;
  for (var i=0; i< align.length; i++) {
     if (align[i].value.toLowerCase() == tr.align.toLowerCase()) {
        align.selectedIndex = i;
     }
  }
  vAlign.selectedIndex = 0;
  for (var i=0; i< vAlign.length; i++) {
     if (vAlign[i].value.toLowerCase() == tr.vAlign.toLowerCase()) {
        vAlign.selectedIndex = i;
     }
  }
  //if (tr.bgColor == ''){
     bgColor.innerText = ' ' + tr.bgColor.toUpperCase();
  //}
  bgColor.style.backgroundColor = tr.bgColor;
  //if (tr.borderColor == ''){
     borderColor.innerText = ' ' + tr.borderColor.toUpperCase();
  //}
  borderColor.style.backgroundColor = tr.borderColor;
  //if (tr.borderColorDark == ''){
     borderColorDark.innerText = ' ' + tr.borderColorDark.toUpperCase();
  //}
  borderColorDark.style.backgroundColor = tr.borderColorDark;
  //if (tr.borderColorLight == ''){
     borderColorLight.innerText = ' ' + tr.borderColorLight.toUpperCase();
  //}
  borderColorLight.style.backgroundColor = tr.borderColorLight;
  if (tr.height == null){
    height.value = '';
    heightExt.value = '';
  } else { 
     if (tr.height.search(/%/) < 0) {
        height.value = tr.height;
        heightExt.value = '';
     } else {
        height.value = tr.height.split('%')[0]; 
        heightExt.value = '%';
     }
  }   
  if (tr.width == null){
    width.value = '';
    widthExt.value = '';
  } else { 
     if (tr.width.search(/%/) < 0) {
        widthExt.value = '';
        width.value = tr.width;
     } else {
        width.value = tr.width.split('%')[0]; 
        widthExt.value = '%';
     }
  } 
  id.value = tr.id;
  title.value = tr.title;
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
   if (width.value == ''){widthExt.value = '';}
   tr.align = align[align.selectedIndex].value;
   tr.vAlign = vAlign[vAlign.selectedIndex].value;
   tr.bgColor = bgColor.style.backgroundColor;
   tr.borderColor = borderColor.style.backgroundColor;
   tr.borderColorDark = borderColorDark.style.backgroundColor;
   tr.borderColorLight = borderColorLight.style.backgroundColor;
//   tr.bgColor = bgColor.value;
//   tr.borderColor = borderColor.value;
//   tr.borderColorDark = borderColorDark.value;
//   tr.borderColorLight = borderColorLight.value;
   tr.height = height.value + heightExt.value;
   tr.id = id.value ;
   if (title.value == ''){
      if (tr.getAttribute('title') != null){tr.removeAttribute('title');}
   } else {tr.title = title.value;}
   if (width.value == ''){
      if (tr.getAttribute('width') != null){tr.removeAttribute('width');}
   } else {tr.width = width.value + widthExt.value;}

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
<div style="left: 1em; width: 10em; top: 7em; height: 1.21em;" id="divAlign"><fmt:message key='richTextBox.horizontalAlign'/>:
</div><select name="align" tabindex="70" style="left: 10em; width: 6em; top: 6.8em; height: 1.9em;"><option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option><option value="center"><fmt:message key='richTextBox.center'/></option><option value="left"><fmt:message key='richTextBox.left'/></option><option value="right"><fmt:message key='richTextBox.right'/></option></select>
<div style="left: 20em; width: 10em; top: 7em; height: 1.21em;" id="divvAlign"><fmt:message key='richTextBox.verticalAlign'/>:
</div><select name="vAlign" tabindex="80" style="left: 31em; width: 6em; top: 6.8em; height: 1.9em;"><option selected="selected" value=""><fmt:message key='richTextBox.notSet'/></option><option value="middle"><fmt:message key='richTextBox.middle'/></option><option value="baseline"><fmt:message key='richTextBox.baseline'/></option><option value="bottom"><fmt:message key='richTextBox.bottom'/></option><option value="top"><fmt:message key='richTextBox.top'/></option></select>
<div style="left: 1em; width: 10em; top: 9em; height: 1.21em;" id="divBgColor"><fmt:message key='richTextBox.backgroundColour'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 9em; height: 1.21em;" id="bgColor"></div>
<div style="left: 20em; width: 10em; top: 9em; height: 1.21em;" id="divBorderColor"><fmt:message key='richTextBox.borderColor'/>: </div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 9em; height: 1.21em;" id="borderColor"></div>
<div style="left: 1em; width: 10em; top: 11em; height: 1.21em;" id="divBorderColorDark"><fmt:message key='richTextBox.borderColorDark'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 10em; width: 6em; top: 11em; height: 1.21em;" id="borderColorDark"></div>
<div style="left: 20em; width: 10em; top: 11em; height: 1.21em;" id="divBorderCOlorLight"><fmt:message key='richTextBox.borderColorLight'/>:
</div>
<div onclick="setColors(this);" style="border: thin outset ; left: 31em; width: 6em; top: 11em; height: 1.21em;" id="borderColorLight"></div><button type="submit" tabindex="90" style="left: 10em; width: 8.5em; top: 13em; height: 2.2em;" id="btnOK"><fmt:message key='richTextBox.ok'/></button><button type="reset" tabindex="100" onclick="window.close();" style="left: 19.9em; width: 8.5em; top: 13em; height: 2.2em;" id="btnCancel"><fmt:message key='richTextBox.cancel'/></button> </body></html>