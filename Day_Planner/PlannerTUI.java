import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 *  A Textual UI to help you interact with the planner.
 *  Use with the command:
 *      java PlannerTUI 
 *  or 
 * 		java PlannerTUI Input_File_Name
 *  
 *  @author Y. Zhong
 */	
public class PlannerTUI {

	/**
	 * Two possible input modes.
	 */
	
	enum Mode { 
		/**
		 * Keyboard input.
		 */
		KEYBOARD, 
		
		/**
		 * File input.
		 */
		FILE};
	
	/**
	 * String for better formatting.
	 */
	private static String divider = "----------------------------------------\n";

	/**
	 * Scanner to get input from keyboard or file.
	 */
	private static Scanner scanner = null;
	
	/**
	 * Planner we maintain to demo.
	 */	
	private static Planner planner = null; 
	 
	/**
	 *  The main method that presents the UI.
	 *  
	 *  @param args command line args: first arg can specify an input file 
	 */
	public static void main(String[] args) {

		// initialize an empty planner
		planner = new Planner();
		Mode mode = Mode.FILE;

		if(args.length > 1){
			System.out.println("Usage: java PlannerTUI [Input_File_Name]");
			System.exit(0);
		}
		else if (args.length == 1){
			try{
				// open file for input
				scanner = new Scanner(new File(args[0]));		
		
			}catch(IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
		}
		else{

			//keyboard
			scanner = new Scanner(System.in);
			mode = Mode.KEYBOARD;
		
		}
		
		System.out.print(divider);
		System.out.println("----- Welcome to our Day Planner! -----");
		System.out.print(divider);
		
		int option;
		while(true){
			displayMenu();
			
			if (mode == Mode.FILE)
				enterToContinue();

			option = scanner.nextInt(); //get the next menu choice
			switch(option){
				case 1: //display
					System.out.print(divider);
					System.out.format("Current planner has %d event(s).\n", planner.size());
					System.out.print(divider);
					System.out.println(planner.toString());
					break;
				case 2: //add an event
					processAddEvent();
					break;
				case 3: //move an event
					processChangeStart();
					break;
				case 4: //change duration of an event
					processChangeDuration();
					break;
				case 5: //change description of an event
					processChangeDescription();
					break;
				case 6: //delete an event
					processRemoveEvent();
					break;
				case 7: //exit
					System.out.println("Good-bye!");
					return;
				default:
					System.out.println("Option not supported!");
			
			}			
		}
		
	}
	
	/**
	 *  The method that displays the menu.
	 *  
	 */
	private static void displayMenu(){
		System.out.println(divider);
		System.out.println("Please select from the following options:");
		System.out.println("1 - Show current planner");
		System.out.println("2 - Add an event");
		System.out.println("3 - Change the start time of an event");
		System.out.println("4 - Change the duration of an event");
		System.out.println("5 - Change the description of an event");
		System.out.println("6 - Remove an event");
		System.out.println("7 - Exit");
		System.out.print(divider);
		System.out.print("Your choice (1-7): ");
	
	}
	
	/**
	 *  The method that interact with the user to add a new event into planner.
	 *  
	 */
	private static void processAddEvent(){
		MyTime startTime, endTime;
		
		//get the starting time (hour/minute) 
		System.out.print("Please enter the starting hour of the new event (0-23): ");
		int startHour = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Please enter the starting minute of the new event (0-59): ");
		int startMin = scanner.nextInt();
		scanner.nextLine();
		
		//verify input
		try{
			startTime = new MyTime(startHour, startMin);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			return;
		}
		
		//get the ending time (hour/minute) 
		System.out.print("Please enter the ending hour of the new event (0-23): ");
		int endHour = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Please enter the ending minute of the new event (0-59): ");
		int endMin = scanner.nextInt();
		scanner.nextLine();
		
		//verify input
		try{
			endTime = new MyTime(endHour, endMin);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			return;
		}
				
		//create an event, exit on error
		Event event;
		try{
			event = new Event(startTime, endTime);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			System.out.println("New event cannot be added!");
			return;		
		}
		
		//get the description of the event
		System.out.println("Please enter a description of the new event: ");
		String description = scanner.nextLine();		
		event.setDescription(description);
		
		//add event 
		planner.addEvent(event);
		System.out.println("New event added!");
		System.out.println("New event details: " + event.toString());
			
	}
	
	/**
	 *  The method that interact with the user to remove an existing event.
	 *  
	 */
	private static void processRemoveEvent(){
		// get the index of event to be removed
		System.out.print("Please select the event number to remove: ");
		int eventIndex = scanner.nextInt();
		scanner.nextLine();
		
		//verify index
		Event toRemove = planner.getEvent(eventIndex);
		if (toRemove == null){
			System.out.println("Invalid event number!");
			return;
		}
		
		//remove event
		if (planner.removeEvent(eventIndex)){
			System.out.println("Event removed!");
			System.out.println("Removed event details: " +toRemove.toString());
		}
		else{
			System.out.println("Event cannot be removed!");
		
		}
	}
		
		
	/**
	 *  The method that interact with the user to move the starting time of 
	 *  an existing event without changing its duration.
	 *  
	 */
	private static void processChangeStart(){
		// get the index of event to be changed
		System.out.print("Please select the event number to change: ");
		int eventIndex = scanner.nextInt();
		scanner.nextLine();

		//verify index
		Event event = planner.getEvent(eventIndex);
		if (event == null){
			System.out.println("Invalid event number!");
			return;
		}
		
		System.out.println("You select to change this event:");
		System.out.println(event);

		//get new starting time		
		System.out.print("Please enter the new starting hour of the event (0-23): ");
		int newHour = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Please enter the new starting minute of the event (0-59): ");
		int newMin = scanner.nextInt();
		scanner.nextLine();
		
		//verify input
		MyTime newStart;
		try{
			newStart = new MyTime(newHour, newMin);
		}
		catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			return;
		}

		//change start time of the event		
		if (planner.moveEvent(eventIndex, newStart)){
			System.out.println("Event changed!");
		}
		else{
			System.out.println("Event cannot be changed!");
		
		}
	
	}

	/**
	 *  The method that interact with the user to change the duration of an existing event.
	 *  
	 */
	private static void processChangeDuration(){
		// get the index of event to be changed
		System.out.print("Please select the event number to change: ");
		int eventIndex = scanner.nextInt();
		scanner.nextLine();

		//verify index
		Event event = planner.getEvent(eventIndex);
		if (event == null){
			System.out.println("Invalid event number!");
			return;
		}
		
		System.out.println("You select to change this event:");
		System.out.println(event);
		
		//get new duration (in minutes)
		System.out.print("Please enter the new duration in minutes: ");
		int newDuration = scanner.nextInt();
		scanner.nextLine();
		
		//change duration
		if (planner.changeDuration(eventIndex, newDuration)){
			System.out.println("Event changed!");
		}
		else{
			System.out.println("Event cannot be changed!");
		
		}
		
	}

	/**
	 *  The method that interact with the user to change the description of an existing event.
	 *  
	 */
	private static void processChangeDescription(){
		// get the index of event to be changed
		System.out.print("Please select the event number to change: ");
		int eventIndex = scanner.nextInt();
		scanner.nextLine();

		//verify index
		Event event = planner.getEvent(eventIndex);
		if (event == null){
			System.out.println("Invalid event number!");
			return;
		}
		
		System.out.println("You select to change this event:");
		System.out.println(event);
		
		//get new description
		System.out.print("Please enter the new description: ");
		String newDescription = scanner.nextLine();
		
		//change description
		if (planner.changeDescription(eventIndex, newDescription)){
			System.out.println("Event changed!");
		}
		else{
			System.out.println("Event cannot be changed!");
		
		}
		
	}
	
	/**
	 * The metod that interacts with the user and returns when user presses enter/return.
	 *
	 */
	private static void enterToContinue() {
		System.out.print("Press enter to continue ...");
		Scanner s = new Scanner(System.in);
		s.nextLine();
	}


	
}