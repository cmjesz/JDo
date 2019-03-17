package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
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
    
    @FXML
    void generateReport(ActionEvent event) {
    	// TODO: Add code to popup a table view and populate with data from toDoTasks
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
        
        grid.add(createLabel("Name:", true, "", true), 0, 0);
        
        TextField tf = new TextField();
        tf.setPromptText("Enter Task Name");
        tf.setPrefColumnCount(15);
        tf.setId("textField");
        grid.add(tf, 0, 1);
        grid.add(createLabel("", false, "textFieldLabel", false), 0, 1);
        
        grid.add(createLabel("Notes:", true, "", true), 0, 2);
        
        TextArea ta = new TextArea();
        ta.setPrefRowCount(2);
        ta.setPrefColumnCount(15);
        ta.setWrapText(true);
        ta.setPromptText("Enter Task Notes");
        ta.setId("textArea");
        grid.add(ta, 0, 3);
        grid.add(createLabel("", false, "textAreaLabel", false), 0, 3);
        
        grid.add(createLabel("Creation:", true, "", true), 0, 4);
        
        grid.add(new Label(dateFormat.format(date)), 0, 5);  
        
        Button saveEdit = new Button("Save");
        saveEdit.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		TitledPane titledPane = (TitledPane)((Button)event.getSource()).getParent().getParent().getParent();
        		Long id = Long.parseLong(titledPane.getId());
        		
        		if (((Button) event.getSource()).getText().equals("Save")) {
        			ToDoTask toDoItem = toDoTasks.get(id);
        			
        			GridPane gridPane = (GridPane) titledPane.getContent();
        			swapVisibleFields(gridPane, toDoItem, titledPane);
        			
        			((Button) event.getSource()).setText("Edit");
        		}
        		else {

        			GridPane gridPane = (GridPane) titledPane.getContent();
        			swapVisibleFields(gridPane, null, null);
        			
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
                    gp.add(createLabel("Completion:", true, "", true), 0, 6);
                    
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
    
    private void swapVisibleFields(GridPane gridPane, ToDoTask toDoTask, TitledPane titledPane) {
    	Label textAreaLabel = null;
    	Label textFieldLabel = null;
    	TextField textField = null;
    	TextArea textArea = null;
    	
    	for (Node node : gridPane.getChildrenUnmodifiable()) {
			if (node.getId() != null) {
				if (node.getId().equals("textField")) {
					textField = (TextField) node;
				}
				else if (node.getId().equals("textArea")) {
					textArea = (TextArea) node;
				}
				else if (node.getId().equals("textAreaLabel")) {
					textAreaLabel = (Label) node;
				}
				else if (node.getId().equals("textFieldLabel")) {
					textFieldLabel = (Label) node;
				}
			}
		}
    	
    	if (textAreaLabel != null && textFieldLabel != null && textField != null && textArea != null) {
    		if (textAreaLabel.isVisible() && textFieldLabel.isVisible()) {
    			textField.setText(textFieldLabel.getText());
    			textField.setVisible(true);
    			textFieldLabel.setVisible(false);
    			textArea.setText(textAreaLabel.getText());
    			textArea.setVisible(true);
    			textAreaLabel.setVisible(false);
    		}
    		else {
    			String name = textField.getText();
    			textFieldLabel.setText(name);
    			textField.setVisible(false);
    			textFieldLabel.setVisible(true);
    			textAreaLabel.setText(textArea.getText());
    			textArea.setVisible(false);
    			textAreaLabel.setVisible(true);
    			if (toDoTask != null) {
	    			toDoTask.setName(name);
	    			toDoTask.setNotes(textArea.getText());
    			}
    			if (titledPane != null) {
    				titledPane.setText(name);
    			}
    		}
    	}
    }
    
    private Label createLabel(String text, boolean underlined, String id, boolean visible) {
    	Label label = new Label(text);
    	label.setUnderline(underlined);
    	label.setId(id);
    	label.setVisible(visible);
    	return label;
    }
}
