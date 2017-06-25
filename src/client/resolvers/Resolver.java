package client.resolvers;


import client.NameServer;
import client.Protocols;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.RRType;

public class Resolver {

	private Channel channel;
	private NameServer nameServer;
	private boolean recursionDesired = false;

	private Message question, anwser;

	public Resolver(NameServer nameServer, Protocols protocol) {

		this(nameServer, protocol, false); // Iterative client

	}

	private Resolver(NameServer nameServer, Protocols protocol, boolean requestRecursion) {

		this.nameServer = nameServer;
		this.channel = this.nameServer.newChannel(protocol);
		this.recursionDesired = requestRecursion;

	}

	public Message query(String target, RRType rrType) throws Exception {

		question = new Message(target, rrType, recursionDesired);

		channel.write(question);

		anwser = channel.read();

		return anwser;

	}

	public Message getQuestion() {
		return question;
	}
}
