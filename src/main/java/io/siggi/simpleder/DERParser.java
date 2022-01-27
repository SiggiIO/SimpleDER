package io.siggi.simpleder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DERParser {
	private DERParser() {
	}
	public static <T extends DERObject> T parse(byte[] data) {
		try {
			return parse(new ByteArrayInputStream(data));
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	public static <T extends DERObject> T parse(InputStream in) throws IOException {
		int type = in.read();
		if (type == -1) return null;
		int length = Util.readDerLength(in);
		if (length > 1048576) {
			throw new IllegalArgumentException("DER data too large");
		}
		byte[] data = Util.read(in, length);
		if (data.length != 0) {
			try {
				DERList list = new DERList(type);
				ByteArrayInputStream listIn = new ByteArrayInputStream(data);
				DERObject object;
				while ((object = parse(listIn)) != null) {
					list.items.add(object);
				}
				if (listIn.available() == 0)
					return (T) list;
			} catch (Exception e) {
			}
		}
		return (T) new DERData(type, data);
	}
}
