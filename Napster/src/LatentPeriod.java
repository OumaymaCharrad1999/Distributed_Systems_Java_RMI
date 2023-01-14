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

public class LatentPeriod implements Runnable{
	String portNumber=null;
    String sourceDirectoryName=null;
    String fileTobeSearched=null;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
    
    LatentPeriod(String portNumber,String sourceDirectoryName) {
         this.portNumber=portNumber;
         this.sourceDirectoryName=sourceDirectoryName;
    }
    
    @Override
	public void run(){        
    	String peerId=null;
        String sourcePeerId="2";
        String sourcePortNumber="3002";
        String sourcefileName="TD-DigitalSignalProcessing-Chapter1.pdf";
        String sourceDirectoryName="C:\\Users\\charr\\Downloads\\Test_3";
        long responseTime=0;
        long endTime=0;
		try {
			Interface obj=null;    
            try {
            	obj =(Interface) Naming.lookup("Hello");
            } 
            catch (NotBoundException ex) {
            	Logger.getLogger(LatentPeriod.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (MalformedURLException ex) {
            	Logger.getLogger(LatentPeriod.class.getName()).log(Level.SEVERE, null, ex);
            }                      
            // Fetch all the files from the current directory (client)
            File directoryList = new File(sourceDirectoryName);
            String[] store = directoryList.list();
            int counter=0;     
            while(counter<store.length) {
            	File currentFile = new File(store[counter]);
                try {
                	obj.registerFiles(peerId, currentFile.getName(),portNumber,sourceDirectoryName);
                } 
                catch (RemoteException ex) {
                	Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                counter++;
            }            
            // Search for the file (100 times)
            for(int i=0;i<100;i++) {
            	long startTime = System.currentTimeMillis();
            	downloadFromPeer(sourcePeerId,sourcePortNumber,sourcefileName,sourceDirectoryName);
            	endTime= System.currentTimeMillis()-startTime;
            	responseTime=responseTime+endTime;
            	System.out.println("Tracking Count "+i);
            }
            System.out.println("Response Time is : "+responseTime+" ms");
            long latentPeriod=responseTime/100;
            System.out.println("Latent Period is : "+latentPeriod+" ms");
		} 
		catch (RemoteException ex) {
			Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } 
		catch (NotBoundException ex) {
			Logger.getLogger(LatentPeriod.class.getName()).log(Level.SEVERE, null, ex);
		} 
		catch (IOException ex) {
			Logger.getLogger(LatentPeriod.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
		
    public void downloadFromPeer(String peerId,String portNumber,String fileName,String directoryName) throws NotBoundException, RemoteException, MalformedURLException, IOException{
    	String source = directoryName+"\\"+fileName;
    	String target = sourceDirectoryName;
        InputStream is = null;
        OutputStream os = null;
        try {
        	File srcFile = new File(source);
        	File destFile = new File(target);
        	System.out.println("File : "+destFile);
        	if(!destFile.exists()) {
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
    
    public static void main(String [] args) throws IOException, NotBoundException{
         String portNumber="4000";
         String sourceDirectoryName = "C:\\Users\\charr\\Downloads\\Test_2";   
         try {
        	 LocateRegistry.createRegistry(Integer.parseInt(portNumber)); 
        	 Client fi = new ClientImplementation(sourceDirectoryName);
        	 System.out.println("Directory name : "+sourceDirectoryName);
        	 Naming.rebind("rmi://localhost:"+portNumber+"/Server", fi);
         } 
         catch(Exception e) {
        	 System.err.println("Server exception: "+ e.getMessage());
        	 e.printStackTrace();
         }
         new LatentPeriod(portNumber,sourceDirectoryName).run();
	}
}