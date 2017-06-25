package client.resolvers;

import client.NameServer;
import es.uvigo.det.ro.simpledns.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPChannel extends Channel {

	private DatagramSocket socket;

	public UDPChannel(NameServer resolver) {

		super(resolver);

	}


	@Override
	public Message read() throws Exception {
		this.connect();

		byte[] buffer = new byte[MAX_PACKET_SIZE];

		DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length);
		socket.receive(replyPacket);

		// Creamos el mensaje de respuesta a partir de los datos binarios
		return new Message(replyPacket.getData());
	}

	@Override
	public void write(Message message) throws IOException {
		this.connect();

		byte[] messageFrame = message.toByteArray();

		// Abrimos un canal de comunicación UDP
		socket = new DatagramSocket();
		// Creamos un paquete. Se debe indicar el contenido, la longitud del contenido, y el destino.
		DatagramPacket requestPacket = new DatagramPacket(
				messageFrame, messageFrame.length, remoteAddress, remotePort
		);

		// Enviamos el paquete a través del canal
		socket.send(requestPacket);
	}

	@Override
	void connect() throws IOException {
		if (null == this.socket) {

			this.socket = new DatagramSocket();
//			this.socket.setSoTimeout(MAX_PACKET_SIZE);
		}
	}
}
