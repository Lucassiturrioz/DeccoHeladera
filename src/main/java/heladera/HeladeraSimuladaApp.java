package heladera;

import io.javalin.Javalin;
import org.json.JSONObject;

public class HeladeraSimuladaApp {

    public static void main(String[] args) {
        // Iniciar Javalin en el puerto 7000
        Javalin app = Javalin.create().start(8080);

        // Página principal que servirá el archivo HTML desde la carpeta 'views'
        app.get("/", ctx -> {
            // Establece la carpeta donde tienes tu archivo simulacion.html
            ctx.contentType("text/html; charset=UTF-8");
            ctx.result(HeladeraSimuladaApp.class.getResourceAsStream("/views/simulacion.html"));
        });

        // Endpoint que simula el envío de una alerta con heladeraId dinámico
        app.post("/enviar-alerta", ctx -> {
            String heladeraId = ctx.formParam("id");
            String tipoAlerta = ctx.formParam("tipoAlerta"); // Obtener el tipo de alerta desde el formulario
            JSONObject json = new JSONObject();
            json.put("type", "alerta");
            json.put("heladeraId", heladeraId);
            json.put("value", tipoAlerta); // Usar el tipo de alerta recibido

            try {
                // Pasar el id de la heladera a enviarMensajeRabbitMQ
                enviarMensajeRabbitMQ(json.toString(), heladeraId);
                ctx.result("Alerta '" + tipoAlerta + "' enviada a RabbitMQ para heladera " + heladeraId);
            } catch (Exception e) {
                ctx.status(500).result("Error enviando alerta a RabbitMQ: " + e.getMessage());
            }
        });

        // Endpoint que simula el envío de temperatura con heladeraId dinámico
        app.post("/enviar-temperatura", ctx -> {
            String heladeraId = ctx.formParam("id");
            JSONObject json = new JSONObject();
            json.put("type", "temperatura");
            json.put("heladeraId", heladeraId);
            json.put("value", ctx.formParam("value"));

            try {
                // Pasar el id de la heladera a enviarMensajeRabbitMQ
                enviarMensajeRabbitMQ(json.toString(), heladeraId);
                ctx.result("Temperatura enviada a RabbitMQ para heladera " + heladeraId);
            } catch (Exception e) {
                ctx.status(500).result("Error enviando temperatura a RabbitMQ: " + e.getMessage());
            }
        });

        app.post("/solicitud-apertura", ctx -> {
            String heladeraId = ctx.formParam("id");
            JSONObject json = new JSONObject();
            json.put("type", "solicitud-alimento");
            json.put("heladeraId", heladeraId);
            json.put("tarjeta", ctx.formParam("tarjeta"));


            try {
                // Pasar el id de la heladera a enviarMensajeRabbitMQ
                enviarMensajeRabbitMQ(json.toString(), heladeraId);
                ctx.result("Solicitud de Apertura enviada a RabbitMQ para heladera " + heladeraId);
            } catch (Exception e) {
                ctx.status(500).result("Error enviando solicitud de apertura a RabbitMQ: " + e.getMessage());
            }
        });
    }

    // Función para enviar el mensaje a RabbitMQ
    private static void enviarMensajeRabbitMQ(String mensaje, String heladeraId) throws Exception {
        RabbitMQProducer.enviarMensaje(mensaje, heladeraId); // Pasa el heladeraId
    }
}
