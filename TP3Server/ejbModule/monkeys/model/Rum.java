package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
public class Rum extends Element {

	private boolean visible;
	private int energy;
	
	@ManyToOne
	@JoinColumn(name="ISLAND_ID")
	private Island islandRum;

	public Island getIsland() {
		return this.islandRum;
	}
	
	public void setIsland(Island islandRum) {
		this.islandRum = islandRum;
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
