<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/tutorialTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Tutorial Page</title>
<!-- InstanceEndEditable -->
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../../Templates/images/top_fade.png"><h1 class="header">&nbsp;OSP Tutorial - <!-- InstanceBeginEditable name="tutorialName" -->Simulation Core Concepts <!-- InstanceEndEditable --></h1></td>
    <td align="right" background="../../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"><!-- InstanceBeginEditable name="linkToHelp" --><a href="../help.jsp">Help!</a><!-- InstanceEndEditable --></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0">
		</td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<!-- InstanceBeginEditable name="pageBody" -->
<br />
<h1>Welcome</h1>
<blockquote>
  <p>This introductory tutorial is just to familiarize you with a few of the   core concepts. Unlike the future tutorials, you will not be doing any hands on work here, just reading this page. </p>
  <p>While the core concepts discussed here may seem a bit esoteric, they  will   become very concrete in the lessons to come. Understanding the definitions   below will help provide you a frame of reference for all that follows.</p>
  <p>Please Note: We have kept these tutorials rather concise. (Some might say 'terse.') So please read each part rather carefully.</p>
  <p>&nbsp; </p>
  <h2>
    1.) Actor</h2>
  <p>All simulations involve people doing things. Some will be played by the students, others by a facilitator. (And perhaps someday some of them by a computer.) Creating a set of actors is a large part of creating a simulation.</p>
  <p align="center"><img src="images/actor.png" alt="actor image" width="264" height="200" />   </p>
  <h2><br />
    2.) Control Actor</h2>
  <p>Simulations are run by  facilitators, who generally come into the simulation as a 'control' actor. This gives them greater ability to see things and to control what is happening than the other actors. </p>
  <p>&nbsp;</p>
  <h2>3.) Phase</h2>
  <p>Every simulation has different epochs it will go through - even if these are just 'started' and 'finished.' In the different phases the actors may be able to do differen things. For example, in one phase some actors may be able to work on a shared document together. In another phase perhaps, they are no longer in communcation with each other. Development of the phases is up to the simulation author and will be discussed in greater detail later. For now, please just consider a phase as a period of time. </p>
  <h2><br />
    4.) Simulation Sections</h2>
  <p>The USIP OSP provides access to things that the players can see and do in distinct sections. As you will see, these appear to the players as different tab headings. The simulation author defines what these sections will be by selecting from a list of available simulation sections. For example, if the author wants some of the players to be able to chat with each other, she would add the 'chat' section to the applicable players. </p>
  <p>&nbsp;</p>
  <h2>5.) Simulation Objects</h2>
  <p>Some objects have a life outside of any particular simulation section. For example, a document that the players write in one phase may be visible to them, but no longer editable, in another phase. The USIP OSP does its best to create some of these objects in the background when an author is adding sections that use them to the actors in a simulation. But often, these types of objects (documents, conversations, injects, etc.) have to de defined by the simulation author. </p>
  <h2><br />
    6.) Simulation</h2>
  <p> A simulation is nothing more than a set   of actors with different things they can see and do (sections) at different   points in time (phases) possibly working on some of the simulation objects.</p>
  <p align="center"><img src="images/simple_sim.png" alt="simple sim" width="943" height="330" /> </p>
  <h2><br />
    7.) Running   Simulation</h2>
	
  <p>For clarity, a simulation that is in play is referred to as a running simulation. This is just due to the sad fact that the term 'simulation' can refer to both the archetypal simulation and to the process of conducting it.
 (The word 'game' suffers similarly by having at least two distinct meanings. For example, the phrase 'the game of chess' can be used as in, "The game of chess is ancient." or "The game of chess I played on August 8th with my father was the best one I have ever played.")   </p>
  <p>&nbsp;</p>
  <table align="center" width="70%" border="0" cellspacing="4" cellpadding="4">
  <tr>
    <td bgcolor="#FFFFFF"><h2>One Final Note Before You Begin</h2>
      <p>It is worth noting here that the USIP OSP Assumes that the simulation author comes in with something in mind to work on - just as the blank page assumes that the writer has something to write about. To perform these tutorials, we have created some rather simple content, but the eventual content that you create using this platform will be (should be) much better. </p>
      <p>We are just creating the platform to allow you to become a virtuouso. If this platform needs improvement to allow your creative vision to come to fruition, send an email to osp@usip.org</p>
    <p>&nbsp; </p></td>
  </tr>
</table>
  <p>&nbsp;</p>
  <p>This concludes your introduction to the core concepts.</p>
  <p>To continue on to the next tutorial (Simulation Authoring 1), click the 'next' button below. </p>
</blockquote>
<p>&nbsp;</p>
<!-- InstanceEndEditable -->
<table  border="0" align="center" cellspacing="5">
  <tr>
    <td><div align="center"><!-- InstanceBeginEditable name="backButton" --><!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="tocLink" --><a href="../index.jsp">&nbsp; up &nbsp;</a><!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="nextButton" --> <a href="../sim_author_1/index.jsp">next --&gt;</a><!-- InstanceEndEditable --></div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>