    function getTreeCookieVal (offset) {
      var endstr = document.cookie.indexOf (";", offset);
      if (endstr == -1)
        endstr = document.cookie.length;
      return unescape(document.cookie.substring(offset, endstr));
    }

    function getTreeCookie (name) {
      var uid = "";
      name = uid + name;
      var arg = name + "=";  
      var alen = arg.length;  
      var clen = document.cookie.length;  
      var i = 0;  
      while (i < clen) {    
        var j = i + alen;    
        if (document.cookie.substring(i, j) == arg)
          return getTreeCookieVal (j);
        i = document.cookie.indexOf(" ", i) + 1;    
        if (i == 0) break;   
      }  
      return null;
    }

    function treeSave(b) {
      var uid = "";
      var cookieStr = "";
      cookieStr = uid + b + "=" + document.getElementById(b).style.display + "; path=/;";
      document.cookie = cookieStr;

      var sessionId = getTreeCookie("JSESSIONID");
      var sessionCookie = "JSESSIONID=" + sessionId + "; path=/;";
      document.cookie = sessionCookie;
    }

    function treeLoad(b) {
      var c = getTreeCookie(b);
//      if (c == "block" || c == "" || c == null) {
      if (c == "block") {
        treeShow(b);
      }
      else
        treeHide(b);
    }

    function treeHide(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("icon_" + b);
      if (node != null)
          node.style.display = "none";
      if (icon != null)
          icon.innerHTML = "<img src='images/plus.JPG' border='0'/>";
    }

    function treeShow(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("icon_" + b);

      if (node != null)
          node.style.display = "block";
      if (icon != null)
          icon.innerHTML = "<img src='images/minus.JPG' border='0'/>";
    }

    function treeToggle(id) {
        var node = document.getElementById(id);
        if (node.style.display == 'block') {
            treeHide(id);
        }
        else {
            treeShow(id);
        }
        treeSave(id);
    }
