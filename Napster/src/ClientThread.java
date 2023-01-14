import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread implements Runnable{
    String portNumber=null;
    String sourceDirectoryName=null;
    String fileTobeSearched=null;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
    
    ClientThread(String portNumber,String sourceDirectoryName){
         this.portNumber=portNumber;
         this.sourceDirectoryName=sourceDirectoryName;
    }
    
	public void run(){        
		String peerID=null;
		try   
		{    
			Interface hello = (Interface) Naming.lookup("Hello");   
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the peer ID");
			peerID = br.readLine();
			File directoryList = new File(sourceDirectoryName);
			String[] store = directoryList.list();
            int counter=0;
            while(counter<store.length) {
            	File currentFile = new File(store[counter]);
                try {
                	hello.registerFiles(peerID, currentFile.getName(),portNumber,sourceDirectoryName);
                } 
                catch (RemoteException ex) {
                	Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                counter++;
            } 
            // Search Method
            ArrayList<FileDetails> arr = new ArrayList<FileDetails>();
            System.out.println("Enter the file name to be searched"); 
            while((fileTobeSearched=br.readLine())!=null){  
            	arr=hello.search(fileTobeSearched);
                for(int i = 0; i < arr.size(); i++) {
                	System.out.println("Peer ID's having the given file are "+arr.get(i).peerId);
                }
                System.out.println("Enter the ID of the peer you want to connect");
                peerID= br.readLine();
                downloadFromPeer(peerID,arr);
                break;
            }
		}    
		catch (Exception e)    
		{    
		   System.out.println("Client exception: " + e);    
		}       
	}
	
	public void downloadFromPeer(String peerId,ArrayList<FileDetails> arr) throws NotBoundException, RemoteException, MalformedURLException, IOException {
		String sourceDirectory=null;
		for(int i=0;i<arr.size();i++){
			if(peerId.equals(arr.get(i).peerId)){
				sourceDirectory=arr.get(i).sourceDirectoryName;
			}
		}
		String source = sourceDirectory+"\\"+fileTobeSearched;
		String target =sourceDirectoryName;
        InputStream is = null;
        OutputStream os = null;
        try {
        	File srcFile = new File(source);
        	File destFile = new File(target);
        	System.out.println("File "+destFile);
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
        	is.close();
        	os.close();
        }
  }
	
	public static void main(String [] args) throws IOException{
		BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
		String portNumber=null;
		System.out.println("Enter the port number on which peer needs to be registered ");
		portNumber=inp.readLine();
        System.out.println("Enter the directory path ");
        String directoryName = inp.readLine();  
        try{
        	LocateRegistry.createRegistry(Integer.parseInt(portNumber)); 
        	Client fi = new ClientImplementation(directoryName);
        	System.out.println("Directory name : "+directoryName);
        	Naming.rebind("rmi://localhost:"+portNumber+"/Server", fi);
        } 
        catch(Exception e) {
        	System.err.println("Server exception: "+ e.getMessage());
        	e.printStackTrace();
        }
        new ClientThread(portNumber,directoryName).run();
	}
}