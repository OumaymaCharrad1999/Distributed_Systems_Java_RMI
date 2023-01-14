import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class InterfaceImplementation extends UnicastRemoteObject implements Interface    
{   
	private static final long serialVersionUID = 1L;
	private ArrayList<FileDetails> Files;
	public InterfaceImplementation() throws RemoteException {    
		super();    
		Files=new ArrayList<FileDetails>();
	}    
 
     public synchronized void registerFiles(String peerId, String fileName,String portNumber,String sourceDirectoryName) throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerId=peerId;
        fd.fileName=fileName;
        fd.portNumber=portNumber;
        fd.sourceDirectoryName=sourceDirectoryName;
    	this.Files.add(fd);
        System.out.println("File name "+fd.fileName+" registered with peerID "+fd.peerId+" on port number "+fd.portNumber+" and the directory is "+fd.sourceDirectoryName);
     }

     public ArrayList<FileDetails> search(String fileName) throws RemoteException {
    	 ArrayList<FileDetails> FilesMatched= new ArrayList<FileDetails>();
    	 for(int i=0;i<this.Files.size();i++) {
            if(fileName.equalsIgnoreCase(Files.get(i).fileName))
            {
                FilesMatched.add(Files.get(i));
            }
    	 }
    	 return (FilesMatched) ; 
     }
}    