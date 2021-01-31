package fr.utc.assos.payutc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JCappuccino {
    public static void main(String[] args) throws Exception {
    	Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("127.0.0.1");
        connector.setPort(9191);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
        context.addServlet(holderEvents, "/events/*");
        
        CardReader mCardReader = new CardReader();

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
