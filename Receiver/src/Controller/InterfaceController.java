package Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import Model.packet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InterfaceController {

	@FXML
	private TextArea textMessage;

	@FXML
	private TextArea textLog;

	@FXML
	private Button BtnStart;

	@FXML
	private TextField textPort;

	private DatagramSocket socket;

	@FXML
	void BtnStartOnAction(ActionEvent event) throws IOException, InterruptedException {
		byte[] receiveData = new byte[1024];
		int port = Integer.parseInt(textPort.getText());
		socket = new DatagramSocket(port);

		Thread thread = new Thread(new Runnable() {
			public void run() {

				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					try {
						socket.receive(receivePacket);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (packet.validate(receiveData)) {
						String message = null;
						try {
							message = new String(packet.breakMessage(receiveData));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
						textMessage.appendText("["+(sdf.format(new Date()))+"] "+message+"\n");
						String ip = receivePacket.getAddress().toString();
			            
			            textLog.appendText("["+(sdf.format(new Date())) + "] Receive From: "+ip+" Port: "+port +"\n");
			            String ipReceived = null;
						try {
							ipReceived = new String(InetAddress.getLocalHost().getHostAddress().toString());
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	  
						String temp = new String("["+ipReceived+"] Message Received\n");
						byte[] responseMessage = packet.makePacket(temp);
						
						DatagramPacket pkg = new DatagramPacket(responseMessage, responseMessage.length, receivePacket.getAddress(), receivePacket.getPort());

						try {
							socket.send(pkg);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		
			       
					} else {
						textMessage.setText("ERRO NO PACOTE\n");
					}
				}
			}
		});
		thread.start();
	}
}
