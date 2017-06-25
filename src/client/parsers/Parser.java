package client.parsers;

import es.uvigo.det.ro.simpledns.*;

public class Parser {

	/**
	 * Devuelve un string con el valor obtenido en las respuestas formateado
	 * apropiadamente por cada tipo de recurso.
	 *
	 * @param rr
	 * @return
	 */

	public static String recordToString(ResourceRecord rr) {
		switch (rr.getRRType()) {
			case A:
				return ((AResourceRecord) rr).getAddress().getHostAddress();
			case AAAA:
				return ((AAAAResourceRecord) rr).getAddress().getHostAddress();
			case NS:
				return ((NSResourceRecord) rr).getNS().toString();
			case CNAME:
				return ((CNAMEResourceRecord) rr).getCanonicalName().toString();
			case MX:
				return ((MXResourceRecord) rr).toString();
			case TXT:
				return ((TXTResourceRecord) rr).toString();
			case SOA:
				return ((SOAResourceRecord) rr).toString();
			default:
				return rr.getDomain().toString();
		}

	}
}
