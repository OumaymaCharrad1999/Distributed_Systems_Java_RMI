import java.util.ArrayList;

 public class PeerDetails {
	String peerId;
	String portNumber;
	String fileName;
	Integer neighbourId;
    ArrayList<String> numberOfNeighbours = new ArrayList<String>();
	
    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNeighbourId() {
        return neighbourId;
    }

    public void setNeighbourId(Integer neighbourId) {
        this.neighbourId = neighbourId;
    }
    
    public ArrayList<String> getNumberOfNeighbours() {
        return numberOfNeighbours;
    }

    public void setNumberOfNeighbours(ArrayList<String> numberOfNeighbours) {
        this.numberOfNeighbours = numberOfNeighbours;
    }
  
    public void setPeerDetails(String peerId,String portNumber,String fileName,Integer neighbourId) {
    	this.peerId = peerId;
    	this.portNumber = portNumber;
    	this.fileName = fileName;
    	this.neighbourId = neighbourId;
    }  
}