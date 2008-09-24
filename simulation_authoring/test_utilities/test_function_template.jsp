<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*,org.jfree.chart.*;" 
	errorPage="" %>
	<%
	class Chart extends SpecialFeature{

	public String page_title = "";
	public String tab_heading = "";
    public JFreeChart this_chart = null;

    public String title = "";
	

    public String type = "";

    public String xAxisTitle = "";

    public String yAxisTitle = "";

    public int height = 300;

    public int width = 400;
    
    public String selectDataStatement = "";

    public static final String SPECIALFIELDLABEL = "linechart_page";
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "linechart_page_";
    }
    
    public String store() {

        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO charts ( " +
                    "`sf_id` , `sim_id`, `chart_name`, `title` , `tab_heading`, `description`, " +
                    "`type` , `x_axis_title` , `y_axis_title`, `first_data_source`, " +
                    "`height`, `width`) " +
                    "VALUES ( " + "NULL , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.sim_id);
            ps.setString(2, this.name);
            ps.setString(3, this.title);
            ps.setString(4, this.tab_heading);
            ps.setString(5, this.description);
            
            ps.setString(6, this.type);
            ps.setString(7, this.xAxisTitle);
            ps.setString(8, this.yAxisTitle);
            ps.setString(9, this.selectDataStatement);
            ps.setString(10, this.height + "");
            ps.setString(11, this.width + "");

            ps.execute();

            String queryId = "select LAST_INSERT_ID()";

            ResultSet rs = stmt.executeQuery(queryId);

            if (rs.next()) {
                this.set_sf_id(rs.getInt(1) + "");
            }

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }

        return debug;
    }

    public void load() {

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String selectSQL = "select * from charts where chart_id = " + this.get_sf_id();

            ResultSet rst = stmt.executeQuery(selectSQL);

            if (rst.next()) {
                this.loadMeFromResultSet(rst);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void setHeight(String h) {
        
        try {
            this.height = new Integer(h).intValue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setWidth(String w) {
        
        try {
            this.width = new Integer(w).intValue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public Vector getSetForASimulation(String sim_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `charts` WHERE sim_id = " + sim_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                Chart c = new Chart();

                c.loadMeFromResultSet(rst);

                rv.add(c);
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rv;
    }
    
    public void loadMeFromResultSet(ResultSet rst) throws SQLException {

        this.set_sf_id(rst.getString("sf_id"));
        this.sim_id = rst.getString("sim_id");
        this.name = rst.getString("chart_name");
        this.page_title = rst.getString("title");
        this.tab_heading = rst.getString("tab_heading");
        this.description = rst.getString("description");
        this.type = rst.getString("type");
        this.xAxisTitle = rst.getString("x_axis_title");
        this.yAxisTitle = rst.getString("y_axis_title");
        this.selectDataStatement = rst.getString("first_data_source");
        this.setHeight(rst.getString("height"));
        this.setWidth(rst.getString("width"));

    }

    @Override
    public String prep(String running_sim_id, Game game) {
        return "no need to prep this";
    }

    @Override
    public String storeInRunningSimulationTable(String running_sim_id, String tableName) {
        return "No need to store in running sim table.";
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
	
	Chart chart = new Chart();
	
	if ( (sending_page != null) && (test_func != null) && (sending_page.equalsIgnoreCase("test_func"))){
	
		f1 = (String) request.getParameter("f1");
		f2 = (String) request.getParameter("f2");
		f3 = (String) request.getParameter("f3");
		f4 = (String) request.getParameter("f4");
		f5 = (String) request.getParameter("f5");
		f6 = (String) request.getParameter("f6");
		
		chart.sim_id = "1";
		
		chart.title = f1;
		chart.type = f2;
		chart.xAxisTitle = f3;
		chart.yAxisTitle = f4;
		chart.setHeight(f5);
		chart.setWidth(f6);
		
        chart.name = "chart_name";
        chart.tab_heading = "tab_heading";
        chart.description = "description";
        chart.selectDataStatement = "first_data_source";

		debug += chart.store();
		
		chart.load();
		//debug += chart.load();
		debug += "<BR>";
		
		debug += chart.title;
		debug += "<BR>";
		debug += chart.type;
		debug += "<BR>";
		debug += chart.xAxisTitle;
		debug += "<BR>";
		debug += chart.yAxisTitle;
		debug += "<BR>";
		debug += chart.height;
		debug += "<BR>";
		debug += chart.width;
		debug += "<BR>";
        debug += chart.name;
		debug += "<BR>";
        debug += chart.tab_heading;
		debug += "<BR>";
        debug += chart.description;
		debug += "<BR>";
        debug += chart.selectDataStatement;
		debug += "<BR>";
		
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
      <h1>Test Something</h1>
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
