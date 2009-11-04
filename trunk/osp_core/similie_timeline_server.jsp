<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %><%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	response.setContentType("text/xml");
	
%><?xml version="1.0" encoding="utf-8"?>
<data>
  <event start="Nov 02 2009  07:00:00 GMT"
         end="Nov 02 2009  09:00:00 GMT"
         isDuration="true"
         title="Writing Timeline documentation"
         image="http://simile.mit.edu/images/csail-logo.gif">
    A few days to write some documentation for 
    &lt;a href="http://simile.mit.edu/timeline/"&gt;Timeline&lt;/a&gt;.
  </event>
  <event start="Oct 15 2009  00:00:00 GMT"
         end="Oct 15 2009  00:00:00 GMT"
         title="Friend's wedding">
     I'm not sure precisely when my friend's wedding is.
  </event>
    <event start="Nov 02 2009 17:08:02 EST"
         title="Text of an inject"
         link="http://travel.yahoo.com/">
To: Uzzdwaadi JRTF &lt;br/&gt; 
From: IC Staff           
Re: Letter from TRIBAL Leaders     

We have received a letter from a group of 15 tribal leaders in the Uzzdwaadi region. The letter reads in part: "When the rest of our community is suffering deprivation and hardship, it is unacceptable that any resources should be given to those that left our community and created havoc in our country." It appears that the letter has also been released to the media.
  </event>
<event start="Nov 02 2009 17:08:02 EST" end="Nov 02 2009 17:08:02 EST" title="bicyle recycle  ...">
bicyle recycle&lt;br&gt;
</event>
<event start="Nov 02 2009 17:09:54 EST" title="There is a new announcement: the city  ...">the city&lt;br&gt;</event>
</data>