package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="monkey")
public class Monkey extends Element {

	@ManyToOne
	@JoinColumn(name="ISLAND_ID")
	private Island island;

	public Island getIsland() {
		return this.island;
	}
	
	public void setIsland(Island island) {
		this.island = island;
	}
	
	public Monkey() {}

	@Override
	public String toString() {
		return "Monkey [getId()=" + getId() + ", getPosX()=" + getPosX() + ", getPosY()=" + getPosY() + "]";
	}
}
