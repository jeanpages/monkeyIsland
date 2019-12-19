package monkeys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	@OneToMany(mappedBy="islandMonkey", fetch=FetchType.EAGER, orphanRemoval = true)
	private List<Monkey> monkeys = new ArrayList<>();

	@OneToMany(mappedBy="islandPirate", fetch=FetchType.EAGER, orphanRemoval = true)
	private List<Pirate> pirates = new ArrayList<>();

	@OneToMany(mappedBy="islandRum", fetch=FetchType.EAGER, orphanRemoval = true)
	private List<Rum> rums = new ArrayList<>();

	@OneToOne
	private Treasure treasure;

	
	
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
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}
	
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
	
	public void setRums(List<Rum> rums) {
		this.rums = rums;
	}
	
	public Treasure getTreasure(){
		return treasure;
	}
	
	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	@Override
	public String toString() {
		return "Island [id=" + id + ", map=" + Arrays.toString(map) + ", monkeys=" + monkeys + ", pirates=" + pirates
				+ ", rums=" + rums + ", treasure=" + treasure + "]";
	}
}
