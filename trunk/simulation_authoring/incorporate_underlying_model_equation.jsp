<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "add_underlying_model.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Add Special Features Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Incorporate Equation from Model: DaisyWorld 1.0</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
        <% 
			if (pso.sim_id != null) {
		%>
        <h2>Instructions</h2>
        <table border="1" width="90%" cellspacing="0"><tr><td>
        <p>Below are listed elements (equations and constants) that you will need to either set, or tie to variables inside of your simulation, to incorporate this model.</p>
        <blockquote>
          <p>NB: The OSP is not the place to put a model through its paces. A model should be systematically tested throughout all possible ranges of inputs before human beings are subjected to trying to work with it. </p>
          </blockquote>  
        <p><strong>Steps</strong></p>        
        <ol>      
        <li>Verify that all constants and initial conditions have been met to incorporate this model.</li>
        <li>Select an equation from below to incorporate its usage in this simulation.</li>
        <li>Create sections where the players may either observer or interact with the model variables.<br />
        </li>
        </ol>
        <p>&nbsp;</p>
        </td>
        </tr></table>
        <h2>&nbsp;</h2>
        <h2>Model Description</h2>
        <table border="1" width="90%" cellspacing="0"><tr>
          <td><p>Taken from Wikipedia:</p>
            <p><strong>Daisyworld</strong>, a <a href="http://en.wikipedia.org/wiki/Computer_simulation" title="Computer simulation">computer simulation</a>, is a hypothetical world <a href="http://en.wikipedia.org/wiki/Orbit" title="Orbit">orbiting</a> a <a href="http://en.wikipedia.org/wiki/Sun" title="Sun">sun</a> whose <a href="http://en.wikipedia.org/wiki/Temperature" title="Temperature">temperature</a> is slowly increasing in the simulation. Daisyworld was introduced by <a href="http://en.wikipedia.org/wiki/James_Lovelock" title="James Lovelock">James Lovelock</a> and <a href="http://en.wikipedia.org/wiki/Andrew_Watson_%28scientist%29" title="Andrew Watson (scientist)">Andrew Watson</a> to illustrate the plausibility of the <a href="http://en.wikipedia.org/wiki/Gaia_hypothesis" title="Gaia hypothesis">Gaia hypothesis</a> in a paper published in 1983. The simulated planet is seeded with two different <a href="http://en.wikipedia.org/wiki/Species" title="Species">species</a> of <a href="http://en.wikipedia.org/wiki/Asteraceae" title="Asteraceae">daisy</a> as its only life form: black daisies and white daisies. White daisies have white flowers which reflect <a href="http://en.wikipedia.org/wiki/Light" title="Light">light</a>, and the other species has black flowers that absorb light. Both species have the same growth curve (that is, their <a href="http://en.wikipedia.org/wiki/Reproduction" title="Reproduction">reproduction</a> rate is the same function of temperature) but the black daisies are <em>themselves</em> warmer than the white daisies and bare earth. A planet with  preponderance of white daisies is cooler than one with more black ones.</p></td>
        </tr></table>
        <h2>&nbsp;</h2>
        <h2>Variables Defined in this Model</h2>
        <ul>
          <li>Number Black Daisies: NUMb</li>
          <li>Number White Daisies: NUMw</li>
          <li>Planet Temperature: Pt</li>
          <li>Time: t</li>
          </ul>
        <h2>Constants and Initial Conditions</h2>
        <table>
        	<tr><td><strong>Constant</strong></td>
        	<td><strong>Description</strong></td>
        	<td><strong>Value</strong></td>
        	<td><strong>Units</strong></td>
        	<td><strong>Change</strong></td>
        	</tr>
            <form id="form1" name="form1" method="post" action="">
            <tr><td valign="top">NUMb-Zero</td><td valign="top">Number of Black Daisies at Time Zero</td><td valign="top"><input type="text" name="textfield" id="textfield" value="100" /></td><td valign="top">Daisies</td><td valign="top"><input type="submit" name="button" id="button" value="Change" /></td></tr>
            </form>
            <form id="form1" name="form1" method="post" action="">
            <tr><td valign="top">NUMw-Zero</td><td valign="top">Number of White Daisies at Time Zero</td><td valign="top"><input type="text" name="textfield" id="textfield" value="100" /></td><td valign="top">Daisies</td><td valign="top"><input type="submit" name="button" id="button" value="Change" /></td></tr>
            </form>
            <form id="form1" name="form1" method="post" action="">
            <tr>
              <td valign="top">Pt-Zero</td>
              <td valign="top">Planet Temperature at Time Zero</td>
              <td valign="top"><input type="text" name="textfield2" id="textfield2" value="40"/></td>
              <td valign="top">Degrees</td><td valign="top"><input type="submit" name="button2" id="button2" value="Change" /></td></tr>
            </form>
            <form id="form1" name="form1" method="post" action="">
            <tr>
              <td valign="top">Capacity</td>
              <td valign="top">Total number of daisies the planet can hold.</td><td valign="top"><input type="text" name="textfield3" id="textfield3" value="4000"/></td>
              <td valign="top">Daisies</td><td valign="top"><input type="submit" name="button3" id="button3" value="Change" /></td></tr>
            </form>
        </table>
        
        <h2>&nbsp;</h2>
        <h2>Accessible Equations from this Model</h2>
        <ul>
          <li><a href="incorporate_underlying_model_equation_details.jsp">Planet Temperature</a>: Pt = Funcition (Number Black Daisies, Number White Daises).</li>
          <li><a href="#">Number Black Daisies (at Time 't+1')</a>: NUMb = Function (Pt, NUMb)</li>
          <li><a href="#">Number White Daisies (at Time 't+1')</a>: NUMw = Function (Pt, NUMw)</li>
        </ul>
        <p>&nbsp;</p>
      </blockquote>
      <p align="center"><a href="set_universal_sim_sections.jsp?actor_index=0">Next 
        Step: Assign Simulation Sections to Actors</a></p>
		
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      <a href="create_injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>      <!-- InstanceEndEditable -->
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
<%
	
%>
