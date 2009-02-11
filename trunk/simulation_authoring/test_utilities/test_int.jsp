<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.jfree.chart.*;" 
	errorPage="" %>
<%
	class MYIV extends IntegerVariable{
	

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
	
	MYIV iv = new MYIV();
	
	if ( (sending_page != null) && (test_func != null) && (sending_page.equalsIgnoreCase("test_func"))){
	
		f1 = (String) request.getParameter("f1");
		
		/* f2 = (String) request.getParameter("f2");
		f3 = (String) request.getParameter("f3");
		f4 = (String) request.getParameter("f4");
		f5 = (String) request.getParameter("f5");
		f6 = (String) request.getParameter("f6");
		*/
		iv.set_sf_id(f1);
		iv.load();
		
		f2 = iv.name;
		
		Game sim = new Game();
		
		game.id = "1";
		game.load();
		
		iv.sim_id = "1";
		iv.sim_id = iv.lookUpMySimID("SIM_TEST1_1_var_int", "1");
		
		//debug += iv.propagate(game, "1", "1");
		
		debug += iv.addNewValue("SIM_TEST1_1_var_int_v", "1", "1", "88");
		
		/*
		Vector v = SimulationVariable.getSimVariablesForASimulation("1");
		debug = "size is " + v.size();
		
		SimulationVariable sv = (SimulationVariable) v.get(0);
		
		debug += ", prop type is " + sv.propagation_type;
		
		//String tableName, String simId, String gameId, String dataType, int game_round, int newInt
		debug += sv.addNewValue("SIM_TEST1_1_values", "2", "1", "integer", 1, 77);
		
		Game sim = new Game();
		game.id = f1;
		game.db_tablename = f2;
		debug += "<P>";
		//SimulationVariable sv = new SimulationVariable();
		sv.propagation_type = "fibonacci";
		sv.sim_id = "1";
		debug += sv.propagate(game, null, f3);
		*/
		/*
		debug = SharedDocument.prep(f1, game);
		*/
		//debug = Emailer.postSimReadyMail(f1,f2,f3,f4,f5,f6);
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Test IntegerVariable</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
<form name="form2" id="form2" method="post" action="">
  <table width="80%" border="0" cellspacing="2" cellpadding="1">
    <tr> 
      <td>Field 1:</td>
      <td> <input type="text" name="f1" value="<%= f1 %>"/> 
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
<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
