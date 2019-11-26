package monkeys;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.Topic;

import monkeys.model.Element;
import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;

/**
 * Session Bean implementation class Communication
 */
@Stateless
@LocalBean
public class Communication implements CommunicationLocal {

	@Inject
	private JMSContext context;
	
	@Resource(mappedName = "java:jboss/exported/topic/monkeys")
    private Topic topic;
	
    /**
     * Default constructor. 
     */
    public Communication() {
    	
    }

	@Override
	public void sendMap(int[][] map, String id) {
		sendIntArrayMessage(map, id, "map");
	}
	
	@Override
	public void sendPirate(Pirate pirate, String id) {
		sendElementMessage(pirate, id, "pirate");
	}
	
	@Override
	public void sendMonkey(Monkey monkey, String id) {
		sendElementMessage(monkey, id, "monkey");
	}
	
	@Override
	public void sendRum(Rum rum, String id) {
		sendElementMessage(rum, id, "rum");
	}
	
	@Override
	public void sendTreasure(Treasure treasure, String id) {
		sendElementMessage(treasure, id, "treasure");
	}
	
	
	private void sendIntArrayMessage(int[][] array, String id, String type){
    	StreamMessage message = context.createStreamMessage();
    	try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.writeInt(array.length);
    		for(int i=0;i<array.length;i++){
    			for(int j=0;j<array[i].length;j++){
            		message.writeInt(array[i][j]);
    			}
    		}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
    }
	
	private void sendElementMessage(Element element, String id, String type) {
		ObjectMessage message = context.createObjectMessage();
		try {
    		message.setStringProperty("id", id);
    		message.setJMSType(type);
    		message.setObject(element);;
		} catch (JMSException e) {
			e.printStackTrace();
		}
    	context.createProducer().send(topic, message);
	}
}