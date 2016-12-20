/*
 * Copyright (C) 2016 Miguel Rodriguez Perez <miguel@det.uvigo.gal>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.det.ro.simpledns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Miguel Rodriguez Perez <miguel@det.uvigo.gal>
 */
public class ResourceRecord {

    static public ResourceRecord createResourceRecord(final byte[] rrecord, final byte[] message) throws Exception {
        ResourceRecord temp = new ResourceRecord(rrecord, message);

        switch (temp.getRRType()) {
            case A:
                return new AResourceRecord(temp);
            case AAAA:
                return new AAAAResourceRecord(temp);
            case NS:
                return new NSResourceRecord(temp, message);
            default:
                return temp;
        }
    }

    private final DomainName domain;
    private final RRType rrtype;
    private final RRClass rrclass;
    private final int ttl;
    private final int rdlength;
    private final byte[] rrdata;

    protected ResourceRecord(DomainName domain, RRType type, int ttl, final byte[] rrdata) {
        this.domain = domain;
        this.rrtype = type;
        this.rrclass = RRClass.IN;
        this.ttl = ttl;
        this.rdlength = rrdata.length;
        this.rrdata = rrdata;
    }

    protected ResourceRecord(final byte[] record, final byte[] message) throws Exception {
        byte[] buffer = record;

        domain = new DomainName(record, message);
        buffer = Arrays.copyOfRange(buffer, domain.getEncodedLength(), record.length);

        rrtype = RRType.fromByteArray(buffer);
        buffer = Arrays.copyOfRange(buffer, 2, record.length);

        rrclass = RRClass.fromByteArray(buffer);
        buffer = Arrays.copyOfRange(buffer, 2, record.length);

        ttl = Utils.int32fromByteArray(buffer);
        buffer = Arrays.copyOfRange(buffer, 4, record.length);

        rdlength = Utils.int16fromByteArray(buffer);

        rrdata = Arrays.copyOfRange(buffer, 2, 2 + rdlength);
    }

    protected ResourceRecord(ResourceRecord copy) {
        this.domain = copy.domain;
        this.rrtype = copy.rrtype;
        this.rrclass = copy.rrclass;
        this.ttl = copy.ttl;
        this.rdlength = copy.rdlength;
        this.rrdata = copy.rrdata;
    }

    public int getEncodedLength() {
        return commonSize() + getRDLength();
    }

    protected byte[] toByteArray() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(commonSize());

        os.write(domain.toByteArray());
        os.write(rrtype.toByteArray());
        os.write(rrclass.toByteArray());
        os.write(Utils.int32toByteArray(ttl));
        os.write(Utils.int16toByteArray(rdlength));

        return os.toByteArray();
    }

    protected final int commonSize() {
        return domain.getEncodedLength() + 2 + 2 + 4 + 2; // type + class + ttl + rdlength
    }

    public final DomainName getDomain() {
        return domain;
    }

    public final RRType getRRType() {
        return rrtype;
    }

    public final RRClass getRRClass() {
        return rrclass;
    }

    public final int getTTL() {
        return ttl;
    }

    public final int getRDLength() {
        return rdlength;
    }

    public final byte[] getRRData() {
        return rrdata;
    }
}
