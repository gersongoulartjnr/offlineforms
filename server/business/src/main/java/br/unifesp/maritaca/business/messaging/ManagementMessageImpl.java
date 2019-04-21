package br.unifesp.maritaca.business.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;

@Service("managementMessage")
public class ManagementMessageImpl implements ManagementMessage {
	private static final Log log = LogFactory.getLog(ManagementMessageImpl.class);
	
	public static final String RABBIT_USER 			= "maritaca";
	public static final String RABBIT_USER_PASSWORD	= "maritaca";
	public static final String RABBIT_VHOST 			= "Maritaca";
	public static final String RABBIT_IP_HOST 		= "127.0.0.1";
	public static final int    RABBIT_PORT 			= 5672;
	
	public static final String RABBIT_EXCHANGE_NAME	= "maritacaExchange";
	public static final String RABBIT_ROUTING_KEY 	= "maritacaRoute";
	
	private Connection conn;
	private Channel channel;
	
	@Override
	public void sendMessage(EmailDTO emailDTO) {
		try {
			// Open connection
			channel = this.openRabbitMQConnection();
			log.info("connected");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(emailDTO);
		    byte[] messageBodyBytes = out.toByteArray();
		    
		    String exchangeName = RABBIT_EXCHANGE_NAME;
		    String routingKey   = RABBIT_ROUTING_KEY;
			channel.basicPublish(exchangeName, routingKey,
					MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);

			// Close connection
			this.closeRabbitMQConnection();
		} catch (IOException e) {
			throw new MaritacaException(e.getMessage());
		}
		
	}
	
	private void closeRabbitMQConnection() {
		try {
			channel.close();
			conn.close();
		} catch (IOException e) {
			throw new MaritacaException(e.getMessage());
		}
	}
	
	private Channel openRabbitMQConnection() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(RABBIT_USER);
			factory.setPassword(RABBIT_USER_PASSWORD);
			factory.setVirtualHost(RABBIT_VHOST);
			factory.setHost(RABBIT_IP_HOST);
			factory.setPort(RABBIT_PORT);
			log.info("connecting .....");
			
			conn = factory.newConnection();
			return conn.createChannel();		
		} catch (IOException e) {
			throw new MaritacaException(e.getMessage());
		}
	}
}