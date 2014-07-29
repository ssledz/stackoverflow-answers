package pl.softech.stackoverflow.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Vector3fSerializationExample {

	static class Vector3f {

		double x, y, z;

		Vector3f() {
		}

		Vector3f(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public String toString() {
			return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
		}

	}

	static class Vector3fSubclass2 extends Vector3f implements Serializable {
		
		private static final long serialVersionUID = 1L;

		Vector3fSubclass2() {
		}

		Vector3fSubclass2(double x, double y, double z) {
			super(x, y, z);
		}
	}

	static class Vector3fSubclass extends Vector3f implements Serializable {

		private static final long serialVersionUID = 1L;

		Vector3fSubclass() {
		}

		Vector3fSubclass(double x, double y, double z) {
			super(x, y, z);
		}

		private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
			ois.defaultReadObject();

			x = ois.readDouble();
			y = ois.readDouble();
			z = ois.readDouble();

		}

		private void writeObject(ObjectOutputStream oos) throws IOException {
			oos.defaultWriteObject();

			oos.writeDouble(x);
			oos.writeDouble(y);
			oos.writeDouble(z);

		}

	}

	static class SerializationUtil {

		static Object deserialize(byte[] buff) throws IOException, ClassNotFoundException {

			ByteArrayInputStream in = new ByteArrayInputStream(buff);
			ObjectInputStream ois = new ObjectInputStream(in);
			Object obj = ois.readObject();
			ois.close();
			return obj;
		}

		static byte[] serialize(Object obj) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
			out.close();
			return out.toByteArray();

		}
	}

	public static void main(String[] args) throws Throwable {

		Vector3f v3f = new Vector3fSubclass(1, 2, 3);
		byte[] arr = SerializationUtil.serialize(v3f);
		System.out.println(SerializationUtil.deserialize(arr));
		
		v3f = new Vector3fSubclass2(1, 2, 3); //subclass without needed methods
		arr = SerializationUtil.serialize(v3f);
		System.out.println(SerializationUtil.deserialize(arr));
	}

}
