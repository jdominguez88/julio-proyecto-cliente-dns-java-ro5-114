package client;


import client.resolvers.Command;
import client.resolvers.ResolveCommand;
import es.uvigo.det.ro.simpledns.RRType;

import java.util.Scanner;

public class Client {

	private NameServer nameServer;
	private Protocols protocol;

	public Client(Protocols protocol, NameServer nameServer) {

		this.nameServer = nameServer;
		this.protocol = protocol;

	}

	public NameServer getNameServer() {
		return nameServer;
	}

	public Protocols getProtocol() {
		return protocol;
	}

	public void run() {

		// Creamos el objeto para leer desde la entrada estándar
		Scanner inputScanner = new Scanner(System.in);
		// Creamos el objeto para gestionar la caché
		CacheSystem cacheManager = CacheSystem.getInstance();

		// Mientras no se pulse CNTRL-C ...
		while (inputScanner.hasNext()) {
			// Se actualiza la caché
			cacheManager.update();

			// Leemos los escrito por pantalla
			String rawQuery = inputScanner.nextLine();
			// Creamos el objeto que engloba el tipo de consulta y el dominio
			QueryArgs queryArgs = QueryArgs.parse(rawQuery);
			// Creamos un puntero a un resolvedor de consultas

			try {

				if (cacheManager.has(queryArgs)) {

					cacheManager.get(queryArgs).execute(null, "", RRType.A);


				} else {

					Command command = new ResolveCommand(nameServer, protocol);

					boolean success = command.execute(
							nameServer,
							queryArgs.getTargetDomain(),
							queryArgs.getRRType()
					);

					if (success) cacheManager.put(queryArgs, command.toCache());

				}


			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
