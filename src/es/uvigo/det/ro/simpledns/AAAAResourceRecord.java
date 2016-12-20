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

import static es.uvigo.det.ro.simpledns.RRType.AAAA;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Arrays;

/**
 *
 * @author Miguel Rodriguez Perez <miguel@det.uvigo.gal>
 */
public class AAAAResourceRecord extends ResourceRecord {
    private final Inet6Address addr;    

    public AAAAResourceRecord(DomainName domain, int ttl, Inet6Address addr) {
        super(domain, AAAA, ttl, 16);

        this.addr = addr;        
    }

    protected AAAAResourceRecord(ResourceRecord decoded, final byte[] rrecord, final byte[] message) throws Exception {
        super(decoded);

        int index = commonSize();

        if (getRDLength() != 16) {
            throw new Exception("Incorrect rdlength for A Resource Records");
        }

        InetAddress ad = InetAddress.getByAddress(Arrays.copyOfRange(rrecord, index, index + getRDLength()));
        if (!(ad instanceof Inet6Address)) {
            throw new Exception("Address is not a valid IPv6 Address");
        }

        addr = (Inet6Address) ad;
    }

    public final Inet6Address getAddress() {
        return addr;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(super.toByteArray());
        
        os.write(addr.getAddress());

        return os.toByteArray();
    }
}