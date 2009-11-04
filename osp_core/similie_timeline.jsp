<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.text.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	RunningSimulation running_sim = new RunningSimulation();
	if (pso.running_sim_id != null){
		running_sim = (RunningSimulation) pso.giveMeRunningSim();
	}
	
	// Date expected in format similar to: "Oct 15 2009 00:00:00 GMT"
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
	
	String run_start = sdf.format(running_sim.getEnabledDate());
		
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
        <script>Timeline_urlPrefix = "http://static.simile.mit.edu/timeline/api-2.3.0/";;</script>
        <script src="http://static.simile.mit.edu/timeline/api-2.3.0/timeline-api.js?bundle=true" type="text/javascript"></script>
        <script>
        var tl;
        function onLoad() {
        var eventSource = new Timeline.DefaultEventSource();
        var bandInfos = [
        Timeline.createBandInfo({
             eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "80%", 
            intervalUnit:   Timeline.DateTime.HOUR, 
            intervalPixels: 100
        }),
        Timeline.createBandInfo({
            overview:       true,
            eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "20%", 
            intervalUnit:   Timeline.DateTime.DAY, 
            intervalPixels: 200
        })
        ];
        bandInfos[1].syncWith = 0;
        bandInfos[1].highlight = true;
        tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
        Timeline.loadXML("similie_timeline_server.jsp", function(xml, url) { eventSource.loadXML(xml, url); });
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
    </head>

    <body onLoad="onLoad();" onResize="onResize();">
        <div id="my-timeline" style="height: 300px; border: 1px solid #aaa"></div>
        <noscript>
        This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
        </noscript>
    </body>

 </html>