package monkeys;

import java.io.IOException;

import javax.ejb.Remote;

/**
 * @author Mickael Clavreul
 */
@Remote
public interface MIRemote {
	
	public void subscribe(String id) throws IOException;
	public void disconnect(String pId);
}
