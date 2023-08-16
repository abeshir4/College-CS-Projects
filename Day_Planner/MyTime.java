// TO DO: add your implementation and JavaDocs.
/**
 * A class for working with and initializing the time of certain events.
 * @author Akram Beshir
 */
public class MyTime implements Comparable<MyTime> {

	/**
	 * hour of time.
	 */
	private int hour;
	/**
	 * minute of time.
	 */
	private int min;
	/**
	 * hour difference between 2 objects.
	 */
	private int hourdif;
	/**
	 * minute difference between 2 objects.
	 */
	private int mindif;
	/**
	 * duration.
	 */
	private int dur;
	/**
	 * placeholder object for getting the new endtime.
	 */
	private MyTime timeobject;
	/**
         * Constructor referring to the time object we'll be working with which will be 00:00.
         */	
	public MyTime(){
		// Constructor
		// initialize time to be 00:00
		this.hour = 0;
		this.min = 0;
	}
	/**
         * Constructor referring to the time object we'll be working at a certain hour.
         * @param hour the hour our time object represents
         */
	public MyTime(int hour){
		// Constructor with hour specified
		// initialize time to be hour:00
		
		// A valid hour can only be within [0, 23].
		// For an invalid hour, throw IllegalArgumentException.
		// Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		// "Hour must be within [0, 23]!"
		this.min = 0;
		if(hour < 0 || hour > 23){ //INVALID HOUR
			throw new IllegalArgumentException("Hour must be within [0, 23]!");
		}
		else{
			this.hour = hour;
		}
	}
	/**
         * Constructor referring to the time object we'll be working with at a specific time.
         * @param hour the hour for our time object
         * @param min the minute for our time object
         */
	public MyTime(int hour, int min){
		// Constructor with hour and minutes specified
		// initialize time to be hour:minute

		// A valid hour can only be within [0, 23].
		// A valid minute can only be within [0, 59].

		// For an invalid hour / minute, throw IllegalArgumentException.
		// Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		// "Hour must be within [0, 23]; Minute must be within [0, 59]!");
		if((hour < 0 || hour > 23)||(min < 0 || min > 59)){ //INVALID HOUR AND MINUTE
			throw new IllegalArgumentException("Hour must be within [0, 23]; Minute must be within [0, 59]!");
		}
		else{
			this.hour = hour;
			this.min = min;
		}
	}
	
	/**
         * gets the hour of out time object.
         * @return the hour of our time object
         */
	public int getHour(){
		// report hour		
		return this.hour; //default return, remove/change as needed
	}

	/**
         * gets the minute of our time object.
         * @return the minute of our time object
         */
	public int getMin(){
		// report minute	
		return this.min; //default return, remove/change as needed
	}
	/**
         * Compares two time objects to determine the proper ordering.
         * @param otherTime the object to be compared.
         * @return an integer representing the ordering
         */
	@Override 
	public int compareTo(MyTime otherTime){
		// compare two times for ordering
		// return the value 0 if the argument Time has the same hour and minute of this Time;
		// return a value less than 0 if this Time is before the otherTime argument; 
		// return a value greater than 0 if this Time is after the otherTime argument.
		
		// Throw IllegalArgumentException if otherTime is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null Time object!"
		if(otherTime == null){
			throw new IllegalArgumentException("Null Time object!");
		}
		else if(this.hour==otherTime.hour&&this.min==otherTime.min){ //SAME TIMES
			return 0;
		}
		else if(this.hour==otherTime.hour&&this.min< otherTime.min){ //THIS TIME COMES BEFORE OTHER
			return -1;
		}
		else if(this.hour<otherTime.hour){ //THIS TIME COMES BEFORE OTHER
			return -1;
		}
		else if(this.hour==otherTime.hour&&this.min>otherTime.min){ //THIS TIME COMES AFTER OTHER
			return 1;
		}
		else if(this.hour>otherTime.hour){
			return 1;
		}	
		return 0; //default return, remove/change as needed
	}
	/**
         * gives the duration between two times starting from one time to another.
         * @param endTime the end time we'll use to measure our duration
         * @return the duration
         */
	public int getDuration(MyTime endTime){
		// return the number of minutes starting from this Time and ending at endTime
		// return -1 if endTime is before this Time
	
		// Throw IllegalArgumentException if endTime is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null Time object!"
		if(endTime == null){
			throw new IllegalArgumentException("Null Time object!");
		}
		else if(endTime.hour==this.hour && endTime.min < this.min){
			return -1;
		}
		else if(endTime.hour<this.hour){
			return -1;
		}
		hourdif = endTime.hour - this.hour; //GET THE HOUR DIFFERENCE
		mindif = endTime.min - this.min; //MINUTE DIFFERENCE
		if(mindif<0){ //IF WE GET A NEGATIVE VALUE
			mindif = mindif * -1; //ADJUST ACCORDINGLY
			hourdif = hourdif - 1;
		}
		dur = (hourdif * 60) + mindif; //60 min per 1 hour
		return dur; //default return, remove/change as needed		
	}
	/**
         * gives the end time given the start time and duration.
         * @param duration the duration of the event
         * @return the end time of the event
         */
	public MyTime getEndTime(int duration){
		// return a Time object that is duration minute from this Time
		
		// Throw IllegalArgumentException if duration is negative. 
		// Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		// "Duration must be non-negative!"			
		
		// return null if endTime passes 23:59 given this Time and duration argument
		if(duration < 0){
			throw new IllegalArgumentException("Duration must be non-negative!");
		}
		hourdif = duration/60; //60 min per hour
		mindif = duration%60; //number of minutes left over
		timeobject = new MyTime(); //end time object
		timeobject.hour = this.hour + hourdif;
		timeobject.min = this.min + mindif;
		while(timeobject.min>=60){ //INVALID MIN VALUE
			timeobject.min-=60; //ADJUST
			timeobject.hour++;
		}
		if(timeobject.hour>=24){ //INVALID HOUR VALUE
			return null;
		}
		return timeobject; //default return, remove/change as needed	
	}

	/**
	 * Gives a string representation of the time object.
	 * @return a string
	 */
	public String toString() {
		// return a String representation of this object in the form of hh:mm
		// hh is the hour of the time (00 through 23), as two decimal digits
		// mm is the minute of the time (00 through 59), as two decimal digits
		
		// Hint: String.format() can be helpful here...
		return String.format("%02d:%02d",this.hour,this.min);		
	}
	/**
         * the main function.
         * @param args args
         */
	public static void main(String[] args){
		//This method is provided for testing 
		//(use/modify as much as you'd like)

		//time objects
		MyTime time1 = new MyTime(7);
		MyTime time2 = new MyTime(9,30);
		
		//checking hour/minute
		if (time1.getHour() == 7 && time1.getMin() == 0 && time2.getHour() == 9
			&& time2.getMin() == 30){
			System.out.println("Yay 1");			
		}		
	
		//compareTo, duration
		if (time1.compareTo(time2) < 0 && time1.compareTo(new MyTime(7,0)) == 0
			&& time2.compareTo(time1) > 0 && time1.getDuration(time2) == 150){
			System.out.println("Yay 2");						
		}
		
		//getEndTime
		MyTime time3 = time1.getEndTime(500);
		if (time3!=null && time3.getHour() == 15 && time3.getMin() == 20 
			&& time2.getEndTime(870) == null){
			System.out.println("Yay 3");								
		}
		
	}
}
