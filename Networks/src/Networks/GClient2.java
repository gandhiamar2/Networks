package Networks;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class GClient2 {
	static	ObjectOutputStream os;
	static ObjectInputStream is;
	static String address ;
	int port ;
	static String command;
	static String filepath;
	static Socket s;
	static String status;
	
		public static void main(String[] args)throws Exception {
		
		
		System.out.println("type in host + port + command + filepath");
        Scanner sc = new Scanner(System.in);
        String scanning = sc.nextLine();
        
        String array[]= scanning.split(" ");
		address = array[0];
		int port = Integer.parseInt(array[1]);
		filepath = array[3];
		command = array[2];
	    s = new Socket(address, port);
		os =  new ObjectOutputStream(s.getOutputStream());
		is = new ObjectInputStream(s.getInputStream());	
		try{
			
		if(command.equals("GET")){
			
			clientget(command);
			
		}
		else if(command.equals("PUT")){
			
			clientput(command);
		}
		else{
		System.out.println("invalid command");
		}
		}
		finally{
			System.out.println("thanks for using client good bye");
			s.close();
			os.close();
			is.close();
			sc.close();
		}
	}
	private  static void clientget(String cmd)throws Exception{
		
		try{
			//System.out.println("In get client method");
			os.writeObject(cmd+ " " +filepath+ " ");			
			status = (String) is.readObject();
			if(status.equals("200 OK")){
				System.out.println("status recived from server :" + status);
				System.out.println("requesting file content:" + is.readObject() );				
				String newline =   (String) is.readObject();							
				System.out.println("file content is with client now and content is:");
				while((newline ) != null && newline.length()>0){					
					
					System.out.println(newline);
					newline = null;
					newline =   (String) is.readObject();
					
				}
								
			System.out.println(is.readObject()); 
				
			}
			else if (status.equals("404 not found"))
				System.out.println("invalid request");
		}
		catch(Exception E){

			System.out.println("Client GET CLOSING");
		}
	}
	
	private  static void clientput(String cmd)throws Exception{
		
		try{
			System.out.println("In put client method");
			os.writeObject(cmd+ " " +filepath+ " ");
			status = (String) is.readObject();
			File f = new File(filepath);
			if(status.equals("200 OK")&& f.exists()){
			System.out.println("status recived from server :" + status);
			System.out.println("transfering file name to server:"+ f.getName());
			os.writeObject(f.getName());
			BufferedReader br = new BufferedReader(new FileReader(f));
			String newline = null;
			while((newline = br.readLine())!= null)
			{
				os.writeObject(newline);
				
			}
			
			br.close();
			}
			else if (status.equals("404 not found"))
				System.out.println("invalid request");
		
		}
		catch(Exception E){

			System.out.println("Client PUT CLOSING");

		}
	}
}
