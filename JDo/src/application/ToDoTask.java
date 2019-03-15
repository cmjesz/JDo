package application;

import java.io.Serializable;

public class ToDoTask implements Serializable {

	private static final long serialVersionUID = System.currentTimeMillis();
	private String name, notes, creation, completion;
	private Long id;
	
	public ToDoTask(String name, String notes, String creation, String completion, Long id) {
		this.name = name;
		this.notes = notes;
		this.creation = creation;
		this.completion = completion;
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public void setComplete(String completion) {
		this.completion = completion;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public String getCreation() {
		return this.creation;
	}
	
	public String getCompletion() {
		return this.completion;
	}
}
