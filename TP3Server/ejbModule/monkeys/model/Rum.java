package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
@Table(name="Rum")
public class Rum extends Element {

	private boolean visible;
	private int energy;
	
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
