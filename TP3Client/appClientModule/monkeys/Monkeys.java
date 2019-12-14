package monkeys;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import guybrush.view.Fenetre;
import guybrush.view.GameObserver;
import monkeys.MIRemote;
import monkeys.model.Monkey;
import monkeys.model.Pirate;
import monkeys.model.Rum;
import monkeys.model.Treasure;

/**
 * @author Mickael Clavreul
 */
public class Monkeys implements MessageListener, GameObserver {
	
	private static Monkeys instance;
	private static Fenetre fenetre;
	
	private static int[][] matrix;
	
	private static TopicConnection connection;
	
	private static MIRemote miremote;
	
	private static int id;
	
	public static void main(String[] args) throws Exception {
		 
		fenetre = new Fenetre("MonkeysIsland");

		instance = new Monkeys();
		
		id = (int) System.currentTimeMillis();
		
		connection = subscribeTopic();
		
		miremote = lookup("ejb:/TP3Server/MonkeyIsland!monkeys.MIRemote?stateful");
		
		miremote.subscribe(id);
		
		fenetre.addObserver(instance);
		
		fenetre.setVisible(true);
		
	}

	public Monkeys() {
		super();
	}
	
	private static MIRemote lookup(String url) throws Exception {
		Properties properties = new Properties();
		properties.put("java.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		properties.put("remote.connections", "default");
		properties.put("remote.connection.default.host", "localhost");
		properties.put("remote.connection.default.port", "8080");
		properties.put("remote.connection.default.username", "test");
		properties.put("remote.connection.default.password", "network_1");
		
		EJBClientConfiguration clientConf = new PropertiesBasedEJBClientConfiguration(properties);
		
		ConfigBasedEJBClientContextSelector clientContextSelector = new ConfigBasedEJBClientContextSelector(clientConf);
		
		EJBClientContext.setSelector(clientContextSelector);
		
		Context context = new InitialContext(properties);
		
		MIRemote miremote = (MIRemote) context.lookup(url);
		
		return miremote;
	}
	
	public static TopicConnection subscribeTopic() throws IOException, NamingException, JMSException {
		Properties properties = new Properties();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/jndi-topic-client.properties");

		properties.load(is);
		
		String cFURI = properties.getProperty("connectionFactoryURI");
		String tURI = properties.getProperty("topicURI");
		
		String login = properties.getProperty("java.naming.security.principal");
		String password = properties.getProperty("java.naming.security.credentials");
		
		Context context = new InitialContext(properties);
		
		TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup(cFURI);
		
		Topic topic = (Topic) context.lookup(tURI);
		
		TopicConnection connection = factory.createTopicConnection(login, password);
		
		TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		MessageConsumer messageConsumer = session.createSharedDurableConsumer(topic, String.valueOf(System.currentTimeMillis()));
		
		messageConsumer.setMessageListener(instance);
		
		connection.start();
		
		return connection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message arg0) {
		try {
			StreamMessage streamMessage;
			ObjectMessage objectMessage;
			
			switch (arg0.getJMSType()) {
				case "map":
					streamMessage = (StreamMessage) arg0;
					int arrayLength = streamMessage.readInt();
					matrix = new int[arrayLength][arrayLength];
					for (int i = 0; i < arrayLength; i++) {
						for (int j = 0; j < arrayLength; j++) {
							matrix[i][j] = streamMessage.readInt();
						}
					}
					fenetre.creationCarte(matrix);
					fenetre.repaint();
					break;
				case "removePirates":
					objectMessage = (ObjectMessage) arg0;
					List<Integer> piratesId = (List<Integer>) objectMessage.getObject();
					fenetre.suppressionPirates(piratesId);
					for (int i : piratesId) {
						fenetre.suppressionPirate(i);
					}
					fenetre.repaint();
					break;
				case "removeMonkeys":
					fenetre.removeEMonkeys();
					fenetre.repaint();
					break;
				case "removeRums":
					fenetre.removeRhums();
					fenetre.repaint();
					break;
				case "addPirate":
					objectMessage = (ObjectMessage) arg0;
					Pirate aPirate = (Pirate) objectMessage.getObject();
					fenetre.ajoutPirate(aPirate.getClientId(), aPirate.getPosX(), aPirate.getPosY(), selectAvatar(aPirate), aPirate.getEnergy());
					fenetre.repaint();
					break;
				case "initEnergy":
					objectMessage = (ObjectMessage) arg0;
					Pirate iPirate = (Pirate) objectMessage.getObject();
					if (iPirate.getClientId() == id) {
						fenetre.getEnergyView().setEnergieMax(iPirate.getEnergy());
						fenetre.updateEnergieView(iPirate.getEnergy());
						fenetre.repaint();
					}
					break;
				case "energyIncrease":
					objectMessage = (ObjectMessage) arg0;
					Pirate ePirate = (Pirate) objectMessage.getObject();
					if (ePirate.getClientId() == id) {
						fenetre.getEnergyView().miseAJourEnergie(5);
					}
					break;
				case "movePirate":
					objectMessage = (ObjectMessage) arg0;
					Pirate mPirate = (Pirate) objectMessage.getObject();
					fenetre.suppressionPirate(mPirate.getClientId());
					fenetre.ajoutPirate(mPirate.getClientId(), mPirate.getPosX(), mPirate.getPosY(), selectAvatar(mPirate), mPirate.getEnergy());
					if (mPirate.getClientId() == id) {
						fenetre.getEnergyView().miseAJourEnergie(-1);
					}					
					break;
				case "pirateDeath":
					objectMessage = (ObjectMessage) arg0;
					Pirate dPirate = (Pirate) objectMessage.getObject();
					fenetre.mortPirate(dPirate.getClientId());
					fenetre.repaint();
					break;
				case "rum":
					objectMessage = (ObjectMessage) arg0;
					Rum rum = (Rum) objectMessage.getObject();
					fenetre.creationRhum(rum.getPosX(), rum.getPosY(), rum.isVisible());
					fenetre.repaint();
					break;
				case "treasure":
					objectMessage = (ObjectMessage) arg0;
					Treasure treasure = (Treasure) objectMessage.getObject();
					fenetre.creationTresor(treasure.getPosX(), treasure.getPosY(), treasure.isVisible());
					fenetre.repaint();
					break;
				case "monkey":
					objectMessage = (ObjectMessage) arg0;
					Monkey monkey = (Monkey) objectMessage.getObject();
					fenetre.creationEMonkey(monkey.getId(), monkey.getPosX(), monkey.getPosY());
					fenetre.repaint();
					break;
				case "disconnect":
					streamMessage = (StreamMessage) arg0;
					fenetre.suppressionPirate(streamMessage.readInt());
					fenetre.repaint();
					break;
				default:
					break;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyDisconnect() {
		try {
			miremote.disconnect(id);
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		fenetre.dispose();
	}

	@Override
	public void notifyMove(int arg0, int arg1) {
		try {
			miremote.movePirate(id, arg0, arg1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String selectAvatar(Pirate pirate) {
		if (pirate.getClientId() == id) {
			return "img/Mon_Pirate.png";
		} else {
			return "img/Autres_Pirates.jpg";
		}
	}

}