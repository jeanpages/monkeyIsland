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
		if (manager.find(Island.class, Integer.valueOf(name)) != null) {
			myLand = manager.find(Island.class, Integer.valueOf(name));
			
			
		} else {
			myLand = config.getMap("monkeys.properties");
			myLand.setId(Integer.valueOf(name));
			rum = config.getRum("monkeys.properties");
			rum.setIsland(myLand);
//			monkey = config.getMonkey("monkeys.properties");
//			myPirate = config.getPirate("monkeys.properties");
			treasure = config.getTreasure("monkeys.properties");
			treasure.setIsland(myLand);
			manager.persist(rum);
			manager.persist(myLand);
			manager.persist(treasure);
			
			Query query = manager.createQuery(
		              "SELECT DISTINCT e FROM Element e, Island i Where i.id = e.island AND i.id =" + Integer.valueOf(name) + " AND TYPE(e) = Rum");
			List<Element> resultList = query.getResultList();
		    System.out.println(resultList.size());
			
//			manager.persist(myPirate);
//			manager.persist(monkey);
		}
		
		com.sendMap(myLand.getMap(), String.valueOf(myLand.getId()));
//		com.sendMonkey(monkey, String.valueOf(monkey.getId()));
//		com.sendPirate(myPirate, String.valueOf(myPirate.getId()));
		com.sendRum(rum, String.valueOf(rum.getId()));
		com.sendTreasure(treasure, String.valueOf(treasure.getId()));
	}
}
