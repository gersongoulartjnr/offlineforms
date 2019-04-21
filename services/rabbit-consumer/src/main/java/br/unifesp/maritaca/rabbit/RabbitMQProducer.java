package br.unifesp.maritaca.rabbit;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.unifesp.maritaca.business.messaging.dto.EmailDTO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQProducer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("maritaca");
		factory.setPassword("maritaca");
		factory.setVirtualHost("Maritaca");
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		String exchangeName = "maritacaExchange";
		String routingKey = "maritacaRoute";
		
		EmailDTO objectDTO = new EmailDTO();
		objectDTO.setSubject("Invite test");
		objectDTO.setContent("use this app -> maritaca");
		
		List<String> emails = new ArrayList<String>();
		emails.add("email@domain.com");
		objectDTO.setEmails(emails);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(objectDTO);
	    
		byte[] messageBodyBytes = out.toByteArray();
		channel.basicPublish(exchangeName, routingKey,
				MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
		
		channel.close();
		conn.close();
	}
}
