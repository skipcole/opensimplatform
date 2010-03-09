<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	/*
	if (!(afso.isLoggedin())) {
		return;
	} */
	
	//<li onclick="fill(’something‘);">something</li> 
	
	
	String q = request.getParameter("q");
	
	System.out.println("   q is: " + q);
	
	response.setHeader("Cache-Control", "no-cache");
	
	ArrayList outList = new ArrayList();
	
	for (Enumeration e = USIP_OSP_Cache.getAutocompleteUserNames(afso.schema, request).keys(); e.hasMoreElements();) {
			String s = (String) e.nextElement();
			      System.out.println("                " + s);
			
			if ((q != null) && ((s.toLowerCase().indexOf(q.toLowerCase()) != -1))) {
				System.out.println("found match!  " + s);
				outList.add(s);
			}
	}
	
	for (ListIterator<String> li = outList.listIterator(); li.hasNext();){
			String s = li.next(); %><%= s %><% } // End of loop over matches %>