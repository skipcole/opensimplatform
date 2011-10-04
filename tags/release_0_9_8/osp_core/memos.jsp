<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	RunningSimulation rs = new RunningSimulation();
	List setOfDocs = new ArrayList();
	SharedDocument sd = new SharedDocument(); 
	
	if (!(pso.preview_mode)) {
		rs = pso.giveMeRunningSim();

		setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), rs.getId());

		sd = (SharedDocument) setOfDocs.get(0);
	
		pso.handleMemoPage(sd, request, cs);
	}

%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%">
<tr><td valign="top">

<p>
<p><%= cs.getBigString() %></p>
<form name="form_memos" method="post" action="memos.jsp">
  <input type="submit" name="save_draft" id="save_draft" value="Save Draft"> 
(save early, save often)
<br />
<input type="submit" name="submit_memo" value="Submit Your Memo"><br />
<input type="hidden" name="sending_page" value="memos" />
<input type="hidden" name="cs_id" value="<%= cs_id %>" />

		  <textarea id="memo_text" name="memo_text" style="height: 300px; width: 500px;"><%= pso.memo_starter_text %></textarea>
          <script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
		<script language="javascript1.2">
			wysiwygWidth = 500;
			wysiwygHeight = 300;
  			generate_wysiwyg('memo_text');
		</script>
		  </p>
</form>
</td>
<td valign="top">
	<h1><%= sd.getDisplayTitle() %></h1>
	<%= sd.getBigString() %>

</td></tr></table>
</body>
</html>