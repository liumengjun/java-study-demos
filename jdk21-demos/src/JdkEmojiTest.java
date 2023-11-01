import java.nio.charset.StandardCharsets;

public class JdkEmojiTest {
    public static void main(String[] args) {
        var a = "hello";
        System.out.println(a + ": " + a.length());  // hello: 5
        a = "o中国";
        System.out.println(a + ": " + a.length());  // o中国: 3
        a = "a哈😄";
        System.out.println(a + ": " + a.length());  // a哈😄: 4  --- 和预想的不一样
        for (var i = 0; i < a.length(); i++) {
            Character c = a.charAt(i);
            showCharOrEmoji(c);
            int ic = a.codePointAt(i);  // 单纯的 char 表示不了 Emoji 使用 CodePoint
            showCharOrEmoji(ic);
        }
        byte[] acs = a.getBytes(StandardCharsets.UTF_8);  // 61 e5 93 88 f0 9f 98 84
        acs = a.getBytes(StandardCharsets.UTF_16);  // fe ff 0 61 54 c8 d8 3d de 04
//        acs = a.getBytes(StandardCharsets.UTF_16BE);  // 0 61 54 c8 d8 3d de 04
//        acs = a.getBytes(StandardCharsets.UTF_16LE);  // 61 0 c8 54 3d d8 04 de
        for (byte b : acs) {
            System.out.print(Integer.toHexString(Byte.toUnsignedInt(b)) + ' ');
        }
        System.out.println();
        int ic = 0xf09f;
        showCharOrEmoji(ic);  // CodePoint 不是 UTF_16 字节, 也不是 UTF_8 字节
        ic = 0x9884;
        showCharOrEmoji(ic);  // CodePoint 不是 UTF_16 字节, 也不是 UTF_8 字节
        System.out.println("=".repeat(80));

        /**
         * 多行String
         */
        var m = """
                you known,
                it's very well.""";
        System.out.println(m);

        /**
         * switch match string
         */
        var b = new String[]{"a", "b", "c"}[(int) (Math.random() * 3)];
        switch (b) {
            case "a" -> {
                System.out.println("apple");
            }
            case "b" -> {
                System.out.println("box");
            }
            default -> System.out.println("other");
        }

        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
        } finally {
            System.out.println(1.0);
        }

        System.out.println("=".repeat(80));
        System.out.println(tryfr());
    }

    private static void showCharOrEmoji(int c) {
        System.out.println(c
                + ", " + "0x" + Integer.toHexString(c)  // 十六进制
                + ", " + (char) c
                + ", " + Character.toString(c) // 以 CodePoint 输出字符
                + ", " + Character.isEmoji(c)  // 是不是 Emoji
                + ", " + Character.isEmojiPresentation(c)
                + ", " + Character.isEmojiModifier(c)
                + ", " + Character.isEmojiModifierBase(c)
                + ", " + Character.isEmojiComponent(c)
                + ", " + Character.isExtendedPictographic(c)
                + ", " + Character.getName(c)
        );
    }

    private static int tryfr() {
        var i = 0;
        try {
            i = 1 / 0;
            return i = 1;
        } catch (Exception e) {
            return i = 2;
        } finally {
//            i = 4;
            return i;
        }
    }
}
