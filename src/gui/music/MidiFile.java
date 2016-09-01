package gui.music;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Vector;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class MidiFile {

	private MidiFileHeader	fileHeader;
	private File			midiFile;
	private MidiTrack[]		mtrks;

	public static void main(String[] args) {
		MidiFile midiFile = new MidiFile();
		String midiFileName = "audio/flourish.mid";
		// midiFileName = "audio/onestop.mid";
		// // midiFileName = "audio/town.mid";
		midiFileName = "audio/trippygaia1.mid";

		boolean flag = midiFile.readMidiFile(midiFileName);
		System.out.println(flag);

		if (flag) {
			midiFile.playMusic();
		}
	}

	/**
	 * 读取名为fileName的MIDI文件
	 * 
	 * @param fileName
	 * @return 成功为true
	 */
	public boolean readMidiFile(String fileName) {
		init();
		DataInputStream in = null;
		try {
			midiFile = new File(fileName);
			if (!midiFile.exists()) {
				return false;
			}
			if (0 == midiFile.length()) {
				return false;
			}
			in = new DataInputStream(new FileInputStream(midiFile));
			boolean flag = readFileHeader(in);
			if (!flag) {
				return false;
			}
			flag = readFileBody(in);
			if (!flag) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

	private void init() {
		fileHeader = null;
		midiFile = null;
		mtrks = null;
	}

	/**
	 * 读取midi文件头部
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private boolean readFileHeader(DataInputStream in) throws IOException {
		if (fileHeader == null) {
			fileHeader = new MidiFileHeader();
		}
		in.read(fileHeader.mthd_flag);
		if (!"MThd".equalsIgnoreCase(new String(fileHeader.mthd_flag))) {
			return false;
		}
		fileHeader.mthd_length = in.readInt();
		if (6 != fileHeader.mthd_length) {
			return false;
		}
		fileHeader.format = in.readShort();
		System.out.println("format:" + fileHeader.format);
		if (fileHeader.format < 0 || fileHeader.format > 2) {
			return false;
		}
		fileHeader.track_num = in.readShort();
		System.out.println("track_num:" + fileHeader.track_num);
		if (fileHeader.track_num < 1) {
			return false;
		}
		fileHeader.division = in.readShort();
		System.out.println("division:" + fileHeader.division);
		return true;
	}

	/**
	 * 读取midi文件音轨部分
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private boolean readFileBody(DataInputStream in) throws IOException {
		mtrks = new MidiTrack[fileHeader.track_num];
		for (int i = 0; i < mtrks.length; i++) {
			mtrks[i] = new MidiTrack();
			readMidiTrack(in, mtrks[i]);
		}
		return true;
	}

	private boolean readMidiTrack(DataInputStream in, MidiTrack mtrk) throws IOException {
		in.read(mtrk.mtrk_flag);
		if (!"MTrk".equalsIgnoreCase(new String(mtrk.mtrk_flag))) {
			return false;
		}
		mtrk.mtrk_length = in.readInt();
		if (in.available() < mtrk.mtrk_length) {
			return false;
		}

		// read each track block
		boolean trackend_flag = false;
		while (!trackend_flag) {
			TrackBlock trckBk = new TrackBlock();
			// 1, read dela_time
			trckBk.delta_time = readVarLen(in, TrackBlock.byteBuf);

			// 2, read event
			int b = in.read();
			int event_type = TrackEvent.getEventType(b);
			TrackEvent tkEvent = null;
			switch (event_type) {
			case TrackEvent.MIDI_EVENT:
				tkEvent = new MidiEvent(b);
				break;
			case TrackEvent.SYSEX_EVENT:
				tkEvent = new SysexEvent(b);
				break;
			case TrackEvent.META_EVENT:
				tkEvent = new MetaEvent(b);
				break;
			}
			// each event class has its own read method
			tkEvent.readEvent(in);
			trckBk.event = tkEvent;
			if (tkEvent instanceof MetaEvent) {
				if (((MetaEvent) tkEvent).sub_type == 3) {
					mtrk.title = new String(tkEvent.params);
				}
				if (((MetaEvent) tkEvent).sub_type == 0x51) {
					int tempo = 0;
					for (int i = 0; i < tkEvent.params.length; i++) {
						tempo = (tempo << 8) | (0xff & tkEvent.params[i]);
					}
					mtrk.tempoSpeed = tempo;
				}
				trackend_flag = ((MetaEvent) tkEvent).isTrackEnd();
			}

			if (mtrk.tempoSpeed <= 0) {
				mtrk.tempoSpeed = 500000;
			}
			// System.out.println("tempoSpeed:" + (mtrk.tempoSpeed / 1000) + "ms");
			// add this trckBk into mtrk
			mtrk.trckBlocks.add(trckBk);
		}
		return true;
	}

	private static long readVarLen(DataInputStream in, ByteBuffer byteBuf) throws IOException {
		byteBuf.clear();
		long l = 0;
		int b = in.read();
		byteBuf.put((byte) b);
		l = 0x7F & b;
		while (b >= 0x80) {
			b = in.read();
			byteBuf.put((byte) b);
			l = l << 7 | 0x7F & b;
		}
		return l;
	}

	/**
	 * 播放该midi文件
	 */
	public void playMusic() {
		if (midiFile == null) {
			return;
		}
		if (fileHeader.format == 1) {
			// 多个MIDI track 同时播放的格式
			Thread[] mtrkPlayers = new Thread[fileHeader.track_num];
			for (int i = 0; i < mtrkPlayers.length; i++) {
				final MidiTrack cur_mtrk = mtrks[i];
				mtrkPlayers[i] = new Thread(new Runnable() {
					public void run() {
						cur_mtrk.play();
					}
				});
			}
			for (int i = 0; i < mtrkPlayers.length; i++) {
				mtrkPlayers[i].start();
			}
		} else {
			for (int i = 0; i < mtrks.length; i++) {
				mtrks[i].play();
			}
		}
	}

	/**
	 * midi file head
	 */
	// format表示MIDI文件存放的格式，当前只有３种格式：
	// 　 0 表示MIDI文件只有一个Track Chunk；
	// 　 1 表示MIDI文件只有一个或多个Track Chunk（所有的Track同时播放）；
	// 　 2 表示MIDI文件只有一个或多个各处独立的Track Chunk。
	//
	// track_num
	// 　 实际音轨数加上一个全局的音轨
	//
	// division指定计数的方法:
	// 　 一种随时间计数（最高位设置为０时）。
	// 　 b15:0 │ b14 -- b0: 每一拍的计数值:如该数据为９６（以八分音符为一拍），四分音符延时数应该为192。
	// 　 另一种使用制式的时间码（最高位设置为１时）。
	// 　 即负数，表示每秒中SMTPE 帧的数量。如：-24 = 24 帧/秒；-25 = 25 帧/秒；-29 = 30 帧/秒, drop frame；-30 = 30 帧/秒, non-drop frame。
	static class MidiFileHeader {
		byte[]	mthd_flag	= new byte[4];
		int		mthd_length;
		short	format;					// 有效的格式是： 0、 1 和 2
		short	track_num;					// MIDI 文件中track chunk的数量。实际音轨数加上一个全局的音轨
		short	division;					// 这个定义在MIDI 文件中（一个）单位的 delta-time数
	}

	// 全局音轨包括歌曲的附加信息(比如标题和版权)、歌曲速度和系统码(Sysx)等内容。

	/**
	 * midi track
	 */
	class MidiTrack {
		byte[]				mtrk_flag	= new byte[4];
		int					mtrk_length;
		Vector<TrackBlock>	trckBlocks	= new Vector<TrackBlock>();
		String				title;
		int					tempoSpeed;

		void play() {
			System.out.println(((title != null) ? title + " Is " : "") + "Playing ...");
			// 计算每帧延时
			// TODO: how to calculate the delay time
			int division = fileHeader.division;
			int delay = 96;
			if ((division & 0x8000) == 0) {
				delay = division & 0x7fff;
			} else {
				delay = 1000000 / (division & 0x7fff) / 1000;
			}

			try {
				// 加载midi合成器
				Synthesizer synthesizer = MidiSystem.getSynthesizer();
				synthesizer.open();
				Soundbank sb = synthesizer.getDefaultSoundbank();
				Instrument[] instruments = sb.getInstruments();
				synthesizer.loadInstrument(instruments[0]);
				MidiChannel[] midiChannels = synthesizer.getChannels();

				// 处理每个event
				Iterator<TrackBlock> trckBkItr = trckBlocks.iterator();
				while (trckBkItr.hasNext()) {
					TrackBlock curBk = trckBkItr.next();
					// TODO: how to calculate the delay time
					long delta_time = curBk.delta_time;
					if (delta_time > 0) {
						long wait = delta_time * tempoSpeed / delay / 1000;
						Thread.sleep(wait);
						// System.out.println("delta_time:" + wait);
					}
					TrackEvent curEvent = curBk.event;
					curEvent.play(midiChannels);
					Thread.sleep(tempoSpeed / delay / 1000);
				}

				if (synthesizer != null) {
					synthesizer.close();
					synthesizer = null;
				}
				instruments = null;
				midiChannels = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * track 数据块
	 */
	// 每一个数据有着相同的结构:时间差+事件。

	// 所谓时间差，指的是前一个事件到该事件的时间数，它的单位是tick(MIDI的最小时间单位)
	// <dela-time>使用可变长度的形式存储数据，它代表处理event之前要计数时间值。
	// 它在音乐中，即表示拍数(Tick),一个点相当于1/4800秒。
	// 为了能连续处理两个event，我们可以将deta-time设置为0。

	// dela-time使用可变长度的形式表示数据值。可变长度形式是MIDI文件中对于大于８位的数据打用的一种存储方式，
	// 它把每一个数据定义为７位，剩下的最高位作为数据长度的识别。
	// 当这一位为0时，表示数据是最后一个，若为１，则表示还有下一个.
	// 如:数值0x3fff，可变长度形式便为0xff,0x7f； 0x4000则应该为0x81,0x80,0x00。
	static class TrackBlock {
		static final ByteBuffer	byteBuf	= ByteBuffer.allocate(10);
		long					delta_time;
		TrackEvent				event;
	}

	// 这些事件，都有统一的表达结构:种类+参数。
	abstract static class TrackEvent {
		static final int	MIDI_EVENT	= 1;
		static final int	SYSEX_EVENT	= 2;
		static final int	META_EVENT	= 3;

		int					event_byte;
		byte[]				params;

		TrackEvent(int event_byte) {
			this.event_byte = event_byte;
		}

		abstract boolean readEvent(DataInputStream in) throws IOException;

		abstract boolean play(MidiChannel[] midiChannels);

		static int getEventType(int b) {
			if (b < 0xF0) {
				return MIDI_EVENT;
			} else if (b < 0xFF) {
				return SYSEX_EVENT;
			} else {
				return META_EVENT;
			}
		}
	}

	/**
	 * 
	 * MIDI Channel 消息包含： <br/>
	 * • Channel Voice messages <br/>
	 * • Channel Mode messages <br/>
	 * Running status 通常应用于MIDI文件中的同一状态的表示。Running status 也可以被取消 。<br/>
	 */
	// 常见MIDI码说明

	// 0、00~7F 上次激活格式的参数(8x、9x、Ax、Bx、Cx、Dx、Ex)

	// 1、关闭发音（0x8n）。
	// 　 格式：0x8n note velocity
	// 　 说明同上。通常它用0x9n,note,0来代替。

	// 2、开始发音（0x9n）
	// 　 格式为：0x9n note velocity
	// 　 它一共占用３个字节，n表示通道号，取值0-15。MIDI可以同时演奏16个通道，用此指定在哪一个通道上发音（以下n相同）。
	// 　 note表示音高数值，即音阶码值。如C4（中音１）为60，它的取值在0xc和0x6c之间（具体码值，可参考「参考书籍１」）。
	// 　 velocity表示按键时的速度，用此表示音的力度。若没有力度感，可以将其设置为64，若为0，表示关闭发音。
	// 　 如：在第２通道上开始演奏３，则MIDI码便为0x91,63,40。
	// 　 MIDI规范还规定，若连续向同一通道上发送多个音，则可以不指出状态码。如上述同时演奏３，５，MIDI码便为：0x91,63,40,65,40。

	// 3、Ax 1010xxxx nn vv 压力变化
	// 　 x表示通道号，取值0-15（以下x相同）。
	// 　 nn=音符号
	// 　 vv=力度

	// ４、设置音量大小
	// 　 格式： 0xbn, 07, size
	// 　 0xbn, 39, size
	// 　 7,表示设置主音量的高字节值；39表示设置主音量的低字节值。

	// 5、切换音色（0xcn）。
	// 　 格式：0xcn,program
	// 　 program表示音色代码，０----255之间
	// 　 如Acou Piano 1（电钢１值为０），Synth Bass 1（电贝司１值为６４）等（详见「参考书籍１」）。

	// 6、Dx 1101xxxx cc 在通道后接触
	//  cc=管道号,改变通道的发声强度cc：通道发声强度

	// 7、Ex 1110xxxx bb tt 改变互相咬和的齿轮 (2000H 表明缺省或没有改变)(什么意思搞不懂:)
	// 　 bb=值的低7位(least sig)
	// 　 tt=值的高7位 (most sig)
	static class MidiEvent extends TrackEvent {
		static int				prev_sub_type	= -1;
		private static int		pre_channel		= -1;
		private static int		pre_note		= -1;
		private static int[]	pre_notes		= new int[16];
		int						sub_type;						// 0x70-0xE0;
		int						channel;						// 通道号，取值0-15

		int						note;
		int						velocity;
		int						pressure;
		int						vol_code;
		int						volume;
		int						tone_color;
		int						intensity;
		int						tone_pitch;

		MidiEvent(int event_byte) {
			super(event_byte);
			sub_type = event_byte & 0xf0;
			channel = event_byte & 0x0f;
		}

		boolean readEvent(DataInputStream in) throws IOException {
			switch (sub_type) {
			case 0x80:
				prev_sub_type = 0x80;
				note = in.read();
				velocity = in.read();
				break;
			case 0x90:
				prev_sub_type = 0x90;
				note = in.read();
				velocity = in.read();
				break;
			case 0xa0:
				prev_sub_type = 0xa0;
				note = in.read();
				pressure = in.read();
				break;
			case 0xb0:
				prev_sub_type = 0xb0;
				vol_code = in.read();
				volume = in.read();
				break;
			case 0xc0:
				prev_sub_type = 0xc0;
				tone_color = in.read();
				break;
			case 0xd0:
				prev_sub_type = 0xd0;
				intensity = in.read();
				break;
			case 0xe0:
				prev_sub_type = 0xe0;
				tone_pitch = in.read() & 0x7f;// low 7 bits
				tone_pitch = (in.read() & 0x7f) << 7 | tone_pitch;// high 7 bits
				break;
			default:
				sub_type = prev_sub_type;
				switch (prev_sub_type) {
				case 0x80:
					break;
				case 0x90:
					break;
				case 0xa0:
					break;
				case 0xb0:
					volume = in.read();
					break;
				case 0xc0:

					break;
				case 0xd0:

					break;
				case 0xe0:

					break;
				default:
					break;
				}
				break;
			}
			return true;
		}

		boolean play(MidiChannel[] midiChannels) {
			// TODO Auto-generated method stub
			switch (sub_type) {
			case 0x80:
				midiChannels[channel].noteOff(note, velocity);
				break;
			case 0x90:
				if (pre_notes[channel] > 0) {
					midiChannels[channel].noteOff(pre_notes[channel], velocity);
				}
				midiChannels[channel].noteOn(note, velocity);
				pre_notes[channel] = note;
				break;
			case 0xa0:
				midiChannels[channel].setPolyPressure(note, pressure);
				break;
			case 0xb0:
				midiChannels[channel].controlChange(vol_code, volume);
				break;
			case 0xc0:
				midiChannels[channel].programChange(tone_color);
				break;
			case 0xd0:
				midiChannels[channel].setChannelPressure(intensity);
				break;
			case 0xe0:
				midiChannels[channel].setPitchBend(tone_pitch);
				break;
			}
			return false;
		}
	}

	/**
	 * Sysex Events<br/>
	 * F0 <长度> <sysex-data> F0 Sysex Event<br/>
	 * F7 <长度> <any-data> F7 Sysex Event (or 'escape')<br/>
	 */
	// F0 - FE的事件吧
	static class SysexEvent extends TrackEvent {
		int	length;

		SysexEvent(int event_byte) {
			super(event_byte);
		}

		boolean readEvent(DataInputStream in) throws IOException {
			length = (int) MidiFile.readVarLen(in, TrackBlock.byteBuf);
			this.params = new byte[length];
			in.read(this.params);
			return true;
		}

		boolean play(MidiChannel[] midiChannels) {
			System.out.println("SysexEvent");
			return false;
		}
	}

	/**
	 * Meta Events 是用来表示象 track 名称、歌词、提示点等<br/>
	 * 它并不作为 MIDI 消息被发送， 但是它仍然是 MIDI 文件的（有用的）组成部分。<br/>
	 * <br/>
	 * Meta Events 的基本形式： <br/>
	 * FF <类型> <长度> <数据> <br/>
	 * <类型> <br/>
	 * 一个字节描述 meta-event 的类型。 可能的范围是 00-7F。 并不是所有的值都在这个范围，但是程序能够应付意想不到的值，并诊断这个长度和忽略预期外的数据部分。 <br/>
	 * <长度> <br/>
	 * 紧跟的数据的长度。 这是一个可变长度（数）。 0 是一个有效 <长度> <br/>
	 * <数据> <br/>
	 * 0 或更多的字节数据。 <br/>
	 */
	// FF 00 02 ss ss 音序号
	// FF 01 <长度> <数据> 文本事件
	// FF 02 <长度> <数据> 版本通告
	// FF 03 <长度> <数据> 音序 / Track 名称
	// FF 04 <长度> <数据> 乐器名称
	// FF 05 <长度> <数据> 歌词
	// FF 06 <长度> <数据> 标记
	// FF 07 <长度> <数据> 暗示
	// FF 20 01 cc MIDI Channel 前缀
	// FF 2F 00 Track 结束
	// FF 51 03 tt tt tt 拍子：设置演奏速度。
	// 　 tt tt tt 表示第一拍定义多少个Micro Seconds。它即是用来崐变演奏的速度。
	// FF 54 05 hh mm ss fr ff SMTPE 偏移量
	// FF 58 04 nn dd cc bb 拍子记号：设置时间记号。
	// 　 nn和dd直接对应到谱号的数字，dd使用2的指数。如3/8,则nn=3,dd=3。
	// 　 cc是代表第次节拍器打后的时间是几个MIDI clock。
	// 　 bb通常设置为8表示多少个MIDI clock等于1/4 拍。
	// FF 59 02 sf mi 音调符号
	// FF 7F <len> <id> <data> 音序器描述 Meta-event
	static class MetaEvent extends TrackEvent {
		int	sub_type;	// 0x00-0x7F;
		int	length;

		MetaEvent(int event_byte) {
			super(event_byte);
		}

		public boolean isTrackEnd() {
			if (sub_type == 0x2f && length == 0) {
				return true;
			}
			return false;
		}

		boolean readEvent(DataInputStream in) throws IOException {
			sub_type = in.read();
			length = (int) MidiFile.readVarLen(in, TrackBlock.byteBuf);
			this.params = new byte[length];
			in.read(this.params);
			return true;
		}

		boolean play(MidiChannel[] midiChannels) {
			// TODO Auto-generated method stub
			// System.out.println("MetaEvent");
			if (sub_type == 0x2f) {
				for (int i = 0; i < midiChannels.length; i++) {
					midiChannels[i].allNotesOff();
					midiChannels[i].allSoundOff();
				}
			} else {
				System.out.println(new String(this.params));
			}
			return false;
		}
	}

	/**
	 * <pre>
	 * MIDI事件指令 	参数（16进制） 	描述
	 * 16进制 	2进制
	 * 8x 	1000xxxx 	nn  vv 	音符关指令：关掉一个正在发音的音符，类型和强度有参数决定nn：音符编号，vv：音符力度
	 * 9x 	1001xxxx 	nn  vv 	音符开指令：发出一个声音nn:音符编号，vv：音符力度
	 * ax 	1010xxxx 	nn  vv 	压力变化指令，改变一个声音的发生强度，类型和新强度由参数决定nn：音符编号，vv：音符力度
	 * bx 	1011xxxx 	cc  vv 	乐器变化指令，改变乐器的演奏强度cc:乐器编号，vv：乐器演奏强度
	 * cx 	1100xxxx 	pp 	          音色变化指令，改变音色pp：音色编号
	 * dx 	1101xxxx 	cc 	           通道变化指令，改变通道的发声强度cc：通道发声强度
	 * ex 	1110xxxx 	bb  tt 	音调变化指令，改变音乐的音调bb：音调的低7位，tt：音调的高7位
	 * </pre>
	 */

	/**
	 * <pre>
	 * 	系统信息指令 	参数 	描述
	 * 	16进制 	2进制
	 * 	f0 	11110000 	ii dd …dd 	设备生产厂商专用信息指令。ii：设备设备生产厂商代码，当某个设备发现ii与自身代码相同时，接受其余的数据位dd，否则数据位被忽略；dd：用来记录数据信息（音色参数等），最高位为“0”
	 * 	f1 	11110001 	  	未定义
	 * 	f2 	11110010 	ll mm 	           乐曲位置指针指令，是一个内部14位寄存器，存储音乐开始计数时的MIDI的节拍数，MIDI规定一个节拍相当于6个DDT。ll：寄存器的低7位，mm：寄存器的高7位
	 * 	f3 	11110011 	ss 	乐曲选择指令，制定了以什么序列演奏，或者演奏什么乐曲，ss：序列或者乐曲编号
	 * 	f4 	11110100 	  	未定义
	 * 	f5 	11110101 	  	未定义
	 * 	f6 	11110110 	无 	音调调整要求指令，模拟合成器收到此信息，调整振荡器振荡频率，用于老式的电子合成器。
	 * 	f7 	11110111 	无 	结束系统专用信息指令，结束系统专用信息传递
	 * 	f8 	11111000 	无 	时钟指令，有同步要求时，没四分之一音符发送24次
	 * 	f9 	11111001 	  	未定义
	 * 	fa 	11111010 	无 	开始指令，开始现有演奏序列
	 * 	fb 	11111011 	无 	继续指令，继续执行中断的演奏序列
	 * 	fc 	11111100 	无 	停止指令，停止当前演奏序列
	 * 	fd 	11111101 	  	未定义
	 * 	fe 	11111110 	无 	联系激活指令，当使用该指令的时候，接收器每300毫秒接收下一个“fe”，否则认为连接终止。连接终止后，停止所有发音并恢复到正常工作状态（非联系激活状态）。
	 * 	ff 	11111111 	xx ll dd …dd 	系统复位指令，将所有接收器都恢复到电源打开的初始状态。另外，还可以容纳版权等注释信息和作为轨迹块的结束标志；xx：复位方式，最高位必须是“0”，ll：dd字节的个数，最高位必须是“0”，dd：注释等信息，最高位没有特殊要求
	 * </pre>
	 */
}
