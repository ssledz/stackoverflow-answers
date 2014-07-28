package pl.softech.stackoverflow.pattern;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PublisherListenerSignalExample {

	private interface Listener {
		void onEventRecieved(Event e);
	}
	
	private static class Event {
		private double distance;
		private double atten;

		public Event(double distance, double atten) {
			this.distance = distance;
			this.atten = atten;
		}

		public double getDistance() {
			return distance;
		}

		public double getAtten() {
			return atten;
		}

	}

	private static class GUI {

		public static double d;

	}

	private static class Mover {

		public static double instantDistance(double animpos) {
			return 0;
		}

	}

	private static double distance, animpos;
	
	private static List<Listener> listeners = new LinkedList<>();
	
	private static void fire(Event event) {
		for(Listener l : listeners) {
			l.onEventRecieved(event);
		}
	}
	
	public static void addListener(Listener l) {
		listeners.add(l);
	}

	public static double calculateSignal(double d, double f) {

		GUI.d = Mover.instantDistance(animpos); // needs to be displayed

		double atten = 20 * (Math.log10(d)) + 20 * (Math.log10(f)) + 32.44; // would
																			// also
																			// like
																			// to
																			// display
																			// this
		
		fire(new Event(d, atten));
		return atten;
	}
	
	public static void main(String[] args) {
		
		addListener(new Listener() {
			
			@Override
			public void onEventRecieved(Event e) {
				System.out.println(String.format("distance=%f atten=%f", e.getDistance(), e.getAtten()));
			}
		});
		
	    final JTextArea textArea_3 = new JTextArea();
	    textArea_3.setEditable(false);
	    textArea_3.setBounds(147, 481, 215, 32);


	    final JTextArea textArea_4 = new JTextArea();
	    textArea_4.setEditable(false);
	    textArea_4.setBounds(147, 527, 215, 27);
		
		addListener(new Listener() {
			
			@Override
			public void onEventRecieved(Event e) {
			    textArea_3.setText(""+e.getDistance()); //would have to be converted to String
			    textArea_4.setText(""+e.getAtten()); //would have to be converted to String
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel panel = new JPanel();
				frame.setContentPane(panel);
				panel.add(textArea_3);
				panel.add(textArea_4);
				frame.pack();
				frame.setVisible(true);
			}
		});
		

		Thread worker = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Random rnd = new Random();
				
				while(true) {
					
					calculateSignal(rnd.nextDouble(), rnd.nextDouble());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		worker.setDaemon(true);
		worker.start();
		
	}

}
