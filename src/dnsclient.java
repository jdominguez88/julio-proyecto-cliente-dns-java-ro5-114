import client.Client;
import client.DNSClientArgs;
import client.NameServer;
import client.Protocols;

import java.net.InetAddress;


public class dnsclient {

	/**
	 * Objeto que representa la aplicación
	 */

	public static void main(String[] args) {

		// Parseamos los argumentos enviados al programa (-t o -u y dirección IP)
		DNSClientArgs argsParser = new DNSClientArgs(args);

		// Si son correctos
		if (argsParser.parse()) {

			Protocols protocol = argsParser.getProtocol();
			InetAddress resolverAddress = argsParser.getResolverAddress();

			// Ejecutamos la aplicación enviando el protocolo y el servidor de nombres
			// de dominio inicial
			new Client(protocol, new NameServer(resolverAddress)).run();
//
//			try {
//
//				/
//
//			} catch (DNSClientExceptions.DNSClientException e) {
//
//				// Si se produce alguna excepción, terminamos el programa con
//				// un código de error representativo.
//				System.exit(e.exitCode);
//
//			} catch (Exception e) {
//
//				// Para cualquier otro tipo de error, mostramos la traza
//				// Descomentar la línea para ver los errores
//				//e.printStackTrace();
//
//			}

		} else {

			// si no, mostramos ayuda
			DNSClientArgs.printHelp();

		}
	}

}
