package io.siggi.simpleder;

import java.io.ByteArrayOutputStream;

public abstract class DERObject {
	DERObject() {
	}

	public abstract int getType();

	public abstract byte[] getData();

	public byte[] getEncoded() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(getType());
			byte[] data = getData();
			Util.writeDerLength(out, data.length);
			out.write(data);
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
