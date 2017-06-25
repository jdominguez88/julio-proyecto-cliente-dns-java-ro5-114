package client.resolvers;


import client.NameServer;
import client.Protocols;
import client.parsers.Parser;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.ResourceRecord;

import java.net.InetAddress;


public class ResolveCommand extends Command {

	public ResolveCommand(NameServer nameServer, Protocols protocol, Command parent) {
		super(nameServer, protocol, parent);
	}

	public ResolveCommand(NameServer nameServer, Protocols protocol) {
		super(nameServer, protocol, null);
	}

	@Override
	public boolean execute(NameServer prev, String target, RRType rrType) throws Exception {

		resolver = new Resolver(nameServer, protocol);

		Message response = resolver.query(target, rrType);

		printQuestion();

		if (!response.getAnswers().isEmpty()) {

			for (ResourceRecord resourceRecord : response.getAnswers()) {
				printAnswer(resourceRecord, nameServer);
			}

			makeTrace();

			return true;

		}

		if (response.getNameServers().size() > 0) {

			if (response.getAdditonalRecords().size() > 0) {
				for (ResourceRecord rr : response.getAdditonalRecords()) {
					if (rr.getRRType() == RRType.A) {

						printAnswer(rr, nameServer);

						if (new ResolveCommand(
								new NameServer(InetAddress.getByName(Parser.recordToString(rr))),
								protocol,
								this
						).execute(nameServer, target, rrType)) return true;

					}

				}

				if (response.getNameServers().size() > 0) {
					for (ResourceRecord rr : response.getNameServers()) {
						printAnswer(rr, nameServer);

						if (new ResolveCommand(
								new NameServer(InetAddress.getByName(Parser.recordToString(rr))),
								protocol,
								this
						).execute(nameServer, target, rrType)) return true;
					}
				}
			} else {
				System.out.println("No hay respuesta");
			}
		}
		return false;
	}
}
