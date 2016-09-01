package resourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleTest {
    public ResourceBundleTest() {

    }

    public void testResource() {
        Class c = this.getClass();
        StringBuffer resFileName = new StringBuffer(c.getName().substring(0,
                c.getName().lastIndexOf(".")));
        resFileName.append(".LocalStrings");
        System.out.println(resFileName);
        // default
        ResourceBundle resources = ResourceBundle.getBundle(
                resFileName.toString(), Locale.getDefault(), c.getClassLoader());
        System.out.println(resources);
        System.out.println(resources.getString("gekk"));
        // ja
        resources = ResourceBundle.getBundle(
                resFileName.toString(), Locale.JAPANESE, c.getClassLoader());
        System.out.println(resources);
        System.out.println(resources.getString("gekk"));
        // ja_JP
        resources = ResourceBundle.getBundle(
                resFileName.toString(), Locale.JAPAN, c.getClassLoader());
        System.out.println(resources);
        System.out.println(resources.getString("gekk"));
    }

    public static void main(String[] args) {
        new ResourceBundleTest().testResource();
    }
}
