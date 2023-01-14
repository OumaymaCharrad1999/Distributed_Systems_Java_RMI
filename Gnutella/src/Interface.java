import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Interface extends Remote{
	public void registerFiles(String peerId, String fileName,String portNumber,String sourceDirectoryName, Integer fileNamect) throws RemoteException; 
	public ArrayList<FileDetails> search(String fileName, String messageId, Integer timeToLive) throws RemoteException; 
	public void downloadFromPeer(String peerId, ArrayList<FileDetails> arr,String destinationDirectory) throws RemoteException;
}