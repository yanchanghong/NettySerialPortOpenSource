package changhongyun.NettySerialPortOpenSource.util;

public class Convert {

	/**
	 * This function converts an array of byte values to a String in hex format
	 * 
	 * @param ba
	 *            an array of byte values
	 * @return a String in hex format
	 */
	public static String ByteArrayToHexString(byte[] ba) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < ba.length; i++)
			output.append(ByteToHexString(ba[i]));
		return output.toString();
	}

	/**
	 * This function converts an array of byte values into a String with hex
	 * format
	 * 
	 * @param ia
	 *            an array of byte values
	 * @return a String with hex format
	 */

	public static String ByteToHexString(byte b) {
		if ((b & 0x80) != 0) {
			char c = (char) b;
			c &= 0x00FF;
			return Integer.toHexString(c);
		} else {
			if ((b & 0xF0) == 0)
				return "0" + Integer.toHexString(b);
			else
				return Integer.toHexString(b);
		}
	}
}
