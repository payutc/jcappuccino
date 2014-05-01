package fr.utc.assos.payutc;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter
{
	private Session mSession;
	private CardReader mCardReader;
	
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        mSession = sess;
        mCardReader = new CardReader(this);
        System.out.println("Socket Connected: " + sess);
    }
    
    public void sendData(String message) {
    	try {
        	mSession.getRemote().sendString(message);
        } catch (IOException x) {
            mSession.close();
        }
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message");
        String[] event = message.split(":", 2);
        
        String reply = "";
        
        if(event[0].equals("ping")) {
        	System.out.println("Received ping, sending pong");
        	reply = "pong:jcappuccino/0.1";
        }
        else if(event[0].equals("print")) {
        	System.out.println("Received print, sending to Printer");
        	Printer.print(event[1]);
        	reply = "ok";
        }
        
        if(reply.isEmpty()) {
    		reply = "error:Unknown command " + message;
    	}
        
        try {
        	mSession.getRemote().sendString(reply);
        } catch (IOException x) {
            mSession.close();
        }
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        
        mCardReader.close();
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}