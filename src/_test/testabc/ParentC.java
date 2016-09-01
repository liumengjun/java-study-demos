package _test.testabc;

public class ParentC {
    String objectName;

    public void init(String objectName) {
        this.objectName = objectName;
    }

    public void execute() {
        System.out.println(objectName);
    }
}
