package fr.honertis.settings;

public class KeyBindSettings extends Settings{
	public int key;
	public boolean change;
	
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
	@Override
	public void setValue(Object o) {
		if (o instanceof Integer) {
			this.key = (int) o;
		}
	}

	public void change() {
		change = !change;
	}
	public boolean isChanging() {
		return change;
	}
}
