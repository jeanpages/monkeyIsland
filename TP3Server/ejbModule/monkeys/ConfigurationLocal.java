package monkeys;

import java.io.IOException;

import javax.ejb.Local;

@Local
public interface ConfigurationLocal {

	public Island getMap(String file) throws IOException;
	
}
