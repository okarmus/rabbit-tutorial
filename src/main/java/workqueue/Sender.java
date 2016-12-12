package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.rabbitmq.client.MessageProperties.PERSISTENT_TEXT_PLAIN;

/**
 * Created by mateusz on 04.11.16.
 */
public class Sender {

    private static final String QUEUE_NAME = "worker2";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        String message = getMessage(args);

        channel.basicPublish("", QUEUE_NAME, PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
    }

    private static String getMessage(String[] args) {
        if (args.length < 1){
            return "hello world";
        }
        return Arrays.stream(args).collect(Collectors.joining(" "));
    }
}
