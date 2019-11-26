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
@Table(name="Treasure")
public class Treasure {
	
	private int idtreasure;
	private boolean hidden;
	
	public Treasure() {}
	
	/**
	 * 
	 * @return idTreasure
	 */
	@Id
	public int getIdtreasure() {
		return idtreasure;
	}
	
	public void setIdtreasure(int idtreasure) {
		this.idtreasure = idtreasure;
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

	@Override
	public String toString() {
		return "Treasure [idtreasure=" + idtreasure + ", hidden=" + hidden + "]";
	}
	
	
	
	

}
