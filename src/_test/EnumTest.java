package _test;
enum EnumTest {
	FRANK("The given name of me", 423),	//
	LIU("The family name of me");		//
	private String context;
	private Integer code;

	public String getContext() {
		return this.context
				+ (this.code == null ? "" : ", code NO. is " + this.code);
	}

	private EnumTest(String context) {
		this.context = context;
	}

	private EnumTest(String context, Integer code) {
		this.context = context;
		this.code = code;
	}

	public static void main(String[] args) {
		for (EnumTest name : EnumTest.values()) {
			System.out.println(name + " : " + name.getContext());
		}
		System.out.println(EnumTest.FRANK.getDeclaringClass());
		System.out.println(EnumTest.FRANK.ordinal());
		System.out.println(EnumTest.FRANK.name());

		EnumTest direct = EnumTest.LIU;
		System.out.println(direct);
    System.out.println("LIU".equals(EnumTest.LIU));
    System.out.println("LIU".equals(EnumTest.LIU.name()));
    System.out.println("LIU".equals(EnumTest.LIU.toString()));
	}
}
