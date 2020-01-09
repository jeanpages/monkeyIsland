package monkeys.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import monkeys.model.Island;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.State;
import monkeys.model.Treasure;

/**
 * Session Bean implementation class Configuration
 */
@Stateless
@LocalBean
public class Configuration implements ConfigurationLocal {

    /**
     * Default constructor. 
     */
    public Configuration() {

    }
    
    @Override
	public Island getMap(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);
		
		String mapProperties = (String) properties.getProperty("MONKEYS_MAP");
		mapProperties = mapProperties.replace("\"", "");
		
		String[] lines = mapProperties.split(";");
		
		int[][] map = new int[lines.length][lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines[i].split(",").length; j++) {
				map[i][j] = Integer.parseInt(lines[i].split(",")[j]);
			}
		}
		
		Island island = Island.getInstance();
		island.setMap(map);
		
		return island;
	}

	@Override
	public Pirate getPirate(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);
		
		String maxEnergieProperty = (String) properties.getProperty("PIRATE_MAX_ENERGIE");
		Pirate pirate = new Pirate();
		pirate.setEnergy(Integer.parseInt(maxEnergieProperty));
		pirate.setStatus(State.SOBER);
		return pirate;
	}
	
	@Override
	public int getPirateMaxEnergy(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);
		
		int maxEnergieProperty = Integer.parseInt(properties.getProperty("PIRATE_MAX_ENERGIE"));
		return maxEnergieProperty;
	}

	@Override
	public int getMonkeyNumber(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);

		return Integer.parseInt(properties.getProperty("MONKEY_NUMBER"));
	}
	
	@Override
	public int getRumNumber(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);

		return Integer.parseInt(properties.getProperty("RUM_NUMBER"));
	}

	@Override
	public Rum getRum(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);
		
		String energieProperty = (String) properties.getProperty("RUM_ENERGIE");
		Rum rum = new Rum();
		rum.setEnergy(Integer.parseInt(energieProperty));
		rum.setVisible(true);
		return rum;
	}

	@Override
	public Treasure getTreasure() {
		Treasure treasure = new Treasure();
		treasure.setVisible(false);
		return treasure;
	}
}
