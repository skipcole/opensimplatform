<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
    
	String timeline_to_show = (String) cs.getContents().get(SimilieTimelineCustomizer.KEY_FOR_DISPLAY);
	
	System.out.println(timeline_to_show);
	
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getDefault());
	
	String run_start = sdf.format(new java.util.Date());
	
	RunningSimulation running_sim = new RunningSimulation();
	
	if ((timeline_to_show != null) && (timeline_to_show.equalsIgnoreCase("show_plan")  ) ){
	
		System.out.println("We are showing the plan!");
		
		Simulation simulation = new Simulation();	
	
		if (pso.sim_id != null){
			System.out.println("pso.sim_id: " + pso.sim_id);
			simulation = pso.giveMeSim();
			run_start = sdf.format(simulation.getPhaseStartTime(pso.schema, simulation.getFirstPhaseId(pso.schema)));
		}
	} else {
		if (pso.running_sim_id != null){
			running_sim = (RunningSimulation) pso.giveMeRunningSim();
			run_start = sdf.format(running_sim.getEnabledDate());
		}
	}
		
	
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
			timeZone:       -5,
             eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "80%", 
            intervalUnit:   Timeline.DateTime.HOUR, 
            intervalPixels: 100
        }),
        Timeline.createBandInfo({
			timeZone:       -5,
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
        Timeline.loadXML("similie_timeline_server.jsp?timeline_to_show=<%= timeline_to_show %>", function(xml, url) { eventSource.loadXML(xml, url); });
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