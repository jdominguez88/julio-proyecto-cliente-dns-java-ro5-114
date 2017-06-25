package client;

import es.uvigo.det.ro.simpledns.RRType;

public class QueryArgs {

	/**
	 * Esta clase encapsula los parámetros escritos por consola. Por ejemplo:
	 * <p>
	 * A www.uvigo.es
	 * <p>
	 * targetDomain = targetDomain;
	 * rrType = A
	 */

	private String targetDomain;

	private RRType rrType;

	public QueryArgs(RRType rrType, String targetDomain) {

		this.rrType = rrType;
		this.targetDomain = targetDomain;

	}

	public static QueryArgs parse(String rawQueryString) {
		rawQueryString = rawQueryString.trim();

		if (rawQueryString.length() < 5) {
			throw new IllegalArgumentException("Malformed query");
		}
		if (rawQueryString.indexOf(" ") < 0) {
			throw new IllegalArgumentException("Malformed query");
		}

		String[] aux = rawQueryString.split(" ");
		String domain = aux[1];
		String rrType = aux[0];

		boolean rrTypeExists = false;
		for (RRType tmpRRType : RRType.values()) {
			if (tmpRRType.name().equals(rrType)) {
				rrTypeExists = true;
				break;
			}
		}

		if (!rrTypeExists) {
			throw new IllegalArgumentException("Unknown Resource Record Type");
		}

		return new QueryArgs(RRType.valueOf(rrType), domain);
	}

	public String getTargetDomain() {
		return targetDomain;
	}

	public RRType getRRType() {
		return rrType;
	}

	/**
	 * Devuelve una cadena md5 a partir de los datos
	 *
	 * @return
	 */

	public String getKey() {
		return Utils.md5(targetDomain.concat(rrType.name()));
	}
}