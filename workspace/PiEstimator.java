import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PiEstimator{
//the following code is just to jog your memory about how labels and buttons work!
//implement your Pi Estimator as described in the project. You may do it all in main below or you 
//may implement additional functions if you feel it necessary.
	public static class piThread extends Thread {
		public volatile int numTrials=0;
		public volatile int numInCircle=0;
		public boolean paused = true;

        public void randomXY(){
			double x = Math.random();
			double y = Math.random();
			if (x*x+y*y<1){
				numInCircle++;
			}
			numTrials++;
		}
		public void run() {
            while(true){
				synchronized (this) {
					if (paused) {
						try {
							System.out.println("Paused!");
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (!paused) {
					randomXY();
				}
			}
        }
    }
	
	public static void main(String[] args) {  
		piThread myThread = new piThread();
		myThread.start();  // Start the thread
		
	    JFrame f=new JFrame("Button Example");  
		JButton playButton=new JButton("Run");
	    JLabel example = new JLabel("Pi: "+Double.toString(Math.PI));
		JLabel piEstimation = new JLabel("Est: 0.0");
		JLabel numTrials = new JLabel("# of Trials: 0");
		

		playButton.addActionListener(e -> {
            if (playButton.getText().equals("Run")){
				synchronized(myThread){
					playButton.setText("Pause");
					myThread.paused=false;  // False to unpause
					myThread.notify();
				}
			} else {
				playButton.setText("Run");
				myThread.paused=true;  // True to pause
			}
        });

		// Update displays every 100ms
		Timer updateTimer = new Timer(100, e -> {
			double piEst = myThread.numTrials > 0 ? 4.0 * myThread.numInCircle / myThread.numTrials : 0.0;
			piEstimation.setText("Est: " + piEst);
			numTrials.setText("# of Trials: " + myThread.numTrials);
		});
		updateTimer.start();

	    f.add(example);
	    f.add(piEstimation);
		f.add(numTrials);
		f.add(playButton);
 
	    f.setSize(300,300);  
	    f.setLayout(new GridLayout(4, 1));  
	    f.setVisible(true);      
	}  
}
