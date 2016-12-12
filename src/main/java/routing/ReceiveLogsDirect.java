package routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by mateusz on 06.11.16.
 */
public class ReceiveLogsDirect {

    private static final String EXCHANGE_CHANNEL = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_CHANNEL, "direct");
        String queueName = channel.queueDeclare().getQueue();

        if (args.length < 1) {
            System.out.println("Usage: ReceiveLogsDirect [warning] [error] [info]");
            return;
        }

        for (String severity : args) {
            channel.queueBind(queueName, EXCHANGE_CHANNEL, severity);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
