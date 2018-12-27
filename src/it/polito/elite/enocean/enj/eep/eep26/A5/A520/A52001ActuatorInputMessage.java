package it.polito.elite.enocean.enj.eep.eep26.A5.A520;

public class A52001ActuatorInputMessage
{

	public enum FunctionMode
	{
		RCU, SERVICE_ON
	};

	public enum SetPointSelectionModeMode
	{
		VALVE_POSITION, TEMPERATURE
	};

	private int valvePosition;
	private int temperatureFromRcu;
	private boolean runInitSequence = false;
	private boolean liftSet = false;
	private boolean valveOpen = false;
	private boolean valveClosed = false;
	private boolean summerMode = false;
	private SetPointSelectionModeMode setPointSelectionMode = SetPointSelectionModeMode.VALVE_POSITION;
	private boolean setpointInverse = false;
	private FunctionMode selectFunction = FunctionMode.RCU;

	public byte[] getMessageBytes()
	{
		byte[] data = { 0, 0, 0, 0 };

		data[3] = (byte) valvePosition;
		data[2] = (byte) temperatureFromRcu;

		if (runInitSequence)
		{
			data[1] |= 0x80;
		}

		if (liftSet)
		{
			data[1] |= 0x40;
		}

		if (valveOpen)
		{
			data[1] |= 0x20;
		}

		if (valveClosed)
		{
			data[1] |= 0x10;
		}

		if (summerMode)
		{
			data[1] |= 0x08;
		}

		if (SetPointSelectionModeMode.TEMPERATURE.equals(setPointSelectionMode))
		{
			data[1] |= 0x04;
		}

		if (setpointInverse)
		{
			data[1] |= 0x02;
		}

		if (FunctionMode.SERVICE_ON.equals(selectFunction))
		{
			data[1] |= 0x01;
		}

		// Data Telegram
		data[0] = 0x08;

		return data;
	}

	public void setValvePosition(int valvePosition)
	{
		this.valvePosition = valvePosition;
	}

	public void setTemperatureFromRcu(int temperatureFromRcu)
	{
		this.temperatureFromRcu = temperatureFromRcu;
	}

	public void setRunInitSequence(boolean runInitSequence)
	{
		this.runInitSequence = runInitSequence;
	}

	public void setLiftSet(boolean liftSet)
	{
		this.liftSet = liftSet;
	}

	public void setValveOpen(boolean valveOpen)
	{
		this.valveOpen = valveOpen;
	}

	public void setValveClosed(boolean valveClosed)
	{
		this.valveClosed = valveClosed;
	}

	public void setSummerMode(boolean summerMode)
	{
		this.summerMode = summerMode;
	}

	public void setSetPointSelectionMode(
			SetPointSelectionModeMode setPointSelectionMode)
	{
		this.setPointSelectionMode = setPointSelectionMode;
	}

	public void setSetpointInverse(boolean setpointInverse)
	{
		this.setpointInverse = setpointInverse;
	}

	public void setSelectFunction(FunctionMode selectFunction)
	{
		this.selectFunction = selectFunction;
	}

	@Override
	public String toString()
	{
		return "A52001ActuatorInputMessage [valvePosition=" + valvePosition
				+ ", temperatureFromRcu=" + temperatureFromRcu
				+ ", runInitSequence=" + runInitSequence + ", liftSet="
				+ liftSet + ", valveOpen=" + valveOpen + ", valveClosed="
				+ valveClosed + ", summerMode=" + summerMode
				+ ", setPointSelectionMode=" + setPointSelectionMode
				+ ", setpointInverse=" + setpointInverse + ", selectFunction="
				+ selectFunction + "]";
	}

}
