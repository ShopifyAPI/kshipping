
<%--
  Copyright (c) 2002 by Phil Hanna
  All rights reserved.
  
  You may study, use, modify, and distribute this
  software for any purpose provided that this
  copyright notice appears in all copies.
  
  This software is provided without warranty
  either expressed or implied.
--%>
<%@ page
      errorPage="ErrorPage.jsp"
      import="java.io.*"
      import="java.util.*"
%>

<%
   Enumeration enames;
   Map map;
   String title;

   // Print the session itself
   Map sessMap = new LinkedHashMap();
   sessMap.put("id",session.getId() );
   
   long lastAccessedTime = session.getLastAccessedTime();   // : returns the time session was last accessed
   int maxInactiveInterval = session.getMaxInactiveInterval();  // : returns the timeout period.
   long createTime = session.getCreationTime();

   sessMap.put("max Inactive Interval (s)", Integer.toString(maxInactiveInterval));
   
   Date d = new Date(createTime);
   sessMap.put("create Time", d.toString());
   
   d = new Date(lastAccessedTime);
   sessMap.put("last access time", d.toString());
   
  
   out.println(createTable(sessMap, "Session"));
   
   // Print the request headers

   map = new TreeMap();
   enames = request.getHeaderNames();
   while (enames.hasMoreElements()) {
      String name = (String) enames.nextElement();
      String value = request.getHeader(name);
      map.put(name, value);
   }
   out.println(createTable(map, "Request Headers"));

   // Print the session attributes

   map = new TreeMap();
   enames = session.getAttributeNames();
   while (enames.hasMoreElements()) {
      String name = (String) enames.nextElement();
      String value = "" + session.getAttribute(name);
      map.put(name, value);
   }
   out.println(createTable(map, "Session Attributes"));

%>

<%-- Define a method to create an HTML table --%>

<%!
   private static String createTable(Map map, String title)
   {
      StringBuffer sb = new StringBuffer();

      // Generate the header lines

      sb.append("<table border='1' cellpadding='3'>");
      sb.append("<tr>");
      sb.append("<th colspan='2'>");
      sb.append(title);
      sb.append("</th>");
      sb.append("</tr>");

      // Generate the table rows

      Iterator imap = map.entrySet().iterator();
      while (imap.hasNext()) {
         Map.Entry entry = (Map.Entry) imap.next();
         String key = (String) entry.getKey();
         String value = (String) entry.getValue();
         if ( key.equals("cookie")) {
        	 value = value.replaceAll(";" , ";<br>" );
         }
         sb.append("<tr>");
         sb.append("<td>");
         sb.append(key);
         sb.append("</td>");
         sb.append("<td>");
         sb.append(value);
         sb.append("</td>");
         sb.append("</tr>");
      }

      // Generate the footer lines

      sb.append("</table><p></p>");

      // Return the generated HTML

      return sb.toString();
   }
%>
