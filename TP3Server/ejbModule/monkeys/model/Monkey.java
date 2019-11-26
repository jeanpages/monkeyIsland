package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Monkey")
public class Monkey extends Element {

	public Monkey() {}

	@Override
	public String toString() {
		return "Monkey [getId()=" + getId() + ", getPosX()=" + getPosX() + ", getPosY()=" + getPosY() + "]";
	}
}
