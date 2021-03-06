package monkeys.communication;

import java.util.List;

import javax.ejb.Local;

import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void resetMap(int[][] map, String id);
	public void sendPirate(Pirate pirate, String id);
	public void sendMonkey(Monkey monkey, String id);
	public void sendRum(Rum rum, String id);
	public void sendTreasure(Treasure treasure, String id);
	public void movePirate(Pirate pirate, String id);
	public void pirateDeath(Pirate pirate, String id);
	public void energyIncrease(Pirate pirate, String id);
	public void removePirates(List<Integer> pirates);
	public void removeMonkeys();
	public void removeRums();
	public void disconnect(int id);
	public void initEnergy(Pirate pirate, String id);
}