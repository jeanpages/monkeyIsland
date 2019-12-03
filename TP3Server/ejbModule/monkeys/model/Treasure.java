package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
@Table(name="Treasure")
public class Treasure extends Element{
	
	private boolean visible;
	
	private Island island;

	@OneToOne
	@JoinColumn(name="ISLAND_ID")
	public Island getIsland() {
		return this.island;
	}
	
	public void setIsland(Island island) {
		this.island = island;
	}
	
	public Treasure() {}
	
	/**
	 * 
	 * @return hidden
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return "Treasure [isHidden()=" + isVisible() + ", getId()=" + getId() + ", getPosX()=" + getPosX()
				+ ", getPosY()=" + getPosY() + "]";
	}

	
}
