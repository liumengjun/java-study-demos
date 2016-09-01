import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import junit.framework.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class hello {
	private static Random rnd = new Random(); 
	public static final int END = Integer.MAX_VALUE; 
    public static final int START = END - 100; 
	public static void main(String[] args) throws Exception {
		Integer a = 1;
		Integer b = new Integer(1);
		Integer c = 1;
		System.out.println(a==b); // false
		System.out.println(a==c); // true
		System.out.println(a.equals(b)); // true
		Double d = 1D;
		Double e = 1.0;
		Double e2 = 1.0;
		System.out.println(d==e); // false
		System.out.println(d.equals(e)); // true
		System.out.println(e2==e); // false
		System.out.println(e2.equals(e)); // true
		Float f = 1F;
		Float g = 1.0F;
		System.out.println(f==g); // false
		System.out.println(f.equals(g)); // true
		// across
		System.out.println();
		System.out.println(a.equals(d)); // false
		System.out.println(d.equals(f)); // false
		System.out.println(f.equals(e)); // false
		// big
		System.out.println();
		BigInteger i = new BigInteger("1");
		BigInteger j = BigInteger.ONE;
		BigInteger j2 = BigInteger.ONE;
		System.out.println(i==j); // false
		System.out.println(i.equals(j)); // true
		System.out.println(i.compareTo(j)); // 0
		System.out.println(j==j2); // true
		BigDecimal o = new BigDecimal(1);
		BigDecimal o1 = new BigDecimal(BigInteger.ONE);
		BigDecimal o11 = new BigDecimal(BigInteger.ONE);
		BigDecimal p = new BigDecimal(1.0);
		BigDecimal p1 = new BigDecimal(1.00);
		BigDecimal p11 = new BigDecimal("1.00");
		System.out.println(o==o1); // false
		System.out.println(o1==o11); // false
		System.out.println(o.equals(o1)); // true
		System.out.println(o.equals(o11)); // true
		System.out.println(o==p); // false
		System.out.println(o.equals(p)); // true
		System.out.println(p==p1); // false
		System.out.println(p.equals(p1)); // true
		System.out.println(p1==p11); // false
		System.out.println(p1.equals(p11)); // false
		System.out.println(p1.compareTo(p11)); // 0
	}
	
	static void concurrentQueueTest() {
		final ConcurrentLinkedDeque<Integer> conQueue = new ConcurrentLinkedDeque<Integer>();
		int i;
		for ( i=0; i<10; i++) {
			conQueue.add(i);
		}
		Thread thread = new Thread(new Runnable(){
			public void run() {
				int size = conQueue.size();
				int c = 0;
				Iterator<Integer> itr = conQueue.descendingIterator();
				while(itr.hasNext()) {
					c++;
					System.out.println(itr.next());
				}
				System.out.println("size: "+size);
				System.out.println("count: "+c);
			}
		});
		thread.start();
		
		System.out.println(conQueue.poll());
		System.out.println(conQueue.poll());
		System.out.println(conQueue.poll());
		System.out.println(conQueue.poll());
		conQueue.offer(i++);
		conQueue.offer(i++);
		conQueue.offer(i++);
		conQueue.offer(i++);
		conQueue.offer(i++);
		conQueue.offer(i++);
		System.out.println(conQueue.poll());
	}
	
	/*
	 *  sqrt(2*PI*n) * (n/e)^n
	 * ------------------------   ==>> 1
	 *           n!
	 */
	static void tet(){
		int n;
		for(n=2; n<170; n++){
			double factorial = Math.sqrt(2*Math.PI*n)*Math.pow(n/Math.E, n);
			System.out.print(n+"!:"+factorial+",");
			BigInteger lFactorial = BigInteger.valueOf(n);
			for(int i=n-1;i>1;i--){
				lFactorial =lFactorial.multiply(BigInteger.valueOf(i));
			}
			System.out.print(lFactorial.doubleValue()+",");
			System.out.println((factorial-lFactorial.doubleValue())/lFactorial.doubleValue());
		}
	}
	
	static void fastJsonTest() {
		Object o = new hello();
		String oText = JSON.toJSONString(o);//JSON.toJSONString(o, SerializerFeature.UseSingleQuotes);
		System.out.println(oText);
		
		long millis = 1234567898765L;
		Date date = new Date(millis);       
		System.out.println(JSON.toJSONString(date));
		System.out.println(JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat));
		System.out.println(JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS"));
		
		Color color = Color.RED;
		String text = JSON.toJSONString(color, SerializerFeature.WriteClassName);
		System.out.println(text);
		System.out.println(JSON.toJSONString(color, SerializerFeature.BrowserCompatible));
		
		String text2 = "{\"@type\":\"java.awt.Color\",\"r\":255,\"g\":0,\"b\":0,\"alpha\":255}";
		Color color2 = (Color) JSON.parse(text2);
		System.out.println(color2);
		
		class User {
			private int ID = new Random(System.currentTimeMillis()).nextInt();
			private int age;
			private String name;
		    @JSONField(name="ID")
		    public int getId() { return this.ID; }
		    public void setName(String name) { this.name = name; }
		    public void setAge(int age) { this.age = age; }
		    @JSONField(name="name")
		    public String getName(String name) { return this.name; }
		    @JSONField(name="age")
		    public int getAge(int age) { return this.age; }
		    public String toString() {
		    	return "{name="+name+",age="+age+"}";
		    }
		}
		 
		User user = new User();
		System.out.println(JSON.toJSONString(user));
		
		//
		//
		String text3 = "[{ }, { }]";
		List<User> users = JSON.parseArray(text3, User.class);
		System.out.println(users);
		
		String text4 = " {\"name\":{\"name\":\"ljw\",age:18}}";
		Map<String, User> userMap = JSON.parseObject(text4, new TypeReference<Map<String, User>>() {});
		System.out.println(userMap);
		
		final class Entity {
		    private final int    id;
		    private final String name;
		    @JSONCreator
		    public Entity(@JSONField(name = "id") int id, @JSONField(name = "name") String name){
		        this.id = id;
		        this.name = name;
		    }
		    public int getId() { return id; }
		    public String getName() { return name; }
		}
		
		String text5 = "{\"id\":123, \"name\":\"chris\"}";
		Bean bean = JSON.parseObject(text5, Bean.class);
		 
		// 按接口调用
		Assert.assertEquals(123, bean.getId());
		Assert.assertEquals("chris", bean.getName());
		bean.setId(234);
		Assert.assertEquals(234, bean.getId());
	}
	
	interface Bean {
	    int getId();
	    void setId(int value);
	    String getName();
	    void setName(String value);
	}
	
	static void testExec() throws Exception {
		URL file_url = new URL("file:///E:\\temp\\c\\a.exe");
		String[] execArgs = new String[] {
//				"cmd.exe",
//				"/c",
//				"start",
//				"\"\"",
				/*'"' + DirectoryUtil.getDocUrl(filename).toString() + '"' });*/
				'"' + "E:\\temp\\c\\a.exe" + '"',
//				">runtime.exec.txt",
//				"2>&1"
				};
		Process p = Runtime.getRuntime().exec(execArgs);
		BufferedReader pw = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ( (line = pw.readLine()) != null) {
			System.out.println(line);
		}
		pw.close();
	}
	
	static String classify(char ch) { 
        if ("0123456789".indexOf(ch) >= 0) 
             return "NUMERAL "; 
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(ch) >= 0) 
             return "LETTER "; 
            if ("+-*/&|!=".indexOf(ch) >= 0) 
                 return "OPERATOR "; 
        return "UNKNOWN"; 
    }

	public static void readIcon() {
		String name = "javax/swing/plaf/metal/icons/ocean/warning.png";
		URL iconUrl = ClassLoader.getSystemResource(name);
		System.out.println(iconUrl);
		ImageIcon icon = new ImageIcon(iconUrl);
		System.out.println(icon.getClass());
		System.out.println(icon.getIconWidth() + "*" + icon.getIconHeight());

		// String name2 = "icons/ocean/warning.png";
		// URL iconUrl2 = OceanTheme.class.getResource(name2);
		// System.out.println(iconUrl2);
		// ImageIcon icon2 = new ImageIcon(iconUrl2);
		// System.out.println(icon2.getClass());
		// System.out.println(icon2.getIconWidth()+"*"+icon2.getIconHeight());

		// Object ddkkd = LookAndFeel.makeIcon(OceanTheme.class, "icons/ocean/warning.png");
		// System.out.println(ddkkd);
		// Object dfsf = ((LazyValue)ddkkd).createValue(null);
		// System.out.println(dfsf);
		// ImageIconUIResource ii = (ImageIconUIResource)dfsf;
		// System.out.println(ii.getImage());
		// System.out.println(ii.getIconWidth()+"*"+ii.getIconHeight());

		JOptionPane.showConfirmDialog(null, "232", "YoSi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
				icon);
	}

	public static Color mixingColor(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		int aa = a.getAlpha();
		int ar = a.getRed();
		int ag = a.getGreen();
		int ab = a.getBlue();

		int ba = b.getAlpha();
		int br = b.getRed();
		int bg = b.getGreen();
		int bb = b.getBlue();

		int ca = aa | ba;
		int cr = ar | br;
		int cg = ag | bg;
		int cb = ab | bb;

		Color c = new Color(cr, cg, cb, ca);
		return c;
	}

	public static Color mixingColor2(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		float aa = a.getAlpha() / 255.0F;
		float ar = a.getRed() / 255.0F;
		float ag = a.getGreen() / 255.0F;
		float ab = a.getBlue() / 255.0F;

		float ba = b.getAlpha() / 255.0F;
		float br = b.getRed() / 255.0F;
		float bg = b.getGreen() / 255.0F;
		float bb = b.getBlue() / 255.0F;

		float fd = 1 - aa;

		float ca = aa + ba * fd;
		float cr = ar + br * fd;
		float cg = ag + bg * fd;
		float cb = ab + bb * fd;

		Color c = new Color(cr, cg, cb, ca);
		return c;
	}

	public static Color mixingColor3(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		int aa = a.getAlpha();
		int ar = a.getRed();
		int ag = a.getGreen();
		int ab = a.getBlue();

		int ba = b.getAlpha();
		int br = b.getRed();
		int bg = b.getGreen();
		int bb = b.getBlue();

		float[] a_hsbvals = Color.RGBtoHSB(ar, ag, ab, null);
		float[] b_hsbvals = Color.RGBtoHSB(br, bg, bb, null);

		float ch = (a_hsbvals[0] + b_hsbvals[0]) / 2;
		float cb = (a_hsbvals[2] + b_hsbvals[2]);
		float cs = (a_hsbvals[1] * a_hsbvals[2] + b_hsbvals[1] * b_hsbvals[2]) / cb;

		int cc = Color.HSBtoRGB(ch, cs, cb);
		System.out.println(Integer.toHexString(cc));
		Color c = new Color(cc);
		return c;
	}

	public static void javacTest() throws Exception {
		// TODO Auto-generated method stub
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		Scanner reader = new Scanner(System.in);
		String fileName = "JavacTestWEOINVASDJFOIW.java";
		File javaFile = new File(fileName);
		String pre = "public class JavacTestWEOINVASDJFOIW {\n"
				+ "	public static void main(String[] args) throws Exception {\n";
		String suf = ";\n	}\n" + "}\n";
		while (true) {
			String line = reader.nextLine();
			if (line.equals("exit")) {
				break;
			}
			PrintWriter fileWriter = new PrintWriter(javaFile);
			fileWriter.print(pre);
			fileWriter.print(line);
			fileWriter.print(suf);
			fileWriter.flush();
			fileWriter.close();
			int result = javac.run(null, System.out, System.err, fileName);
			System.out.println(result);
		}
	}

	public static void javascriptEngineTest() throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		engine.eval("n = 1728;");
		Object result = engine.eval("n*n");
		System.out.println(result);

		engine.put("k", 234);
		engine.eval("k = n+k;");
		Object k = engine.get("k");
		System.out.println(k);
	}

	public static void loopBreakerTest() {
		int i = 0;
		boolean flag = true;
		outer: while (flag) {
			i++;
			while (flag) {
				i++;
				if (Math.random() > 0.999) {
					System.out.println("break outer aha");
					break outer;
				}
				if (Math.random() > 0.99) {
					System.out.println("break inner");
					break;
				}
			}
			if (Math.random() < 0.004) {
				System.out.println("break outer normal");
				break;
			}
		}
		System.out.println(i);
	}

}

class Hello1{
	public static int world;
	public static final int you;
	static {
		you = Hello2.country + world;
	}
}

class Hello2{
	public static int country;
	static {
		country = new Random().nextInt();
		country += Hello1.world;
	}
	
}