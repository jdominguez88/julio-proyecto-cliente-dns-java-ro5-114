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
import java.util.logging.Level;
import java.util.logging.Logger;

/*
	+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    /                   TXT-DATA                    /
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

where:

TXT-DATA        One or more <character-string>s.
*/

public class TXTResourceRecord extends ResourceRecord {
	private String text = "";

	protected TXTResourceRecord(ResourceRecord decoded) {
		super(decoded);

		text = new String(Arrays.copyOfRange(decoded.getRRData(), 1, decoded.getRDLength()));
	}

	public final String getText() {
		return text;
	}

	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			os.write(super.toByteArray());
			os.write(text.getBytes());
		} catch (IOException ex) {
			Logger.getLogger(TXTResourceRecord.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(-1);
		}

		return os.toByteArray();
	}

	@Override
	public String toString() {
		return getText();
	}
}
