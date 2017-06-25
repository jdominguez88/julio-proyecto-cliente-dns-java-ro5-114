package client.resolvers;

import client.CacheSystem;
import client.NameServer;
import client.Protocols;
import es.uvigo.det.ro.simpledns.Message;
import es.uvigo.det.ro.simpledns.RRType;
import es.uvigo.det.ro.simpledns.ResourceRecord;

import java.util.Comparator;
import java.util.List;

public class CachedCommand extends Command implements CacheSystem.Cacheable {

	private int TTL;
	private long storedAt;

	private Command rootCommand;

	public CachedCommand(Command command) {
		super(command.nameServer, command.protocol, null);
		this.rootCommand = command;

		// Almacenamos cuándo se produjo la consulta
		this.storedAt = System.currentTimeMillis() / 1000;
		// Calcula el mínimo TTL del recurso indicado
		this.TTL = lowestTTL();
		// Con éstos datos, podremos borrar de la caché respuestas que hayan
		// excedido el TTL y así mantener datos coherentes
	}


	private int lowestTTL() {
		int lowestValue = Integer.MAX_VALUE;
		List<Command> trace = this.rootCommand.getTrace();

		for (Command command : trace) {
			int lTTL = command.usedRecords.stream().min(
					Comparator.comparing(ResourceRecord::getTTL)
			).get().getTTL();

			if (lTTL < lowestValue) {
				lowestValue = lTTL;
			}
		}

		return lowestValue;
	}


	@Override
	public boolean execute(NameServer prev, String target, RRType rrType) throws Exception {
		List<Command> trace = this.rootCommand.getTrace();

		for (Command command : trace) {
			printQuestion(command.nameServer, command.getResolver().getQuestion());
			command.usedRecords.forEach(urr -> printAnswer(urr, command.nameServer));
		}

		return false;
	}

	@Override
	public CachedCommand toCache() {
		return null;
	}

	@Override
	public boolean hasExpired() {
		return (System.currentTimeMillis() / 1000) >= (storedAt + TTL);
	}

	@Override
	public int getTL() {
		return TTL;
	}


	protected void printQuestion(NameServer server, Message question) {

		System.out.println(String.format(
				"Q: %s %s\t%s\t%s",
				Protocols.CACHE,
				server.getHostAddress(),
				question.getQuestionType(),
				question.getQuestion()
		));

	}
}
