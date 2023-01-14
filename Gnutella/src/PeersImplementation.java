import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

class PeersImplementation{
	String result = "";
	InputStream inputStream;
    ArrayList<PeersImplementation> listOfPeers = new ArrayList<PeersImplementation>();
    ArrayList<PeerDetails> finalList=new ArrayList<PeerDetails>();
    Scanner readIndividualFile = null;
    
    public ArrayList<PeerDetails> getPeerDetails() throws IOException {
    	ArrayList<PeerDetails> tokenzisedList = new ArrayList<PeerDetails>();
        String filepath="C:\\Users\\charr\\Downloads\\StarTopology.txt";
        readIndividualFile = new Scanner(new File(filepath));
        while((readIndividualFile.hasNextLine())) {
        	String sb = readIndividualFile.nextLine();
        	if (!sb.startsWith("*") && sb.length() != 0) {
        		if (!sb.contains("Peer")) {
        			tokenzisedList = getTokenzisedList (sb);   
        		}
        	}
        }   
        readIndividualFile.close();
        return tokenzisedList;     
    }
                           
    private  ArrayList<PeerDetails> getTokenzisedList(String sb) {
    	String[] tokens = sb.split("\\t");
    	ArrayList<String> extractedList = new ArrayList<String>(Arrays.asList(tokens));
    	ArrayList<String> tmpList = new ArrayList<String>();
    	for (int i = 0; i < extractedList.size(); i++) {
    		if (extractedList.get(i).length() != 0) {
    			tmpList.add(extractedList.get(i));
    		}
    	}
    	PeerDetails p =new PeerDetails();
        System.out.println("Peer ID : "+tmpList.get(0));
        p.setPeerId(tmpList.get(0));
        System.out.println("Port Number : "+tmpList.get(1));
        p.setPortNumber(tmpList.get(1));
        System.out.println("Directory Path : "+tmpList.get(2));
        p.setFileName(tmpList.get(2));
        getNeighbours(tmpList.get(3));
        p.setNeighbourId(getNeighbours(tmpList.get(3)).size());
        p.setNumberOfNeighbours(getNeighbours(tmpList.get(3)));
        for(int i=0;i<getNeighbours(tmpList.get(3)).size();i++) {
        	System.out.println("Neighbouring node is "+getNeighbours(tmpList.get(3)).get(i));
        }
        finalList.add(p);
        return finalList;
    }
              
     public ArrayList<String> getNeighbours(String ngh) { 
    	 String[] tokens = ngh.split(",");
    	 ArrayList<String> neighbrList = new ArrayList<String>( Arrays.asList(tokens));
         return neighbrList;
     }
}