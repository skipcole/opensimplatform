<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.io.*,java.sql.*,java.util.*,org.usip.osp.graphs.*,org.jfree.chart.*;" 
	errorPage="" %>
<%
		response.setContentType("image/png"); //$NON-NLS-1$
        response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
        response.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
        response.setHeader("Expires", "-1"); //$NON-NLS-1$ //$NON-NLS-2$
		
		Chart cci = new Chart();
        
        cci.setThis_chart(ChartDisplayHelper.getDemoChart());

        ChartDisplayHelper.writeChartAsPNG(response.getOutputStream(), cci);
%>