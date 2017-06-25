package client.resolvers;

import client.NameServer;
import es.uvigo.det.ro.simpledns.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPChannel extends Channel {

	private Socket socket;

	public TCPChannel(NameServer resolver) {

		super(resolver);

	}

	@Override
	public Message read() throws Exception {
		this.connect();

		DataInputStream tcpInputStream = new DataInputStream(socket.getInputStream());
		// Los primeros 16 bits son la longitud del mensaje de respuesta que acompaña
		short offset = tcpInputStream.readShort();
		// Creamos un array de bytes de dicha longitud
		byte[] tcpInputBytes = new byte[offset];
		// Y almacenamos en él el resto del mensaje
		tcpInputStream.readFully(tcpInputBytes);

		// Finalmente creamos el objeto mensaje de respuesta a partir de los datos binarios
		return new Message(tcpInputBytes);
	}

	@Override
	public void write(Message message) throws IOException {
		this.connect();

		byte[] messageFrame = message.toByteArray();

		// Abrimos un canal. Lo que escribamos en él se transmite hacia el servidor
		DataOutputStream tcpOutputStream = new DataOutputStream(socket.getOutputStream());

		// En el protocolo TCP se envía, en 16 bits, la longitud de la consulta subsiguiente
		// el método writeShort reserva memoria para números comprendidos en 2 bytes (16 bits)
		tcpOutputStream.writeShort(messageFrame.length);
		// Escribimos el resto del mensaje
		tcpOutputStream.write(messageFrame);
	}

	@Override
	void connect() throws IOException {
		if (null == this.socket) {

			this.socket = new Socket(remoteAddress, remotePort);
			this.socket.setSoTimeout(MAX_PACKET_SIZE);
		}
	}
}
