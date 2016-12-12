package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * Created by mateusz on 06.11.16.
 */
public class EmitLogDirect {

    private static final String EXCHANGE_CHANNEL = "direct_logs";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_CHANNEL, "direct");

        String severity = getSeverity(args);
        String message = getMessage(args);

        System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

    }

    private static String getSeverity(String[] args) {
        return args[0];
    }

    private static String getMessage(String[] args) {
        return Arrays.stream(args).skip(1).collect(joining(" "));
    }
}
