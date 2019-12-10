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
@Table(name="rum")
public class Rum extends Element {

	private boolean visible;
	private int energy;
	
	@OneToOne
	private Island island;

	public Island getIsland() {
		return this.island;
	}
	
	public void setIsland(Island island) {
		this.island = island;
	}
	
	public Rum() {}
	
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
	
	/**
	 * 
	 * @return energy
	 */
	public int getEnergy() {
		return energy;
	}
	
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	@Override
	public String toString() {
		return "Rum [isHidden()=" + isVisible() + ", getEnergy()=" + getEnergy() + ", getId()=" + getId()
				+ ", getPosX()=" + getPosX() + ", getPosY()=" + getPosY() + "]";
	}
	
	
}
