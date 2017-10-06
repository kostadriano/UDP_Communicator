/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Packet;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Adriano Costa
 */
public class InterfaceController implements Initializable {

    @FXML
    private TextArea textMessage;
    @FXML
    private TextField textIp;
    @FXML
    private TextField textPor;
    @FXML
    private Button btnSend;
    @FXML
    private TextArea textLog;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void BtnSendOnActions(ActionEvent event) throws IOException {
        if ((textPor.getText().equals("")) || (textIp.getText().equals("")) || (textMessage.getText().equals(""))) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro 0: Campo Vazio");
            alert.setContentText("Por favor preencha todos os campos.");
            alert.showAndWait();
        } else {
            String ip = textIp.getText();
            int port = Integer.parseInt(textPor.getText());
            String message = textMessage.getText();

            Packet packet = new Packet(ip, port);
            byte[] pkg = Packet.makePacket(message);
            DatagramSocket serverdgram = new DatagramSocket();
            Packet.sendPacket(packet, pkg, serverdgram);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            textLog.appendText("[" + (sdf.format(new Date())) + "] Send To: " + ip + "\n");

            byte[] receiveData = new byte[1024];
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
                            serverdgram.receive(receivePacket);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (Packet.validate(receiveData)) {
                            String confirm = null;
                            try {
                                confirm = Packet.breakMessage(receiveData);
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            textLog.appendText(confirm);
                        } else {
                            textLog.appendText("Erro de Pacote\n");
                        }
                    }
                }
            });
            thread.start();
        }
    }
}
