<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Iterator" %>
<%!
  private String getThreadDump() {
      Map stackMap = Thread.getAllStackTraces();
      Set keySet = stackMap.keySet();
      StringBuffer sb = new StringBuffer();
      String lineSeparator = System.getProperty("line.separator");
      SimpleDateFormat sdf = new SimpleDateFormat();
      String now = sdf.format(Calendar.getInstance().getTime());

      sb.append("Thread dump generated at " + now);
      sb.append(lineSeparator);
      sb.append("Java Runtime Name: " + System.getProperty("java.runtime.name"));
      sb.append(lineSeparator);
      sb.append("Java Runtime Version: " + System.getProperty("java.runtime.version"));
      sb.append(lineSeparator);
      sb.append("Java Version: " + System.getProperty("java.version"));
      sb.append(lineSeparator);
      sb.append("VM Name: " + System.getProperty("java.vm.name"));
      sb.append(lineSeparator);
      sb.append("VM Info: " + System.getProperty("java.vm.info"));
      sb.append(lineSeparator);
      sb.append("VM Version: " + System.getProperty("java.vm.version"));
      sb.append(lineSeparator);
      sb.append("OS Architecture: " + System.getProperty("os.arch"));
      sb.append(lineSeparator);
      sb.append("OS Name: " + System.getProperty("os.name"));
      sb.append(lineSeparator);
      sb.append("OS Version: " + System.getProperty("os.version"));
      Runtime rt = Runtime.getRuntime();
      int freeMb = (int) (rt.freeMemory() / 1024 / 1024);
      int totalMb = (int) (rt.totalMemory() / 1024 / 1024);
      int maxMb = (int) (rt.maxMemory() / 1024 / 1024);
      sb.append(lineSeparator);
      sb.append("Memory Status. Free: " + freeMb + "MB. Total: " + totalMb + "MB. Max: " + maxMb + "MB.");
      sb.append(lineSeparator);
      sb.append(lineSeparator);

      for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
        Thread thread = (Thread) iterator.next();
        sb.append("\"" + thread.getName() + "\"");
        if (thread.isDaemon()) {
          sb.append(" daemon");
        }
        sb.append(" in group=\"" + thread.getThreadGroup().getName() + "\"");
        sb.append(" priority=" + thread.getPriority());
        sb.append(" id=" + thread.getId());
        sb.append(" state=" + thread.getState());
        sb.append(lineSeparator);
        StackTraceElement[] traceArray = (StackTraceElement[]) stackMap.get(thread);
        for (int i = 0; i < traceArray.length; i++) {
          StackTraceElement element = traceArray[i];
          sb.append(element.toString());
          sb.append(lineSeparator);
        }

        sb.append("----- End: " + thread.getName());
        sb.append(lineSeparator);
        sb.append(lineSeparator);
      }

      sb.append("--- End: Thread dump generated at " + now);
      sb.append(lineSeparator);
      sb.append(lineSeparator);

      return sb.toString();
    }
%>
<html>
  <head><title>Thread Dump</title></head>
  <body>
  <pre>
    <%= getThreadDump() %>
  </pre>
  </body>
</html>