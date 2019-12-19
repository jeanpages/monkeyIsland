package monkeys.timers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import monkeys.communication.Communication;
import monkeys.model.Island;
import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.State;

@Stateless
@LocalBean
public class MoveMonkeyTimer {
	
	Island island = Island.getInstance();
	
	@EJB
	private Communication com;
	
	@PersistenceContext(unitName="MonkeysDS")
	private EntityManager manager;

	public MoveMonkeyTimer() {
		
	}

	@Schedule(second="*/1", minute="*", hour="*", dayOfWeek="*", dayOfMonth="*", month="*", year="*", info="moveMonkeyTimer")
	private void updateMonkeys() {
		com.removeMonkeys();
		for (Monkey m : island.getMonkeys()) {
			move(m);
			manager.merge(m);
			com.sendMonkey(m, String.valueOf(m.getId()));
		}
	}
	
	private void move(Monkey monkey) {
		List<int[]> possibleMoves = new ArrayList<>();
		
		if (availableMove(monkey.getPosX() + 1, monkey.getPosY())) {
			int[] move = new int[2];
			move[0] = monkey.getPosX() + 1;
			move[1] = monkey.getPosY();
			possibleMoves.add(move);
		}
		
		if (availableMove(monkey.getPosX(), monkey.getPosY() + 1)) {
			int[] move = new int[2];
			move[0] = monkey.getPosX();
			move[1] = monkey.getPosY() + 1;
			possibleMoves.add(move);
		}
		
		if (availableMove(monkey.getPosX() - 1, monkey.getPosY())) {
			int[] move = new int[2];
			move[0] = monkey.getPosX() - 1;
			move[1] = monkey.getPosY();
			possibleMoves.add(move);
		}
		
		if (availableMove(monkey.getPosX(), monkey.getPosY() - 1)) {
			int[] move = new int[2];
			move[0] = monkey.getPosX();
			move[1] = monkey.getPosY() - 1;
			possibleMoves.add(move);
		}
		
		
		if (!possibleMoves.isEmpty()) {
			Random random = new Random();
			
			int index = random.nextInt(possibleMoves.size());

			monkey.setPosX(possibleMoves.get(index)[0]);
			monkey.setPosY(possibleMoves.get(index)[1]);
			
			for (Pirate p : island.getPirates()) {
				if (monkey.getPosX() == p.getPosX() && monkey.getPosY() == p.getPosY()) {
					p.setStatus(State.DEAD);
					p.setEnergy(0);
					manager.merge(p);
					com.pirateDeath(p, String.valueOf(p.getClientId()));
				}
			}
		}
	}
	
	private boolean availableMove(int newPosX, int newPosY) {
		boolean available = true;

		if (island.getMap()[newPosX][newPosY] != 0) {
			for (Monkey m : island.getMonkeys()) {
				if (m.getPosX() == newPosX && m.getPosY() == newPosY) {
					available = false;
				}
			}
		} else {
			available = false;
		}
		
		return available;
	}
}
