package main;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Barber barber = new Barber(5);
		barber.setVisible(true);
		for (int i = 1; i <= 10; i++) {
			Thread.sleep(100);
			new Client(barber, "Client " + i).start();
		}
	}
}
