package es.uvigo.det.ro.simpledns;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
	+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                     MNAME                     /
    /                                               /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                     RNAME                     /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    SERIAL                     |
    |                                               |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    REFRESH                    |
    |                                               |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                     RETRY                     |
    |                                               |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    EXPIRE                     |
    |                                               |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    MINIMUM                    |
    |                                               |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

MNAME           The <domain-name> of the name server that was the
                original or primary source of data for this zone.

RNAME           A <domain-name> which specifies the mailbox of the
                person responsible for this zone.

SERIAL          The unsigned 32 bit version number of the original copy
                of the zone.  Zone transfers preserve this value.  This
                value wraps and should be compared using sequence space
                arithmetic.

REFRESH         A 32 bit time interval before the zone should be
                refreshed.

RETRY           A 32 bit time interval that should elapse before a
                failed refresh should be retried.

EXPIRE          A 32 bit time value that specifies the upper limit on
                the time interval that can elapse before the zone is no
                longer authoritative.

MINIMUM         The unsigned 32 bit minimum TTL field that should be
                exported with any RR from this zone.
 */

public class SOAResourceRecord extends ResourceRecord {
	private DomainName mName;
	private DomainName rName;
	private int serial;
	private int refresh;
	private int retry;
	private int expire;
	private int minimum;


	protected SOAResourceRecord(ResourceRecord decoded, final byte[] message) {
		super(decoded);

		final byte[] buffer = decoded.getRRData();
		int offset = 0;

		mName = new DomainName(buffer, message);

		offset += mName.getEncodedLength();

		rName = new DomainName(
				Arrays.copyOfRange(buffer, offset, buffer.length),
				message
		);

		offset += rName.getEncodedLength();

		serial = Utils.int32fromByteArray(Arrays.copyOfRange(buffer, offset, buffer.length));
		refresh = Utils.int32fromByteArray(Arrays.copyOfRange(buffer, offset + 4, buffer.length));
		retry = Utils.int32fromByteArray(Arrays.copyOfRange(buffer, offset + 8, buffer.length));
		expire = Utils.int32fromByteArray(Arrays.copyOfRange(buffer, offset + 12, buffer.length));
		minimum = Utils.int32fromByteArray(Arrays.copyOfRange(buffer, offset + 16, buffer.length));

	}

	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			os.write(super.toByteArray());
			os.write(mName.toByteArray());
			os.write(rName.toByteArray());
			os.write(serial);
			os.write(refresh);
			os.write(retry);
			os.write(expire);
			os.write(minimum);
		} catch (IOException ex) {
			Logger.getLogger(TXTResourceRecord.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(-1);
		}

		return os.toByteArray();
	}

	@Override
	public String toString() {
		return String.format(
				"%s\n\tmail exchanger: %s\n\tserial: %s\n\trefresh: %s\n\tretry: %s\n\texpire: %s\n\tminimum: %s",
				mName, rName, serial, refresh, retry, expire, minimum
		);
	}
}
