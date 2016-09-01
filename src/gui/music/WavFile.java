package gui.music;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WavFile {

	private WavFileHeader	fileHeader		= null;
	private File			wavFile			= null;
	private boolean			headAppendFlag	= false;
	private boolean			pcmFormatFlag	= false;
	private int				dataBlockNum	= 0;
	private WavDataBlock[]	dataBlocks		= null;

	public static void main(String[] args) {
		WavFile wavFile = new WavFile();
		String fileName = "audio/untitled1.wav";
		// fileName = "audio/eightiesJam.wav";
		 fileName = "audio/hitchcock.wav";
//		 fileName = "audio/977792068.wav";
		// fileName = "audio/test.wav";
		// fileName = "audio/1398284679.wav";
//		 fileName = "audio/1-welcome.wav";

		boolean flag = wavFile.readWaveFile(fileName);
		System.out.println(flag);
		System.out.println(wavFile.pcmFormatFlag);

		if (flag) {
			wavFile.showWave();
		}

		// test more
		// for (int i = 1; i <= 11; i++) {
		// System.out.println();
		// fileName = "audio/test/" + i + ".wav";
		// System.out.println(fileName);
		// flag = wavFile.readWaveFile(fileName);
		// System.out.println(flag);
		// System.out.println(wavFile.pcmFormatFlag);
		// }
	}

	/**
	 * 读取wav文件,现在只读取PCM文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean readWaveFile(String fileName) {
		init();
		FileInputStream in = null;
		try {
			wavFile = new File(fileName);
			if (!wavFile.exists()) {
				return false;
			}
			if (0 == wavFile.length()) {
				return false;
			}
			in = new FileInputStream(wavFile);
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

	public void showWave() {
		boolean dual_flag = (fileHeader.channel_num == 2);
		boolean bit_8 = (fileHeader.sample_bits == 8);
		StringBuffer sb = new StringBuffer(256);
		for (int i = 0; i < dataBlocks.length; i++) {
			quantify(dataBlocks[i], bit_8, dual_flag, sb);
		}
	}

	private void quantify(WavDataBlock dataBlock, boolean bit_8, boolean dual_flag, StringBuffer sb) {
		final int QUANT_MAX = 40;
		sb.setLength(0);
		if (dual_flag) {
			int l = dataBlock.getTrackLeft();
			int r = dataBlock.getTrackRight();
			if (bit_8) {
				l = l * QUANT_MAX / 256;
				r = r * QUANT_MAX / 256;
			} else {
				l = l * QUANT_MAX / (256 * 256);
				r = r * QUANT_MAX / (256 * 256);
			}
			charN('-', l, sb);
			space(QUANT_MAX - l, sb);
			sb.append('|');
			charN('+', r, sb);
		} else {
			int val = dataBlock.getTrack();
			val = val * 40 / (bit_8 ? 256 : 256 * 256);
			charN('_', val, sb);
		}
		System.out.println(sb.toString());
	}

	private void space(int n, StringBuffer sb) {
		for (int i = 0; i < n; i++) {
			sb.append(' ');
		}
	}

	private void charN(char c, int n, StringBuffer sb) {
		for (int i = 0; i < n; i++) {
			sb.append(c);
		}
	}

	/**
	 * 清除数据
	 */
	private void init() {
		fileHeader = null;
		wavFile = null;
		headAppendFlag = false;
		pcmFormatFlag = false;
		dataBlockNum = 0;
		dataBlocks = null;
	}

	/**
	 * 低位在前，高位在后
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static int readInt(FileInputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
	}

	/**
	 * 低位在前，高位在后
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static short readShort(FileInputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch2 << 8) + (ch1 << 0));
	}

	/**
	 * 读取文件头
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private boolean readFileHeader(FileInputStream in) throws IOException {
		if (fileHeader == null) {
			fileHeader = new WavFileHeader();
		}
		in.read(fileHeader.riff_flag);
		fileHeader.file_length = readInt(in);
		// System.out.println(Integer.toHexString(fileHeader.file_length));
		in.read(fileHeader.wave_flag);
		in.read(fileHeader.fmt_flag);
		fileHeader.mid_size = readInt(in);
		fileHeader.format_type = readShort(in);
		// System.out.println(Integer.toHexString(fileHeader.format_type));
		fileHeader.channel_num = readShort(in);
		// System.out.println(Integer.toHexString(fileHeader.channel_num));
		fileHeader.sample_rate = readInt(in);
		// System.out.println(Integer.toHexString(fileHeader.sample_rate));
		fileHeader.speed = readInt(in);
		// System.out.println(Integer.toHexString(fileHeader.speed));
		fileHeader.block_size = readShort(in);
		// System.out.println(Integer.toHexString(fileHeader.block_size));
		fileHeader.sample_bits = readShort(in);
		// System.out.println(Integer.toHexString(fileHeader.sample_bits));

		if (fileHeader.mid_size == 18) {
			headAppendFlag = true;
			in.read(fileHeader.mid_data);
			in.read(fileHeader.fact_flag);
			fileHeader.fact_chunk_size = readInt(in);// 一般为4；
			if (fileHeader.fact_chunk_size == 4) {
				fileHeader.fact_chunk_num = readInt(in);
			} else {
				fileHeader.fact_chunk = new byte[fileHeader.fact_chunk_size];
				in.read(fileHeader.fact_chunk);
			}
		}
		in.read(fileHeader.data_flag);
		fileHeader.block_num = readInt(in);
		// System.out.println(Integer.toHexString(fileHeader.block_num));
		return checkFileHeader();
	}

	/**
	 * 检查文件头
	 * 
	 * @return
	 */
	private boolean checkFileHeader() {
		if (!"RIFF".equalsIgnoreCase(new String(fileHeader.riff_flag))) {
			return false;
		}
		// System.out.println(Long.toHexString(wavFile.length()));
		if (wavFile.length() != (fileHeader.file_length + 8)) {
			return false;
		}
		if (!"WAVE".equalsIgnoreCase(new String(fileHeader.wave_flag))) {
			return false;
		}
		if (!"fmt ".equalsIgnoreCase(new String(fileHeader.fmt_flag))) {
			return false;
		}
		// 只支持PCM格式
		if (fileHeader.format_type == 0x0001) {
			pcmFormatFlag = true;
		} else {
			pcmFormatFlag = false;
		}
		// System.out.println("channel_num:" + fileHeader.channel_num);
		// System.out.println("sample_rate:" + fileHeader.sample_rate);
		// System.out.println("sample_bits:" + fileHeader.sample_bits);
		// System.out.println("speed:" + fileHeader.speed);
		// System.out.println("C*R*B=" + fileHeader.channel_num * fileHeader.sample_rate * fileHeader.sample_bits);
		if (fileHeader.speed != fileHeader.channel_num * fileHeader.sample_rate * fileHeader.sample_bits / 8) {
			return false;
		}
		// System.out.println("block_size:" + fileHeader.block_size);
		// System.out.println("C*B=" + fileHeader.channel_num * fileHeader.sample_bits);
		if (fileHeader.block_size != fileHeader.channel_num * fileHeader.sample_bits / 8) {
			return false;
		}

		if (headAppendFlag) {
			// System.out.println(new String(fileHeader.fact_flag));
			// System.out.println(fileHeader.fact_chunk_size);// 一般为4；
			if (fileHeader.fact_chunk_size == 4) {
				// System.out.println(fileHeader.fact_chunk_num);
				// System.out.println("fact_chunk_num*block_size=" + fileHeader.fact_chunk_num * fileHeader.block_size);
				if (fileHeader.block_num != fileHeader.fact_chunk_num * fileHeader.block_size) {
					return false;
				}
			} else {
				fileHeader.fact_chunk = new byte[fileHeader.fact_chunk_size];
				// System.out.println(new String(fileHeader.fact_chunk));
			}
		}

		if (!"data".equalsIgnoreCase(new String(fileHeader.data_flag))) {
			return false;
		}
		// System.out.println(fileHeader.block_num);
		// System.out.println(fileHeader.file_length - fileHeader.block_num);
		if (headAppendFlag) {
			if (50 != (fileHeader.file_length - fileHeader.block_num)) {
				return false;
			}
		} else { // 16
			if (36 != (fileHeader.file_length - fileHeader.block_num)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 读取数据部分
	 * 
	 * @param in
	 */
	private boolean readFileBody(FileInputStream in) throws IOException {
		// 判断采样和声道数类型
		int type = 0;
		if (fileHeader.channel_num == 1) {
			if (fileHeader.sample_bits == 8) {
				type = WavDataBlock.TYPE_PCM8BITSINGLE;
			} else if (fileHeader.sample_bits == 16) {
				type = WavDataBlock.TYPE_PCM16BITSINGLE;
			} else {
				return false;
			}
		} else if (fileHeader.channel_num == 2) {
			if (fileHeader.sample_bits == 8) {
				type = WavDataBlock.TYPE_PCM8BITDUAL;
			} else if (fileHeader.sample_bits == 16) {
				type = WavDataBlock.TYPE_PCM16BITDUAL;
			} else {
				return false;
			}
		} else {
			return false;
		}
		// 计算块数
		dataBlockNum = fileHeader.block_num / fileHeader.block_size;
		dataBlocks = new WavDataBlock[dataBlockNum];
		// 读取
		for (int i = 0; i < dataBlockNum; i++) {
			dataBlocks[i] = WavDataBlock.readBlock(in, type);
		}
		return true;
	}

	static class WavFileHeader {
		byte[]	riff_flag	= new byte[4];
		int		file_length;
		byte[]	wave_flag	= new byte[4];
		byte[]	fmt_flag	= new byte[4];
		int		mid_size;
		short	format_type;
		short	channel_num;
		int		sample_rate;
		int		speed;
		short	block_size;
		short	sample_bits;
		byte[]	mid_data	= new byte[2];
		byte[]	fact_flag	= new byte[4];
		int		fact_chunk_size;
		int		fact_chunk_num;
		byte[]	fact_chunk	= new byte[4];
		byte[]	data_flag	= new byte[4];
		int		block_num;
	}

	static abstract class WavDataBlock {
		abstract boolean readBlock(FileInputStream in) throws IOException;

		static final int	TYPE_PCM8BITSINGLE	= 1;
		static final int	TYPE_PCM8BITDUAL	= 2;
		static final int	TYPE_PCM16BITSINGLE	= 3;
		static final int	TYPE_PCM16BITDUAL	= 4;

		int getTrack() {
			return 0x80;
		}

		int getTrackLeft() {
			return 0x8000;
		}

		int getTrackRight() {
			return 0x8000;
		}

		static WavDataBlock readBlock(FileInputStream in, int type) throws IOException {
			WavDataBlock wdb = null;
			switch (type) {
			case TYPE_PCM8BITSINGLE:
				wdb = new pcm8bitSingle();
				wdb.readBlock(in);
				break;
			case TYPE_PCM8BITDUAL:
				wdb = new pcm8bitDual();
				wdb.readBlock(in);
				break;
			case TYPE_PCM16BITSINGLE:
				wdb = new pcm16bitSingle();
				wdb.readBlock(in);
				break;
			case TYPE_PCM16BITDUAL:
				wdb = new pcm16bitDual();
				wdb.readBlock(in);
				break;
			}
			return wdb;
		}
	}

	static class pcm8bitSingle extends WavDataBlock {
		byte	value;

		boolean readBlock(FileInputStream in) throws IOException {
			value = (byte) in.read();
			return true;
		}

		int getTrack() {
			return (0xFF & value);
		}
	}

	static class pcm8bitDual extends WavDataBlock {
		byte	trackLeft;
		byte	trackRight;

		boolean readBlock(FileInputStream in) throws IOException {
			trackLeft = (byte) in.read();
			trackRight = (byte) in.read();
			return true;
		}

		int getTrackLeft() {
			return (0xFF & trackLeft);
		}

		int getTrackRight() {
			return (0xFF & trackRight);
		}
	}

	static class pcm16bitSingle extends WavDataBlock {
		short	value;

		boolean readBlock(FileInputStream in) throws IOException {
			value = WavFile.readShort(in);
			return true;
		}

		int getTrack() {
			return (0x8000 + value);
		}
	}

	static class pcm16bitDual extends WavDataBlock {
		short	trackLeft;
		short	trackRight;

		boolean readBlock(FileInputStream in) throws IOException {
			trackLeft = WavFile.readShort(in);
			trackRight = WavFile.readShort(in);
			return true;
		}

		int getTrackLeft() {
			return (0x8000 + trackLeft);
		}

		int getTrackRight() {
			return (0x8000 + trackRight);
		}
	}
}

/**
 * 
 * <pre>
 *  WAVE文件格式说明表
 *  
 * 文件头
 *   偏移地址    字节数    数据类型            内   容
 *      00H    4     char        "RIFF"标志
 *      04H    4     long int    文件长度
 *      08H    4     char        "WAVE"标志
 *      0CH    4     char        "fmt"标志
 *      10H    4    　                                      过渡字节（不定）  数值为16或18，18则最后又附加信息 
 *      14H    2     int         格式类别（0001H为PCM形式的声音数据)
 *      16H    2     int         通道数，单声道为1，双声道为2
 *      18H    4     int         采样率（每秒样本数），表示每个通道的播放速度，
 *      1CH    4     long int    波形音频数据传送速率，其值为通道数×每秒数据位数×每样本的数据位数／8。播放软件利用此值可以估计缓冲区的大小。
 *      20H    2     int         数据块的调整数（按字节算的），其值为通道数×每样本的数据位值／8。播放软件需要一次处理多个该值大小的字节数据，以便将其值用于缓冲区的调整。
 *      22H    2        　                                  每样本的数据位数，表示每个声道中各个样本的数据位数。如果有多个声道，对每个声道而言，样本大小都一样。
 *      24H    4     char        数据标记符＂data＂
 *      28H    4     long int    语音数据的长度
 *  
 * PCM数据的存放方式： 
 *               样本1                           样本2
 *   8位单声道         0声道                                                                       0声道
 *   8位立体声         0声道（左）                  1声道（右）                    0声道（左）                  1声道（右）
 *   16位单声道      0声道低字节              0声道高字节               0声道低字节              0声道高字节
 *   16位立体声      0声道（左）低字节    0声道（左）高字节    1声道（右）低字节    1声道（右）高字节
 *  
 * WAVE文件的每个样本值包含在一个整数i中，i的长度为容纳指定样本长度所需的最小字节数。首先存储低有效字节，表示样本幅度的位放在i的高有效位上，剩下的位置为0，这样8位和16位的PCM波形样本的数据格式如下所示。　
 *   样本大小       数据格式                         最大值    最小值
 *   8位PCM    unsigned int     225     0
 *   16位PCM   int              32767   -32767
 * </pre>
 */
