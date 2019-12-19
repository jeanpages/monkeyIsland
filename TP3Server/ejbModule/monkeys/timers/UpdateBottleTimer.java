package monkeys.timers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.communication.Communication;
import monkeys.model.Island;

@Singleton
@Startup
@LocalBean
public class UpdateBottleTimer implements TimedObject {
	
	private Island island = Island.getInstance();
	private List<Integer> ids = new ArrayList<>();
	
	@EJB
	private Communication com;
	
	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;

	@Resource
	TimerService timerService;
	
	@PostConstruct
	public void createUpdateBottleTimer() {
		Timer timer = timerService.createSingleActionTimer(15000, new TimerConfig());
		ids.add(((int) System.currentTimeMillis()/1000) + 15);
	}
	

	@Override
	public void ejbTimeout(Timer timer) {
		for (int i = 0; i < island.getRums().size(); i++) {
			for (int j = ids.size() - 1; j >= 0; j--) {
				if (!island.getRums().get(i).isVisible() && ids.get(j) == ((int) System.currentTimeMillis()/1000)) {
					island.getRums().get(i).setVisible(true);
					manager.merge(island.getRums().get(i));
					com.sendRum(island.getRums().get(i), String.valueOf(island.getRums().get(i).getId()));
					ids.remove(j);
				}
			}
		}
	}
}
