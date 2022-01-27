package io.siggi.simpleder;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

class Util {
	private Util() {
	}

	public static int readDerLength(InputStream in) throws IOException {
		int length = in.read();
		if (length == -1) throw new EOFException();
		if (length >= 128) {
			if (length == 128) {
				throw new UnsupportedEncodingException("Indefinite length");
			}
			int octetCount = length - 128;
			length = 0;
			for (int i = 0; i < octetCount; i++) {
				length <<= 8;
				int value = in.read();
				if (value == -1) throw new EOFException();
				length += value;
			}
		}
		return length;
	}

	public static void writeDerLength(OutputStream out, int value) throws IOException {
		if (value < 0)
			throw new IllegalArgumentException("Negative number");
		if (value < 128) {
			out.write(value);
			return;
		}
		byte[] data = new byte[4];
		int start = 4;
		do {
			start -= 1;
			data[start] = (byte) (value & 0xff);
			value >>>= 8;
		} while (value != 0);
		int length = 4 - start;
		out.write(length + 128);
		out.write(data, start, length);
	}

	private static final char[] hexCharset = "0123456789abcdef".toCharArray();

	public static String hex(byte[] data) {
		char[] hex = new char[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			hex[i * 2] = hexCharset[(((int) data[i]) >> 4) & 0xf];
			hex[(i * 2) + 1] = hexCharset[((int) data[i]) & 0xf];
		}
		return new String(hex);
	}

	public static byte[] unhex(String hex) {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException("Invalid hex data");
		}
		byte[] data = new byte[hex.length() / 2];
		try {
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i + 1) * 2), 16);
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid hex data");
		}
		return data;
	}

	public static byte[] read(InputStream in, int length) throws IOException {
		byte[] bytes = new byte[length];
		int read = 0;
		while (read < length) {
			int c = in.read(bytes, read, length - read);
			if (c == -1) throw new EOFException();
			read += c;
		}
		return bytes;
	}
}
