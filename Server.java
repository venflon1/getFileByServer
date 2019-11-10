package it.ra.getImageServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
	
	private static Logger logger = Logger.getLogger(Server.class.getName());
	
	private ServerSocket serverSocket;
	private static int PORT;
	
	private Socket socketClient;
	
	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	public Server(final int port) throws IOException {
		super();
		Server.PORT = port;
		this.serverSocket = this.initServer(Server.PORT);
		logger.info("serverSocket created, port=" + Server.PORT);
		
		this.socketClient = this.acceptClient();
		this.in = new BufferedInputStream(this.socketClient.getInputStream());
		this.out = new BufferedOutputStream(this.socketClient.getOutputStream());
		
	}
	
	private ServerSocket initServer(final int port) {
		ServerSocket serverSocket = null;
		
		try {
			 serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		return serverSocket;
	}
	
	private Socket acceptClient() {
		Socket socketClient = null;
		
		try {
			socketClient = this.serverSocket.accept();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		return socketClient;
	}

	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int pORT) {
		PORT = pORT;
	}

	public BufferedInputStream getInputStream() {
		return in;
	}

	public BufferedOutputStream getOutputStream() {
		return out;
	}
	
	public String handle() throws IOException {
		String strFileSource = "C:\\Users\\ramato\\Desktop\\img.jpg";
		File file = new File(strFileSource);
		BufferedInputStream inFile = null;
		
		if(!file.exists())
			return "il file cercato non esiste";
		else {
			try {
				inFile = new BufferedInputStream(new FileInputStream(file));

				int tmp;
				while( (tmp = inFile.read()) != -1)
					out.write(tmp);
				
				
			} catch (IOException e) {
				logger.info(e.getMessage());
			}finally {
				if(inFile != null)
					inFile.close();
				
				this.in.close();
				this.out.close();
				this.socketClient.close();
				this.serverSocket.close();
			}
		}
		return "OK";
	}
	public static void main(String[] args) {
		try {
			Server server = new Server(9876);
			server.handle();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
//	public static void main(String[] args) throws IOException {
//		String strFileSource = "C:\\Users\\ramato\\eclipse-workspace\\networking-project\\resource\\home.jpg";
//		String strFileDest = "C:\\Users\\ramato\\eclipse-workspace\\networking-project\\resource\\other-home.jpg";
//
//		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(strFileSource)));
//		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(strFileDest)));
//		
//		int tmp;
//		while( (tmp = in.read()) != -1) {
//			out.write(tmp);
//		}
//		
//		in.close();
//		out.close();
//	}
}