package sys;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * -Xmx256 -XX:MaxDirectMemorySize=100M -Dsun.nio.MaxDirectMemorySize=50000000<br>
 * or -Xmx256<br>
 * NOTE: ָ��-Dsun.nio.MaxDirectMemorySizeû��������
 * 
 * @author liumengjun
 *
 */
public class MaxDirectMemorySize {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, SecurityException,
	        IllegalArgumentException, IllegalAccessException {
		System.out.println("maxDirectMemory:" + sun.misc.VM.maxDirectMemory());

		System.out.println("================================");

		ByteBuffer buffer = ByteBuffer.allocateDirect(0);
		Class<?> c = Class.forName("java.nio.Bits");
		Field maxMemory = c.getDeclaredField("maxMemory");
		maxMemory.setAccessible(true);
		synchronized (c) {
			Long maxMemoryValue = (Long) maxMemory.get(null);
			System.out.println("java.nio.Bits.maxMemory:" + maxMemoryValue);
		}

		System.out.println("================================");

		System.out.println("Runtime.maxMemory:" + Runtime.getRuntime().maxMemory());

	}
}
