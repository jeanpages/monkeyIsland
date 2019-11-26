package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
@Table(name="Rum")
public class Rum {

	private int idRum;
	private boolean hidden;
	private int energy;
	
	
	public Rum() {}
	
	/**
	 * 
	 * @return idRum
	 */
	@Id
	public int getIdRum() {
		return idRum;
	}
	
	public void setIdRum(int idRum) {
		this.idRum = idRum;
	}
	
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
		return "Rum [idRum=" + idRum + ", hidden=" + hidden + ", energy=" + energy + "]";
	}
	
	
	
	
}
