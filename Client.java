package it.ra.getImageServer;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class Client {
	private static Logger logger = Logger.getLogger(Client.class.getName());
	
	private Socket socket;
	private String serverAddress;
	private int serverPort;
	
	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	public Client(String serverAddress, int serverPort) throws IOException {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		
		this.socket = this.connectServer(this.serverAddress, this.serverPort);
		
		this.in = new BufferedInputStream(this.socket.getInputStream());
//		this.out = new BufferedOutputStream(this.socket.getOutputStream());
	}
	
	private Socket connectServer(String serverAddress, int serverPort) {
		Socket sock = null;
		
		try {
			sock = new Socket(serverAddress, serverPort);
			logger.info("CLIENT connected to server:" + serverAddress);
		} catch (UnknownHostException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sock;
	}

	public BufferedInputStream getInputStream() {
		return in;
	}

	public BufferedOutputStream getOutputStream() {
		return out;
	}
	
	public void downloadImage() throws IOException {
		String fileOut = "C:\\Users\\ramato\\Desktop\\download.jpg";
		BufferedOutputStream outFile = null;
		
		try {
			outFile = new BufferedOutputStream(new FileOutputStream(new File(fileOut)));
			
			int fileImg;
			while( (fileImg = this.in.read()) != -1)
				outFile.write(fileImg);
			
			logger.info("download done..");
				
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}finally {
			if(outFile != null)
				outFile.close();
			
			this.in.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client("localhost", 9876);
			client.downloadImage();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}
