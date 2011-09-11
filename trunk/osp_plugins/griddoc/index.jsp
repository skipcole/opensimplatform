<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
</head>

<body>
<h1>Grid Doc Plugin</h1>
<h2>Description</h2>
<p>This plugin allows the user to create a grid of editable data that is part of their simulation.</p>
<h2>Files Included</h2>
<blockquote>
  <h3>Classes</h3>
</blockquote>
<ol>
  <li>GridData** - Holds individual cells of data.</li>
  <li>GridDocCustomizer - Allows for the customization of the Grid Document by the Simulatino Author.</li>
  <li>GridPageDocument - Holds data about the page beind </li>
</ol>
<blockquote>
  <p>** This object is saved back to the database.</p>
  <h3>JSPs</h3>
</blockquote>
<ol>
  <li>edit_grid_data.jsp - Provides interface to user to edit the data in a cell.</li>
  <li>grid_doc.jsp - Displays the Grid Document.</li>
  <li>grid_doc_linear_print.jsp - Allows for the printing of the document in a linear (non-matrix) format.</li>
  <li>make_grid_doc_page.jsp - Interface for the author to create the Grid Document page.</li>
</ol>
<p>&nbsp;</p>
<h2>Technical Details On How it Works</h2>
<p><br />
  From CustomSection<br />
Page Title<br />
Intro<br />
get col designator<br />
get row designator</p>
<p>From GridData elements (matching on sim_id, rs_id, cs_id)<br />
  number of columns<br />
  number of rows<br />
  cell data<br />
  --------------------------------</p>
<p>&nbsp; </p>
</body>
</html>
