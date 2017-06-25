package client;

public class DNSClientExceptions {

	/**
	 * Esta clase encapsula las diferentes excepciones que se puedan producir
	 * durante el transcurso del programa.
	 */

	public static abstract class DNSClientException extends Exception {
		public int exitCode = 1;
	}

	public static class InvalidQueryException extends DNSClientException {
		public InvalidQueryException() {
			exitCode = 2;
		}
	}

	public static class MalformedArgumentException extends DNSClientException {
		public MalformedArgumentException() {
			exitCode = 3;
		}
	}

	public static class UnknownHostException extends DNSClientException {
		public UnknownHostException() {
			exitCode = 4;
		}
	}

	public static class ConnectionErrorException extends DNSClientException {
		public ConnectionErrorException() {
			exitCode = 5;
		}
	}

}
