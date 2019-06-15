package supertolles.schachspiel.test;

import java.util.Random;

import supertolles.schachspiel.Timer;

public class Test_Timer implements Runnable{

	Timer t;
	public Test_Timer() {
		t=new Timer(null, null, 500);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(new Random().nextInt(40000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.nextColor();
		}
	}

}
