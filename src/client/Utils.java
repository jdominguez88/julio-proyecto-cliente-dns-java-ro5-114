package client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class Utils {

	/**
	 * Obtiene una suma de comprobación a partir de una cadena de caracteres
	 * P.ej: www.uvigo.esCNAME = 6c04d7366dc619cff598b1f52a03ed44
	 *
	 * @param raw
	 * @return
	 */

	public static String md5(String raw) {
		StringBuffer hexString = new StringBuffer();
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return raw;
		}

		byte[] hash = md.digest(raw.getBytes());

		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}

		return new String(hexString);
	}

}
