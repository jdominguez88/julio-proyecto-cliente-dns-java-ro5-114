package es.uvigo.det.ro.simpledns;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
	+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                     CNAME                     /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

CNAME           A <domain-name> which specifies the canonical or primary
                name for the owner.  The owner name is an alias.

*/

public class CNAMEResourceRecord extends ResourceRecord {
	private DomainName canonicalName;

	protected CNAMEResourceRecord(DomainName domain, RRType type, int ttl, byte[] rrdata) {
		super(domain, type, ttl, rrdata);
	}

	protected CNAMEResourceRecord(ResourceRecord decoded, final byte[] message) throws Exception {
		super(decoded);

		canonicalName = new DomainName(decoded.getRRData(), message);
	}

	public DomainName getCanonicalName() {
		return canonicalName;
	}

	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			os.write(super.toByteArray());
			os.write(canonicalName.toByteArray());
		} catch (IOException ex) {
			Logger.getLogger(CNAMEResourceRecord.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(-1);
		}

		return os.toByteArray();
	}
}
