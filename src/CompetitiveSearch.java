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
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompetitiveSearch implements Runnable{
	String portNumber=null;
    String sourceDirectoryName=null;
    String fileTobeSearched=null;
    String peerId=null;
    long responseTime=0;
    long startTime=0;
    long endTime=0;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
    
    CompetitiveSearch(String portNumber,String sourceDirectoryName,String peerId){
         this.portNumber=portNumber;
         this.sourceDirectoryName=sourceDirectoryName;
         this.peerId=peerId;
    }
	
    public void run(){  
		try {    
		  Interface obj=(Interface) Naming.lookup("Hello");    
		  File directoryList = new File(sourceDirectoryName);
		  String[] store = directoryList.list();
          int counter=0;
          while(counter<store.length){
        	  File currentFile = new File(store[counter]);
              try {
            	  obj.registerFiles(peerId, currentFile.getName(),portNumber,sourceDirectoryName);
              } 
              catch (RemoteException ex) {
            	  Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
              }
              counter++;
          } 
          // Search for the file
          startTime = System.nanoTime();  
          downloadFromPeer("C:\\Users\\charr\\Downloads\\Test_4","4","DS-MobileNetworkProtocols-2012.pdf");
          endTime= System.nanoTime()-startTime;
          responseTime=responseTime+endTime;
          System.out.println("Response Time is " + responseTime + " ns for the download from peer " + peerId);  
		}    
		catch (Exception e) {    
		   System.out.println("Client exception: " + e);    
		}       
	}
    
    public void downloadFromPeer(String directoryName,String peerId,String fileName) throws NotBoundException, RemoteException, MalformedURLException, IOException{
    	String source=directoryName+"\\"+fileName;
    	String target=sourceDirectoryName;
        InputStream is = null;
        OutputStream os = null;
        try {
        	File srcFile = new File(source);
        	File destFile = new File(target);
        	System.out.println("File : "+destFile);
        	if(!destFile.exists())
        	{
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
    String clientPortNumber1="5000";
    String sourceDirectoryName1="C:\\Users\\charr\\Downloads\\Test_1";
    String clientPortNumber2="5001";
    String sourceDirectoryName2="C:\\Users\\charr\\Downloads\\Test_2";
    String clientPortNumber3="5002";
    String sourceDirectoryName3="C:\\Users\\charr\\Downloads\\Test_3";
    try{
         LocateRegistry.createRegistry(Integer.parseInt(clientPortNumber1)); 
         Client fi1 = new ClientImplementation(sourceDirectoryName1);
         Naming.rebind("rmi://localhost:"+clientPortNumber1+"/Server", fi1);
         LocateRegistry.createRegistry(Integer.parseInt(clientPortNumber2)); 
         Client fi2= new ClientImplementation(sourceDirectoryName2);
         Naming.rebind("rmi://localhost:"+clientPortNumber2+"/Server", fi2);
         LocateRegistry.createRegistry(Integer.parseInt(clientPortNumber3)); 
         Client fi3 = new ClientImplementation(sourceDirectoryName3); 
         Naming.rebind("rmi://localhost:"+clientPortNumber3+"/Server", fi3);         
    } 
    catch(Exception e) {
         System.err.println("Server exception : "+ e.getMessage());
         e.printStackTrace();
    }
    Thread thread1 = new Thread(new CompetitiveSearch(clientPortNumber1,sourceDirectoryName1,"1"));  
    thread1.start();
    Thread thread2 = new Thread(new CompetitiveSearch(clientPortNumber2,sourceDirectoryName2,"2"));  
    thread2.start();
    Thread thread3 = new Thread(new CompetitiveSearch(clientPortNumber3,sourceDirectoryName3,"3")); 
    thread3.start();
	}  
}