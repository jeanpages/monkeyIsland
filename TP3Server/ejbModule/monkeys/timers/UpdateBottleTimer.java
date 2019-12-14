package monkeys.timers;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.communication.Communication;
import monkeys.model.Island;

@Stateless
@LocalBean
public class UpdateBottleTimer {
	
	Island island = Island.getInstance();
	
	@EJB
	private Communication com;
	
	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;

	public UpdateBottleTimer() {
		
	}

	@Schedule(second=" */5 ", minute="*", hour="*", dayOfWeek="*", dayOfMonth="*", month="*", year="*", info="updateBottleTimer")
	private void updateBottles() {
		for (int i = 0; i < island.getRums().size(); i++) {
			if (!island.getRums().get(i).isVisible()) {
				island.getRums().get(i).setVisible(true);
				manager.merge(island.getRums().get(i));
				com.sendRum(island.getRums().get(i), String.valueOf(island.getRums().get(i).getId()));
			}
		}
	}
	
}
