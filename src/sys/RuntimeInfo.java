package sys;

import java.io.PrintStream;
import java.util.*;
import java.net.*;
import java.net.*;

/**
 * <p>RuntimeInfo consolidates and prints all system information available thru standard
 * Java APIs. It includes Java system properties, various system information,
 * memory information and network information.</p>
 * <p>Copyright 2004 by Sams Publishing</p>
 * @author Alex Kalinovsky
 */
public class RuntimeInfo {


    public RuntimeInfo() {
    }

    /**
     * Prints all system properties available thru java.lang.System
     * @param stream output stream
     */
    public void printSystemProperties(PrintStream stream) {
        StringBuffer buffer = new StringBuffer(1000);
        Properties props = System.getProperties();
        for (Enumeration keys = props.keys(); keys.hasMoreElements();) {
            String key = (String)keys.nextElement();
            buffer.append(key);
            buffer.append("=");
            buffer.append(System.getProperty(key));
            buffer.append('\n');
        }
        stream.print(buffer.toString());
    }


    /**
     * Prints information on security manager, class loader and the number
     * of processors
     * @param stream output stream
     */
    public void printSystemInfo(PrintStream stream) {
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("Security manager: ");
        buffer.append(System.getSecurityManager() == null? "null": System.getSecurityManager().getClass().getName());
        buffer.append('\n');

        buffer.append("Class loader for this class: ");
        ClassLoader classLoader = this.getClass().getClassLoader();
        buffer.append(classLoader == null? "null": classLoader.getClass().getName());
        buffer.append('\n');

        buffer.append("Number of available processors to JVM: ");
        buffer.append(Runtime.getRuntime().availableProcessors());
        buffer.append('\n');

        stream.println(buffer.toString());
    }


    /**
     * Prints the amounts of maximum, allocated and free memory
     * @param stream output stream
     */
    public void printMemoryInfo(PrintStream stream) {
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("Maximum memory allowed for JVM: ");
        buffer.append(toMb(Runtime.getRuntime().maxMemory()));
        buffer.append(" Mb\n");
        buffer.append("Memory currently allocated in JVM: ");
        buffer.append(toMb(Runtime.getRuntime().totalMemory()));
        buffer.append(" Mb\n");
        buffer.append("Free memory in JVM: ");
        buffer.append(toMb(Runtime.getRuntime().freeMemory()));
        buffer.append(" Mb\n");
        stream.println(buffer.toString());
    }


    /**
     * Prints local host name and IP address
     * @param stream output stream
     */
    public void printNetworkInfo(PrintStream stream) {
        StringBuffer buffer = new StringBuffer(200);
        InetAddress localhost = null;
        try {
            localhost = java.net.InetAddress.getLocalHost();
            buffer.append("Local host name: ");
            buffer.append(localhost.getHostName());
            buffer.append('\n');
            buffer.append("Local host IP address: ");
            buffer.append(localhost.getHostAddress());
        }
        catch (UnknownHostException ex) {
            buffer.append("*** Failed to detect network properties due to UnknownHostException: ");
            buffer.append(ex.getMessage());
        }
        stream.println(buffer.toString());
    }


    /**
     * Prints all availabe system information to System.out
     */
    public void printAll() {
        printAll(System.out);
    }


    /**
     * Prints all availabe system information to a given stream
     */
    public void printAll(PrintStream stream) {
        stream.println();
        stream.println("SYSTEM PROPERTIES:");
        printSystemProperties(stream);
        stream.println();

        stream.println("SYSTEM INFORMATION:");
        printSystemInfo(stream);
        stream.println();

        stream.println("MEMORY INFORMATION");
        printMemoryInfo(stream);

        stream.println("NETWORK INFORMATION:");
        printNetworkInfo(stream);
        stream.println();
    }


    /**
     * Prints all sytem information to stdout
     */
    public static void main(String[] args) {
        RuntimeInfo runtimeInfo = new RuntimeInfo();
        runtimeInfo.printAll();
    }


    /**
     * Rounds a number to the specified number of decimal places
     * @param number
     * @param places
     * @return
     */
    private double round(double number, int places) {
        double a=Math.pow(10.0, (double)places);
        double b=Math.rint(number*a);
        return b/a;
    }

    /**
     * Converts bytes to megabytes with 2 decimal places precision
     * @param bytes
     * @return number of megabytes
     */
    private double toMb(double bytes) {
        return round((double)bytes/1024/1024, 2);
    }

}
