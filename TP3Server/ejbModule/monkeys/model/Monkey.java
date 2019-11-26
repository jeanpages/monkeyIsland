package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Monkey")
public class Monkey {
	
	private int idMonkey;

	public Monkey() {}

	public int getIdMonkey() {
		return idMonkey;
	}

	public void setIdMonkey(int idMonkey) {
		this.idMonkey = idMonkey;
	}
	
	

}
