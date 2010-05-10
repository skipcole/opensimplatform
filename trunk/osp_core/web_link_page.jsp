<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	WebLinkObjects wlo = new WebLinkObjects();
	
	//System.out.println("hit top");
		
	String cs_id = (String) request.getParameter("cs_id");
	String sending_page = (String) request.getParameter("sending_page");
	String command = (String) request.getParameter("command");
	String wlo_id = (String) request.getParameter("wlo_id");
	
	//System.out.println(command);
	
	if ((cs_id != null) && (sending_page != null) && (sending_page.equalsIgnoreCase("web_link_page_bottom"))){
	
		if (command != null) {
		
			Long csId = new Long (cs_id);
		
			String wlo_name = request.getParameter("wlo_name");
			String wlo_description = request.getParameter("wlo_description");
			String wlo_url = request.getParameter("wlo_url");
			
			if (command.equalsIgnoreCase("Create")){
				wlo = new WebLinkObjects(pso.schema, wlo_name, wlo_description, wlo_url, pso.running_sim_id, csId);
			} else if (command.equalsIgnoreCase("Update")) {
				wlo = WebLinkObjects.getMe(pso.schema, new Long(wlo_id));
				
				//System.out.println("updatin");
				
				wlo.setWeblinkDescription(wlo_description);
				wlo.setWeblinkName(wlo_name);
				wlo.setWeblinkURL(wlo_url);
				wlo.saveMe(pso.schema);
			}
		}
	
	} else  {
		if (wlo.getId() != null) {
			wlo_id = wlo.getId() + "";
		}
	}
	////////////////////////
	
	String topPage = "web_link_page_top.jsp?cs_id=" + cs_id;
	String bottomPage = "web_link_page_bottom.jsp?cs_id=" + cs_id;
	
	String edit_button = (String) request.getParameter("edit_button");
	
	if ((edit_button != null) && (edit_button.equalsIgnoreCase("Edit")) && (wlo_id != null) && 
		(!(wlo_id.equalsIgnoreCase("0")))
	) {
		//System.out.println("edit button hit: " + wlo_id);
		bottomPage += "&wlo_id=" + wlo_id + "&editMode=true";
	} else	if ((wlo_id != null) && (!(wlo_id.equalsIgnoreCase(""))) && (!(wlo_id.equalsIgnoreCase("0")))){
		wlo = WebLinkObjects.getMe(pso.schema, new Long(wlo_id));
		
		bottomPage = wlo.getWeblinkURL();
		
		topPage += "&wlo_id=" + wlo_id;
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Web Link Page</title>
</head>

<frameset rows="58,*">
  <frame src="<%= topPage %>" name="wlo_top">
  <frame src="<%= bottomPage %>" name="wlo_bottom">
</frameset>
<noframes><body>
</body>
</noframes></html>
