package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Monkey")
public class Monkey extends Element {
	
	private Island island;

	@ManyToOne
	@JoinColumn(name="ISLAND_ID")
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
