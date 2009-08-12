<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
errorPage="../error.jsp" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);
		
	afso.handleProcessCustomPage(request);
	
	response.sendRedirect(afso.backPage);
	return;
	
%>
