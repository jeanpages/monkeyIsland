package monkeys.model;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="pirate")
public class Pirate extends Element {
	
	private String avatar;
	private State status;
	private int energy;

	@ManyToOne
	@JoinColumn(name="ISLAND_ID")
	private Island islandPirate;

	public Island getIsland() {
		return this.islandPirate;
	}
	
	public void setIsland(Island islandPirate) {
		this.islandPirate = islandPirate;
	}
	
	public Pirate() {}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public State getStatus() {
		return status;
	}

	public void setStatus(State status) {
		this.status = status;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	@Override
	public String toString() {
		return "Pirate [getAvatar()=" + getAvatar() + ", getStatus()=" + getStatus() + ", getEnergy()=" + getEnergy()
				+ ", getId()=" + getId() + ", getPosX()=" + getPosX() + ", getPosY()=" + getPosY() + "]";
	}
	
	
}
