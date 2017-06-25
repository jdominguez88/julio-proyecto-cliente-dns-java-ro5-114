package client.resolvers;

import client.NameServer;
import client.Protocols;
import client.parsers.Parser;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.ResourceRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public abstract class Command {

	protected final NameServer nameServer;
	protected final Protocols protocol;

	protected Resolver resolver;

	protected Command parent;
	protected List<Command> trace;
	protected List<ResourceRecord> usedRecords;

	public Command(NameServer nameServer, Protocols protocol, Command parent) {
		this.nameServer = nameServer;
		this.protocol = protocol;
		this.parent = parent;
		this.usedRecords = new ArrayList<>();
	}

	public Command(NameServer nameServer, Protocols protocol) {
		this(nameServer, protocol, null);
	}

	public abstract boolean execute(NameServer prev, String target, RRType rrType) throws Exception;

	protected void printQuestion() {

		System.out.println(String.format(
				"Q: %s %s\t%s\t%s",
				protocol.name(),
				nameServer.getHostAddress(),
				resolver.getQuestion().getQuestionType(),
				resolver.getQuestion().getQuestion()
		));

	}

	protected void printAnswer(ResourceRecord rr, NameServer resolver) {
		usedRecords.add(rr);

		System.out.println(
				String.format(
						"A: %s %s %s %s",
						resolver.getAddress().getHostAddress(),
						rr.getRRType(),
						rr.getTTL(),
						Parser.recordToString(rr)
				)
		);
	}

	public List<Command> getTrace() {
		return trace;
	}

	protected void setTrace(List<Command> trace) {
		this.trace = trace;
	}

	protected void makeTrace() {
		Stack<Command> stackTrace = new Stack<>();
		Command tmp = this;


		for (; tmp != null; tmp = tmp.parent) stackTrace.push(tmp);

		// En ese punto, el primer nodo es el nodo raiz
		List<Command> trace = new ArrayList<>(stackTrace);
		Collections.reverse(trace);
		stackTrace.peek().setTrace(trace);
	}

	public Resolver getResolver() {
		return resolver;
	}

	public CachedCommand toCache() {
		return new CachedCommand(this);
	}

}
