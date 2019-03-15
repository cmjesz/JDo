package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.skin.TitledPaneSkin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class LayoutController implements Initializable {

    @FXML
    private Label timeElapsed;
    @FXML
    private Button startStopButton;
    @FXML
    private MenuButton intervalSelectMenu;
    @FXML
    private Accordion taskAccordion;

	private TimerLogic timerLogic;
	private HashMap<Long, ToDoTask> toDoTasks;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timerLogic = new TimerLogic();
		toDoTasks = new HashMap<>();
		
		if (taskAccordion.getPanes().size() >= 1) {
			setupNewTitledPane(taskAccordion.getPanes().get(0));
		}
	}
	
    @FXML
    void startStopButtonClicked(ActionEvent event) {
    	timerLogic.startStopTimer(startStopButton, timeElapsed);
    }
    
    @FXML
    void setNewInterval(ActionEvent event) {
    	timerLogic.setInterval((MenuItem) event.getSource());
    }    
    
    @FXML
    void createNewTask(ActionEvent event) {
    	TitledPane tp = new TitledPane();
    	setupNewTitledPane(tp);
    	taskAccordion.getPanes().add(tp);
    }
    
    private void setupNewTitledPane(TitledPane titledPane) {
    	titledPane.setText("New Task");
    	
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();

    	// Set ID to be used for looking up Object later for storage
        Long titledPaneId = System.currentTimeMillis();
    	titledPane.setId(titledPaneId.toString());
    	
    	ToDoTask newTask = new ToDoTask("New Task", "", dateFormat.format(date), "", titledPaneId);
    	toDoTasks.put(titledPaneId, newTask);
		
    	// Setup Parent Grid for layout purposes
		GridPane grid = new GridPane();
        grid.setVgap(2);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        Label l1 = new Label("Name:");
        l1.setUnderline(true);
        grid.add(l1, 0, 0);
        
        TextField tf = new TextField();
        tf.setPromptText("Enter Task Name");
        tf.setPrefColumnCount(15);
        tf.setId("textField");
        Label tfl = new Label();
        tfl.setId("textFieldLabel");
        tfl.setVisible(false);
        grid.add(tf, 0, 1);
        grid.add(tfl, 0, 1);
        
        Label l2 = new Label("Notes:");
        l2.setUnderline(true);
        grid.add(l2, 0, 2);
        
        TextArea ta = new TextArea();
        ta.setPrefRowCount(2);
        ta.setPrefColumnCount(15);
        ta.setWrapText(true);
        ta.setPromptText("Enter Task Notes");
        ta.setId("textArea");
        Label tal = new Label();
        tal.setId("textAreaLabel");
        tal.setVisible(false);
        grid.add(ta, 0, 3);
        grid.add(tal, 0, 3);
        
        Label l3 = new Label("Creation:");
        l3.setUnderline(true);
        grid.add(l3, 0, 4);
        
        grid.add(new Label(dateFormat.format(date)), 0, 5);  
        
        Button saveEdit = new Button("Save");
        saveEdit.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		TitledPane titledPane = (TitledPane)((Button)event.getSource()).getParent().getParent().getParent();
        		Long id = Long.parseLong(titledPane.getId());
        		
        		if (((Button) event.getSource()).getText().equals("Save")) {
        			ToDoTask toDoItem = toDoTasks.get(id);
        			String title = "";
        			String notes = "";
        			Label textAreaLabel = null;
        			Label textFieldLabel = null;
        			
        			GridPane gridPane = (GridPane) titledPane.getContent();
        			for (Node node : gridPane.getChildrenUnmodifiable()) {
        				if (node.getId() != null) {
	        				if (node.getId().equals("textField")) {
	        					title = ((TextField)node).getText();
	        					((TextField)node).setVisible(false);
	        					toDoItem.setName(title);
	        					titledPane.setText(title);
	        				}
	        				else if (node.getId().equals("textArea")) {
	        					notes = ((TextArea)node).getText();
	        					((TextArea)node).setVisible(false);
	        					toDoItem.setNotes(notes);
	        				}
	        				else if (node.getId().equals("textAreaLabel")) {
	        					textAreaLabel = (Label) node;
	        				}
	        				else if (node.getId().equals("textFieldLabel")) {
	        					textFieldLabel = (Label) node;
	        				}
        				}
        			}
        			
        			if (textAreaLabel != null) {
        				textAreaLabel.setText(notes);
        				textAreaLabel.setVisible(true);
        			}
        			if (textFieldLabel != null) {
        				textFieldLabel.setText(title);
        				textFieldLabel.setVisible(true);
        			}
        			
        			((Button) event.getSource()).setText("Edit");
        		}
        		else {
        			String title = "";
        			String notes = "";
        			TextArea textArea = null;
        			TextField textField = null;

        			GridPane gridPane = (GridPane) titledPane.getContent();
        			for (Node node : gridPane.getChildrenUnmodifiable()) {
        				if (node.getId() != null) {
	        				if (node.getId().equals("textField")) {
	        					textField = (TextField) node;
	        				}
	        				else if (node.getId().equals("textArea")) {
	        					textArea = (TextArea) node;
	        				}
	        				else if (node.getId().equals("textAreaLabel")) {
	        					Label textAreaLabel = (Label) node;
	        					notes = textAreaLabel.getText();
	        					textAreaLabel.setVisible(false);
	        				}
	        				else if (node.getId().equals("textFieldLabel")) {
	        					Label textFieldLabel = (Label) node;
	        					title = textFieldLabel.getText();
	        					textFieldLabel.setVisible(false);
	        				}
        				}
        			}
        			
        			if (textArea != null) {
        				textArea.setText(notes);
        				textArea.setVisible(true);
        			}
        			if (textField != null) {
        				textField.setText(title);
        				textField.setVisible(true);
        			}
        			
        			((Button) event.getSource()).setText("Save");
        		}
        	}
        });
        grid.add(saveEdit, 0, 8);
        
        Button completeDelete = new Button("Complete");
        completeDelete.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		TitledPane tp = (TitledPane)((Button)event.getSource()).getParent().getParent().getParent();
        		Long id = Long.parseLong(tp.getId());
        		
        		if (((Button) event.getSource()).getText().equals("Complete")) {
        			GridPane gp = (GridPane)((Button)event.getSource()).getParent();
            		Label l4 = new Label("Completion:");
                    l4.setUnderline(true);
                    gp.add(l4, 0, 6);
                    
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date date = new Date();
                    String completion = dateFormat.format(date);
                    gp.add(new Label(completion), 0, 7); 
                    
                    toDoTasks.get(id).setComplete(completion);
                    
                    ((Button) event.getSource()).setText("Delete");
        		}
        		else {
	        		toDoTasks.remove(id);
	        		taskAccordion.getPanes().remove(tp);
        		}
        	}
        });
        grid.add(completeDelete, 0, 9);
        
        titledPane.setContent(grid);
    }
}
