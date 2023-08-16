Overview:

There are three major components to this project:
1. Implementing one of the most fundamental data structures in computer science (the dynamic array list).
2. Using this data structure to implement a larger program.
3. Practicing many fundamental skills learned in prior programming courses including generic classes.
We will implement a sorted version of the dynamic array list for this project. It still supports the basic features of a
dynamic array, which can grow capacity when needed. Furthermore, all items stored inside our sorted dynamic array
must be kept in an ascending order. This additional requirement will affect how we could insert new values into the
storage and how we are allowed to change the values. The end product will use the sorted dynamic array to implement a day
planner. It would allow users to perform various operations in maintaining a single-day planner, including adding an
event, moving an event, and changing an event. 

The end product of this project is a day planner. With the provided simple textual user interface, users can
add events into a 24-hour period (from 0:00 to 23:59). We allow overlap of events but the list of events must be sorted
based on their starting times. Users can also delete and update existing events. 

Implementation/Classes:

This project will be built using a number of classes representing time, event, the generic sorted dynamic array, and the day
planner. Here's a description of each of these classes.

• MySortedArray (MySortedArray.java): The implementation of a sorted dynamic array list. It will be used as the storage of events in the day planner.

• MyTime (MyTime.java): The class representing a time with two integer components: an hour within [0,23] and
a minute within [0,59]. It implements Comparable interface. Comparison of MyTime objects is the basis of
ordering events in the day planner.

• Event (Event.java): The implementation of an event with a starting time, an ending time, and a description. It
also implements Comparable. The ordering of two events is determined by the ordering of their starting times.

• Planner (Planner.java): The implementation of a day planner. It stores a collection of events in ascending order
of their starting times. The planner supports multiple operations for maintenance, including adding a new event,
deleting an event, and updating an event.

• PlannerTUI (PlannerTUI.java): A textual user interface class to interact with the user and to help testing your
implementation.

-This project should compile with the command: javac *.java

-This project should run with the command: java PlannerTUI
