package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSClientArgs {

	private String[] args;

	/**
	 * La dirección IP a ser consultada
	 */

	private InetAddress resolverIpAddress;

	/**
	 * El protocolo indicado
	 */
	private Protocols protocol;

	public DNSClientArgs(String[] rawArgs) {
		this.args = rawArgs;
	}

	/**
	 * Si los parámetros indicados al ejecutar el programa son incorrectos, se muestro
	 * una ayuda
	 */

	public static void printHelp() {
		System.out.println("DNSCLient v0.0.1\n");
		System.out.println("Usage: dnsclient [-t | -u] resolverIpAddress\n");
		System.out.println("-t\t\t: Make request over TCP protocol");
		System.out.println("-u\t\t: Make request over UDP protocol");
		System.out.println("resolverIpAddress\t\t: IP address used to query as the root DNS server.");
	}

	public InetAddress getResolverAddress() {
		return resolverIpAddress;
	}

	public Protocols getProtocol() {
		return protocol;
	}

	/**
	 * Lee los argumentos enviados al programa y comprueba que son válidos. En ese caso
	 * los almacena modelizados
	 *
	 * @return
	 */

	public boolean parse() {
		if (args.length < 2) {
			return false;
		}
		// First expected argument: client.resolvers.Protocols

		switch (args[0]) {
			case "-t":
				this.protocol = Protocols.TCP;
				break;
			case "-u":
				this.protocol = Protocols.UDP;
				break;
			default:
				return false;
		}
		try {
			this.resolverIpAddress = InetAddress.getByName(args[1]);
		} catch (UnknownHostException e) {
			return false;
		}

		return true;
	}
}