package io.siggi.simpleder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class DERData extends DERObject {
	private int type;
	private byte[] data;

	public DERData(int type, byte[] data) {
		if (data == null) throw new NullPointerException();
		this.type = type;
		this.data = data;
	}

	@Override
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		if (data == null) throw new NullPointerException();
		this.data = data;
	}

	@Override
	public String toString() {
		boolean isPossiblyText = true;
		if (data.length == 0)
			isPossiblyText = false;
		else
			for (int i = 0; i < data.length; i++) {
				if (data[i] < 0x20 && data[i] != 0x0A && data[i] != 0x0D && data[i] != 0x09) {
					isPossiblyText = false;
					break;
				}
			}
		if (isPossiblyText) {
			try {
				String text = new String(data, StandardCharsets.UTF_8);
				byte[] backToData = text.getBytes(StandardCharsets.UTF_8);
				if (Arrays.equals(data, backToData)) {
					return "0x" + Integer.toString(type, 16) + ": text:" + text;
				}
			} catch (Exception e) {
			}
		}
		return "0x" + Integer.toString(type, 16) + ": hex:" + Util.hex(data);
	}
}
