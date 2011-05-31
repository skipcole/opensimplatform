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
 <h1> Step 12. Creating universal sections - Part A </h1>
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
     <p>Tip: A 'universal section' is one that will be applied to all actors at that phase. An author can then go in and remove it from some actors.</p>
   </blockquote>
   <p>b.) Observe the list of sections one can add to one's simulation. A sample image of a list is shown in the image below, but this list will change depending upon a.) what sections have been loaded on your installation, and b.) what sections have already been customized for this particular simulation.<br />
   </p>
   <p align="center"><img src="images/tut_sim_auth_1_page12_sections_list.PNG" alt="sections list" width="200" height="819" border="1" /></p>
   <p>This list contains (from top to bottom) 4 different kinds of sections that one can add:</p>
   <p>1.) Sections that require no customization.(white backgrounds)
   <br />
     2.) Sections that require customization<br />
   3.) Sections that have been customized, that have already been added.</p>
    
   <p align="left">While the list is long, it represents all of the things one can 'add' to a player's world. If, for example, one wants the players to be able to communicate via in-game email, one would add the 'Email' section. </p>
   <p>&nbsp;</p>
   <p>c.) Select 'Cast' from the pulldown and then click the 'Add Section' button.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_select_cast.PNG" alt="Select Cast" width="908" height="468" border="1" /></p>
     <p>&nbsp;</p>
   </blockquote>
   <p>d.) You will be taken to a page where you can customize the 'Cast' page.</p>
   <blockquote>
     <p><img src="images/tut_sim_auth_1_page12_customize_cast.PNG" alt="Customize Cast" width="662" height="448" border="1" /></p>
   </blockquote>
   <p>e.) Select 'Yes' for the question &quot;Cast page will display control characters               
     <label> </label>
     ?&quot;   </p>
   <p>f.) Select 'Save and Add Section.' This will return you back to the 'Set Universal Sections' page.</p>
   <blockquote>
     <table width="70%" border="1" cellspacing="2" cellpadding="0">
    <tr>
      <td><h2 align="center"><strong>Tip: The Difference between 'Save' and 'Save and Add'</strong></h2>
        <p>When you 'Save' changes to a customized section being added, you are just changing the way it will look or behave. You are not modifying which actors will have it during the current phase you are working on.</p>
        <p>When you 'Save and Add' a section you are not only making changes to it, you are also adding it to the actor being worked on (or all actors if you working on the universal sections) during the current phase that you are working on.</p></td>
    </tr>
  </table>
   </blockquote>
   <p>g.) Continue on to the <a href="page12b.jsp">next page</a> in this tutorial. </p>
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
    <td><div align="center"><!-- InstanceBeginEditable name="nextButton" --><a href="page12b.jsp"> next --&gt;</a><!-- InstanceEndEditable --></div></td>
  </tr>
</table>
<!-- InstanceBeginEditable name="errorReporting" -->

To report corrections or errors, please <a href="mailto:tech@opensimplatform.org?subject=Problem_on_tutorial_page_a1_page12">click here</a>. <br/>
(If the above link does not work, you can also just send an email to tech@opensimplatform.org with the subject heading 'Problem on Tutorial Page Simulation Authoring Page 12'.)

<!-- InstanceEndEditable -->
<p>&nbsp;</p>
<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
<P CLASS="breakhere"></P>
</body>
<!-- InstanceEnd --></html>