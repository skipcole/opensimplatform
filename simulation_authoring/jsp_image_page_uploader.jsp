<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
import="java.sql.*,java.util.*,com.oreilly.servlet.*, com.oreilly.servlet.multipart.*,java.io.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" errorPage="../error.jsp" %>

<%

        ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

        String debug = "";

		PlaceHolderPage pp = new PlaceHolderPage();
		
        try {
            new File("uploads").mkdir();
        } catch (Exception e) {

        }

        try {

            MultipartRequest mpr = new MultipartRequest(request, "uploads");

            String sending_page = (String) mpr.getParameter("sending_page");
            String submit_new_image_page = (String) mpr
                    .getParameter("submit_new_image_page");

            if ((sending_page != null) && (submit_new_image_page != null)
                    && (sending_page.equalsIgnoreCase("add_image_page"))) {
                

                debug += " pp created.";
                // pp.section_short_name = (String)
                // request.getParameter("section_short_name");
                pp.sim_id = pso.simulation.id;
                pp.name = (String) mpr.getParameter("section_short_name");
                pp.tabheading = (String) mpr.getParameter("page_tab_heading");
                pp.page_title = (String) mpr.getParameter("page_title");
                pp.description = (String) mpr.getParameter("page_description");
                // = (String) request.getParameter("section_short_name");

                String initFileName = mpr.getOriginalFileName("uploadedfile");

                if ((initFileName != null)
                        && (initFileName.trim().length() > 0)) {
                    pp.img_file_name = mpr.getOriginalFileName("uploadedfile");
                } else {
                    pp.img_file_name = "no_image_default.jpg";
                }

                if (submit_new_image_page != null) {
                    pp.store();
                }

                for (Enumeration e = mpr.getFileNames(); e.hasMoreElements();) {
                    String fn = (String) e.nextElement();

                    File thisFile = mpr.getFile(fn);

                    File outFile = new File(
                            "/home/comskipc/public_html/osp_core/images/placeholders/"
                                    + pp.img_file_name);

                    byte[] readData = new byte[1024];
                    FileInputStream fis = new FileInputStream(thisFile);

                    FileOutputStream fos = new FileOutputStream(outFile);
                    int i = fis.read(readData);

                    while (i != -1) {
                        fos.write(readData, 0, i);
                        i = fis.read(readData);
                    }
                    fis.close();
                    fos.close();

                } // End of file print outs

            } // End of if coming from this page and have added page

        } catch (Exception e) {
            debug += e.getMessage();
        }

        if (true) {
            response.sendRedirect("sf_image_page.jsp?editmode=true&actorid=" + pp.get_sf_id());
        }

	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Untitled Document</title>
</head>

<body>
<p>Hello World</p>
<p>image here: <img src="../osp_core/images/placeholders/<%= pp.img_file_name %>"  /><br>
<%= pp.description %><br>
<%= pp.name %>
</p>
<%= Debug.getDebug(debug) %>
<br />

</body>
</html>
