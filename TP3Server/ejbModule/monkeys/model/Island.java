package monkeys.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * @author Mickael Clavreul
 */
@Entity
@Table(name="island")
public class Island {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(length = 100000)
	private int[][] map;

	public Island() {
		this.map = null;
	}

	@OneToMany(mappedBy="island", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Collection<Monkey> monkeys = new ArrayList<>();

	@OneToMany(mappedBy="island", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Collection<Pirate> pirates = new ArrayList<>();

	@OneToOne(mappedBy="island", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Rum rum;

	@OneToOne(mappedBy="island", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Treasure treasure;
	
	public Collection<Monkey> getMonkeys(){
		return monkeys;
	}
	
	public void setMonkeys(Collection<Monkey> monkeys) {
		this.monkeys = monkeys;
	}
	
	public Collection<Pirate> getPirates(){
		return pirates;
	}
	
	public void setPirates(Collection<Pirate> pirates) {
		this.pirates = pirates;
	}
	
	public Rum getRum(){
		return rum;
	}
	
	public void setRum(Rum rum) {
		this.rum = rum;
	}
	
	public Treasure getTreasure(){
		return treasure;
	}
	
	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the map
	 */
	public int[][] getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(int[][] map) {
		this.map = map;
	}
	
	public String toString() {
		return "Island[id=" + this.getId() + "; map=" + this.getMap() + "]";
	}
}
