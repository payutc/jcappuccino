package fr.utc.assos.payutc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class CardReader {
	private CardTerminal mReader;
	private static List<EventSocket> mSockets = new ArrayList<EventSocket>();

	private byte[] mApduArray = {
			(byte) 0xFF,
			(byte) 0xCA,
			(byte) 0x00,
			(byte) 0x00,
			(byte) 0x00
	};

	private CardThread mThread;

	public CardReader(Boolean simulateReader) {
		try {

			// Get the list of readers
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();
	        System.out.println("Liste des lecteurs détectés : " + terminals);

			// Choose the first reader
	        if(terminals.size() > 0){
	        	mReader = terminals.get(0);
	        	System.out.println("Connecté au premier lecteur.");

	        	// Start loop in a thread
	        	mThread = new CardThread();
	        	new Thread(mThread).start();
	        }
	        else {
	        	System.out.println("Aucun lecteur détecté, passage en mode simulation");
						noReaderAction(simulateReader);
	        }
		}  catch (CardException e) {
					System.out.println("Impossible de récupérer la liste de lecteurs");
					noReaderAction(simulateReader);
		}
	}

	public void close() {
		// Tell the loop to stop
		mThread.stop();
	}

	private void noReaderAction(Boolean simulateReader) {
		if (simulateReader) {
			System.out.println("Passage en mode simulation");
			mThread = new FakeCardThread();
			new Thread(mThread).start();
		} else {
			System.out.println("Envoie d'un message 'badgeuseNotFound'");
			dispatch("badgeuseNotFound", "1");
		}
	}

  public static boolean addEventSocket(EventSocket e) {
      System.out.println("Added EventSocket callback: "+e.toString()+" (Count="+(mSockets.size()+1)+")");
      return mSockets.add(e);
  }

  public static boolean removeEventSocket(EventSocket e) {
      System.out.println("Removed EventSocket callback: "+e.toString()+" (Count="+(mSockets.size()-1)+")");
      return mSockets.remove(e);
  }

  public void dispatch(String message, String value) {
      for (EventSocket mSocket : mSockets) {
          mSocket.sendData(message + ":" + value);
      }
  }

	private class CardThread implements Runnable {
		private volatile boolean mRun = true;

		public void stop() {
			mRun = false;
		}

		public void run() {
			CommandAPDU GetData = new CommandAPDU(mApduArray);
			while(mRun){
				try {
					if(mReader.waitForCardPresent(500)) {
						System.out.println("Carte détectée.");

						// Connect to the card currently in the reader
						Card card = mReader.connect("*");

						// Exchange APDUs with the card
						CardChannel channel = card.getBasicChannel();
						ResponseAPDU CardApduResponse = channel.transmit(GetData);

						// Disconnect
						card.disconnect(true);

						System.out.println("Carte lue");

						String carduid = getHexString(CardApduResponse.getData());

						System.out.println("cardInserted:" + carduid);
						dispatch("cardInserted", carduid);

						mReader.waitForCardAbsent(0);
					}
				} catch (CardException e) {
					System.out.println("Impossible de communiquer avec la carte");
				}
			}
		}
	}

	private class FakeCardThread extends CardThread {
		private volatile boolean mRun = true;

		@Override
		public void stop() {
			mRun = false;
		}

		@Override
		public void run() {
			try {
				String cardId = "123456AB";
				while(mRun){
					System.out.println("Appuyer sur entrée pour envoyer " + cardId.toString() + "...");
					BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
					buffer.readLine();

	        System.out.println("cardInserted:" + cardId.toString());
	        dispatch("cardInserted", cardId.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result.toUpperCase();
	}
}
