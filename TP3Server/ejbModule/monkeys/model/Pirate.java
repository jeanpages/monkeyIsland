package monkeys.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Pirate")
public class Pirate {
	
	private int idPirate;
	private String avatar;
	private State status;
	private int energy;
	
	public Pirate() {}

	@Id
	public int getIdPirate() {
		return idPirate;
	}

	public void setIdPirate(int idPirate) {
		this.idPirate = idPirate;
	}

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
}
