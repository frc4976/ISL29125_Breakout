package ca._4976.color;

import edu.wpi.first.wpilibj.I2C;

import java.awt.*;

public class ISL29125 {

	public I2C sensor;
	public byte[] buffer8;
	public byte[] buffer16;

	public ISL29125() {
		sensor = new I2C(I2C.Port.kOnboard, ISL_I2C_ADDR);
		buffer8 = new byte[1];
		buffer16 = new byte[2];
	}
	
	public boolean init() {
		boolean ret = true;
		ret = (read8(DEVICE_ID) == 0x7D);
		ret &= reset();
		ret &= config(CFG1_MODE_RGB | CFG1_375LUX, CFG2_IR_ADJUST_HIGH, CFG_DEFAULT);
		return ret;
	}
	
	public boolean reset() {
		int ret = 0x00;
		write8(DEVICE_ID, 0x46);
		ret = read8(CONFIG_1);
		ret |= read8(CONFIG_2);
		ret |= read8(CONFIG_3);
		ret |= read8(STATUS);
		return (ret == 0x00);
	}
	
	public boolean config(int config1, int config2, int config3) {
		boolean ret = true;
		
		write8(CONFIG_1, config1);
		write8(CONFIG_2, config2);
		write8(CONFIG_3, config3);
		
		ret &= (read8(CONFIG_1) == config1);
		ret &= (read8(CONFIG_2) == config2);
		ret &= (read8(CONFIG_3) == config3);
		
		return ret;
	}
	
	public int read8(int registerAddress) {
		sensor.read(registerAddress, 1, buffer8);
		return buffer8[0];
	}
	
	public int read16(int registerAddress) {
		sensor.read(registerAddress, 2, buffer16);
		return (buffer16[0] |= buffer16[1]);
	}
	
	public void write8(int registerAddress, int value) {
		sensor.write(registerAddress, value);
	}
	
	// BEGIN CONSTANTS //
	
	// ISL29125 I2C Address
	public final int ISL_I2C_ADDR = 0x44;

	// ISL29125 Registers
	public final int DEVICE_ID = 0x00;
	public final int CONFIG_1 = 0x01;
	public final int CONFIG_2 = 0x02;
	public final int CONFIG_3 = 0x03;
	public final int THRESHOLD_LL = 0x04;
	public final int THRESHOLD_LH = 0x05;
	public final int THRESHOLD_HL = 0x06;
	public final int THRESHOLD_HH = 0x07;
	public final int STATUS = 0x08;
	public final int GREEN_L = 0x09;
	public final int GREEN_H = 0x0A;
	public final int RED_L = 0x0B;
	public final int RED_H = 0x0C;
	public final int BLUE_L = 0x0D;
	public final int BLUE_H = 0x0E;
	
	// Configuration Settings
	public final int CFG_DEFAULT = 0x00;

	// CONFIG1
	// Pick a mode, determines what color[s] the sensor samples, if any
	public final int CFG1_MODE_POWERDOWN = 0x00;
	public final int CFG1_MODE_G = 0x01;
	public final int CFG1_MODE_R = 0x02;
	public final int CFG1_MODE_B = 0x03;
	public final int CFG1_MODE_STANDBY = 0x04;
	public final int CFG1_MODE_RGB = 0x05;
	public final int CFG1_MODE_RG = 0x06;
	public final int CFG1_MODE_GB = 0x07;

	// Light intensity range
	// In a dark environment 375Lux is best, otherwise 10KLux is likely the best option
	public final int CFG1_375LUX = 0x00;
	public final int CFG1_10KLUX = 0x08;

	// Change this to 12 bit if you want less accuracy, but faster sensor reads
	// At default 16 bit, each sensor sample for a given color is about ~100ms
	public final int CFG1_16BIT = 0x00;
	public final int CFG1_12BIT = 0x10;

	// Unless you want the interrupt pin to be an input that triggers sensor sampling, leave this on normal
	public final int CFG1_ADC_SYNC_NORMAL = 0x00;
	public final int CFG1_ADC_SYNC_TO_INT = 0x20;

	// CONFIG2
	// Selects upper or lower range of IR filtering
	public final int CFG2_IR_OFFSET_OFF = 0x00;
	public final int CFG2_IR_OFFSET_ON = 0x80;

	// Sets amount of IR filtering, can use these presets or any value between = 0x00 and = 0x3F
	// Consult datasheet for detailed IR filtering calibration
	public final int CFG2_IR_ADJUST_LOW = 0x00;
	public final int CFG2_IR_ADJUST_MID = 0x20;
	public final int CFG2_IR_ADJUST_HIGH = 0x3F;

	// CONFIG3
	// No interrupts, or interrupts based on a selected color
	public final int CFG3_NO_INT = 0x00;
	public final int CFG3_G_INT = 0x01;
	public final int CFG3_R_INT = 0x02;
	public final int CFG3_B_INT = 0x03;

	// How many times a sensor sample must hit a threshold before triggering an interrupt
	// More consecutive samples means more times between interrupts, but less triggers from short transients
	public final int CFG3_INT_PRST1 = 0x00;
	public final int CFG3_INT_PRST2 = 0x04;
	public final int CFG3_INT_PRST4 = 0x08;
	public final int CFG3_INT_PRST8 = 0x0C;

	// If you would rather have interrupts trigger when a sensor sampling is complete, enable this
	// If this is disabled, interrupts are based on comparing sensor data to threshold settings
	public final int CFG3_RGB_CONV_TO_INT_DISABLE = 0x00;
	public final int CFG3_RGB_CONV_TO_INT_ENABLE = 0x10;

	// STATUS FLAG MASKS
	public final int FLAG_INT = 0x01;
	public final int FLAG_CONV_DONE = 0x02;
	public final int FLAG_BROWNOUT = 0x04;
	public final int FLAG_CONV_G = 0x10;
	public final int FLAG_CONV_R = 0x20;
	public final int FLAG_CONV_B = 0x30;
	
}
