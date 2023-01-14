import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Interface extends Remote{
	public void registerFiles(String peerId, String fileName,String portNumber,String sourceDirectoryName) throws RemoteException; 
	public ArrayList<FileDetails> search(String fileName) throws RemoteException; 
}