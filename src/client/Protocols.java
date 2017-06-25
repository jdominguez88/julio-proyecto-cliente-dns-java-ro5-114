package client;

/**
 * Enumeración que almacena los diferentes tipos
 * de protocolos disponibles
 */

public enum Protocols {

	TCP(1),
	UDP(2),
	CACHE(3);

	private final int id;

	Protocols(int id) {
		this.id = id;
	}
}
