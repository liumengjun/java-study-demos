package _test.testabc;

public class ParentC {
    String objectName;

    static {
        System.out.println("ci ParentC");
    }

    {
        System.out.println("o ParentC");
    }

    public ParentC() {
        System.out.println("i ParentC");
    }

    public ParentC(String objectName) {
        System.out.println("i ParentC 2");
        this.init(objectName);
    }

    public void init(String objectName) {
        this.objectName = objectName;
    }

    public void execute() {
        System.out.println(objectName);
    }
}
