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

import static es.uvigo.det.ro.simpledns.RRType.NS;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Miguel Rodriguez Perez <miguel@det.uvigo.gal>
 */
public class NSResourceRecord extends ResourceRecord {
    private final DomainName ns;

    public NSResourceRecord(DomainName domain, int ttl, DomainName ns) {
        super(domain, NS, ttl, ns.toByteArray());
        
        this.ns = ns;
    }

    protected NSResourceRecord(ResourceRecord decoded, final byte[] message) {
        super(decoded);

        ns = new DomainName(getRRData(), message);
    }

    public final DomainName getNS() {
        return ns;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        os.write(super.toByteArray());
        
        os.write(ns.toByteArray());
        
        return os.toByteArray();
    }
}
