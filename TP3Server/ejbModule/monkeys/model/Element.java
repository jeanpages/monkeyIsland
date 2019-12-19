package monkeys.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
/**
 * @author Mickael Clavreul
 */
@Entity
@DiscriminatorOptions(force=true)
@Table(name = "Element")
public abstract class Element implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	private int posX;
	private int posY;
	
	public Element() {}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return posX
	 */
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return posY
	 */
	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	@Override
	public String toString() {
		return "id=" + id + ", posX=" + posX + ", posY=" + posY;
	}
	
	
}
