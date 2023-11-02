package sys;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * -Xmx256 -XX:MaxDirectMemorySize=100M -Dsun.nio.MaxDirectMemorySize=50000000<br>
 * or -Xmx256<br>
 * NOTE: 指定-Dsun.nio.MaxDirectMemorySize没有其作用
 *
 * java9+访问jdk.internal.misc.VM需要指定 --add-exports java.base/jdk.internal.misc=ALL-UNNAMED
 * 
 * @author liumengjun
 *
 */
public class MaxDirectMemorySize {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, SecurityException,
	        IllegalArgumentException, IllegalAccessException {
//		System.out.println("maxDirectMemory:" + sun.misc.VM.maxDirectMemory());  // in java8
//		System.out.println("maxDirectMemory:" + jdk.internal.misc.VM.maxDirectMemory());  // not in java8

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
