



import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ParseApk
{
    public static List<Apk> Apkdetail=new  ArrayList<Apk>();
    static String []Archs={"mips64","mips","x86","x86_64","arm64-v8a","armeabi-v7a","armeabi"};
    
    static String []PortF={"http://www.codeproject.com/Articles/487074/Terminal-Velocity-Android",
    	                    "https://software.intel.com/sites/campaigns/perceptualshowcase/air-piano.htm",
    	                    "https://play.google.com/store/apps/developer?id=ILab",
    	                    };
    
    static int arch_number=7;
    public static void main(String args[]) 
    {
        if(args.length < 1)
        {
            System.out.println("Use /help for help");
           
        }
       else
        {
        
        if(args.length==1)
        {
        	if(args[0].equals("/help")||args[0].equals("-help")||args[0].equals("help"))
        	{ help();
        	 return;
        	}
        	else if(args[0].equals("/port")||args[0].equals("-port")||args[0].equals("port"))
        	{port(0);
        	 return;
        	}
        	else if(args[0].equals("/port1")||args[0].equals("-port1")||args[0].equals("port1"))
        	{port(1);
        	return;
        	}
        	else if(args[0].equals("/port2")||args[0].equals("-port2")||args[0].equals("port2"))
        	{port(2);
        	return;
        	}
        }		
        int i=0;
           //  Apkdetail.clear();
        while(i<args.length)
        {
        	Apk tempApk=new Apk();
        	tempApk.detail=new ArrayList<LibDetails>();
        	tempApk.Name=args[i];
        	try{
        	ParseApkDetails(tempApk);
        	Apkdetail.add(tempApk);
        	}
        	catch(Exception e)
        	{
        		if(args.length==1&&Apkdetail.size()<1)
        		 System.out.println("Something fishy here may be missing file or wrong path use /help" );
        	}
         i++;
        }
        printapkdetails();
       }
                
    } 
    
    private static void printapkdetails() 
    {
    	
    	System.out.println("Number of Apk's analysed " +""+Apkdetail.size()+"\n");
    	if(Apkdetail.size()==0)
    	{
    	 System.out.println("This could be due to missing files or  may be due to wrong path use /help" );
    	}
    	 
    	 int i=0;
    	 while(i<Apkdetail.size())
    	 {
    		 System.out.println("Apk's Name with full path :" +""+Apkdetail.get(i).Name+"\n");
    		 System.out.println("Number Of library files in Apk = " +Apkdetail.get(i).detail.size()+"\n");
    		 int k=0;
    		 while(k<Apkdetail.get(i).detail.size())
    		 { 
    		 System.out.println("Lib Name = " +Apkdetail.get(i).detail.get(k).pathname+"\n");
    		 System.out.println("Lib Arch = " +Apkdetail.get(i).detail.get(k).arch +" , Lib Size =" +Apkdetail.get(i).detail.get(k).size +"\n");
    		 k++;
    		 }
    		 i++;
    		 System.out.println("\n");
    	 }
		
	}

    
    


	private static void help()
    {
    	System.out.println("Pass apks with full path ,if it contain space please make sure to contian path inside Quotation Marks "+"\n");
    	System.out.println("Use /port for my Jni article:Jni And C++ "+"\n");
    	System.out.println("Use /port1 for my realsense demo: (First Prize Winner)Fast prtotyping on latest technology"+"\n");
    	System.out.println("Use /port2 for my latest android apps :OpenGl Shaders"+"\n");
    }
	private static void port(int i)
    {
        try 
        {	Desktop.getDesktop().browse(new URI(PortF[i]));
        }
        catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void ParseApkDetails(Apk temp) throws Exception 
    {
	    InputStream theFile = new FileInputStream(temp.Name);
        ZipInputStream stream = new ZipInputStream(theFile);
        try
        {

            // now looping through eachh apk entry for reading filepaths and adding them to list 
            ZipEntry entry;
            while((entry = stream.getNextEntry())!=null)
            {
            	String filename=entry.getName();
            	
            	if(filename!=null)
            	 if(filename.endsWith("so")||filename.endsWith("So")||filename.endsWith("SO"))
            	  {
            	   
            	 int beginindex=filename.substring(0,filename.lastIndexOf("/")).lastIndexOf("/");	
            	 int endindex=filename.lastIndexOf("/");
            	 
            	 
            	 String Archstring= filename.substring(beginindex+1, endindex);
            	 
            	 int i=0;
            	 while(i<arch_number)
            	 {
            		 if(Archstring.equalsIgnoreCase(Archs[i]))
            			 break;
            		i++; 
            	 }
            	 
                 LibDetails tempDetail=new LibDetails();
                 if(i!=7)
                 tempDetail.arch=Archs[i];
                 else
                 tempDetail.arch="NotSure";
                 
            	 tempDetail.pathname=filename.substring(filename.lastIndexOf("/")+1,filename.length());
            	 tempDetail.size=""+entry.getSize();
            	 
            	 if(entry.getSize()==-1)
            	 tempDetail.size="Not Known" ;
            	 
            	 temp.detail.add(tempDetail);
            	 }
            }
        }
        finally
        {
            stream.close();
        }
    }
    
    
    
}