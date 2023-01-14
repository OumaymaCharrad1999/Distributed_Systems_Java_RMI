import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplementation extends UnicastRemoteObject implements Client {
	private static final long serialVersionUID = 1L;
	private String directoryName;
	
	public ClientImplementation(String s) throws RemoteException{
      super();
      directoryName = s;
	}

	public byte[] downloadFile(String fileName){
		try {
			File file = new File(directoryName+"/"+fileName);
			byte buffer[] = new byte[(int)file.length()];
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(directoryName+"//"+fileName));
			input.read(buffer,0,buffer.length);
			input.close();
			return(buffer);
		} 
		catch(Exception e){
			System.out.println("ClientImplementation: "+e.getMessage());
			e.printStackTrace();
			return(null);
		}
   }
}