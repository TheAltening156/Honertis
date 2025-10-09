package fr.honertis.settings;

public class NumberSettings extends Settings{
	private double defValue,
			   	   min,
			   	   max,
			   	   increment;

	public NumberSettings(String name, double val, double min, double max, double increment) {
		this.name = name;
		this.defValue = val;
		this.min = min;
		this.max = max;
		this.increment = increment;
		
	}

	public void setValue(double val) {
		double precision = 1.0 / this.increment;
		this.defValue = Math.round(Math.max(this.min, Math.min(this.max, val)) * precision) / precision;
	}
	@Override
	public Double getValue() {
		return defValue;
	}

	public int getIntValue() {
		return (int) defValue;
	}

	public float getFloatValue() {
		return (float) defValue;
	}

	public long getLongValue() {
		return (long) defValue;
	}
	public double getDefValue() {
		return defValue;
	}

	public void setDefValue(double defValue) {
		this.defValue = defValue;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}
	
	public void setValue(Object value) {
		if (value instanceof Double) {
			if ((double)value < this.min || (double)value > this.max) {
				return;
			}
			this.defValue = (Double) value;
		}
	}
}
