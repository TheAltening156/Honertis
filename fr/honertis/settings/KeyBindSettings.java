package fr.honertis.settings;

public class KeyBindSettings extends Settings{
	public int key;
	
	public KeyBindSettings(String name, int key) {
		this.name = name;
		this.key = key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
	
	@Override
	public Integer getValue() {
		return getKey();
	}
	
	public void setValue(Object o) {
		if (o instanceof Boolean) {
			this.key = (int) o;
		}
	}
}
