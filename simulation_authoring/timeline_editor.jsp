<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String timeline_id = (String) request.getParameter("timeline_id");
	
	Long timeLineId = null;
	
	if ((timeline_id != null) && (!(timeline_id.equalsIgnoreCase("null"))) ) {
		timeLineId = new Long(timeline_id);
		afso.timelineOnScratchPad = TimeLine.getById(afso.schema, timeLineId);
	}
	
	// Date expected in format similar to: "Oct 15 2009 00:00:00 GMT"
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getDefault());
	
	SimpleDateFormat short_sdf = new SimpleDateFormat(" MM/dd/yyyy HH:mm");
	short_sdf.setTimeZone(TimeZone.getDefault());
	
	String run_start = sdf.format(afso.timelineOnScratchPad.getTimeline_start_date());
	
	Simulation simulation = new Simulation();
	if (afso.sim_id != null) {
		simulation = afso.giveMeSim();
	}
	
	Event event = TimeLine.handleAddTimeLineEvents(request, afso.timelineOnScratchPad.getId(), afso);
			
	String Timeline_ajax_url = "";
	String Timeline_urlPrefix = "http://static.simile.mit.edu/timeline/api-2.3.0/";
	String timeLineSrc = "http://static.simile.mit.edu/timeline/api-2.3.0/timeline-api.js?bundle=true";
	
	//Timeline_ajax_url = USIP_OSP_Properties.getValue("base_sim_url") + "third_party_libraries/timeline_2.3.0/timeline_ajax/simile-ajax-api.js";
	//Timeline_urlPrefix = USIP_OSP_Properties.getValue("base_sim_url") + "third_party_libraries/timeline_2.3.0/timeline_js/";
	//timeLineSrc = USIP_OSP_Properties.getValue("base_sim_url") + "third_party_libraries/timeline_2.3.0/timeline_js/timeline-api.js?bundle=false";
	
	System.out.println("ajax url: " + Timeline_ajax_url);
	System.out.println("timeline url: " + Timeline_urlPrefix);
	System.out.println("src: " + timeLineSrc);

	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
        
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"> </script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/js/jquery-ui-1.8.4.custom.min.js"> </script>
<link type="text/css" href="../third_party_libraries/jquery/jquery-ui-1.8.4/css/cupertino/jquery-ui-1.8.4.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-timepicker-addon.js"></script>
        
<script type="text/javascript">

$(document).ready(function(){

	$('#event_time').datetimepicker();
	
});

</script>
        
        <script>Timeline_urlPrefix = "<%= Timeline_urlPrefix %>";</script>
        <script src="<%= timeLineSrc %>" type="text/javascript"></script>

        
    <script>

        var tl;
        function onLoad() {
        var eventSource = new Timeline.DefaultEventSource();
        var bandInfos = [
        Timeline.createBandInfo({
			timeZone:       -5,
             eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "80%", 
            intervalUnit:   Timeline.DateTime.HOUR, 
            intervalPixels: 400
        }),
        Timeline.createBandInfo({
			timeZone:       -5,
            overview:       true,
            eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "20%", 
            intervalUnit:   Timeline.DateTime.DAY, 
            intervalPixels: 800
        })
        ];
        bandInfos[1].syncWith = 0;
        bandInfos[1].highlight = true;
        tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
        Timeline.loadXML("timeline_server.jsp?timeline_id=<%= timeline_id %>", function(xml, url) { eventSource.loadXML(xml, url); });
        }

        var resizeTimerID = null;
        function onResize() {
            if (resizeTimerID == null) {
                resizeTimerID = window.setTimeout(function() {
                    resizeTimerID = null;
                    tl.layout();
                }, 500);
            }
        }
        </script>     
        
    <link href="../usip_osp.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
</head>

    <body onLoad="onLoad();" onResize="onResize();">
        <p>
          <% 
			if (afso.sim_id != null) {
		%>
        <% if (USIP_OSP_Util.stringFieldHasValue(afso.errorMsg)) { %>
        	<p><h1><font color="#FF0000"><%= afso.errorMsg %></font></h1></p>
        <% } 
			
			afso.errorMsg = "";
		
		%>
    
        </p>
        <h2>Add Events to Your Timeline</h2>

    <div id="my-timeline" style="height: 300px; border: 1px solid #aaa"></div>
        <hr>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="50%">Timeline Name <a href="helptext/timeline_name.jsp" target="helpinright">(?)</a>: 
			<strong><%= afso.timelineOnScratchPad.getName() %></strong></td>
            <td>Timeline Phase (if applicable): <strong>All</strong></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
    </table>
    <h2>&nbsp;</h2>
        <h2>Add Event
        
        </h2>
        <blockquote>
<form name="form1" method="post" action="timeline_editor.jsp">
        <input type="hidden" name="sending_page" value="timeline_creator">
        <input type="hidden" name="timeline_id" value="<%= timeline_id %>">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td valign="top"><strong>Title</strong></td>
            <td valign="top">
              <label>
                <input type="text" name="event_title" id="event_title" value="<%= event.getEventTitle() %>">
              </label>            </td>
          </tr>
          <tr>
            <td valign="top"><strong>Text</strong></td>
            <td valign="top"><label>
              <textarea name="event_text" id="event_text" cols="45" rows="2"><%= event.getEventMsgBody() %></textarea>
              <script language="javascript1.2">
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  			generate_wysiwyg('event_text');
		</script>
            </label></td>
          </tr>
          <tr>
            <td valign="top"><strong>Color</strong></td>
            <td valign="top"><label>
            
            <%
				String selected_red = "";
				String selected_blue = "";
				String selected_green = "";
				
				if (event.getEventType() == 3) {
					selected_red = " selected ";
				} else if (event.getEventType() == 2) {
					selected_green = " selected ";
				} else {
					selected_blue = " selected ";
				}
			%>
            
              <select name="event_type" id="event_type">
                <option value="1" <%= selected_blue %>>Planned (Blue)</option>
                <option value="2" <%= selected_green %>>Possible (Green)</option>
                <option value="3" <%= selected_red %>>Conditional (Red)</option>
            </select>
            </label></td>
          </tr>
          <tr>
            <td valign="top"><strong>Event Date and Time</strong></td>
            <td valign="top">
			<%
				SimpleDateFormat sdf_startdate = new SimpleDateFormat("MM/dd/yyyy kk:mm");
				String start_date_formatted = sdf_startdate.format(new java.util.Date());
				if (event.getEventStartTime() != null) {
					 start_date_formatted = sdf_startdate.format(event.getEventStartTime());
				}
			%>
			<input name="timeline_event_date" type="text" id="event_time" value="<%= start_date_formatted %>">
			(mm/dd/yyyy hh:mm)</td>
      </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top"><label>
            <%
				if (event.getId() == null) {
				%>
            <input type="submit" name="command" value="Create" />
            <%
				} else {
				%>
            <input type="hidden" name="event_id" value="<%= event.getId() %>" />
            <input type="submit" name="command" value="Clear" tabindex="6" />
            <input type="submit" name="command" value="Update" />
            <%
					}
				%>
</label></td>
          </tr>
    </table>
    </form>
    </blockquote>
        <h2>Edit/Delete Events</h2>
        <blockquote>
        <table width="80%">
        <%
		
		for (ListIterator li = Event.getAllForTimeLine(afso.schema, timeLineId).listIterator(); li.hasNext();) {
			Event event_l = (Event) li.next();
			
			String displayEventTime = "";
			if (event_l.getEventStartTime() != null) {
				displayEventTime = short_sdf.format(event_l.getEventStartTime());
			}
			%>
            <tr>
              <td width="60%" valign="top"><%= event_l.getEventTitle() %></td>
              <td width="20%" valign="top"><%= displayEventTime %>
             </td>
            <td width="10%" valign="top"><a href="timeline_editor.jsp?remove_event=true&event_id=<%= event_l.getId() %>">Remove </a></td>
            <td width="10%" valign="top"><a href="timeline_editor.jsp?edit_event=true&event_id=<%= event_l.getId() %>&timeline_id=<%= timeline_id %>">Edit </a></td>
            </td></tr> 
			<% } %>
        </table>
        <p>&nbsp; </p>
        </blockquote>
        
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      
        
        
<noscript>
        This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
    </noscript>
    </body>

 </html>