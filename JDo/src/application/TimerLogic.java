package application;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class TimerLogic {
	
	private Timer timer;
	private int interval;
	private int currentIntervalSelection;
	
	private final int DEFAULT_INTERVAL = 300;
	private final int TEN_MINUTES = 600;
	private final int FIFTEEN_MINUTES = 900;
	private final int TWENTY_MINUTES = 1200;
	private final int THIRTY_MINUTES = 1800;
	private final int SIXTY_MINUTES = 3600;
	
	public TimerLogic() {
		interval = DEFAULT_INTERVAL;
		currentIntervalSelection = DEFAULT_INTERVAL;
	}

	public void startStopTimer(Button startStopButton, Label timeElapsed) {
		if (startStopButton.getText().equals("Start Timer")) {
    		interval = currentIntervalSelection;
    		setTimer(timeElapsed, startStopButton);
    		startStopButton.setText("Stop Timer");
    	}
    	else {
    		timer.cancel();
    		startStopButton.setText("Start Timer");
    		timeElapsed.setText("Stopped");
    	}
	}
	
	public void setInterval(MenuItem menuItem) {
		switch (menuItem.getText()) {
	    	case "5 Minutes":
	    		currentIntervalSelection = DEFAULT_INTERVAL;
	    		break;
	    	case "10 Minutes":
	    		currentIntervalSelection = TEN_MINUTES;
	    		break;
	    	case "15 Minutes":
	    		currentIntervalSelection = FIFTEEN_MINUTES;
	    		break;
	    	case "20 Minutes":
	    		currentIntervalSelection = TWENTY_MINUTES;
	    		break;
	    	case "30 Minutes":
	    		currentIntervalSelection = THIRTY_MINUTES;
	    		break;
	    	case "60 Minutes":
	    		currentIntervalSelection = SIXTY_MINUTES;
	    		break;
			default:
				currentIntervalSelection = DEFAULT_INTERVAL;
				break;
		}
	}
	
	public void setTimer(Label timeElapsed, Button startStopButton) {
	    timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            if(interval > 0)
	            {
	            	if (interval / 60 >= 1) {
		                Platform.runLater(() -> timeElapsed.setText(String.format("Minutes: %02d" , interval / 60)));
	            	}
	            	else {
		                Platform.runLater(() -> timeElapsed.setText(String.format("Seconds: %02d" , interval % 60)));
	            	}
	            	interval--;
	            }
	            else {
	                timer.cancel();
	                Platform.runLater(() -> startStopButton.setText("Start Timer"));
	                Platform.runLater(() -> timeElapsed.setText("Complete"));
	            }
	        }
	    }, 0, 1000);
	}
}
