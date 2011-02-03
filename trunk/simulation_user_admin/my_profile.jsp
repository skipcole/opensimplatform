<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	String error_msg = "";
	
	// Eventually we will use this insread of the AFSO here.
	SessionObjectBase sob = USIP_OSP_Util.getSessionObjectBaseIfFound(request);
		
	if ((sob == null) || (!(sob.isLoggedin()))) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	sob.handleMyProfile(request);
	
	User user = sob.giveMeUser();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>My Profile </h1>
              <br />
      <form id="form1" name="form1" method="post" action="my_profile.jsp">
  <table border="0" cellspacing="2" cellpadding="1">
    <tr>
      <td valign="top"><strong>First Name <a href="helptext/first_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="first_name" id="first_name" value="<%= user.getBu_first_name() %>" />
          </label></td>
    </tr>
    <tr>
      <td valign="top"><strong>Middle Name <a href="helptext/middle_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="middle_name" id="middle_name" value="<%= user.getBu_middle_name() %>" />
          </label></td>
    </tr>
    <tr>
      <td valign="top"><strong>Last Name <a href="helptext/last_name.jsp" target="helpinright">(?)</a>:</strong></td>
      <td valign="top">
        <label>
          <input type="text" name="last_name" id="last_name" value="<%= user.getBu_last_name() %>"  />
          </label></td>
    </tr>
    <tr>
      <td valign="top"><strong>Authorization Level:</strong></td>
      <td valign="top">
      		<% if (sob.isAdmin()) { %>Administrator, <% } %>
	  		<% if (sob.isAuthor()) { %>Simulation Author, <% } %> 
			<% if (sob.isFacilitator()) { %>Simulation Facilitator, <% } %>
Player      </td>
    </tr>
    <tr>
      <td valign="top"><strong>Email Address:</strong></td>
      <td valign="top"><%= sob.user_email %><input type="hidden" name="email" value="<%= sob.user_email %>" /> </td>
    </tr>
	    <tr>
      <td valign="top"><strong>Password:</strong></td>
      <td valign="top"><a href="change_password.jsp">Change Password </a></td>
    </tr>
    <tr>
      <td valign="top"><strong>Language:</strong></td>
      <td valign="top">
      <%
		
		String checkedEnglish = "";
		String checkedSpanish = "";
		
		if (sob.languageCode == UILanguageObject.ENGLISH_LANGUAGE_CODE){
			System.out.println("its in english");
			checkedEnglish = " selected=\"selected\" ";
		} else if (sob.languageCode == UILanguageObject.SPANISH_LANGUAGE_CODE) {
			checkedSpanish = " selected=\"selected\" ";
			System.out.println("its in spanish");
		} else {
			System.out.println("its in unknown");
		}
	  %>
        <label>
          <select name="language_id" id="select">
            <option value="1" <%= checkedEnglish %>>English</option>
            <option value="2" <%= checkedSpanish %>>Spanish</option>
          </select>
          </label>     </td>
    </tr>
        <tr>
          <td valign="top"><strong>Time Zone: </strong></td>
          <td valign="top"><select name="timezone" id="DropDownTimezone">
      <option value="-12.0">(GMT -12:00) Eniwetok, Kwajalein</option>
      <option value="-11.0">(GMT -11:00) Midway Island, Samoa</option>
      <option value="-10.0">(GMT -10:00) Hawaii</option>
      <option value="-9.0">(GMT -9:00) Alaska</option>
      <option value="-8.0">(GMT -8:00) Pacific Time (US &amp; Canada)</option>
      <option value="-7.0">(GMT -7:00) Mountain Time (US &amp; Canada)</option>
      <option value="-6.0">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>
      <option value="-5.0">(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima</option>
      <option value="-4.0">(GMT -4:00) Atlantic Time (Canada), Caracas, La Paz</option>
      <option value="-3.5">(GMT -3:30) Newfoundland</option>
      <option value="-3.0">(GMT -3:00) Brazil, Buenos Aires, Georgetown</option>
      <option value="-2.0">(GMT -2:00) Mid-Atlantic</option>
      <option value="-1.0">(GMT -1:00 hour) Azores, Cape Verde Islands</option>
      <option value="0.0">(GMT) Western Europe Time, London, Lisbon, Casablanca</option>
      <option value="1.0">(GMT +1:00 hour) Brussels, Copenhagen, Madrid, Paris</option>
      <option value="2.0">(GMT +2:00) Kaliningrad, South Africa</option>
      <option value="3.0">(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg</option>
      <option value="3.5">(GMT +3:30) Tehran</option>
      <option value="4.0">(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi</option>
      <option value="4.5">(GMT +4:30) Kabul</option>
      <option value="5.0">(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option>
      <option value="5.5">(GMT +5:30) Bombay, Calcutta, Madras, New Delhi</option>
      <option value="5.75">(GMT +5:45) Kathmandu</option>
      <option value="6.0">(GMT +6:00) Almaty, Dhaka, Colombo</option>
      <option value="7.0">(GMT +7:00) Bangkok, Hanoi, Jakarta</option>
      <option value="8.0">(GMT +8:00) Beijing, Perth, Singapore, Hong Kong</option>
      <option value="9.0">(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk</option>
      <option value="9.5">(GMT +9:30) Adelaide, Darwin</option>
      <option value="10.0">(GMT +10:00) Eastern Australia, Guam, Vladivostok</option>
      <option value="11.0">(GMT +11:00) Magadan, Solomon Islands, New Caledonia</option>
      <option value="12.0">(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka</option>
</select></td>
        </tr>
        <tr>
          <td valign="top"><strong>Phone Number: </strong></td>
          <td valign="top"><label>
            <input type="text" name="phonenumber" value="<%= user.getPhoneNumber() %>" />
          </label></td>
        </tr>
        <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top"><label>
        <input type="hidden" name="sending_page" value="my_profile" /> 
        <input type="submit" name="update" id="update" value="Update" />
        </label></td>
    </tr>
  </table>
        </form>      <p>&nbsp;</p>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>