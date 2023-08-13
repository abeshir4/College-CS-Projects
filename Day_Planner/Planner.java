// TO DO: add your implementation and JavaDocs.
/**
 * A class that will deal with a day planner to store events.
 * @author Akram Beshir
 */
public class Planner{

	// DO NOT MODIFY INSTANCE VARIABLES PROVIDED BELOW
	
	//underlying array of events  -- you MUST use this for credit!
	//Do NOT change the name or type

	/**
	 * an array of events to be put in the planner.
	 */
	private MySortedArray<Event> events;
	
	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	/**
         * A constructor referring to a a planner with no events.
         */
	public Planner(){
		// Constructor with no arguments.
		
		// A list of events should be created.  The initial capacity should be 
		// DEFAULT_CAPACITY defined in our MySortedArray class. 
		// The list should be empty (with no events).
		events = new MySortedArray<Event>();
	}

	/**
         * gives us how many events are in our planner.
         * @return the number of events in our planner
         */
	public int size(){
		// return the number of events in the list.
		// O(1)
		
		return events.size(); //default return, remove/change as needed
	}
	/**
         * Gives a string representation of our planner.
         * @return a string
         */
	public String toString(){
		// return the string representation of the planner with this format:
		// - include all events in the list in ascending order of the indexes;
		// - each event goes into a separate line;
		// - each line except for the last uses this format (quotes excluded): "[index]event\n"
		// - the last line does not end with a new line and uses this format: "[index]event"

		// The format of an event is the same as .toString() of Event class

		// Hint: String.format() can be helpful here...
		
		// Note: use StringBuilder instead of String concatenation for a better efficiency 
		StringBuilder t = new StringBuilder("");
		for (int i = 0; i < events.size(); i++) {
			if(i!= events.size()-1){
				t.append("["+i+"]" + events.get(i).toString() + "\n");
			}
			else{
				t.append("["+i+"]" + events.get(i).toString());
			}
		}
		return t.toString();
	}
	/**
         * Adds an event to our planner.
         * @param event the event we want to add
         */
	public void addEvent(Event event){
		
		// Add a new event into the list
		//	- make sure events are sorted after addition

		// Throw IllegalArgumentException if event is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null Event object!"
		
		// O(N) where N is the number of events in the list
		if(event==null){
			throw new IllegalArgumentException("Null Event object!");
		}
		else{
			events.add(event);
		}
	}
	/**
         * changes the start time of an event at a certain index.
         * @param index the index of the event we want to change
         * @param newStart the new start time to be updated for our event
         * @return if the operation was a success
         */
	public boolean moveEvent(int index, MyTime newStart){
		// Move the event at index to be start at newStart.
		// Hint: we will keep the same duration but the end time may need to be changed.
		
		// Return true if event can be updated; return false otherwise.
		// - return false for an invalid index
		// - return false if event cannot be moved to newStart
		// - return false if newStart is null
		
		// If with the updated starting time, the events are still sorted in ascending 
		// order of their starting times, do not change the index of the event.
		// Otherwise, fix the ordering by first removing the updated event, 
		// then adding it back.


		// O(N) where N is the number of events currently in list
		if(index<0||index>= events.size()){
			return false;
		}
		else if(events.get(index)==null){
			return false;
		}
		else if(events.get(index).moveStart(newStart)==false){
			return false;
		}
		else if(newStart==null){
			return false;
		}
		events.get(index).moveStart(newStart);
		if(events.size()==1){
			return true;
		}
		for(int i = 0; i < events.size()-1; i++){
			if(events.get(i).compareTo(events.get(i+1))>0){
				events.add(events.delete(index));
			}
		}
		return true; //default return, remove/change as needed
	}

	/**
         * Changes the duration of an event at a certain index.
         * @param index the index of the event we want to change
         * @param minute the new duration
         * @return if the operation was a success
         */
	public boolean changeDuration (int index, int minute){
		// Change the duration of event at index to be the given number of minutes.
		
		// Return true if the duration can be changed.
		// Return false if:
		// - index is invalid; or
		// - minute is negative; or
		// - the duration of event at index can not be updated with the specified minute

		// O(1)		
		if(index<0||index>= events.size()){
			return false;
		}
		else if(events.get(index)==null){ //SINCE U ALREADY HAVE THIS EXCEPTION IN OTHER CLASS, U MIGHT NOT NEED IT; ASK TA
			return false;
		}
		else if(minute<0){
			return false;
		}
		else if(events.get(index).changeDuration(minute)==false){
			return false;
		}
		else{
			events.get(index).changeDuration(minute);
		}
		return true;
	
	}

	/**
         * Changes the description of an event at a certain index.
         * @param index the index that we want to change
         * @param description the new description
         * @return if the operation was a success
         */
	public boolean changeDescription(int index, String description){
		// Change the description of event at index.
		
		// Return true if the event can be changed.
		// Return false for an invalid index.

		// If description argument is null, 
		// set description of the event to be empty string ""
		
		// O(1)
		if(index<0||index>= events.size()){
			return false;
		}
		else if(events.get(index)==null){ //SINCE U ALREADY HAVE THIS EXCEPTION IN OTHER CLASS, U MIGHT NOT NEED IT; ASK TA
			return false;
		}
		events.get(index).setDescription(description);
		return true;
	}
	/**
         * Removes an event at a certain index.
         * @param index the index we want to remove
         * @return if the operation was a success
         */
	public boolean removeEvent(int index){
		// Remove the event at index.
		
		// Return true if the event can be removed
		// Return false for an invalid index.
		
		// O(N) where N is the number of elements currently in the storage
		if(index<0||index>= events.size()){
			return false;
		}
		else if(events.get(index)==null){ //SINCE U ALREADY HAVE THIS EXCEPTION IN OTHER CLASS, U MIGHT NOT NEED IT; ASK TA
			return false;
		}
		events.delete(index);
		return true;
	}
	
	/**
         * Gets an event at a certain index.
         * @param index the index we want to retrieve
         * @return the event at that index
         */
	public Event getEvent(int index){
		// Return the event at index
		
		// Return null for an invalid index.
		
		//O(1)
		if (index < 0 || index >= events.size()) {
			return null;
		}
		return events.get(index);
	}

	/**
         * the main function.
         * @param args args
         */
	public static void main(String[] args){
	
		// creating a planner
		Planner day1 = new Planner();

		// adding two events		
		Event breakfast = new Event(new MyTime(7), new MyTime(7,30), "breakfast");
		Event jogging = new Event(new MyTime(5), new MyTime(6), "jogging");
		day1.addEvent(breakfast);
		day1.addEvent(jogging);
		
		if (day1.size()==2 && day1.getEvent(0)==jogging && day1.getEvent(1)==breakfast ){
			System.out.println("Yay 1");					
		}
		
		//toString
		if (day1.toString().equals("[0]05:00-06:00/jogging\n[1]07:00-07:30/breakfast")){
			System.out.println("Yay 2");							
		}
		//System.out.println(day1);

		// move start of breakfast		
		MyTime newBFTime = new MyTime(6,30);
		
		if (day1.moveEvent(1, newBFTime) && day1.getEvent(1).getStart().getHour() == 6
			&& day1.getEvent(1).getStart().getMin() == 30){
			System.out.println("Yay 3");								
		}
		//System.out.println(day1);
		
		// change duration
		if (day1.changeDuration(0, 45) && day1.getEvent(0).getEnd().getHour() == 5 
			&& day1.getEvent(0).getEnd().getMin() == 45 && day1.changeDuration(1, 60)
			&& day1.getEvent(1).getEnd().getHour() == 7 
			&& day1.getEvent(1).getEnd().getMin() == 30){
			System.out.println("Yay 4");											
		}
		//System.out.println(day1);
		
		// change description, remove
		if (day1.changeDescription(1, "sleeping") && !day1.removeEvent(3) 
			&& !day1.removeEvent(-2) && day1.removeEvent(0)){
			System.out.println("Yay 5");							
		}
		//System.out.println(day1);
		
	}
}
