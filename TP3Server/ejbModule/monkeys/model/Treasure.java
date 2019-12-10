package monkeys.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
@Table(name="treasure")
public class Treasure extends Element{
	
	private boolean visible;
	
	@OneToOne(mappedBy="treasure", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Island islandTreasure;
	
	public Treasure() {}
	
	public Island getIsland() {
		return this.islandTreasure;
	}
	
	public void setIsland(Island islandTreasure) {
		this.islandTreasure = islandTreasure;
	}
	
	
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
