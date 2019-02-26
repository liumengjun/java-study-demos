package _test.testabc;

public class ChildC extends ParentC {
    private String objectName;

    {
        System.out.println("o ChildC");
    }

    static {
        System.out.println("ci ChildC");
    }

    public ChildC() {
        System.out.println("i ChildC");
    }

    public ChildC(String objectName) {
        // super(objectName);
        System.out.println("i ChildC 2");
        this.setObjectName(objectName);
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void execute() {
        super.execute();
    }
}
