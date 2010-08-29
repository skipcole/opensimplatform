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
<STYLE TYPE="text/css">
     P.breakhere {page-break-after: always}
</STYLE> 
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_top.png" width="60" height="50" border="0" /></td>
    <td width="80%" valign="middle"  background="../../Templates/images/top_fade.png"><h2 class="header"><font color="#FFFFFF">&nbsp;USIP OSP Tutorial - <!-- InstanceBeginEditable name="tutorialName" -->Simulation Authoring 1.0<!-- InstanceEndEditable --></font></h2></td>
    <td align="right" background="../../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"><!-- InstanceBeginEditable name="linkToHelp" --><a href="../help.jsp">Help!</a><!-- InstanceEndEditable --></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_bot.png" width="60" height="10" /></td>
    <td height="10" colspan="2" valign="bottom" bgcolor="#475DB0">
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
 <h1> Step 12. Creating universal sections </h1>
 <blockquote>
   <p>a.) Observe the 'Set Universal Simulation Sections' form shown below. This form shows the tab heading that will be seen by the players during the phase selected. Observe that:</p>
   <blockquote>
     <ol>
       <li>The phase, as indicted in the upper right hand corner, is the phase 'Started.'</li>
       <li>Every player currently has two sections automatically assigned to them at this phase: Introduction and Schedule.</li>
       <li>In the lower left hand corner is  pulldown menu of  new sections to add.</li>
       <li>In the lower right hand is the 'Add Section' button which will add that section to all actors at this phase.</li>
       <li>In the center is an icon (currently for the 'After Action Report') for the section that is queued up to add.</li>
     </ol>
     <p><img src="images/tut_sim_auth_1_page12_createuniverals1.PNG" alt="Create Universals" width="737" height="734" border="1" /></p>
     <p>&nbsp;</p>
     <p>Tip: A 'universal section' is one that will be applied to all actors at that phase. An author can then go in and remove it from some actors.</p>
     <p>&nbsp;</p>
   </blockquote>
   <p>b.) Select 'Cast' from the pulldown and then click the 'Add Section' button.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_select_cast.PNG" alt="Select Cast" width="908" height="468" border="1" /></p>
     <p>&nbsp;</p>
   </blockquote>
   <p>c.) You will be taken to a page where you can customize the 'Cast' page.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_customize_cast.PNG" alt="Customize Cast" width="662" height="448" border="1" /></p>
   </blockquote>
   <p>d.) Select 'Yes' for the question &quot;Cast page will display control characters               
     <label> </label>
     ?&quot;   </p>
   <p>e.) Select 'Save and Add Section.' This will return you back to the 'Set Universal Sections' page.</p>
   <blockquote>
     <table width="70%" border="1" cellspacing="2" cellpadding="0">
    <tr>
      <td><h2 align="center"><strong>Tip: The Difference between 'Save' and 'Save and Add'</strong></h2>
        <p>When you 'Save' changes to a customized section being added, you are just changing the way it will look or behave. You are not modifying which actors will have it during the current phase you are working on.</p>
        <p>When you 'Save and Add' a section you are not only making changes to it, you are also adding it to the actor being worked on (or all actors is you working on the universal sections) during the current phase that you are working on.</p></td>
    </tr>
  </table>
   </blockquote>
   <p>f.) Select 'Meeting' from the pulldown and then click the 'Add Section' button.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_select_meeting.PNG" alt="Select Meeting" width="337" height="477" border="1" /></p>
   </blockquote>
   <p>g. Customize the Meeting room as shown below, and then select 'Save and Add'</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_customize_market.PNG" alt="Customize Market" width="580" height="579" border="1" /></p>
     <p>&nbsp;</p>
     <p>Tip: You will not see the 'Market' listed in the universal sections. This is because one select the actors that can participate in a meeting room, and not all of them may have been selected. These selections will show up when you look at the individual actors, which is the next step.</p>
   </blockquote>
   <p>h.) Use the control in the upper right hand corner to to change phase to the completed phase. </p>
   <blockquote>
     <p>Tip: First select the phase 'Completed' and then push the 'Change Phase' button.</p>
     <p><img src="images/tut_sim_auth_1_page12_change_phase.PNG" alt="Change Phase 1" width="145" height="75" border="1" align="middle" /> to <img src="images/tut_sim_auth_1_page12_change_phase2.PNG" alt="Change Phase 2" width="150" height="79" border="1" align="middle" /> to <img src="images/tut_sim_auth_1_page12_change_phase3.PNG" alt="Change Phase 3" width="149" height="79" border="1" align="middle" /></p>
   </blockquote>
   <p>i.) Observe that 'AAR' has already been added to the the 'completed' phase as shown below.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_aar.PNG" alt="AAR in Completed Phase" width="717" height="457" border="1" /></p>
   </blockquote>
   <p>j.) Add the 'Cast' phase, as done before, to this 'Completed' phase.</p>
   <p>k.) Change phase back to the 'Started' phase.</p>
   <p>l..) Select the link 'Next Step: Customize Sections for the Actor Orange Salesman' to move on to the next step.</p>
   <p>&nbsp;</p>
   <blockquote>
     <p>&nbsp;</p>
   </blockquote>
 </blockquote>
<!-- InstanceEndEditable -->
<table  border="0" align="center" cellspacing="5">
  <tr>
    <td><div align="center"><!-- InstanceBeginEditable name="backButton" --><a href="page11.jsp">&lt;-- back </a><!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="tocLink" -->  ||  <a href="index.jsp#toc"> table of contents </a>  ||<!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="nextButton" --><a href="page13.jsp"> next --&gt;</a><!-- InstanceEndEditable --></div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
<P CLASS="breakhere"></P>
</body>
<!-- InstanceEnd --></html>