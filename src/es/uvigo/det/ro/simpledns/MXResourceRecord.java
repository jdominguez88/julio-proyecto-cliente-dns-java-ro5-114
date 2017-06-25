package es.uvigo.det.ro.simpledns;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
	+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                  PREFERENCE                   |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   EXCHANGE                    /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

PREFERENCE      A 16 bit integer which specifies the preference given to
                this RR among others at the same owner.  Lower values
                are preferred.

EXCHANGE        A <domain-name> which specifies a host willing to act as
                a mail exchange for the owner name.
 */

public class MXResourceRecord extends ResourceRecord {

	private int preference;
	private DomainName exchange;

	protected MXResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);

		this.preference = Utils.int16fromByteArray(decoded.getRRData());
		this.exchange = new DomainName(
				Arrays.copyOfRange(decoded.getRRData(), 2, decoded.getRDLength()),
				message
		);
	}


	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			os.write(super.toByteArray());
			os.write(this.preference);
			os.write(this.exchange.toByteArray());
		} catch (IOException ex) {
			Logger.getLogger(NSResourceRecord.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(-1);
		}

		return os.toByteArray();
	}

	public DomainName getExchange() {
		return exchange;
	}

	public int getPreference() {
		return preference;
	}

	@Override
	public String toString() {
		return getPreference() + "\t" + getExchange().toString();
	}
}
