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

	private boolean hidden;
	private int energy;
	
	public Rum() {}
	
	/**
	 * 
	 * @return hidden
	 */
	public boolean isHidden() {
		return hidden;
	}
	
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
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
		return "Rum [isHidden()=" + isHidden() + ", getEnergy()=" + getEnergy() + ", getId()=" + getId()
				+ ", getPosX()=" + getPosX() + ", getPosY()=" + getPosY() + "]";
	}
	
	
}
