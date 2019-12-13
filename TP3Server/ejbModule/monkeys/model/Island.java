package monkeys.model;

import java.io.Serializable;
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
import javax.persistence.Transient;
/**
 * @author Mickael Clavreul
 */
@Entity
@Table(name="island")
public class Island implements Serializable {
	
	@Transient
	private static Island instance = null;

	@Id
	@GeneratedValue
	private int id;
	
	@Column(length = 100000)
	private int[][] map;

	private Island() {
		this.map = null;
	}
	
	public static synchronized Island getInstance() {
		if (instance == null) {
			synchronized (Island.class) {
				if (instance == null) {
					instance = new Island();
				}
			}
		}
		return instance;
	}

	@OneToMany(mappedBy="islandMonkey", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Monkey> monkeys = new ArrayList<>();

	@OneToMany(mappedBy="islandPirate", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Pirate> pirates = new ArrayList<>();

	@OneToMany(mappedBy="islandRum", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Rum> rums = new ArrayList<>();

	@OneToOne
	private Treasure treasure;
	
	public List<Monkey> getMonkeys(){
		return monkeys;
	}
	
	public void setMonkeys(List<Monkey> monkeys) {
		this.monkeys = monkeys;
	}
	
	public List<Pirate> getPirates(){
		return pirates;
	}
	
	public void setPirates(List<Pirate> pirates) {
		this.pirates = pirates;
	}
	
	public List<Rum> getRums(){
		return rums;
	}
	
	public void setRum(List<Rum> rums) {
		this.rums = rums;
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
