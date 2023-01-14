import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable{
	public static  PeersImplementation properties= new PeersImplementation();  
    public void run() { 
    	long responseTime = 0;
    	long endTime=0;
    	long latentPeriod=0;
    	try   
    	{
    		String fileTobeSearched=null;
    		Integer sequenceId=0;
    		String peerId=null;
    		String messageId=null;
    		Integer timeToLive=null;
    		String destinationDirectory=null;
    		System.out.println("Total Number of nodes in the network are " + properties.finalList.size());
        	LocateRegistry.createRegistry(1099);
    		LocateRegistry.createRegistry(Integer.parseInt(properties.finalList.get(0).getPortNumber()));
    		Interface obj = new InterfaceImplementation();    
    		Naming.rebind("Hello", obj);     
    		System.out.println("Ready to send Query Message"); 
    		System.out.println("Sending query message to neighbouring nodes of Peer : 1");
    		// Set parameters of the server for file search
    		timeToLive=properties.finalList.get(0).getNeighbourId();
    		peerId=properties.finalList.get(0).getPeerId();
    		destinationDirectory=properties.finalList.get(0).getFileName();
    		// Clients registration
            for(int i=1;i<properties.finalList.size();i++) {
            	LocateRegistry.createRegistry(Integer.parseInt(properties.finalList.get(i).getPortNumber())); 
            	Client fi = new ClientImplementation(properties.finalList.get(i).getFileName());
            	System.out.println("Sending query message to neighbouring nodes of Peer : "+properties.finalList.get(i).getPeerId());
            	Naming.rebind("rmi://localhost:"+properties.finalList.get(i).getPortNumber()+"/Server", fi);
            	// Files registration
            	File directoryList = new File(properties.finalList.get(i).getFileName());
            	String[] store = directoryList.list();
            	int counter=0; 
            	while(counter<store.length){
            		File currentFile = new File(store[counter]);
                    try {
                    	obj.registerFiles(properties.finalList.get(i).getPeerId(), currentFile.getName(),properties.finalList.get(i).getPortNumber(),properties.finalList.get(i).getFileName(),store.length);
                    } 
                    catch (RemoteException ex) {}
                    counter++;
                }   
            }
            // Search for file - peer ID 1 - Star Topology
            ArrayList<FileDetails> arr = new ArrayList<FileDetails>();
            fileTobeSearched="DS-OpticalCommunications-2017.docx";
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String fileSrchOption=null;
            System.out.print("Do you want to search for a file ? (Yes or No)\n");
            try {
            	fileSrchOption = br.readLine();
            } 
            catch (IOException ex) {
            	Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(fileSrchOption.equalsIgnoreCase("Yes")) {
                sequenceId++;
                messageId=peerId.concat("+").concat(sequenceId.toString());
                try {
                   arr=obj.search(fileTobeSearched, messageId, timeToLive);
                   System.out.println("Following are the hit query messages");
                   for(int i = 0; i < arr.size(); i++) {
                	   //System.out.println("Hit query message are : "+"\n"+"Peer ID : "+arr.get(i).peerId+"\t"+" Port Number : "+arr.get(i).portNumber+"\t"+" File Name : "+arr.get(i).fileName+"\t"+" Message ID : "+arr.get(i).messageId+"\t"+" Time to Live : "+arr.get(i).timeToLive);
                	   System.out.println("Hit query message are : "+"\n"+"Peer ID : "+arr.get(i).peerId+"\t"+" Port Number : "+arr.get(i).portNumber+"\t"+" File Name : "+arr.get(i).fileName);
                   }
                   for(int i = 0; i < arr.size(); i++) {
                	   System.out.println("Peer ID's having the given file is : "+arr.get(i).peerId);
                   }
                } 
                catch (RemoteException ex) {
                   Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String PeerToDownload=null;
                System.out.println("Enter the peer ID you want to connect and download the file from : ");
                try {
                	PeerToDownload = br1.readLine();
                } 
                catch (IOException ex) {
                   Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                	obj.downloadFromPeer(PeerToDownload,arr,destinationDirectory);
                	System.out.println("File downloaded successfully");
                	System.out.println("Do you want to continue searching ? (Yes or No)");
                	BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                	fileSrchOption = br2.readLine();
                    if(fileSrchOption.equalsIgnoreCase("Yes")) {
                    	sequenceId++;
                    	messageId=peerId.concat("+").concat(sequenceId.toString());
                        try {
                        	arr = new ArrayList<FileDetails>();
                        	arr=obj.search(fileTobeSearched, messageId, timeToLive);
                        	System.out.println("Following are the hit query messages");
                            for(int i = 0; i < arr.size(); i++) {
                            	//System.out.println("Hit query message are"+"\n"+"PeerID "+arr.get(i).peerId+"\t"+"Port Number "+arr.get(i).portNumber+"\t"+"FileName "+arr.get(i).fileName+"\t"+"Message ID"+arr.get(i).messageId+"\t"+"Time to Live "+arr.get(i).timeToLive);
                         	   	System.out.println("Hit query message are : "+"\n"+"Peer ID : "+arr.get(i).peerId+"\t"+" Port Number : "+arr.get(i).portNumber+"\t"+" File Name : "+arr.get(i).fileName);

                            }
                        } 
                        catch (RemoteException ex) {
                        	Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                        	BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
                            String peerDwnld=null;
                            System.out.println("Enter the peer ID you want to connect and download the file from ");
                            peerDwnld = br3.readLine();
                            obj.downloadFromPeer(peerDwnld,arr,destinationDirectory);
                            System.out.println("File downloaded successfully \n");
                            System.out.println("Latent Period Calculating");
                            for(int i=0;i<100;i++) {
                            	long startTime = System.currentTimeMillis(); 
                            	arr = new ArrayList<FileDetails>();
                            	arr=obj.search(fileTobeSearched, messageId, timeToLive);
                            	for(int j = 0; j < arr.size(); j++) {
                            		obj.downloadFromPeer(peerDwnld,arr,destinationDirectory);
                            		endTime= System.currentTimeMillis()-startTime;
                            		responseTime=responseTime+ endTime;
                            	}
                            	latentPeriod=responseTime/100;
                            }
                            System.out.println("Response Time is "+responseTime+" ms");
                            System.out.println("Latent Period is "+latentPeriod+" ms");
                            exit(0);
                         }
                         catch(Exception e) {
                        	 e.printStackTrace();
                         }                       
                    }
                    else {
                    	System.out.print("Thank you!");
                        exit(0);
                    }
                } 
                catch (RemoteException ex) {
                   Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                System.out.print("Thank you!");
                exit(0);
            }
    	}     
    	catch (Exception e) {   
          e.printStackTrace();
    	}    
    }  
  
    public static void main(String [] args) throws IOException {   
    	properties.getPeerDetails();
    	new ServerThread().run();
    } 
}