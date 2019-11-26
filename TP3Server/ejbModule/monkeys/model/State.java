package monkeys.model;

public enum State {
	SOBER("Sobre"),
	DRUNK("Ivre"),
	DEAD("Mort");
	
	private String name = "";
	
	State(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
