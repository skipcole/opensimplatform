<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	pso.backPage = "push_injects.jsp";
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	if (pso.handlePushInject(request)){
		response.sendRedirect("select_actors.jsp");
		return;
	}
		
%>
<html>
<head>
<title>News Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Push Injects </h2>
<table width="100%" border="0" cellspacing="0" cellpadding="4">
<form name="formforinject_blankc" action="push_injects.jsp" method="post">
<input type="hidden" name="sending_page" value="push_injects">
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="3" valign="top">Blank Inject</td>
    <td width="33%" rowspan="3" valign="top"><label>
      <input name="player_target" type="radio" value="all" checked>
      To All Players </label>
  <br>
  <label>
  <input name="player_target" type="radio" value="some">
    To Some Players<BR />
    <I>(Select to who after hitting submit.)</I></label> 
  <br>
  <label>
  <input type="submit" name="button" id="button" value="Push Inject">
  </label></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td width="4%" valign="top">&nbsp;</td>
    <td colspan="2" valign="top">
        <label>
          <textarea name="announcement_text" id="textarea" cols="45" rows="5"></textarea>
        </label>
        <p>
          <label>
          Inject Trailer: 
          <select name="inject_action" id="inject_action">
            <option value="1" selected> </option>
            <option value="2">Communicate with Control your actions</option>
                                        </select>
          </label>
      </p></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td colspan="2" valign="top">Just type above anything you want.</td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="4" valign="top"><hr></td>
  </tr>
  </form>
          <tr>
            <td colspan="5" valign="top">&nbsp;</td>
          </tr>
		  <%
			for (ListIterator li = InjectGroup.getAllForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>

  <tr>
    <td colspan="5" valign="top"><%= ig.getName() %></td>
  </tr>
  <tr>
    <td width="15%" valign="top">&nbsp;</td>
    <td colspan="4" valign="top"><%= ig.getDescription() %></td>
  </tr>
    <% 
	List injectList = Inject.getAllForSimAndGroup(pso.schema, pso.sim_id, ig.getId());

		int iii = 0;
		
		  for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
			iii += 1;
			
%>
<form name="formforinject<%= iii %>" action="push_injects.jsp" method="post">
<input type="hidden" name="sending_page" value="push_injects">
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="3" valign="top"><%= da_inject.getInject_name() %></td>
    <td width="33%" rowspan="3" valign="top"><label>
      <input name="player_target" type="radio" value="all" checked>
      To All Players </label>
  <br>
  <label>
  <input name="player_target" type="radio" value="some">
    To Some Players<BR />
    <I>(Select to who after hitting submit.)</I></label> 
  <br>
  <label>
  <input type="submit" name="button" id="button" value="Push Inject">
  </label></td>
  </tr>
  <%
  	String injectLineColor = "#FFCCCC";
  %>
  <tr bgcolor="<%= pso.getInjectColor(da_inject.getId())%>">
    <td valign="top">&nbsp;</td>
    <td width="4%" valign="top">&nbsp;</td>
    <td colspan="2" valign="top">
    	
        <label>
          <textarea name="announcement_text" id="textarea" cols="45" rows="5"><%= da_inject.getInject_text() %></textarea>
          <input type="hidden" name="inject_id" value="<%= da_inject.getId() %>">
        </label>
        <p>
          <label>
          Inject Trailer: 
          <select name="inject_action" id="inject_action">
            <option value="1" selected> </option>
            <option value="2">Communicate with Control your actions</option>
          </select>
          </label>
      </p></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td colspan="2" valign="top"><%= da_inject.getInject_Notes() %></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="4" valign="top"><hr></td>
  </tr>
  </form>
  <% } // End of loop over injects %>
 
 <% } // end of loop over inject groups %>
</table>
<p>For a print out of the injects, <a href="pretty_print_injects.jsp">click here</a>.</p>
</body>
</html>
<%
	
%>
