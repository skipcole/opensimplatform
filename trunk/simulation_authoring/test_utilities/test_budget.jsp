<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*,org.jfree.chart.*;" 
	errorPage="" %>
<%
	class X{
	
	    public String sumRound(String tableName, String sim_id, String running_sim_id, String game_round){
        
        String selectRecords = "SELECT * FROM `" + tableName + "` " +
                "WHERE `sim_id` = -1 and sim_id = '1' " +
                "and running_sim_id = '1' and game_round = '1' and trans_type = 'move'";
            
        // Select the debits
        
        // Select the credits
        
        // Sum the results
        
        // Insert the 'final' line, mark it clean
        
        return selectRecords;
    }
	
	}
%>
<%
	String f1 = "";
	String f2 = "";
	String f3 = "";
	String f4 = "";
	String f5 = "";
	String f6 = "";
	
	String debug = "okay";

	String sending_page = (String) request.getParameter("sending_page");
	String test_func = (String) request.getParameter("test_func");
	
	if ( (sending_page != null) && (test_func != null) && (sending_page.equalsIgnoreCase("test_func"))){
	
		f1 = (String) request.getParameter("f1");
		f2 = (String) request.getParameter("f2");
		f3 = (String) request.getParameter("f3");
		f4 = (String) request.getParameter("f4");
		f5 = (String) request.getParameter("f5");
		f6 = (String) request.getParameter("f6");
		
		X x = new X();
		
		debug = x.sumRound(f1, f2, f3, f4);
	}
	
	BudgetVariable bvd = new BudgetVariable();
	
	String nowValue = 
            getCurrentValue(game.db_tablename_var_bud_v, sim_id, "1");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Test Budget</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="../creationwebui.jsp" target="_top">Create</a><br>
		<a href="../../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
<form name="form2" id="form2" method="post" action="">
  <table width="80%" border="0" cellspacing="2" cellpadding="1">
    <tr> 
            <td>table name1:</td>
      <td> <input type="text" name="f1" value="SIM_TEST1_1_var_bud_v"/> 
      </td>
    </tr>
    <tr> 
      <td>Field 2:</td>
      <td> <input type="text" name="f2"  value="<%= f2 %>"/> 
      </td>
    </tr>
    <tr> 
      <td>Field 3:</td>
      <td> <input type="text" name="f3"  value="<%= f3 %>"/> 
      </td>
    </tr>
    <tr> 
      <td>Field 4:</td>
      <td><input type="text" name="f4"  value="<%= f4 %>"/></td>
    </tr>
    <tr> 
      <td>Field 5:</td>
      <td><input type="text" name="f5"  value="<%= f5 %>"/></td>
    </tr>
    <tr> 
      <td>Field 6:</td>
      <td><input type="text" name="f6"  value="<%= f6 %>"/></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><input type="hidden" name="sending_page" value="test_func" />
	  <input type="submit" name="test_func" value="Submit" /></td>
    </tr>

  </table>
</form>
<p><%= Debug.getDebug(debug) %></p>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="../intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="../logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
