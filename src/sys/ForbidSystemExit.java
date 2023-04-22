package sys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.Permission;

public class ForbidSystemExit {
    private static class ExitTrappedException extends SecurityException {
    }

    private static boolean forbidden = false;

    private static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            @Override
            public void checkPermission(Permission permission) {
                System.out.println(permission);
                if (permission.getName().startsWith("exitVM")) {
                    System.out.println(-1);
                    throw new ExitTrappedException();
                }
                if (permission.getName().equals("suppressAccessChecks")) {
                    System.out.println(-2);
                    throw new AccessControlException("can't change accessible");
                }
                if (permission.getName().equals("setSecurityManager")) {
                    System.out.println(-3);
                    if (forbidden) {
                        System.out.println(-4);
                        throw new AccessControlException("can't replace security manager");
                    }
                }
            }
        };
        forbidden = true;
        System.setSecurityManager(securityManager);
    }

    private static void enableSystemExitCall() {
        forbidden = false;
        System.setSecurityManager(null);
    }

    static {
        forbidSystemExitCall();
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        System.out.println(1);
//        System.exit(0);
//        System.setSecurityManager(null);
        Class sc = Class.forName("java.lang.Shutdown");
        Method[] ms = sc.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals("exit")) {
                m.setAccessible(true);
                m.invoke(sc, new Integer(-1));
            }
//            if (m.getName().equals("shutdown")) {
//                m.setAccessible(true);
//                m.invoke(sc);
//            }
//            if (m.getName().equals("halt0")) {
//                m.setAccessible(true);
//                m.invoke(sc, new Integer(-1));
//            }
//            if (m.getName().equals("halt0")) {
//                m.setAccessible(true);
//                m.invoke(sc, new Integer(-1));
//            }
        }
        System.out.println(2);
    }

}
