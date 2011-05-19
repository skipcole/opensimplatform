<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.bishops.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String cs_id = (String) request.getParameter("cs_id");
	
			///////////////////////////////////////////////////////////
		SharedDocument bneeds = new SharedDocument();
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);

		String hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id " +   //$NON-NLS-1$ //$NON-NLS-2$
					" AND base_id = -1"; //$NON-NLS-1$

		List returnList = MultiSchemaHibernateUtil.getSession(pso.schema)
				.createQuery(hql_string)
				.setLong("sim_id", pso.sim_id)
				.setLong("rs_id", pso.getRunningSimId())
				.list();

			if ((returnList == null) || (returnList.size() == 0)) {
				//System.out.println("No player document found, creating new one."); //$NON-NLS-1$

				bneeds.setBase_id(new Long(-1));
				bneeds.setRs_id(pso.getRunningSimId());
				bneeds.setSim_id(pso.sim_id);
				bneeds.saveMe(pso.schema);

			} else {
				bneeds = (SharedDocument) returnList.get(0);
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
			////////////////////////////////////////
		
		SharedDocument bsolutions = new SharedDocument();
		
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);

		hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id " +   //$NON-NLS-1$ //$NON-NLS-2$
					" AND base_id = -2"; //$NON-NLS-1$

		returnList = MultiSchemaHibernateUtil.getSession(pso.schema)
				.createQuery(hql_string)
				.setLong("sim_id", pso.sim_id)
				.setLong("rs_id", pso.getRunningSimId())
				.list();

			if ((returnList == null) || (returnList.size() == 0)) {
				//System.out.println("No player document found, creating new one."); //$NON-NLS-1$

				bsolutions.setBase_id(new Long(-2));
				bsolutions.setRs_id(pso.getRunningSimId());
				bsolutions.setSim_id(pso.sim_id);
				bsolutions.saveMe(pso.schema);

			} else {
				bsolutions = (SharedDocument) returnList.get(0);
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
			//////////////////////////////////
		
%>
<html>
<head>
<title>Introduction Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<p align="center">This needs to get moved into an Nx2 column page. </p>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%" valign="top">Submitter</td>
    <td width="50%" valign="top"><h1><strong>Brainstorm Needs</strong></h1></td>
    <td width="50%" valign="top"><h1><strong>Brainstorm Solutions</strong></h1></td>
  </tr>
  <tr>
    <td valign="top">country (select color text) (edit) </td>
    <td valign="top"><%= bneeds.getBigString() %><br>
      <a href="brainstorm_data.jsp?sd_id=<%= bneeds.getId() %>">edit</a></td>
    <td valign="top"><%= bsolutions.getBigString() %><br>
      <a href="brainstorm_data.jsp?sd_id=<%= bsolutions.getId() %>">edit</a></td>
  </tr>
</table>
<form name="form1" method="post" action="">
  Add Row 
  <label>
  <input type="text" name="textfield">
(color)  </label>
  <label>
  <input type="submit" name="Submit" value="Submit">
  </label>
</form>
<p align="center">&nbsp;</p>
<p align="center">&nbsp;</p>
<p align="center">(Also need to move over <strong>Brainstorm 2 Needs</strong> documents) </p>
</body>
</html>
