package monkeys;

import javax.ejb.Local;

import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendPirate(Pirate pirate, String id);
	public void sendMonkey(Monkey monkey, String id);
	public void sendRum(Rum rum, String id);
	public void sendTreasure(Treasure treasure, String id);
}