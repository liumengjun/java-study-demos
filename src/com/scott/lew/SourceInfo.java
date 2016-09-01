package com.scott.lew;

import java.io.PrintStream;

/**
 * 利用当前线程（Thread.currentThread()）的栈追踪信息（StackTraceElement[]）来或者java代码信息<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;需要注意的时，在编译时若采用 -g 参数的话必须有 lines 项。
 *     普通情况下是不带 -g 参数的，这时在编译后的类中存放有行号信息，否则就会输出 -1。<br/>
 * <br/>
 * 
 * @see Thread#getStackTrace()
 * 
 * @author 21714900R2960
 */
public class SourceInfo {
	/**
	 * 输出流
	 */
	private static PrintStream printer = System.out;
	
	/**
	 * just a static class
	 */
	private SourceInfo() {
		super();
	}
	
	/**
	 * 设定打印流，默认是标准输出
	 * @param printStream
	 */
	public static void initPrintStream(PrintStream printStream){
		if(printStream==null){
			return;
		}
		printer = printStream;
	}

	/**
	 * 打印当前代码信息，形如："ClassName::MethodName() (FileName：LineNO)"
	 */
	public static void printCurrentInfo() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		StackTraceElement curSte = ste[2];
		StringBuilder sbe = new StringBuilder(256);
		buildInfoString(curSte, sbe);
		printer.println(sbe.toString());
	}

	/**
	 * 打印当前代码调用者信息，即<b>调用该方法printCallerInfo（）的调用者<i>(注意是"再调用者")</i></b>的代码信息<br/>
	 *     &nbsp;&nbsp;&nbsp;&nbsp;形如："ClassName::MethodName() (FileName：LineNO)"<br/>
	 * <br/>
	 * 注意：<br/>
	 *   &nbsp;&nbsp;不要在main（）方法中调用该方法，否则该方法什么也不做直接返回<br/>
	 *   &nbsp;&nbsp;适用于在自己再编写的调试方法中调用，否则只需调用{@link #printCurrentInfo()}
	 */
	public static void printCallerInfo(){
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if(ste.length<4){
			return;
		}
		StackTraceElement curSte = ste[3];
		StringBuilder sbe = new StringBuilder(256);
		buildInfoString(curSte, sbe);
		printer.println(sbe.toString());
	}
	
	/**
	 * 打印程序启点代码信息，形如："ClassName::MethodName() (FileName：LineNO)"
	 */
	public static void printAppStartPointInfo() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		StackTraceElement curSte = ste[ste.length - 1];
		StringBuilder sbe = new StringBuilder(256);
		buildInfoString(curSte, sbe);
		printer.println(sbe.toString());
	}

	/**
	 * 打印当前代码调用者的调用层级（Call Hierarchy）信息，
	 * 即<b>调用该方法printCallerCallHierarchyInfo（）的调用者<i>(注意是"再调用者")</i></b>的调用层级信息<br/>
	 *     &nbsp;&nbsp;&nbsp;&nbsp;逐层打印，每个调用者信息形如："ClassName::MethodName() (FileName：LineNO)"<br/>
	 * <br/>
	 * 注意：<br/>
	 *   &nbsp;&nbsp;不要在main（）方法中调用该方法，在main()中调用不会输出任何调用信息，
	 *   因为没有代码区调用main（）方法。<br/>
	 *   &nbsp;&nbsp;适用于在自己再编写的调试方法中调用，否则只需调用{@link #printCurrentInfo()}
	 */
	public static void printCallerCallHierarchyInfo() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if(ste.length<4){
			return;
		}
		StringBuilder sbe = new StringBuilder(256);
		final int caller_begin = 3;
		for (int i = caller_begin; i < ste.length; i++) {
			for(int temp=caller_begin; temp<i; temp++){
				sbe.append(' ');sbe.append(' ');
			}
			if(i>caller_begin){
				sbe.append("\\->");
			}
			StackTraceElement curSte = ste[i];
			buildInfoString(curSte, sbe);
			sbe.append("\n");
		}
		printer.println(sbe.toString());
	}

	/**
	 * 打印当前代码调用层级（Call Hierarchy）信息，逐层打印每个调用者信息
	 * 形如："ClassName::MethodName() (FileName：LineNO)"
	 */
	public static void printCallHierarchyInfo() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		StringBuilder sbe = new StringBuilder(256);
		final int caller_begin = 2;
		for (int i = caller_begin; i < ste.length; i++) {
			for(int temp=caller_begin; temp<i; temp++){
				sbe.append(' ');sbe.append(' ');
			}
			if(i>caller_begin){
				sbe.append("\\->");
			}
			StackTraceElement curSte = ste[i];
			buildInfoString(curSte, sbe);
			sbe.append("\n");
		}
		printer.println(sbe.toString());
	}
	
	/**
	 * 构造StackTraceElement信息字符串<br/>&nbsp;&nbsp;形如："ClassName::MethodName() (FileName：LineNO)"
	 * @param curSte
	 * @param sbe
	 */
	private static void buildInfoString(StackTraceElement curSte, StringBuilder sbe){
		/*sbe.append(curSte.getFileName());
		sbe.append(":");
		sbe.append(curSte.getLineNumber());
		sbe.append("=");
		sbe.append(curSte.getClassName());
		sbe.append("::");
		sbe.append(curSte.getMethodName());
		sbe.append("()");*/

		sbe.append(curSte.getClassName());
		sbe.append("::");
		sbe.append(curSte.getMethodName());
		sbe.append("() (");
		sbe.append(curSte.getFileName());
		sbe.append(":");
		sbe.append(curSte.getLineNumber());
		sbe.append(")");
	}
}
