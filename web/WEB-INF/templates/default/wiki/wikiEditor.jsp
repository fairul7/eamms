<%@ include file="/common/header.jsp" %>

<c:set var="textbox" value="${widget}"/>
<c:set var="height" value="${widget.rows*15}"/>
<c:if test="${height < 300}">
    <c:set var="height" value="300"/>
</c:if>

<c:if test="${textbox.invalid}">
  !<div style="border:1 solid #de123e">
</c:if>
                                                                                                                                                      </script>
<script language="javascript" type="text/javascript" charset="{@encoding}">

 //
 // **********************************************************************************
 //
 // Utility functions
//
// ***********************************************************************************
//

//
// Function trim(s)
// takes string and returns it with leading and trialing spaces removed
// I think IE has this built into javascript but put this in for a measure of cross browser support.
//

function trim(s)
{
	reg=/^ *(\S.+[\n]*.+\S) *$/;
	s=s.replace(reg,"$1");
	return s;
}

//
// function SetEditFocus()
// returns focus to textarea
//

function SetEditFocus()
{
    var form = document.forms['<c:out value="${textbox.rootForm.absoluteName}"/>'];
    var field = form['<c:out value="${textbox.absoluteName}"/>'];
	field.focus();
}


//
// Function setCaretAtEnd (field)
// Function to move claret to end of form / textarea
// Added to page load along with SetEditFocus
//

function setCaretAtEnd (field) {
  if (field.createTextRange) {
    var r = field.createTextRange();
    r.moveStart('character', field.value.length);
    r.collapse();
    r.select();
  }
}

//
// function setCaretAtEndOfRange (range)
// sets claret at end of textrange, ie after formatting selected text
//

function setCaretAtEndOfRange (range) {
 {
    var r = range;
    r.collapse(false);
    r.select();
  }
}



//
// *****************************************************************
//
// functions to insert wiki markup.
//
// *****************************************************************

//
// Function format_sel(s,e)
// Inserts Wiki markup (ie s(tart) and e(nd) around selected text
// Used for buttons
// leading and trailing spaces removed
//

function format_sel(s,e) {
  	var str = document.selection.createRange().text;
  	//added to remove white space at start or end
  	var sPre = "", sPost = ""
  	if (str.charAt(0) == " "){sPre = " "}
  	if (str.charAt(str.length - 1) == " "){sPost = " "}
  	str = trim(str);
  	SetEditFocus();
  	var sel = document.selection.createRange();
	sel.text = sPre + unescape(s) + str + unescape(e) + sPost ;
	setCaretAtEndOfRange(sel);
	return;
}

//
// function insert_code(v)
//
// Inserts wiki code at selected point
//

function insert_code(v) {
 	 var str = document.selection.createRange().text;
 	 SetEditFocus()
	 var sel = document.selection.createRange();
	 //sel.text =  v + " ";
	sel.text =  unescape(v);
	setCaretAtEndOfRange(sel);
 	 return;
}



//
//
// Function head(what)
// Inserts heading markup from 'Headings' dropdown
// Text is surrounded by markup value from dropdown.
//
//

function head(what)
{
	if (what.selectedIndex != 1);
	{
	var str = document.selection.createRange().text;
	var v = what.value;
	SetEditFocus();
 	var sel = document.selection.createRange();
  	sel.text = v + " " + str + " " + v ;
  	setCaretAtEndOfRange(sel);
	what.selectedIndex = 0;
	return;
	}
}

//
// Function dd_insertcode(what)
// Inserts macro code from drop down.
//

function dd_insertcode(what)
{
	if (what.selectedIndex != 1);
	{
	var str = document.selection.createRange().text;
  	var sPre = "", sPost = ""
  	if (str.charAt(0) == " "){sPre = " "}
  	if (str.charAt(str.length - 1) == " "){sPost = " "}
	//added to remove white space at start or end
  	str = trim(str);
	var v = what.value;
	pos = v.indexOf(",");
	var len = v.length;
	s = v.substring(0,pos);
	e = v.substring(pos + 1,len);
	SetEditFocus();
 	var sel = document.selection.createRange();
	sel.text = sPre + unescape(s) + str +  unescape(e) + sPost;
	setCaretAtEndOfRange(sel);
	what.selectedIndex = 0;
	return;
	}
}


//
// Function insert_rss ()
// Inserts RSS feed as entered in a prompt
//

function insert_rss () {
  	var str = document.selection.createRange().text;
  	SetEditFocus();
  	var my_link = prompt("Enter URL:","http://");
  	if (my_link != null)    {
   		var sel = document.selection.createRange();
		var v =  '<Syndicate(\"';
		v+= my_link;
		v+= '\",120) /> '
		sel.text = unescape(v);
		setCaretAtEndOfRange(sel);
		 	 			}
  			return;
			}


//
// function insert_link ()
// Inserts url link as entered in a prompt
//

function insert_link () {
  	var str = document.selection.createRange().text;
  	SetEditFocus();
  	var my_link = prompt("Enter URL:","http://");
  	if (my_link != null)    {
   		var sel = document.selection.createRange();
		sel.text = unescape( my_link);
		setCaretAtEndOfRange(sel);
		 	 			}
  			return;
			}

//
// Function insert_interwiki(what)
// Inserts interwiki code from dropdown
//


function insert_interwiki(what) {
	if (what.selectedIndex != 1);
{
	var v = what.value;
  	SetEditFocus();
  	var sel = document.selection.createRange();
  	sel.text =  v;
	what.selectedIndex = 0;
	setCaretAtEndOfRange(sel);
	return;
	}
}


// **********************************************************************
// capitalise etc
// still writing...
//
// See caps_dd() that is called by the drop down select menu
// What I want is an equivalent that runs off a button and that cycles through the 3 options. This is what caps() tries (but fails) to do.

function caps(what) {
 counts=0;
 getCount()
 switch (counts)
{
	case 0:
		capitalise();
	break
	case 1:
		lowercase();
	break
	case 2:
		uppercase();
	break
	default:
		return;
}

}

 function getCount(){
  counts = counts + 1;
  counts = counts % 3;
  return counts;}

  //
  // called by drop down
  //

  function caps_dd(what) {

  if (what.selectedIndex != 0)
{
	var v = what.value;
	what.selectedIndex = 0;
	switch (v)
{
	case "caps":
		capitalise();
	break;
	case "lowercase":
		lowercase();
	break;
	case "uppercase":
		uppercase();
	break;
	default:
		return;
}
		return;
}
}

//
// Function capitalise()
// from various places, function to turn selected text into leading capitals.
//

function capitalise() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
  	 if ( !str.length ) return;
	str=str.toLowerCase();
	str=str.replace(/\b\w+\b/g, function(word) {
	return word.substring(0,1).toUpperCase()+ word.substring(1);
});
sel.text=str
return;
}

//
// Function lowercase()
// function to turn selected text into lowercase.
//

function lowercase() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
	str=str.toLowerCase();
	sel.text=str;
	return;
}

//
// Function uppercase()
// function to turn selected text into lowercase.
//

function uppercase() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
	str=str.toUpperCase();
	sel.text=str;
	return;
}


//
//  By Michael Meyers
//

function indent_line( s){
  var new_s="";
  if ( s.match(/^\s+$/) ) return s;
  if ( s.match(/^\s*:|^\s*\*\s|^\s*[0-9]+\.|^\s*[a-z]+\.|^\s*;/) ){
     new_s = "  " + s;
  }else{
     new_s = "  : " + s;
  }

  return new_s;
}

function dd_indent()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = indent_line( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += indent_line( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += indent_line( str )  ;
         }else{
           new_str += "\n";
         }


         sel.text =  new_str;

}

function unindent_line( s){
  var new_s="";
  if ( s.match(/^(\s+)/)  ){
     len = RegExp.$1.length ;

     if ( len == 1 ) {
       if( s.match(/^ |^\t/) ) {
         new_s = new_s = s.substring( 1, s.length );
       }else{
         new_s = s;
       }

     }else if ( len == 2 ){
       new_s = s.substring( 1, s.length );
     }else{
       new_s = s.substring( 2, s.length );
     }
  }else{
     new_s = s;
  }

  return new_s;
}

function dd_unindent()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = unindent_line( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += unindent_line( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += unindent_line( str )  ;
         }else{
           new_str += "\n";
         }
         sel.text =  new_str;
}


// re-clicking button cycles through styles 1. --> a. -->  1.  ect...
function number_list( s){
  var new_s="";
  if ( s.match(/^(\s+):/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)\*\s/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)([0-9]+)\./) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "a" + s.substring( len + RegExp.$2.length , s.length );
  }else if( s.match(/^(\s+)([a-z]+)\./) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1" + s.substring( len + RegExp.$2.length , s.length );
  }else if( s.match(/^(\s+);/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else{
     new_s = "  1. " + s;
  }

  return new_s;
}


function dd_numberlist()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = number_list( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

	    var counting=1;
         do {

           line = str.substring( 0, pos );
             
           //new_str += number_list( line );
           new_str +="<br>"+""+counting+".";
           new_str +=line ;           
           str = str.substring( pos+1 , str.length );   
       
           pos = str.indexOf( "\n" );

		  counting +=1;

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           
           new_str = number_list( str ) ;
         }else{
           new_str += "<br>";
         }      
       

         sel.text =  new_str;
}

function bullet_list( s){
  var new_s="";
  if ( s.match(/^(\s+):/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)\*\s/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)([0-9]+\.#?[0-9]*)/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len +  RegExp.$2.length  , s.length );
  }else if( s.match(/^(\s+)([a-z]+\.#?[0-9]*)/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + RegExp.$2.length, s.length );
  }else if( s.match(/^(\s+);/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else{
     new_s = "*" + s;
  }

  return new_s;
}

function dd_bulletlist()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = bullet_list( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";

         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += bullet_list( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += bullet_list( str )  ;
         }else{
           new_str += "\n";
         }

         sel.text =  new_str;
}



// Mouse over functions to animate the buttons

function mouseover(el) {
  el.className = "raised";
}

function mouseout(el) {
  el.className = "button";
}

function mousedown(el) {
  el.className = "pressed";
}

function mouseup(el) {
  el.className = "raised";
}

</script>

<div style="width:80%; border:solid 1px silver">
<div id="toolbar">

 <img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="insert_code('\n%3CBR%3E\n');"
 src="/common/wikieditor/images/UI_code.gif"
 width="20"
 height="21"
 align="middle"
 TABINDEX="1"
 alt="Insert new line">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="format_sel('\'\'\'','\'\'\'');"
 src="/common/wikieditor/images/UI_bold.gif"
 align="middle"
 ACCESSKEY="B"
 TABINDEX="1"
 alt="Make selection bold">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="format_sel('\'\'','\'\'');"
 src="/common/wikieditor/images/UI_italic.gif"
 align="middle"
 ACCESSKEY="I"
 TABINDEX="1"
 alt="Make selection italic">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="format_sel('<u>','</u>');"
 src="/common/wikieditor/images/UI_underline.gif"
 align="middle"
 ACCESSKEY="U"
 TABINDEX="1"
 alt="Underline selection">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="insert_code('\n----\n');"
 src="/common/wikieditor/images/UI_line.gif"
 align="middle"
 TABINDEX="1"
 alt="Insert a horizontal rule">
</img>



<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="dd_numberlist();"
 src="/common/wikieditor/images/UI_numberlist.gif"
 width="24" height="25"
 align="middle"
 TABINDEX="1"
 alt="Numbering">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="dd_bulletlist();"
 src="/common/wikieditor/images/UI_bulletlist.gif"
 width="24" height="25"
 align="middle"
 TABINDEX="1"
 alt="Bullets">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="dd_unindent();"
 src="/common/wikieditor/images/UI_outdent.gif"
 width="24" height="25"
 align="middle"
 TABINDEX="1"
 alt="Decrease indent">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="dd_indent();"
 src="/common/wikieditor/images/UI_indent.gif"
 width="24" height="25"
 align="middle"
 TABINDEX="1"
 alt="Increase indent">
 </img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="capitalise();"
 src="/common/wikieditor/images/UI_font_size.gif"
 width="40" height="22"
 align="middle"
 TABINDEX="1"
 alt="Make selection initial capitals">
</img>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="caps();"
 src="/common/wikieditor/images/UI_font_size.gif"
 width="40" height="22"
 align="middle"
 TABINDEX="1"
 alt="Alternative make selection initial capitals">
</img>


<!--
 <img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="locatorWin(); return false;"
 src="/common/wikieditor/images//UI_find.gif"
 align="middle"
 TABINDEX="1"
 ACCESSKEY="F"
 alt="Search for text - slow to open">
</img>
-->

<!--img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="HighlightAll('f.text');"
 src="/common/wikieditor/images//./ow/plugins/wysiwygicons/UI_spell.gif"
 align="middle"
 TABINDEX="1"
 alt="Spell check selection - not yet coded ">
</img-->


<img class="spacer" scr="seperator.gif" width="6" height="22"  align="middle" ></img >

<select class="select" name="headings" align="middle" onchange="dd_insertcode(this);">
<option value=" ">Format</option>
<option value="[[ , ]]">InterWiki</option>
<option value="= , =">Heading 1</option>
<option value="== , ==">Heading 2</option>
<option value="=== ,  ===">Heading 3</option>
<option value="==== ,  ====">Heading 4</option>
<option value="===== , =====">Heading 5</option>
<option value="<sub>,</sub>">Subscript</option>
<option value="<sup>,</sup>">Superscript</option>
<option value="<s>,</s>">Strikethru</option>
<!--<option value="!!,!!">Big &amp; bold</option> 
<option value="<Class(box)>,<Class />">Box</option>
<option value="<Class(Quote)>,<Class />">Quote</option> -->
</select>


<select class="select"  name="case" align="middle"  TABINDEX="1" onChange="caps_dd(this);">
<option value="">Case</option>
<option value="caps">LeadCaps</option>
<option value="lowercase">lowercase</option>
<option value="uppercase">UPPERCASE</option>
</select>

<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="insert_link();"
 src="/common/wikieditor/images/UI_link.gif"
 width="24" height="25"
 align="middle"
 alt="click to add a link">


<img class="button"
 onmouseover="mouseover(this);"
 onmouseout="mouseout(this);"
 onmousedown="mousedown(this);"
 onmouseup="mouseup(this);"
 onclick="insert_rss();"
 src="/common/wikieditor/images/UI_xml.gif"
 width="30" height="14"
 align="middle"
TABINDEX="1"
 alt="Add a RSS feed">
</img>
</div>
<c:choose>
	<c:when test="${!textbox.invalid}" >
		<textarea cols="80" rows="20" 
		name="<c:out value="${textbox.absoluteName}"/>" 
		id="<c:out value="${textbox.absoluteName}"/>" ><c:out value="${textbox.value}"/></textarea>
	</c:when>
	<c:otherwise>
 		<textarea cols="80" rows="20" style="border:1px solid #de123e" 
 		name="<c:out value="${textbox.absoluteName}"/>"
 		id="<c:out value="${textbox.absoluteName}"/>" ><c:out value="${textbox.value}"/></textarea> 
	</c:otherwise>
</c:choose>
<c:forEach var="child" items="${textbox.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>
