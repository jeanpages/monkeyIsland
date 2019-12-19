package monkeys.configuration;

import java.io.IOException;

import javax.ejb.Local;

import monkeys.model.Island;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;


@Local
public interface ConfigurationLocal {

	public Island getMap(String file) throws IOException;
	public Pirate getPirate(String file) throws IOException;
	public int getPirateMaxEnergy(String file) throws IOException;
	public int getMonkeyNumber(String file) throws IOException;
	public int getRumNumber(String file) throws IOException;
	public Rum getRum(String file) throws IOException;
	public Treasure getTreasure();
	
}
