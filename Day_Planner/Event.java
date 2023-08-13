// TO DO: add your implementation and JavaDocs.
/**
 * A class that works with Events that have times and descriptions.
 * @author Akram Beshir
 */
public class Event implements Comparable<Event> {


	/**
	 * Start time of the event represented by time objects.
	 */
	private MyTime startTime;
	/**
	 * End time of the event represented by time objects.
	 */
	private MyTime endTime;
	/**
	 * Event description.
	 */
	private String description;
	/**
	 * placeholder object for the start time.
	 */
	private MyTime placeholderstart;
	/**
	 * placeholder object for the end time.
	 */
	private MyTime placeholderend;
	/**
	 * placeholder object for the new endtime when duration and start times are changed.
	 */
	private MyTime placeholdernewend;
	/**
	 * start time hour.
	 */
	private int placeholderstarthour;
	/**
	 * start time minute.
	 */
	private int placeholderstartmin;
	/**
	 * end time hour.
	 */
	private int placeholderendhour;
	/**
	 * end time minute.
	 */
	private int placeholderendmin;
	/**
	 * event duration.
	 */
	private int placeholderdur;
	/**
	 * new start time hour.
	 */
	private int placeholdernewstarthour;
	/**
	 * new start time minute.
	 */
	private int placeholdernewstartmin;
	/**
	 * new end time hour.
	 */
	private int hr;
	/**
	 * new end time minute.
	 */
	private int mn;
	/**
	 * new description.
	 */
	private String placeholdername;
	/**
         * A constructor referring to a nameless Event with their start and end times.
         * @param startTime start time
         * @param endTime end time
         */
	public Event(MyTime startTime, MyTime endTime){
		// constructor with start and end times
		// set description to be empty string ""
		
		// Throw IllegalArgumentException if endTime comes before startTime
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//        "End Time cannot come before Start Time!"
		// - Assume that the start time can be the same as the end time 
		//   (0-duration event allowed)

		// Throw IllegalArgumentException if either time is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null Time object!"
		description = "";
		if (startTime==null || endTime==null){ //null time
			throw new IllegalArgumentException("Null Time object!");
		}
		else if (startTime.getHour() >endTime.getHour()){
			throw new IllegalArgumentException("End Time cannot come before Start Time!"); //ORDERING GONE WRONG
		}
		else if (startTime.getHour() ==endTime.getHour() && startTime.getMin()> endTime.getMin()){
			throw new IllegalArgumentException("End Time cannot come before Start Time!");
		}
		else{
			placeholderstarthour = startTime.getHour();
			placeholderstartmin = startTime.getMin();
			placeholderendhour = endTime.getHour();
			placeholderendmin = endTime.getMin();
			placeholderdur = startTime.getDuration(endTime);
			placeholdername = description;
			
		}
		//description="";
	}
	/**
         * A constructor referring to an Event with their start and end times as well as a description.
         * @param startTime the event start time
         * @param endTime the event end time
         * @param description the event description
         */
	public Event(MyTime startTime, MyTime endTime, String description){
		// constructor with start time, end time, and description
		
		// perform the same checking of start/end times and 
		// throw the same exception as the constructor above
		
		// if description argument is null, 
		// set description of the event to be empty string ""
		if (startTime==null || endTime==null){
			throw new IllegalArgumentException("Null Time object!");
		}
		else if (startTime.getHour() >endTime.getHour()){
			throw new IllegalArgumentException("End Time cannot come before Start Time!");
		}
		else if (startTime.getHour() ==endTime.getHour() && startTime.getMin()> endTime.getMin()){
			throw new IllegalArgumentException("End Time cannot come before Start Time!");
		}
		else if(description==null){
			placeholderstarthour = startTime.getHour();
			placeholderstartmin = startTime.getMin();
			placeholderendhour = endTime.getHour();
			placeholderendmin = endTime.getMin();
			placeholderdur = startTime.getDuration(endTime);
			description="";
			placeholdername = description;
			
		}
		else{
			placeholderstarthour = startTime.getHour();
			placeholderstartmin = startTime.getMin();
			placeholderendhour = endTime.getHour();
			placeholderendmin = endTime.getMin();
			placeholderdur = startTime.getDuration(endTime);
			placeholdername = description;
		}
	}
	/**
         * gives the starting time of an event.
         * @return the start time
         */
	public MyTime getStart(){
		// report starting time
		placeholderstart = new MyTime(placeholderstarthour,placeholderstartmin);
		return placeholderstart;
	}
	/**
         * gives the end time of an event.
         * @return the end time
         */
	public MyTime getEnd(){
		// report starting time
		placeholderend = new MyTime(placeholderendhour,placeholderendmin);
		return placeholderend;
	}
	/**
         * gives a description of the event.
         * @return the event name
         */
	public String getDescription(){
		// report description
		
		return placeholdername; //default return, remove/change as needed
	}
	/**
         * Compares the start times of two events.
         * @param otherEvent the object to be compared.
         * @return an integer representing the ordering of events
         */
	@Override 
	public int compareTo(Event otherEvent){
		// compare two times for ordering
		
		// The ordering of two events is the same as the ordering of their start times
	
		// Throw IllegalArgumentException if otherEvent is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null Event object!"
		//try{
		if(otherEvent==null||this==null){
			throw new IllegalArgumentException("Null Event object!");
		}
		else if(this.getStart().compareTo(otherEvent.getStart())==0){
			return 0;
		}
		else if(this.getStart().compareTo(otherEvent.getStart())<0){
			return -1;
		}
		else if(this.getStart().compareTo(otherEvent.getStart())>0){
			return 1;
		}
		
		return 0; //default return, remove/change as needed

	}

	/**
         * moves the start time of an event to another one while changing the end time with its duration.
         * @param newStart the new start time we want to have
         * @return if the operation was a success
         */
	public boolean moveStart(MyTime newStart){
		// Move the start time of this Event to be newStart but keep the same duration. 
		// - Remember to update the end time to ensure duration unchanged.
		
		// The start time can be moved forward or backward but the end time cannot 
		// go beyond 23:59 of the same day.  Do not update the event if this condition
		// cannot be satisfied and return false.  Return false if newStart is null. 
		
		// Return true if the start time can be moved to newStart successfully.
		
		// Note: a false return value means the specified newStart can not be used 
		//       for the current event.  Hence if newSart is the same as the current 
		//       start, we will still return true.
		if(newStart==null){
			return false;
		}
		placeholdernewstarthour = newStart.getHour();
		placeholdernewstartmin = newStart.getMin();
		placeholdernewend = newStart.getEndTime(placeholderdur);
		hr = placeholdernewend.getHour();
		mn = placeholdernewend.getMin();
		if(hr>=24){
			return false;
		}
		placeholderstarthour = placeholdernewstarthour;
		placeholderstartmin = placeholdernewstartmin;
		placeholderendhour = hr;
		placeholderendmin = mn;
		return true;
	}
	/**
         * change the duration of an event while adjusting the end time accordingly.
         * @param minute the length we want out new duration
         * @return if the operation was a success
         */
	public boolean changeDuration(int minute){
		// Change the duration of event to be the given number of minutes.
		// Update the end time of event based on the updated duration.	
			
		// The given minute cannot be negative; and the updated end time cannot go 
		// beyond 23:59 of the same day.  Do not update the event if these conditions
		// cannot be satisfied and return false.  
		// Return true if the duration can be changed.
		
		// Note: a false return value means the specified duration is invalid for some 
		// 		 reason.  Hence if minute argument is the same as the current duration, 
		//       we will still return true.
		if(minute<0){
			return false;
		}
		placeholdernewend = placeholderstart.getEndTime(minute);
		hr = placeholdernewend.getHour();
		mn = placeholdernewend.getMin();
		if(hr>=24){
			return false;
		}
		placeholderdur = minute;
		placeholderendhour = hr;
		placeholderendmin = mn;
		return true;
	}
	
	/**
         * Changes the description of an event.
         * @param newDescription the new description we want
         */
	public void setDescription(String newDescription){
		// set the description of this event

		// if newDescription argument is null, 
		// set description of the event to be empty string ""
		if(newDescription==null){
			description = "";
			placeholdername = description;
		}
		else{
			description = newDescription;
			placeholdername = description;
		}
	}
	
	/**
         * Gives a string representation of our event.
         * @return the String with our event details
         */
	public String toString(){
		// return a string representation of the event in the form of
		// startTime-endTime/description
		// example: "06:30-07:00/breakfast"

		// Hint: String.format() can be helpful here...
		
		// The format of start/end times is the same as .toString() of MyTime


		return String.format("%02d:%02d-%02d:%02d/%s",placeholderstarthour,placeholderstartmin,placeholderendhour,placeholderendmin,placeholdername);
	
	}
	/**
         * our main function.
         * @param args args
         */
	public static void main(String[] args){
		// creating an event
		Event breakfast = new Event(new MyTime(7), new MyTime(7,30), "breakfast");
		
		// checking start/end times
		if (breakfast.getStart()!=null && breakfast.getEnd()!=null &&
			breakfast.getStart().getHour() == 7 && breakfast.getEnd().getHour() == 7 && 
			breakfast.getStart().getMin() == 0 && breakfast.getEnd().getMin() == 30){
			System.out.println("Yay 1");			
		}		
		//System.out.println(breakfast.toString());
		//expected output (excluding quote):
		//"07:00-07:30/breakfast"

		// moveStart
		if (breakfast.moveStart(new MyTime(6,30)) && breakfast.getStart().getHour() == 6
			&& breakfast.getStart().getMin() == 30 && breakfast.getEnd().getMin() == 0){
			System.out.println("Yay 2");					
		}
		//System.out.println(breakfast);
		
		//longer duration
		if (breakfast.changeDuration(45) && breakfast.getStart().getHour() == 6
			&& breakfast.getStart().getMin() == 30 && breakfast.getEnd().getMin() == 15
			&& breakfast.getEnd().getHour() == 7){

			System.out.println("Yay 3");					
		}
		//System.out.println(breakfast);
		
		//shorter duration
		if (!breakfast.changeDuration(-10) && breakfast.changeDuration(15) 
			&& breakfast.getStart().getHour() == 6 && breakfast.getStart().getMin() == 30 
			&& breakfast.getEnd().getMin() == 45 && breakfast.getEnd().getHour() == 6){
			System.out.println("Yay 4");					
		}
		//System.out.println(breakfast);
		
		// compareTo
		Event jogging = new Event(new MyTime(5), new MyTime(6), "jogging");
		Event morningNews = new Event(new MyTime(6, 30), new MyTime(7), "morning news");
		if (breakfast.compareTo(jogging)>0 && jogging.compareTo(breakfast)<0
			&& breakfast.compareTo(morningNews) == 0){
			System.out.println("Yay 5");								
		}
	}

}
