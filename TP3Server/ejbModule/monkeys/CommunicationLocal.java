package monkeys;

import javax.ejb.Local;

import monkeys.model.Pirate;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendPirate(Pirate pirate, String id);
}