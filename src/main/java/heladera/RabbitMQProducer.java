package heladera;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class RabbitMQProducer {

    public static void enviarMensaje(String mensaje, String id) throws Exception {
        // Crear conexión a RabbitMQ usando URI
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://ucxjlgia:XUEu7C4ny55dCIJPExpKETg7k-F30Yd5@prawn.rmq.cloudamqp.com/ucxjlgia"); // Usa setUri para CloudAMQP

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Nombre de la cola basado en el id de la heladera
            String queueName = "heladera." + id;

            // Declarar la cola (si no existe)
            channel.queueDeclare(queueName, false, false, false, null);

            // Enviar mensaje
            channel.basicPublish("", queueName, null, mensaje.getBytes());
            System.out.println(" [x] Enviado: '" + mensaje + "' a la cola: " + queueName);
        }
    }
}
