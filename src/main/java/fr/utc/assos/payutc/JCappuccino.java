package fr.utc.assos.payutc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JCappuccino {
    public static void main(String[] args) throws Exception {

        // Set user parameters
        Integer wsPort = Integer.parseInt(System.getProperty("ws-port", "9191"));
        Integer wssPort = System.getProperty("wss-port") != null ? Integer.parseInt(System.getProperty("wss-port")) : null;
        Boolean readerSimulation = System.getProperty("no-reader-simulation") == null;
        Boolean limitLocalhost = System.getProperty("limit-localhost") == null;

        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        if (limitLocalhost) {
          connector.setHost("127.0.0.1");
        }

        connector.setPort(wsPort);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
        context.addServlet(holderEvents, "/events/*");

        CardReader mCardReader = new CardReader(readerSimulation);

        try
        {
            server.start();

            // Keep the server running until this thread is killed
            server.join();

            mCardReader.close();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }

    }
}
