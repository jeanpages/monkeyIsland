package monkeys;

import java.io.IOException;

import javax.ejb.Remote;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public void subscribe(int id) throws IOException;
	public void disconnect(int pId);
	public void movePirate(int id, int posX, int posY) throws IOException;
}
