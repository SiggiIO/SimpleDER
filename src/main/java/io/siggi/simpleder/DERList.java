package io.siggi.simpleder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class DERList extends DERObject {
	public final List<DERObject> items = new ArrayList<>();
	private int type;

	public DERList() {
		this(0x30);
	}

	public DERList(int type) {
		this.type = type;
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
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (DERObject entry : items) {
				out.write(entry.getEncoded());
			}
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(0, sb);
		return sb.toString();
	}

	private void indent(int indent, StringBuilder sb) {
		for (int i = 0; i < indent; i++) {
			sb.append("  ");
		}
	}

	private void toString(int indent, StringBuilder sb) {
		indent(indent, sb);
		sb.append("0x" + Integer.toString(type, 16) + ": [\n");
		for (DERObject object : items) {
			if (object instanceof DERList) {
				DERList list = (DERList) object;
				list.toString(indent + 1, sb);
				sb.append("\n");
			} else {
				indent(indent + 1, sb);
				sb.append(object.toString()).append("\n");
			}
		}
		indent(indent, sb);
		sb.append("]");
	}
}
