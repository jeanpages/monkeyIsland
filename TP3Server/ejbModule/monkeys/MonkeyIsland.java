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

    /**
     * Connexion du pirate.
     */
	@Override
	public void subscribe(int id) throws IOException {
		newGame(id);
	}
	
	/**
	 * Déconnexion du pirate.
	 */
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
	
	/**
	 * Gère le déplacement d'un pirate.
	 */
	@Override
	public void movePirate(int id, int posX, int posY) throws IOException {
		Pirate pirate = null;
		
		// Recherche du pirate qui doit bouger
		for (int i = 0; i < myLand.getPirates().size(); i++) {
			if (myLand.getPirates().get(i).getClientId() == id) {
				pirate = myLand.getPirates().get(i);
			}
		}
		
		// Si le pirate n'est pas mort
		if (!State.DEAD.equals(pirate.getStatus())) {
			
			// Si la nouvelle case n'est pas une case d'eau
			if (myLand.getMap()[pirate.getPosX() + posX][pirate.getPosY() + posY] != 0) {
				
				// Si le pirate est saoul
				if (pirate.getEnergy() > config.getPirateMaxEnergy("monkeys.properties")) {
					pirate.setStatus(State.DRUNK);
					int[] randomCell = getRandomCell(pirate);
					posX = randomCell[0];
					posY = randomCell[1];
					
				} else {
					pirate.setStatus(State.SOBER);
				}
				
				// S'il n'y a aucun élément sur la prochaine case
				if (isEmpty(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
					pirate.setPosX(pirate.getPosX() + posX);
					pirate.setPosY(pirate.getPosY() + posY);
				
				// S'il y a une bouteille de rhum sur la prochaine case
				} else if (isRum(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
					Rum rum = findRumSameCellAsPirate(pirate.getPosX() + posX, pirate.getPosY() + posY);
					
					pirate.setPosX(pirate.getPosX() + posX);
					pirate.setPosY(pirate.getPosY() + posY);
					
					// Si la bouteille de rhum est visible
					if (rum.isVisible()) {
						pirate.setEnergy(pirate.getEnergy() + config.getRum("monkeys.properties").getEnergy());
						myLand.getRums().get(this.findRumIndex(rum)).setVisible(false);
						manager.merge(myLand.getRums().get(this.findRumIndex(rum)));
						com.removeRums();
						
						for (Rum r : myLand.getRums()) {
							if (r.isVisible()) {
								com.sendRum(r, String.valueOf(r.getId()));
							}
						}
						
						com.energyIncrease(pirate, String.valueOf(pirate.getClientId()));
						
						// Creation du timer de repop des bouteilles
						updateBottleTimer.createUpdateBottleTimer();
					}
					
				// S'il y a un singe sur la prochaine case	
				} else if (isMonkey(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
					pirate.setPosX(pirate.getPosX() + posX);
					pirate.setPosY(pirate.getPosY() + posY);
					pirate.setEnergy(1);
				
				// S'il y a le trésor sur la prochaine case
				} else if (isTreasure(pirate.getPosX() + posX, pirate.getPosY() + posY)) {
					pirate.setPosX(pirate.getPosX() + posX);
					pirate.setPosY(pirate.getPosY() + posY);
					myLand.getTreasure().setVisible(true);
					manager.merge(myLand.getTreasure());
					com.sendTreasure(myLand.getTreasure(), String.valueOf(myLand.getTreasure().getId()));
				}
				
				pirate.setEnergy(pirate.getEnergy() - 1);
				
				manager.merge(pirate);
				com.movePirate(pirate, String.valueOf(pirate.getClientId()));
				
				// Si le déplacement a tué le pirate
				if (pirate.getEnergy() == 0) {
					pirate.setStatus(State.DEAD);
					com.pirateDeath(pirate, String.valueOf(id));
				}
			}
		}
	}

	/**
	 * Configure la partie pour le nouveau client.
	 * @param id
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void newGame(int id) throws IOException {
		
		// Si l'ile n'existe pas dans la BDD on la crée
		if (manager.find(Island.class, myLand.getId()) == null) {
			myLand = config.getMap("monkeys.properties");
			manager.persist(myLand);	
			
		// Sinon on récupère ses infos
		} else {
			myLand.setId(manager.find(Island.class, myLand.getId()).getId());
			myLand.setMap(manager.find(Island.class, myLand.getId()).getMap());
			myLand.setMonkeys(manager.find(Island.class, myLand.getId()).getMonkeys());
			myLand.setPirates(manager.find(Island.class, myLand.getId()).getPirates());
			myLand.setRums(manager.find(Island.class, myLand.getId()).getRums());
			myLand.setTreasure(manager.find(Island.class, myLand.getId()).getTreasure());
		}
		
		if (manager.find(Pirate.class, id) == null) {
			myPirate = config.getPirate("monkeys.properties");
			myPirate.setClientId(id);
			randomInit(myPirate);
			myPirate.setIsland(myLand);
			manager.persist(myPirate);
			myLand.getPirates().add(myPirate);
		} else {
			myPirate = manager.find(Pirate.class, id);
		}
		
		if(myLand.getTreasure() == null) {
			treasure = config.getTreasure();
			randomInit(treasure);
			treasure.setIsland(myLand);
			manager.persist(treasure);
			myLand.setTreasure(treasure);
		}
		
		if (myLand.getMonkeys().isEmpty()) {
			
			// Crée le nombre de singe demandé dans le fichier properties
			for (int i = 0; i < config.getMonkeyNumber("monkeys.properties"); i++) {
				Monkey monkey = new Monkey();
				randomInit(monkey);
				monkey.setIsland(myLand);
				manager.persist(monkey);
				myLand.getMonkeys().add(monkey);				
			}
		}
		
		if (myLand.getRums().isEmpty()) {
			
			// Crée le nombre de bouteilles demandé dans le fichier properties
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
		
		// Supprime les éléments de tous les clients
		com.removePirates(iPirates);
		com.removeMonkeys();
		com.removeRums();
		
		// Envoi des pirates
		for (Pirate p : myLand.getPirates()) {
			com.sendPirate(p, String.valueOf(p.getClientId()));
		}
		
		// Envoi des singes
		for (Monkey m : myLand.getMonkeys()) {
			com.sendMonkey(m, String.valueOf(m.getId()));
		}
		
		// Envoi des bouteilles
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
	private int findRumIndex(Rum rum) {
		Integer index = null;
		for (int i = 0 ; i < myLand.getRums().size(); i++) {
			if (rum.getPosX() == myLand.getRums().get(i).getPosX() &&
					rum.getPosY() == myLand.getRums().get(i).getPosY()) {
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Si le pirate est ivre, trouve une cellule disponible aléatoirement.
	 * @param pirate
	 * @return
	 */
	private int[] getRandomCell(Pirate pirate) {
		List<int[]> possibleMoves = new ArrayList<>();
		
		if (availableMove(pirate.getPosX() + 1, pirate.getPosY())) {
			int[] move = new int[2];
			move[0] = 1;
			move[1] = 0;
			possibleMoves.add(move);
		}
		
		if (availableMove(pirate.getPosX(), pirate.getPosY() + 1)) {
			int[] move = new int[2];
			move[0] = 0;
			move[1] = 1;
			possibleMoves.add(move);
		}
		
		if (availableMove(pirate.getPosX() - 1, pirate.getPosY())) {
			int[] move = new int[2];
			move[0] = -1;
			move[1] = 0;
			possibleMoves.add(move);
		}
		
		if (availableMove(pirate.getPosX(), pirate.getPosY() - 1)) {
			int[] move = new int[2];
			move[0] = 0;
			move[1] = -1;
			possibleMoves.add(move);
		}
		
		
		if (!possibleMoves.isEmpty()) {
			Random random = new Random();
			
			int index = random.nextInt(possibleMoves.size());

			return possibleMoves.get(index);
		}
		
		return null;
	}
	
	/**
	 * Regarde s'il n'y a pas de pirate sur la cellule de destination.
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	private boolean availableMove(int newPosX, int newPosY) {
		boolean available = true;

		if (myLand.getMap()[newPosX][newPosY] != 0) {
			for (Pirate p : myLand.getPirates()) {
				if (p.getPosX() == newPosX && p.getPosY() == newPosY) {
					available = false;
				}
			}
		} else {
			available = false;
		}
		
		return available;
	}
}
