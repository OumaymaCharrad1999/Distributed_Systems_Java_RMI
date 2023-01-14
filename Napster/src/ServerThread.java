import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;    

public class ServerThread implements Runnable   
{   
	public void run()    
	{    
		try   
		{
			LocateRegistry.createRegistry(1099);    
			Interface hello = new InterfaceImplementation();    
			Naming.rebind("Hello", hello);    
			System.out.println("Hello! Server is ready.");    
		}    
		catch (Exception e)    
		{    
			System.out.println("Hello! Server failed: " + e);    
		}    
	}    
   
	public static void main(String [] args)    
	{    
		new ServerThread().run();
	} 
}   