package monkeys;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.model.Island;

/**
 * Session Bean implementation class MonkeyIsland
 * @author Mickael Clavreul
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;
	private Island myLand;
	
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
			manager.persist(myLand);
		}

		com.sendMap(myLand.getMap(), String.valueOf(myLand.getId()));
	}
}
