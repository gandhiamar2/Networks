package Networks;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class GServer2 extends Thread{
	static String Command;
	static String filepath;
	static Socket s;
	static ObjectInputStream iss;
	static ObjectOutputStream oss;
	final static String cmnd = "200 OK";
	int port;
	

	public static void main( String[] args )
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("enter server and port no");
		String scanning = sc.nextLine();
		String array[] = scanning.split("localhost ");
		int[] port = new int[100];
		
		for (int y=0; y < array.length; y++)
		{

			port[y] = Integer.parseInt(array[y]);
		//	i= i+2;
		}
		
		try
		{       GServer2 Thread;	
		//i=1;
			for (int y=0; y < args.length; y++)
			{       Thread = new GServer2(port[y]);
			//i=i+2;
				Thread.start();
			}
		}  

		catch(Exception e)
		{
			e.printStackTrace();
			sc.close();
			
		}
	}
	public GServer2(int temp) throws IOException {
		port=temp;
		System.out.println( "Starting server on port: " + port + "\n");
		ServerSocket ss = new ServerSocket(port);
		ss.setSoTimeout(120000);
		System.out.println("running on port:"+ ss.getLocalPort());
		System.out.println("waiting for connection from client");
		System.out.println("connected to client "+s.getRemoteSocketAddress()+ " on " +ss.getLocalPort()+ " port");
	}

	public  void run(String[] args) throws IOException, ClassNotFoundException{
		//Scanner sc = new Scanner(System.in);
		//System.out.println("enter server and port no");
		//String scanning = sc.nextLine();
		//String array[] = scanning.split(" ");
		//int port = Integer.parseInt(array[1]);
		//ServerSocket ss = new ServerSocket(port);
		//System.out.println("running on port:"+ ss.getLocalPort());
		//System.out.println("waiting for connection from client");
		//s = ss.accept();
		//System.out.println("connected to client "+s.getRemoteSocketAddress()+ " on " +ss.getLocalPort()+ " port");
		oss = new ObjectOutputStream(s.getOutputStream());
		iss = new ObjectInputStream(s.getInputStream());
		try{
		String read1 =(String) iss.readObject();
		String readarray1[] = read1.split(" ");
		Command = readarray1[0];
		
		filepath = readarray1[1];
		if (Command.equals("GET"))
		{
			serverget();		
		}
		else if (Command.equals("PUT"))
		{
			serverput();
		}
		else{
			System.out.println("invalid commad");
			oss.writeObject("404 not found");
			
			
		}		
		}
		finally{
		System.out.println("thank you for using server");
		s.close();
		
		oss.close();
		iss.close();
		}
		
	}
	private static void serverput() {
		
		try{
			System.out.println("in server put method");
			oss.writeObject("200 OK");
			filepath = filepath.replace("/Client", "/Server");
			System.out.println("file name to be put:"+iss.readObject());
			File f = new File(filepath);
			System.out.println("new file directory on server:" +filepath);
			
			
				BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
				String newline = null;
				
				
				while((newline = (String) iss.readObject())  != null)
				{
					System.out.println("content on file received:" +newline);
					bwr.write(newline);
					bwr.newLine();
					bwr.flush();
					
					
				}
				bwr.close();
				System.out.println("file is received at server");
				//System.out.println();
				oss.writeObject("file is received at server");
			
			}
			catch (Exception E){
				System.out.println("CLOSING SERVER PUT");
				
			}
			
		}
	private static void serverget() {
		try{
			
		System.out.println("in server get method");
		oss.writeObject(cmnd);
		File f = new File(filepath);
		if(f.exists()){
			
		BufferedReader bfr = new BufferedReader(new FileReader(f));
		oss.writeObject(f.getName());	
		String newline = null;
		
		while((newline=bfr.readLine())!= null)
		{
			System.out.println("Content on file:"+ newline);
			oss.writeObject(newline);
			
		}
		bfr.close();
		oss.writeObject("file is transmitted to client");
		}
		else
			System.out.println("requested file not existed");
		}
		catch (Exception E){
			System.out.println("CLOSING SERVER GET");;
			
		}
		
	}
	

}
