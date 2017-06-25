package client.resolvers;


import client.NameServer;
import es.uvigo.det.ro.simpledns.Message;

import java.io.IOException;
import java.net.InetAddress;

public abstract class Channel {

	public static final int MAX_PACKET_SIZE = 512;
	protected InetAddress remoteAddress;
	protected int remotePort;

	protected Channel(NameServer resolver) {

		this.remoteAddress = resolver.getAddress();
		this.remotePort = resolver.getPort();

	}

	abstract Message read() throws Exception;

	abstract void write(Message message) throws IOException;

	abstract void connect() throws IOException;

}
