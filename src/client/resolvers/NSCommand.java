package client.resolvers;


import client.NameServer;
import client.Protocols;
import client.parsers.Parser;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.ResourceRecord;

import java.net.InetAddress;

public class NSCommand extends Command {
	public NSCommand(NameServer nameServer, Protocols protocol) {
		super(nameServer, protocol, null);
	}

	public NSCommand(NameServer nameServer, Protocols protocol, Command parent) {
		super(nameServer, protocol);
	}

	@Override
	public boolean execute(NameServer prev, String target, RRType rrType) throws Exception {

		Resolver resolver = new Resolver(prev, protocol);

		Message response = resolver.query(target, rrType);

		printQuestion();

		if (!response.getAnswers().isEmpty()) {

			response.getAnswers().forEach(resourceRecord -> printAnswer(resourceRecord, prev));

			makeTrace();

			return true;
		}

		if (response.getNameServers().size() > 0) {
			for (ResourceRecord rr : response.getNameServers()) {
				printAnswer(rr, prev);

				NameServer nextResolver = new NameServer(InetAddress.getByName(Parser.recordToString(rr)));

				if (new NSCommand(
						nextResolver,
						protocol,
						this
				).execute(nextResolver, target, rrType)) return true;
			}
		} else {
			System.out.println("No hay respuesta");
		}

		return false;
	}


}


