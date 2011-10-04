<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
%>	
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
			  <h1>Hard, but Worthwhile </h1>
			  <br />
            <p>Creating an effective online training simulation 
              is not an easy task. This wizard will help step you through all of the 
              essential mechanical steps, but this is no assurance that the final 
              product will perform correctly and teach the lessons you want to convey. 
              The only way to verify that is to have people try your simulation and 
              listen closely to their feedback. It is quite possible (really probable) 
              that you will need to modify your simulation </p>
            <p>So, expect this to be an iterative process: You 
              create something. You try it out on some players. You get feedback. 
              You modify your simulation. You try that out. And so on.</p>
            <p>This isn't bad. It&#8217;s just the way that 
              things of this complexity have to grow. You will learn much, and your 
              players will learn much, in the act of perfecting the simulation. The 
              more open one's mind is during the process, the more they will be able 
              to learn.</p>
            <p>In a sense, you can think of this program as 
              tool to create an architectural blueprint. The ability to generate a 
              blueprint, and hence a building, is no assurance at all that the resulting 
              building will be functional. The art of architecture has been developed 
              now over centuries, and so there are established patterns to help someone 
              starting out. There are few established patterns in creating online 
              simulations. </p>
            <p>This means that your job will be difficult at 
              times, but it also means that you get to be one of the founders helping 
              to find out what works and what doesn't. As you develop new methods, 
              you will probably reach the limit to what this tool can do, hence this 
              tool has been created open source, so it can always grow, and modular, 
              so it can grow easily. If you have a feature you would like installed, 
              please contact us..</p>            <p>&nbsp;</p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
