package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
@Table(name="Treasure")
public class Treasure extends Element{
	
	private boolean hidden;
	
	public Treasure() {}
	
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

	@Override
	public String toString() {
		return "Treasure [isHidden()=" + isHidden() + ", getId()=" + getId() + ", getPosX()=" + getPosX()
				+ ", getPosY()=" + getPosY() + "]";
	}

	
}
