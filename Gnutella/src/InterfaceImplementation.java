import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InterfaceImplementation extends UnicastRemoteObject implements Interface    
{   
	private static final long serialVersionUID = 1L;
	private ArrayList<FileDetails> Files;
	List<String> tmpFileList= new ArrayList<String>();
	public InterfaceImplementation() throws RemoteException {    
		super();    
		Files=new ArrayList<FileDetails>();
	}    

     public synchronized void registerFiles(String peerId, String fileName,String portNumber,String sourceDirectoryName, Integer fileNameCt) throws RemoteException {
    	String tmpPeerId=null;
        String tmpFileName=null;
        String tmpPortNumber=null;
        String tmpSourceDirectoryName=null;
        tmpPeerId=peerId;
        tmpFileName=fileName;
        tmpPortNumber=portNumber;
        tmpSourceDirectoryName=sourceDirectoryName;
        tmpFileList.add(fileName);
        if(tmpFileList.size()==fileNameCt)
        {
        	FileDetails fd = new FileDetails();
        	fd.peerId=tmpPeerId;
            fd.fileName=tmpFileName;
            fd.portNumber=tmpPortNumber;
            fd.sourceDirectoryName=tmpSourceDirectoryName;
            fd.files.addAll(tmpFileList);
         	this.Files.add(fd);
         	tmpFileList=new ArrayList<String>();
        }
     }

     public ArrayList<FileDetails> search(String fileName, String messageId, Integer timeToLive) throws RemoteException {
    	 ArrayList<FileDetails> FilesMatched= new ArrayList<FileDetails>();
    	 boolean flag=false;
    	 System.out.println("File to be searched : "+fileName);
    	 String seq= messageId.substring(messageId.indexOf("+")+1, messageId.length());
    	 //System.out.println("Sequence Number : "+seq);
    	 for(int i=0;i<this.Files.size();i++) {
    		 if(this.Files.get(i).messageId!=null) {
    			 if(this.Files.get(i).messageId.contains(seq)) {   
    				 System.out.println("Message already present");
                 }
    			 else {
    				 for(int j=0;j<this.Files.get(i).files.size();j++) {
    					 if(fileName.equalsIgnoreCase(this.Files.get(i).files.get(j))) {
    						 timeToLive--;
    						 flag=true;
    						 System.out.println("The file match");
    						 FileDetails matchedFile = new FileDetails();
    						 matchedFile.messageId=messageId;
    						 matchedFile.timeToLive=timeToLive;
    						 matchedFile.fileName=fileName;
    						 matchedFile.peerId=Files.get(i).peerId;
    						 matchedFile.portNumber=Files.get(i).portNumber;
    						 matchedFile.sourceDirectoryName=Files.get(i).sourceDirectoryName;
    						 FilesMatched.add(matchedFile);
    						 System.out.println("Peer ID containg the file : "+Files.get(i).peerId);
    						 System.out.println("Port number of the peer : "+Files.get(i).portNumber);
    					 }
    				 }
    				 if(!flag) {
    					 timeToLive--;
    					 //System.out.println("Time to Live : "+timeToLive);
    				 }
    			 } 
    		 }
    		 else {
    			 for(int j=0;j<this.Files.get(i).files.size();j++) {
    				 if(fileName.equalsIgnoreCase(this.Files.get(i).files.get(j)))
    	             {
    					 timeToLive--;
    					 flag=true;
    	                 System.out.println("File match found");
    	                 FileDetails matchedFile = new FileDetails();
    	                 matchedFile.messageId=messageId;
    	                 matchedFile.timeToLive=timeToLive;
    	                 matchedFile.fileName=fileName;
    	                 matchedFile.peerId=Files.get(i).peerId;
    	                 matchedFile.portNumber=Files.get(i).portNumber;
    	                 matchedFile.sourceDirectoryName=Files.get(i).sourceDirectoryName;
    	                 FilesMatched.add(matchedFile);
    	                 System.out.println("Peer ID containg the file : "+Files.get(i).peerId);
    	                 System.out.println("Port number of the peer : "+Files.get(i).portNumber);
    	             }
    			 }
    			 if(!flag) {
    				 timeToLive--;
    	             //System.out.println("Time to Live : "+timeToLive);
    	         }
    		}
    	 }
    	 System.out.println("Number of files matched : "+FilesMatched.size());
    	 return (FilesMatched) ; 
     }

	@Override
	public void downloadFromPeer(String peerId, ArrayList<FileDetails> arr, String destinationDirectory) throws RemoteException {
		String portForAnotherClient=null;
		String sourceDirectory=null;
		String fileTobeSearched=null;
		for(int i=0;i<arr.size();i++){
			if(peerId.equalsIgnoreCase(arr.get(i).peerId)){
				portForAnotherClient=arr.get(i).portNumber;
				sourceDirectory=arr.get(i).sourceDirectoryName;
		        fileTobeSearched=arr.get(i).fileName;
		        try {
		        	Naming.lookup("rmi://localhost:"+portForAnotherClient+"/Server");
		        }
		        catch (NotBoundException ex) {
		        	Logger.getLogger(InterfaceImplementation.class.getName()).log(Level.SEVERE, null, ex);
		        }
		        catch (MalformedURLException ex) {
		            Logger.getLogger(InterfaceImplementation.class.getName()).log(Level.SEVERE, null, ex);
		        }
			}
		}
		String source = sourceDirectory+"\\"+fileTobeSearched;
		String target =destinationDirectory;
		InputStream is = null;
	    OutputStream os = null;
	    try {
	    	File srcFile = new File(source);
	        File destFile = new File(target);
	        if(!destFile.exists()){
	            destFile.createNewFile();
	        }
	        is = new FileInputStream(srcFile);
	        os = new FileOutputStream(target+"\\"+srcFile.getName());
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
        }
	    finally {
	    	try {
	            is.close();
	        } 
	    	catch (IOException ex) {
	            Logger.getLogger(InterfaceImplementation.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    	try {
	            os.close();
	        } 
	    	catch (IOException ex) {
	            Logger.getLogger(InterfaceImplementation.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	}
}