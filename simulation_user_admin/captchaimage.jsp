<%@ page import="java.util.*, 
	java.io.*,
	java.awt.*,
	java.awt.image.*,
	javax.imageio.*,
	java.awt.geom.*"%>
<%

	System.setProperty("java.awt.headless", "true");


 response.setContentType("image/jpg");
  /* Define number characters contains the captcha image, declare global */
  int iTotalChars= 4;

  /* Size image iHeight and iWidth, declare globl */
  int iHeight=40;
  int iWidth=170;

  /* font style */
  Font fntStyle1 = new Font("Arial", Font.BOLD, 30);
  Font fntStyle2 = new Font("Verdana", Font.BOLD, 20);

  /* Possible random characters in the image */
  Random randChars = new Random();
  String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0,iTotalChars);
  
  	// Just to make the thing a bit more readable.
  	sImageCode = sImageCode.replaceAll("l", "L");
	sImageCode = sImageCode.replaceAll("I", "i");

  /* BufferedImage is used to create a create new image*/
  /* TYPE_INT_RGB - does not support transpatency, TYPE_INT_ARGB - support transpatency*/
  BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
  Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();

  // Draw background rectangle and noisey filled round rectangles
  int iCircle = 10;
  g2dImage.fillRect(0, 0, iWidth, iHeight);
  for ( int i = 0; i < iCircle; i++ )
  {
    //g2dImage.setColor(new Color(randChars.nextInt(255),randChars.nextInt(255),randChars.nextInt(255)));
	g2dImage.setColor(new Color(randChars.nextInt(55)+ 200,randChars.nextInt(55) + 200 ,randChars.nextInt(55) + 200));
    int iRadius = (int) (Math.random() * iHeight / 2.0);
    int iX = (int) (Math.random() * iWidth - iRadius);
    int iY = (int) (Math.random() * iHeight - iRadius);
    g2dImage.fillRoundRect(iX, iY, iRadius * 2, iRadius * 2,100,100);
  }
  g2dImage.setFont(fntStyle1);
  for ( int i = 0; i < iTotalChars; i++ )
  {
    //g2dImage.setColor(new Color(randChars.nextInt(255),randChars.nextInt(255),randChars.nextInt(255)));
	g2dImage.setColor(new Color(randChars.nextInt(155),randChars.nextInt(155),randChars.nextInt(155)));
    if (i%2==0)
      g2dImage.drawString(sImageCode.substring(i,i+1),25*i,24);
    else
      g2dImage.drawString(sImageCode.substring(i,i+1),25*i,35);
  }

  /* create jpeg image and display on the screen*/
  OutputStream osImage = response.getOutputStream();
  ImageIO.write(biImage, "jpeg", osImage);
  osImage.close();

  /* Dispose function is used destory an image object */
  g2dImage.dispose();

  session.setAttribute("dns_security_code", sImageCode);
  
  out.clear();

	out = pageContext.pushBody(); 
  
%>