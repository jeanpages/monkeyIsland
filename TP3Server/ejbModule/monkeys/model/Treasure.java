package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * 
 * @author Petit Valentin
 *
 */
@Entity
public class Treasure extends Element{
	
	private boolean visible;
	
	@OneToOne(mappedBy="treasure", fetch=FetchType.EAGER, orphanRemoval = true)
	private Island islandTreasure;
	
	public Island getIsland() {
		return this.islandTreasure;
	}
	
	public void setIsland(Island islandTreasure) {
		this.islandTreasure = islandTreasure;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return "Treasure [visible=" + visible + super.toString() + "]";
	}

	
}
