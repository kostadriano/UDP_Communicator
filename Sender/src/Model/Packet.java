package Model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class Packet {

	private String ip;
	private int port;
	private String message;

	public Packet(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static byte[] makePacket(String packet) {
		int slength = packet.length();
		byte[] pkt = new byte[slength + 5];

		pkt[0] = (byte) 0x80;

		pkt[2] = (byte) slength;
		pkt[1] = (byte) (slength >> 8);

		byte temp[] = packet.getBytes();

		for (int i = 0; i < slength; i++)
			pkt[i + 3] = temp[i];

		pkt[pkt.length - 2] = (byte) 0;
		pkt[pkt.length - 1] = (byte) 0;

		return pkt;
	}

	public static void sendPacket(Packet Obpacket, byte[] packet,DatagramSocket serverdgram) throws IOException {
		InetAddress addr = InetAddress.getByName(Obpacket.getIp());
		int port = Obpacket.getPort();

		DatagramPacket pkg = new DatagramPacket(packet, packet.length, addr, port);

		serverdgram.send(pkg);
	}
	public static boolean validate(byte[] packet) {
		if (packet[0] == (byte) 0x80)
			return true;
		return false;
	}

	public static String breakMessage(byte[] packet) throws UnsupportedEncodingException {
		int sizeMessage = (int) packet[1];
		sizeMessage <<= 8;
		sizeMessage |= (int) packet[2];

		byte[] message = new byte[sizeMessage];

		for (int i = 0; i < sizeMessage; i++) {
			message[i] = packet[i + 3];
		}

		String returnMessage = new String(message, "UTF-8");
		return returnMessage;
	}
}
