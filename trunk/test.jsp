<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,
	java.util.*,
	java.text.*,
	java.sql.*" %>
<html>
<head>

<title>Sea Change Platform Test Page</title>

<%

	String hostname = "";
	String port = "3306";
	String schema = "";
	String username = "";
	String password = "";
	
	String sending_page = (String) request.getParameter("sending_page");
	
	
	String conn_string = "";
	
	String error_msg = "";
	String success_msg = "";
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("test"))){

		hostname = (String) request.getParameter("hostname");
		port = (String) request.getParameter("port");
		schema = (String) request.getParameter("schema");
		username = (String) request.getParameter("username");
		password = (String) request.getParameter("password");
		
		String loc = "jdbc:mysql://";
		String url = loc + hostname + ":" + port + "/" + schema + "?autoReconnect=true"; 
		
		conn_string = url + "&user=" + username + "&password=" + password;
		
		Connection connection = null;

        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance(); //$NON-NLS-1$
            connection = DriverManager.getConnection(conn_string);

            if (connection == null) {
				error_msg = "Connection is null";	
			} else {
				connection.close();
				success_msg = "Successfully connected and closed.";	
			}

        } catch (SQLException se) {
            error_msg = se.toString();
        } catch (Exception e) {
            error_msg = e.toString();
        }
		
	}
		
%>

<head>
<body>

<h2>Basic Functionality Test Page</h2>
<blockquote>
  <p>If you have made it here, you have reached a JSP on the client you are trying to reach.</p>
</blockquote>
<h2>Database Test</h2>
<blockquote>
  <p>This test will attempt to reach the database you specify and create a connection.</p>
  <form name="form1" method="post" action="test.jsp">
  <input type="hidden" name="sending_page" value="test" >
  <table width="100%" border="1" cellspacing="0" cellpadding="2">
    <tr>
      <td>Host</td>
      <td>
        <input type="text" name="hostname" value="<%= hostname %>" size="40">
      </td>
    </tr>
    <tr>
      <td>Port</td>
      <td>
        <input type="text" name="port" value="<%= port %>"></td>
    </tr>
    <tr>
      <td>Schema</td>
      <td>
        <input type="text" name="schema" value="<%= schema %>" ></td>
    </tr>
    <tr>
      <td>Username</td>
      <td><input type="text" name="username" id="username" value="<%= username %>"></td>
    </tr>
    <tr>
      <td>Password</td>
      <td><input type="password" name="password" value="<%= password %>"></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="submit" name="button" id="button" value="Submit"></td>
    </tr>
  </table>
  </form>
  <p>&nbsp;</p>
  <p>Connection String so far: <%= conn_string %></p>
  <p>Error Message = <%= error_msg %></p>
  <p>Success Message = <%= success_msg %></p>
</blockquote>
<p>&nbsp;</p>
<p>&nbsp;</p>
</body>
</html>