package com.three_d.mofangv2;

import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

class MyTimerBehavior extends Behavior
{
	BranchGroup1 aBranchGroup1;

	int oldhour;

	int oldfen;

	int oldmiao;

	// int oldri ;
	static long rotateTimes = 0;

	// TimeRun.rotateTimes=
	Date myDate;

	DateFormat df2;

	StringTokenizer st2;

	int hour;

	int fen;

	int miao;

	private long millisecond;

	MyTimerBehavior(BranchGroup1 aaBranchGroup1, long millisecond)
	{
		this.millisecond = millisecond;

		// ---------------
		this.aBranchGroup1 = aaBranchGroup1;

		this.myDate = new Date();
		// DateFormat df1=DateFormat.getDateInstance();//取日起
		DateFormat df2 = DateFormat.getTimeInstance();

		this.st2 = new StringTokenizer(df2.format(this.myDate));

		this.hour = Integer.parseInt(this.st2.nextToken(":"));
		this.fen = Integer.parseInt(this.st2.nextToken(":"));
		this.miao = Integer.parseInt(this.st2.nextToken(":"));

		this.oldhour = this.hour;
		this.oldfen = this.fen;
		this.oldmiao = this.miao;
		// oldri=ri;
	}

	public void initialize()
	{
		this.wakeupOn(new WakeupOnElapsedTime(this.millisecond));
	}

	public void processStimulus(Enumeration criteria)
	{
		WakeupCriterion wakeup;
		while (criteria.hasMoreElements())
		{
			wakeup = (WakeupCriterion) criteria.nextElement();
			// ---------------
			this.renew();
			// ---------------
			this.wakeupOn(new WakeupOnElapsedTime(this.millisecond));
		}
	}

	void renew()
	{
		this.myDate = new Date();
		// DateFormat df1=DateFormat.getDateInstance();//取日起
		this.df2 = DateFormat.getTimeInstance();
		// 取时间

		// StringTokenizer st1=new StringTokenizer(df1.format(myDate));
		this.st2 = new StringTokenizer(this.df2.format(this.myDate));

		this.hour = Integer.parseInt(this.st2.nextToken(":"));
		this.fen = Integer.parseInt(this.st2.nextToken(":"));
		this.miao = Integer.parseInt(this.st2.nextToken(":"));
	}
}
