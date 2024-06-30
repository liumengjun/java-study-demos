package com.three_d.mofangv2;

import java.io.Serializable;
import java.util.Vector;

class MofangStatusMessage implements Serializable
{

	BlockMessage[][][] dataMessage;

	MofangStatusMessage(MofangStatusMessage aMofangStatusMessage)
	{
		BlockMessage[][][] data = new BlockMessage[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					data[i][j][k] = new BlockMessage(
							aMofangStatusMessage.dataMessage[i][j][k]);
				}
			}
		}

		this.dataMessage = data;
	}

	void setMofangStatusMessage(MofangStatusMessage aMofangStatusMessage)
	{
		// BlockMessage[][][] data=new BlockMessage[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.dataMessage[i][j][k]
							.setBlockMessage(aMofangStatusMessage.dataMessage[i][j][k]);
				}
			}
		}

		// dataMessage=data;
	}

	MofangStatusMessage(BlockMessage[][][] arrayBlockMessage)
	{
		BlockMessage[][][] data = new BlockMessage[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					data[i][j][k] = new BlockMessage(arrayBlockMessage[i][j][k]);
				}
			}
		}

		this.dataMessage = data;

	}

	MofangStatusMessage(Vector aVector)
	{
		BlockMessage[][][] data = new BlockMessage[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					data[i][j][k] = new BlockMessage((BlockMessage) aVector
							.get(3 * 3 * i + 3 * j + k));
				}
			}
		}
		this.dataMessage = data;
	}

	MofangStatusMessage(String aString)
	{

		if (aString.compareTo("new") == 0)
		{

			BlockMessage[][][] data = {
					{
							{ new BlockMessage(-1, -1, -1),
									new BlockMessage(-1, -1, 0),
									new BlockMessage(-1, -1, 1) },
							{ new BlockMessage(-1, 0, -1),
									new BlockMessage(-1, 0, 0),
									new BlockMessage(-1, 0, 1) },
							{ new BlockMessage(-1, 1, -1),
									new BlockMessage(-1, 1, 0),
									new BlockMessage(-1, 1, 1) } },
					{
							{ new BlockMessage(0, -1, -1),
									new BlockMessage(0, -1, 0),
									new BlockMessage(0, -1, 1) },
							{ new BlockMessage(0, 0, -1),
									new BlockMessage(0, 0, 0),
									new BlockMessage(0, 0, 1) },
							{ new BlockMessage(0, 1, -1),
									new BlockMessage(0, 1, 0),
									new BlockMessage(0, 1, 1) } },
					{
							{ new BlockMessage(1, -1, -1),
									new BlockMessage(1, -1, 0),
									new BlockMessage(1, -1, 1) },
							{ new BlockMessage(1, 0, -1),
									new BlockMessage(1, 0, 0),
									new BlockMessage(1, 0, 1) },
							{ new BlockMessage(1, 1, -1),
									new BlockMessage(1, 1, 0),
									new BlockMessage(1, 1, 1) } } };

			this.dataMessage = data;

		}
	}

	void moFang3DRotate(int fangShi, int layer012, int jiaoDU)
	{

		if (fangShi == 0)
		{

			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					for (int k = 0; k < 3; k++)
					{
						if (this.dataMessage[i][j][k].blockCenter.x == layer012 - 1)
						{
							this.dataMessage[i][j][k].block3DRotate(fangShi,
									jiaoDU);
						}

					}
				}
			}
		} else if (fangShi == 1)
		{

			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					for (int k = 0; k < 3; k++)
					{
						if (this.dataMessage[i][j][k].blockCenter.y == layer012 - 1)
						{
							this.dataMessage[i][j][k].block3DRotate(fangShi,
									jiaoDU);
						}

					}
				}
			}
		} else if (fangShi == 2)
		{

			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					for (int k = 0; k < 3; k++)
					{
						if (this.dataMessage[i][j][k].blockCenter.z == layer012 - 1)
						{
							this.dataMessage[i][j][k].block3DRotate(fangShi,
									jiaoDU);
						}

					}
				}
			}
		}

	}

}
