package monkeys.model;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "Island")
public class Island {
	
	private int id;
	private int[][] map;

	public Island() {
		this.map = null;
	}
	
	private List<Monkey> monkeys = new ArrayList<>();
	private List<Pirate> pirates = new ArrayList<>();
	private Rum rum;
	private Treasure treasure;
	
	@OneToMany(mappedBy="Monkey", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public List<Monkey> getMonkeys(){
		return monkeys;
	}
	
	public void setMonkeys(List<Monkey> monkeys) {
		this.monkeys = monkeys;
	}
	
	@OneToMany(mappedBy="Pirate", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public List<Pirate> getPirates(){
		return pirates;
	}
	
	public void setPirates(List<Pirate> pirates) {
		this.pirates = pirates;
	}
	
	@OneToOne(mappedBy="Rum", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public Rum getRum(){
		return rum;
	}
	
	public void setRum(Rum rum) {
		this.rum = rum;
	}
	
	@OneToOne(mappedBy="Treasure", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public Treasure getTreasure(){
		return treasure;
	}
	
	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
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
	@Column(length = 100000)
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
