package monkeys;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import monkeys.model.Element;
import monkeys.model.Island;
import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;

/**
 * Session Bean implementation class MonkeyIsland
 * @author Mickael Clavreul
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;
	private Island myLand;
	private Pirate myPirate;
	private Rum rum;
	private Treasure treasure;
	private Monkey monkey;
	
	@EJB
	private Configuration config;
	
	@EJB
	private Communication com;
	
	/**
     * Default constructor
     */
    public MonkeyIsland() {
    	
    }

	@Override
	public void subscribe(String id) throws IOException {
		newGame(id);
	}
	
	@Override
	public void disconnect(String id) {
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void newGame(String name) throws IOException {
		
		Query queryRum = manager.createQuery(
	              "SELECT DISTINCT e FROM Element e WHERE TYPE(e) = Rum");
		
		
		if (!queryRum.getResultList().isEmpty()) {
			rum = (Rum) queryRum.getResultList().get(0);
		} else {
			rum = config.getRum("monkeys.properties");
			manager.persist(rum);
		}
		
		Query queryTreasure = manager.createQuery(
	              "SELECT DISTINCT e FROM Element e WHERE TYPE(e) = Treasure");
		
		
		if (!queryTreasure.getResultList().isEmpty()) {
			treasure = (Treasure) queryTreasure.getResultList().get(0);
		} else {
			treasure = config.getTreasure("monkeys.properties");
			manager.persist(treasure);
		}

		if (manager.find(Island.class, Integer.valueOf(name)) != null) {
			myLand = manager.find(Island.class, Integer.valueOf(name));			
		} else {
			myLand = config.getMap("monkeys.properties");
			myLand.setId(Integer.valueOf(name));
			myLand.setRum(rum);
			myLand.setTreasure(treasure);
			manager.persist(myLand);
		}
		
		Query queryMonkey = manager.createQuery(
	              "SELECT DISTINCT e FROM Element e, Island i Where i.id = e.islandMonkey AND i.id =" + Integer.valueOf(name) + " AND TYPE(e) = Monkey");
		
		
		if (!queryMonkey.getResultList().isEmpty()) {
			monkey = (Monkey) queryMonkey.getResultList().get(0);
		} else {
			monkey = config.getMonkey("monkeys.properties");
			monkey.setIsland(myLand);
			manager.persist(monkey);
		}
		
		Query queryPirate = manager.createQuery(
	              "SELECT DISTINCT e FROM Element e, Island i Where i.id = e.islandPirate AND i.id =" + Integer.valueOf(name) + " AND TYPE(e) = Pirate");
		
		
		if (!queryPirate.getResultList().isEmpty()) {
			myPirate = (Pirate) queryPirate.getResultList().get(0);
		} else {
			myPirate = config.getPirate("monkeys.properties");
			myPirate.setIsland(myLand);
			manager.persist(myPirate);
		}
		
		com.sendMap(myLand.getMap(), String.valueOf(myLand.getId()));
		com.sendMonkey(monkey, String.valueOf(monkey.getId()));
		com.sendPirate(myPirate, String.valueOf(myPirate.getId()));
		com.sendRum(rum, String.valueOf(rum.getId()));
		com.sendTreasure(treasure, String.valueOf(treasure.getId()));
	}
}
