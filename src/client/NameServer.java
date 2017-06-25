package client;

import client.resolvers.Channel;
import client.resolvers.TCPChannel;
import client.resolvers.UDPChannel;

import java.net.InetAddress;

public class NameServer {

	/**
	 * Ésta clase sirve para encapsular los datos del servidor
	 * hacia el cuál lanzaremos las consultas
	 */

	/**
	 * El puerto por defecto del protocolo DNS es el 53
	 */
	public static final int DEFAULT_DNS_PORT = 53;

	/**
	 * Consta de una dirección IP
	 */

	private InetAddress address;

	/**
	 * Consta de un puerto. 53 por defecto
	 */
	private int port;

	public NameServer(InetAddress resolverAddress, int port) {
		this.address = resolverAddress;
		this.port = port;
	}

	public NameServer(InetAddress resolverAddress) {

		this(resolverAddress, DEFAULT_DNS_PORT);

	}

	public InetAddress getAddress() {
		return address;
	}

	public String getHostAddress() {
		return address.getHostAddress();
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return this.address.toString() + ":" + this.port;
	}

	public Channel newChannel(Protocols protocol) {
		switch (protocol) {
			case TCP:
				return new TCPChannel(this);
			case UDP:
				return new UDPChannel(this);
			default:
				return null;
		}
	}


}
