package monkeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.communication.Communication;
import monkeys.configuration.Configuration;
import monkeys.model.Element;
import monkeys.model.Island;
import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.State;
import monkeys.model.Treasure;
import monkeys.timers.MoveMonkeyTimer;
import monkeys.timers.UpdateBottleTimer;

/**
 * Session Bean implementation class MonkeyIsland
 * @author Mickael Clavreul
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;
	private Island myLand = Island.getInstance();
	private Pirate myPirate;
	private Treasure treasure;
	
	@EJB
	private Configuration config;
	
	@EJB
	private Communication com;
	
	@EJB
	private MoveMonkeyTimer timer;
	
	@EJB
	private UpdateBottleTimer updateBottleTimer;
	
    public MonkeyIsland() {
    	
    }

	@Override
	public void subscribe(int id) throws IOException {
		newGame(id);
	}
	
	@Override
	public void disconnect(int id) {
		Pirate pirate = null;
		for (Pirate p : myLand.getPirates()) {
			if (p.getClientId() == id) {
				pirate = p;
			}
		}
		myLand.getPirates().remove(pirate);
		manager.remove(manager.find(Pirate.class, pirate.getId()));
		
		com.disconnect(id);
	}
	
	@Override
	public void movePirate(int id, int posX, int posY) throws IOException {
		Pirate pirate = null;
		for (int i = 0; i < myLand.getPirates().size(); i++) {
			if (myLand.getPirates().get(i).getClientId() == id) {
				pirate = myLand.getPirates().get(i);
			}
		}
		
		if (myLand.getMap()[pirate.getPosX() + posX][pirate.getPosY() + posY] != 0 && pirate.getEnergy() != 0) {
			if (isEmpty(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
				pirate.setPosX(pirate.getPosX() + posX);
				pirate.setPosY(pirate.getPosY() + posY);
				if (pirate.getEnergy() == 1) {
					pirate.setStatus(State.DEAD);
				}
				pirate.setEnergy(pirate.getEnergy() - 1);
				
				manager.merge(pirate);
				com.movePirate(pirate, String.valueOf(pirate.getClientId()));
				
				if (pirate.getEnergy() == 0) {
					com.pirateDeath(pirate, String.valueOf(id));
				}
				
			} else if (isRum(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
				Rum rum = findRumSameCellAsPirate(pirate.getPosX() + posX, pirate.getPosY() + posY);
				pirate.setPosX(pirate.getPosX() + posX);
				pirate.setPosY(pirate.getPosY() + posY);
				if (rum.isVisible()) {
					pirate.setEnergy(pirate.getEnergy() + config.getRum("monkeys.properties").getEnergy());
					myLand.getRums().get(this.findRumPosition(rum)).setVisible(false);
					manager.merge(myLand.getRums().get(this.findRumPosition(rum)));
					com.removeRums();
					com.sendRum(myLand.getRums().get(this.findRumPosition(rum)), 
							String.valueOf(rum.getId()));
					manager.merge(pirate);
					com.energyIncrease(pirate, String.valueOf(pirate.getClientId()));
					com.movePirate(pirate, String.valueOf(pirate.getClientId()));
				}
				else {
					manager.merge(pirate);
					com.movePirate(pirate, String.valueOf(pirate.getClientId()));
				}
				
			} else if (isMonkey(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
				pirate.setPosX(pirate.getPosX() + posX);
				pirate.setPosY(pirate.getPosY() + posY);
				pirate.setStatus(State.DEAD);
				pirate.setEnergy(0);
				manager.merge(pirate);
				com.movePirate(pirate, String.valueOf(pirate.getClientId()));
				com.pirateDeath(pirate, String.valueOf(id));
				
			} else if (isTreasure(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
				pirate.setPosX(pirate.getPosX() + posX);
				pirate.setPosY(pirate.getPosY() + posY);
				myLand.getTreasure().setVisible(true);
				manager.merge(myLand.getTreasure());
				com.sendTreasure(myLand.getTreasure(), String.valueOf(myLand.getTreasure().getId()));
				manager.merge(pirate);
				com.movePirate(pirate, String.valueOf(pirate.getClientId()));
			}
			
		}
	}

	/**
	 * Configure la nouvelle partie.
	 * @param id
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void newGame(int id) throws IOException {
		/*if (manager.find(Island.class, myLand.getId()) != null) {
			myLand.setMonkeys(manager.find(Island.class, myLand.getId()).getMonkeys());
			myLand.setPirates(manager.find(Island.class, myLand.getId()).getPirates());
			myLand.setRums(manager.find(Island.class, myLand.getId()).getRums());
			myLand.setTreasure(manager.find(Island.class, myLand.getId()).getTreasure());
		}*/ if (manager.find(Island.class, myLand.getId()) == null) {
			myLand = config.getMap("monkeys.properties");
			manager.persist(myLand);		
		}
		
		/*if (manager.find(Pirate.class, id) != null) {
			myPirate = manager.find(Pirate.class, id);
		}*/ if (manager.find(Pirate.class, id) == null) {
			myPirate = config.getPirate("monkeys.properties");
			myPirate.setClientId(id);
			randomInit(myPirate);
			myPirate.setIsland(myLand);
			manager.persist(myPirate);
			myLand.getPirates().add(myPirate);
		}
		
		/*if(myLand.getTreasure() != null) {
			treasure = manager.find(Treasure.class, myLand.getTreasure().getId());
		}*/ if(myLand.getTreasure() == null) {
			treasure = config.getTreasure();
			randomInit(treasure);
			treasure.setIsland(myLand);
			manager.persist(treasure);
			myLand.setTreasure(treasure);
		}
		
		if (myLand.getMonkeys().isEmpty()) {
			for (int i = 0; i < config.getMonkeyNumber("monkeys.properties"); i++) {
				Monkey monkey = new Monkey();
				randomInit(monkey);
				monkey.setIsland(myLand);
				manager.persist(monkey);
				myLand.getMonkeys().add(monkey);				
			}
		}
		
		if (myLand.getRums().isEmpty()) {
			for (int i = 0 ; i < config.getRumNumber("monkeys.properties"); i++) {
				Rum rum = config.getRum("monkeys.properties");
				randomInit(rum);
				rum.setIsland(myLand);
				manager.persist(rum);
				myLand.getRums().add(rum);
			}
		}
		
		initClient(id);
	}
	
	/**
	 * Inititialise tous les clients lors d'un nouveau joueur.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private void initClient(int id) {
		
		com.sendMap(myLand.getMap(), String.valueOf(myLand.getId()));
		
		List<Integer> iPirates = new ArrayList<>();
		for (Pirate ip : myLand.getPirates()) {
			iPirates.add(ip.getClientId());
		}
		
		com.removePirates(iPirates);
		com.removeMonkeys();
		com.removeRums();
		
		for (Pirate p : myLand.getPirates()) {
			com.sendPirate(p, String.valueOf(p.getClientId()));
		}
		
		for (Monkey m : myLand.getMonkeys()) {
			com.sendMonkey(m, String.valueOf(m.getId()));
		}
		
		for (Rum r : myLand.getRums()) {
			com.sendRum(r, String.valueOf(r.getId()));
		}
		
		com.initEnergy(myPirate, String.valueOf(myPirate.getClientId()));
	}
	
	/**
	 * Trouve une case vide.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private void randomInit(Element element) {
		Random random = new Random();

		int posX = random.nextInt(myLand.getMap().length);
		int posY = random.nextInt(myLand.getMap().length);
		
		if (!isEmpty(posX, posY) || myLand.getMap()[posX][posY] == 0) {
			randomInit(element);
		} else {
			element.setPosX(posX);
			element.setPosY(posY);
		}
	}
	
	/**
	 * Vérifie s'il n'y a aucun élément sur la case.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean isEmpty(int newPosX, int newPosY) {
		return !(isPirate(newPosX, newPosY) || isRum(newPosX, newPosY) || 
				isMonkey(newPosX, newPosY) || isTreasure(newPosX, newPosY));
	}
	
	/**
	 * Vérifie s'il y a un pirate sur la case.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean isPirate(int newPosX, int newPosY) {
		boolean isPirate = false;
		
		for (Pirate p : myLand.getPirates()) {
			if (p.getPosX() == newPosX && p.getPosY() == newPosY) {
				isPirate = true;
			}
		}
		return isPirate;
	}
	
	/**
	 * Vérifie s'il y a un rum sur la case.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean isRum(int newPosX, int newPosY) {
		boolean isRum = false;
		
		for (Rum r : myLand.getRums()) {
			if (r.getPosX() == newPosX && r.getPosY() == newPosY) {
				isRum = true;
			}
		}
		return isRum;
	}
	
	/**
	 * Vérifie s'il y a un singe sur la case.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean isMonkey(int newPosX, int newPosY) {
		boolean isMonkey = false;
		
		for (Monkey m : myLand.getMonkeys()) {
			if (m.getPosX() == newPosX && m.getPosY() == newPosY) {
				isMonkey = true;
			}
		}
		return isMonkey;
	}
	
	/**
	 * Vérifie s'il y a un tresor sur la case.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean isTreasure(int newPosX, int newPosY) {
		boolean isTreasure = false;
		if (myLand.getTreasure() != null) {
			if (myLand.getTreasure().getPosX() == newPosX && myLand.getTreasure().getPosY() == newPosY) {
				isTreasure = true;
			}
		}
		return isTreasure;
	}
	
	/**
	 * Cherche la bouteille de rhum sur la même case que le pirate
	 * @param newPosX
	 * @param newPosY
	 */
	private Rum findRumSameCellAsPirate(int newPosX, int newPosY) {
		Rum rum = null;
		for (Rum r : myLand.getRums()) {
			if (r.getPosX() == newPosX && r.getPosY() == newPosY) {
				rum = r;
			}
		}
		return rum;
	}
	
	/**
	 * Cherche la position du rum dans la liste
	 * @param rum
	 */
	private int findRumPosition(Rum rum) {
		int pos = 10000;
		for (int i = 0 ; i < myLand.getRums().size(); i++) {
			if (rum.equals(myLand.getRums().get(i))) {
				pos = i;
			}
		}
		return pos;
	}
}
