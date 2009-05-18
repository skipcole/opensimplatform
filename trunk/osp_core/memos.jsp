<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	RunningSimulation rs = pso.giveMeRunningSim();
	
	System.out.println("blah: " + pso.schema +  " " + cs.getId() + " " + rs.getId());

	List setOfDocs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), rs.getId());

	SharedDocument sd = (SharedDocument) setOfDocs.get(0);
	
	//If data has been submitted, tack it at the front, save it and move on
	String sending_page = (String) request.getParameter("sending_page");
	
	String start_memo = (String) request.getParameter("start_memo");
	String save_draft = (String) request.getParameter("save_draft");
	String submit_memo = (String) request.getParameter("submit_memo");
	
	String memo_text = (String) request.getParameter("memo_text");
	
	if (sending_page != null) {
		if (submit_memo != null) {
			if ((memo_text != null) && (memo_text.trim().length() > 0) ){

				java.util.Date today = new java.util.Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
				String memo_time = "<em>(Memo Submitted: " + sdf.format(today) + ")</em><br />";
		
				String fullText = memo_time + memo_text + "<br><hr>" + sd.getBigString();
				sd.setBigString(fullText);
				sd.saveMe(pso.schema);	
				pso.memo_starter_text = "";
			}
		}
		
		if (save_draft != null) {
				pso.memo_starter_text = memo_text;
		}

		if (start_memo != null) {
			pso.memo_starter_text = "To: <BR />From:<BR />Topic:<BR />Message:";
		}
		
	}  // End of if coming back from the form on this page.
	
%>
<html>
<head>
<title>Read Document Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
<input type="submit" name="start_memo" value="Start Memo">
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