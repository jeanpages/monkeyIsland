package monkeys;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import monkeys.model.Island;

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
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public Island getMap(String file) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties ();
		properties.load(is);
		
		String mapProperties = (String) properties.getProperty("MONKEYS_MAP");
		mapProperties = mapProperties.replace("\"", "");
		
		String[] lines = mapProperties.split(";");
		
		int[][] map = new int[10][10];
		
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines[i].split(",").length; j++) {
				map[i][j] = Integer.parseInt(lines[i].split(",")[j]);
			}
		}
		
		Island island = new Island();
		island.setMap(map);
		
		return island;
	}

}
