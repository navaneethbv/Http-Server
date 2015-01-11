//HttpServer.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;


public class HttpServer
{
     public static void main(String [] args)
     {
          int i=1;

System.out.println("*****************************************************
**************************");
          System.out.println("*****************************  HTTP
SERVER
***********************************");

System.out.println("*****************************************************
**************************");
          System.out.println("Server Started....");
          System.out.println("Waiting for connections....");
          try
          {

               ServerSocket s = new ServerSocket(100);
               for(;;)
               {
                    Socket incoming = s.accept();
                    System.out.println("New Client Connected with id - "
+ i
+" from "+incoming.getInetAddress().getHostName() );
                    System.out.println("
");
                    System.out.println("
REQUEST - HEADER                                    ");
                    Thread t = new ThreadedServer(incoming,i);
                    i++;
                    t.start();
               }
          }
          catch(Exception e)
          {
               System.out.println("Error:  " + e);
          }
     }
}



class ThreadedServer extends Thread
{
     final static String CRLF = "
";
     Socket incoming;
     int counter;
     public ThreadedServer(Socket i,int c)
     {
          incoming=i;
          counter=c;
     }

     public void run()
     {
          try
          {
               String statusline=null;
               String contenttypeline=null;
               String contentlength=null;
               String venderline="Server: EXECUTER 1.1";
               String entitybody=null;
               BufferedReader in =new BufferedReader(new
InputStreamReader(incoming.getInputStream()));
               PrintWriter out = new
PrintWriter(incoming.getOutputStream(), true);
               OutputStream output=incoming.getOutputStream();
               String headerline;
               headerline=in.readLine();
               System.out.println(headerline);
               /*String reqh;


               boolean done=false;
               while(!done)
               {
                    reqh=in.readLine();
                    if(reqh == null)
                         done = true;
                    else
                    {
                         //out.println("Server>>> " + headerline);
                         System.out.println(reqh);
                    }
               }


*/

  	       StringTokenizer s = new StringTokenizer(headerline);
	       String meth = s.nextToken();
               if(meth.equals("GET")||meth.equals("POST"))
               {
                int dot1,dot2,fslash;
                String fname,ext,FileName;
                String url = s.nextToken();

                dot1=url.indexOf('.');
                dot2=url.lastIndexOf('.');
		fslash=url.lastIndexOf('/');
  		fname=url.substring(dot1+1,dot2);
                ext=url.substring(dot2,fslash);
                FileName=fname+ext;
//    System.out.println("FNAME:"+FileName);
                if(ext.equals(".html")||ext.equals(".htm"))
                {
                 FileInputStream fis=null;
                 boolean filexists=true;
                 try
                   {
                    fis=new FileInputStream(FileName);
                   }
                 catch(FileNotFoundException e)
                   {
                    System.out.println("Exception: "+e.getMessage());
                    filexists=false;
                   }



                if(filexists)
                {
                 statusline=" HTTP/1.1 200 Ok"+CRLF;
                 contenttypeline="Content-Type: text/html "+CRLF;
                 contentlength="Content-Length:"+(new
Integer(fis.available())).toString() + CRLF;
                }
                else
                {

		 statusline = "HTTP/1.0 404 Not Found" + CRLF ;
		 contenttypeline = "Content-Type: text/html"+CRLF ;
		 entitybody = "<HTML>" +
			    "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
			    "<BODY><H1>404 File Not Found</H1></BODY></HTML>" ;
                }

/*                System.out.println("
RESPONCE HEADER                                    ");

                System.out.println(statusline);
                System.out.println(venderline);
                System.out.println(contentlength);
                System.out.println(contenttypeline);*/

                 output.write(statusline.getBytes());
                 output.write(venderline.getBytes());
                 output.write(contentlength.getBytes());
                 output.write(contenttypeline.getBytes());
                 output.write(CRLF.getBytes());


                 if (filexists)
		    {
			sendBytes(fis, output) ;
			fis.close();
		    }
		else
		    {
			output.write(entitybody.getBytes());
		    }

                }
                else
               {
                 statusline = "HTTP/1.0 400 Not Found" + CRLF ;
		 contenttypeline = "Content-Type: text/html"+CRLF ;
		 entitybody = "<HTML>" +
			    "<HEAD><TITLE>400</TITLE></HEAD>" +
			    "<BODY><H1>400 A malformed HTTP request is
reived</H1></BODY></HTML>";
               }

               }

                else
                 {
                  statusline = "HTTP/1.0 400 Not Found" + CRLF ;
		  contenttypeline = "Content-Type: text/html"+CRLF ;
		  entitybody = "<HTML>" +
			    "<HEAD><TITLE>400</TITLE></HEAD>" +
		            "<BODY><H1>400 A malformed HTTP request is
reived</H1></BODY></HTML>";
                 }

         boolean done=false;
         while(!done)
         {
          headerline=in.readLine();
          if(headerline == null)
           done = true;
          else
          {
           System.out.println(headerline);
          }
         }

         incoming.close();
         in.close();
         out.close();
          }
          catch(Exception e)
          {
               System.out.println("Error: " + e);
          }
     }
 private static void sendBytes(FileInputStream fis, OutputStream os)
	throws Exception
    {
	byte[] buffer = new byte[1024] ;
	int bytes = 0 ;

	while ((bytes = fis.read(buffer)) != -1 )
	    {
		os.write(buffer, 0, bytes);
	    }
    }


}

