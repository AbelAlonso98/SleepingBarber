package main;

public class Client extends Thread{
	private Barber barber;
	
	public Client(Barber barber, String name) {
		super(name);
		this.barber = barber;
	}
	
	@Override
	public void run() {
		barber.enter();
	}
}
