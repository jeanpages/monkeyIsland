package monkeys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Pirate extends Element {
	
	private int clientId;
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
	
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
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
		return "Pirate [clientId=" + clientId + ", status=" + status + ", energy=" + energy + ", " + super.toString() + "]";
	}

	
}
