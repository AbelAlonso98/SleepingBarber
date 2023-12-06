package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * Sleeping BarberExercise with GUI made on Java Swing.
 * 
 * @author Abel Alonso Jiménez
 */
public class Barber extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2791364638696598803L;

	private Semaphore waitingChairs;
	private Semaphore cuttingChairs;

	private JPanel waitingRoomPanel;
	private JPanel cuttingChairPanel;

	public Barber(int nWaitingChairs) {
		waitingChairs = new Semaphore(nWaitingChairs);
		cuttingChairs = new Semaphore(1);

		setTitle("Barber Shop");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		updateUI();
	}

	public void enter() {
		if (waitingChairs.availablePermits() == 0)
			exitClient();
		else
			waitClient();
	}

	public void exitClient() {
		System.err.println("Barber waiting chairs are full " + Thread.currentThread().getName() + " is leaving.");
	}

	public void waitClient() {
		try {
			waitingChairs.acquire();
			updateUI();
			System.out.println(Thread.currentThread().getName() + " is in the waiting room.");
			while (cuttingChairs.availablePermits() == 0)
				;
			waitingChairs.release();
			updateUI();
			hairCut();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void hairCut() {
		try {
			cuttingChairs.acquire();
			updateUI();
			System.out.println(Thread.currentThread().getName() + " is getting a haircut.");
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + " haircut is done, leaving.");
			cuttingChairs.release();
			updateUI();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// ======================== Methods for GUI ========================
	private void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		waitingRoomPanel = createWaitingRoomPanel(5);
		mainPanel.add(waitingRoomPanel);
		cuttingChairPanel = createCuttingChairPanel();
		mainPanel.add(cuttingChairPanel);
		add(mainPanel);
	}

	private JPanel createWaitingRoomPanel(int numChairs) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		for (int i = 0; i < numChairs; i++) {
			JLabel chairLabel = new JLabel();
			chairLabel.setPreferredSize(new Dimension(30, 30));
			chairLabel.setOpaque(true);
			chairLabel.setBackground(Color.GREEN);
			panel.add(chairLabel);
		}
		return panel;
	}

	private JPanel createCuttingChairPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel cuttingChairLabel = new JLabel();
		cuttingChairLabel.setPreferredSize(new Dimension(50, 50));
		cuttingChairLabel.setOpaque(true);
		cuttingChairLabel.setBackground(Color.RED);
		panel.add(cuttingChairLabel);
		return panel;
	}

	private void updateUI() {
		SwingUtilities.invokeLater(() -> {
			Component[] chairs = waitingRoomPanel.getComponents();
			for (int i = 0; i < chairs.length; i++) {
				if (i < waitingChairs.availablePermits()) {
					chairs[i].setBackground(Color.RED);
				} else {
					chairs[i].setBackground(Color.GREEN);
				}
			}
			if (cuttingChairs.availablePermits() == 1)
				cuttingChairPanel.getComponents()[0].setBackground(Color.GREEN);
			else
				cuttingChairPanel.getComponents()[0].setBackground(Color.BLUE);
		});
	}
}
