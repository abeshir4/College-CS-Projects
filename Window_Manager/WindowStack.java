//TODO: Complete java docs and code in missing spots.
//Missing spots are marked by < YOUR_CODE_HERE >.
//Do NOT edit any other parts of the code.

//import java.awt.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 *  A stack of windows within the window.
 *
 *  <p>Adapterion of Nifty Assignment (http://nifty.stanford.edu/) by
 *  Mike Clancy in 2001. Original code by Mike Clancy. Updated Fall
 *  2022 by K. Raven Russell.</p>
 * @author Katherine Russell
 * @author Akram Beshir
 */
public class WindowStack {
    //You'll need some instance variables probably...
    /**
     * A private window list.
     */
    private ThreeTenLinkedList<Window> windowStack;

    /**
     * Initialize an empty list of windows.
     */
    public WindowStack() {
        //Any initialization code you need.

        //O(1)

        windowStack = new ThreeTenLinkedList<Window>();
    }

    /**
     * Gets the first node of the list.
     * @return the first node of the list
     */
    public Node<Window> getHead() {
        //Returns the head (top) of the stack of windows.

        //O(1)

        //We will use this method to test your
        //linked list implementaiton of this
        //class, so whether or not you are using
        //the generic linked list class or bare
        //nodes, you must still be able to return
        //the appropriate head of the list.

        return windowStack.getFirst();
    }

    /**
     * Gets the last node of the list.
     * @return the last node of the list
     */
    public Node<Window> getTail() {
        //Returns the tail (bottom) of the stack of windows.

        //O(1)

        //We will use this method to test your
        //linked list implementaiton of this
        //class, so whether or not you are using
        //the generic linked list class or bare
        //nodes, you must still be able to return
        //the appropriate tail of the list.

        return windowStack.getLast();
    }

    /**
     * Gives length of the list.
     * @return number of items
     */
    public int numWindows() {
        //Gets the number of windows in the stack.

        //O(1)

        return windowStack.size();
    }

    /**
     * Add a window to the beginning of the list.
     * @param r The window to be added
     */
    public void add(Window r) {
        //Add a window at the top of the stack.

        //O(1)

        //throw IllegalArgumentException for invalid windows

        //Note: the "top" of the stack should
        //be the head of your linked list.

        if(r==null){
            throw new IllegalArgumentException();
        }
        if(windowStack.size()==0){ //No windows at all
            windowStack.addFront(r); //r will be our first window to be added
            windowStack.getFirst().data.setSelected(true);
        }
        else{
            windowStack.getFirst().data.setSelected(false); //deselect the current top window
            windowStack.addFront(r); //add the window
            windowStack.getFirst().data.setSelected(true); //select the new top window
        }
    }

    /**
     * Add or delete squares in the top window or deletes the top window if its within a coordinate.
     * @param x x coordinate
     * @param y y coordinate
     * @param leftClick Determines if the window will be moved to the top
     * @return Whether or not the click was handled
     */
    public boolean handleClick (int x, int y, boolean leftClick) {
        //The mouse has been clicked at position (x,y).
        //Left clicks are move windows to the top of the
        //stack or pass the click on to the window at the
        //top. Right clicks remove windows.

        //Returns true if the click was handled, false
        //if no window was found.

        //O(n) where n is the number of windows in the stack


        //Details:

        //Find the top-most window on the stack (if any)
        //that contains the given coordinate.


        //For a left click:

        //If the window is not at the top of the stack,
        //move it to the top of the stack without
        //disturbing the order of the other windows.
        //Mark this window as the "selected" window (and
        //ensure the previous selected window is no longer
        //selected).

        //If the window is at the top of the stack already,
        //ask the window to handle a click-event (using the
        //Window's handleClick() method).

        //If none of the windows on the stack were clicked
        //on, just return.


        //For a right click:

        //Remove the window from the stack completely. The
        //window at the top of the stack should be the
        //selected window.


        //Hint #1: This would be a great time to use helper
        //methods! If you just write one giant method...
        //it'll be much harder to debug...

        //Hint #2: Make sure to use the methods you wrote
        //in the Window class. Don't write those again!

        if(leftClick == true){
            Node<Window> iteratorWindow = windowStack.getFirst();
            boolean handled = false;
            if(iteratorWindow==null){
                ;
            }
            else if(iteratorWindow.data.contains(x, y) == true){//The first window will already be selected
                iteratorWindow.data.setSelected(true);
                iteratorWindow.data.handleClick(x, y);//So it will call handle click to either add or delete squares
                handled = true;
            }
            else{
                while(iteratorWindow!=null){ //iterates thru the window stack
                    if(iteratorWindow.data.contains(x, y)==true){ //if a window is contained in the point
                        Window temp = iteratorWindow.data;
                        windowStack.getFirst().data.setSelected(false); //deselect the current top window
                        windowStack.remove(iteratorWindow.data); //remove our window
                        windowStack.addFront(temp); //add it to the front
                        windowStack.getFirst().data.setSelected(true); //select the new top window
                        handled = true;
                        break;
                    }
                    iteratorWindow = iteratorWindow.next;
                }
            }
            if(handled==true){
                return true;
            }
        }
        else if(leftClick == false) {
            Node<Window> topWindow = windowStack.getFirst();
            int prevsize = windowStack.size();
            if(topWindow==null){
                ;
            }
            else if (topWindow.data.contains(x, y) == true) {
                topWindow.data.setSelected(false);
                windowStack.remove(topWindow.data);
                if(windowStack.size()!=0){
                    windowStack.getFirst().data.setSelected(true);
                }
            }
            if(prevsize > windowStack.size()){
                return true;
            }
        }
        return false; //dummy return, replace this!
    }

    /**
     *  Gets an iterator for the stack of windows.
     *  Windows are returned from bottom to top.
     *
     *  @return the iterator requested
     */
    public Iterator<Window> windows() {
        //Note that this method uses your linked list!
        //so if the iterator doesn't work, that's on you...

        return new Iterator<>() {
            /**
             *  The current node pointed to by the
             *  iterator (containing the next value
             *  to be returned).
             */
            private Node<Window> current = getTail();

            /**
             * {@inheritDoc}
             */
            @Override
            public Window next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                Window ret = current.data;
                current = current.prev;
                return ret;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasNext() {
                return (current != null);
            }
        };
    }

    /**
     * Method to sort the windows in the list by their size.
     */
    public void sortSize() {
        //Sorts the windows in the stack by their area (length x width).
        //MOST of this is done for you, but you still need to assign
        //the returned head and tail back.

        //unselect the top window
        this.getHead().data.setSelected(false);

        //create a way to compare windows by area
        Comparator<Window> comp = new Comparator<>() {
            public int compare(Window w1, Window w2) {
                return (w1.getWidth()*w1.getHeight())-(w2.getWidth()*w2.getHeight());
            }
        };

        //create a pair of nodes to pass into the sort function
        ThreeTenLinkedList.NodePair<Window> pair = new ThreeTenLinkedList.NodePair<>(getHead(), getTail());

        //call the sort function with the comparator
        ThreeTenLinkedList.NodePair<Window> ret = ThreeTenLinkedList.sort(pair, comp);

        //make the returned list the head and tail of this list
        //this is for PART 5 of the project... don't try to do this
        //before you complete ThreeTenLinkedList.sort()!
        //< YOUR_CODE_HERE > U MIGHT NOT NEED TO ADD CODE SINCE UR sort method returns the new head n tail pair
        //BUT ASK A TA TO BE SURE
        pair.head = ret.head;
        pair.tail = ret.tail;
        //re-select the top of the stack
        this.getHead().data.setSelected(true);
    }

    /**
     * Method to sort the windows in the list by their location.
     */
    public void sortLoc() {
        //Sorts the windows in the stack by their upper left
        //corner loction. Right things (bigger-X) are on top
        //of left things (smaller-X). Tie-breaker: lower
        //things (bigger-Y) top of  higher things (smaller-Y).

        //This should use your ThreeTenLinkedList.sort() method you
        //write in Part 5, so don't do this until you have (a) read
        //part 5, (b) looked at the example in sortSize() above, and
        //(c) are sure you understand comparators.

        //O(n^2)
        this.getHead().data.setSelected(false);
        Comparator<Window> comp = new Comparator<>() {
            public int compare(Window w1, Window w2) {
                if(w1.getUpperLeftX()!=w2.getUpperLeftX()){
                    return (w2.getUpperLeftX()-w1.getUpperLeftX());
                }
                else {
                    return (w2.getUpperLeftY()-w1.getUpperLeftY());
                }
            }
        };

        ThreeTenLinkedList.NodePair<Window> pair = new ThreeTenLinkedList.NodePair<>(getHead(), getTail());

        //call the sort function with the comparator
        ThreeTenLinkedList.NodePair<Window> ret = ThreeTenLinkedList.sort(pair, comp);
        //U MIGHT NEED TO ADD SOMETHING AT THE VERY END. REFER TO SortSize above AND ASK TA
        pair.head = ret.head;
        pair.tail = ret.tail;
        this.getHead().data.setSelected(true);

    }

}


