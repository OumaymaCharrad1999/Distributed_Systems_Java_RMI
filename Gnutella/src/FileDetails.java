import java.io.Serializable;
import java.util.ArrayList;

public class FileDetails implements Serializable
{
	private static final long serialVersionUID = 1L;
	String peerId;
	String fileName;
    String portNumber;
    String sourceDirectoryName;
    String messageId;
    Integer timeToLive;
    ArrayList<String>files= new ArrayList<String>();
}