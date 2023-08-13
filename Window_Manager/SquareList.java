//TODO: Complete java docs and code in missing spots.
//Missing spots are marked by < YOUR_CODE_HERE >.
//Do NOT edit any other parts of the code.

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 *  A list of squares within a single window.
 *
 *  <p>Adapterion of Nifty Assignment (http://nifty.stanford.edu/) by
 *  Mike Clancy in 2001. Original code by Mike Clancy. Updated Fall
 *  2022 by K. Raven Russell.</p>
 * @author Katherine Russell
 * @author Akram Beshir
 */
public class SquareList {
    //You'll need some instance variables probably...
    /**
     * A private square list.
     */
    private ThreeTenLinkedList<Square> squareList;

    /**
     *  Initialize an empty list of squares.
     */
    public SquareList() {
        //Any initialization code you need.

        //O(1)

        squareList = new ThreeTenLinkedList<Square>();
    }

    /**
     * Gets the first node of the list.
     * @return the first node of the list
     */
    public Node<Square> getHead() {
        //Returns the head of the list of squares.

        //O(1)

        //We will use this method to test your
        //linked list implementaiton of this
        //class, so whether or not you are using
        //the generic linked list class or bare
        //nodes, you must still be able to return
        //the appropriate head of the list.

        return squareList.getFirst();
    }

    /**
     * Gets the last node of the list.
     * @return the last node of the list
     */
    public Node<Square> getTail() {
        //Returns the tail of the list of squares.

        //O(1)

        //We will use this method to test your
        //linked list implementaiton of this
        //class, so whether or not you are using
        //the generic linked list class or bare
        //nodes, you must still be able to return
        //the appropriate tail of the list.

        return squareList.getLast();
    }

    /**
     * Gives length of the list.
     * @return amount of items
     */
    public int numSquares() {
        //Gets the number of squares in the list.

        //O(1)

        return squareList.size();
    }

    /**
     * Add a square to the end of the list.
     * @param sq The square to be added
     */
    public void add(Square sq) {
        //Add a square to the list. Newly added squares
        //should be at the back end of the list.

        //O(1)

        //throw IllegalArgumentException for invalid sqares
        if(sq==null){
            throw new IllegalArgumentException();
        }
        squareList.append(sq); //ASK TA WHAT IS MEANT BY INVALID SQUARES
    }

    /**
     * Deletes squares within a coordinate.
     * @param x x coordinate
     * @param y y coordinate
     * @return Whether or not squares were deleted
     */
    public boolean handleClick (int x, int y) {
        //Deletes all squares from the list that contain the
        //position (x,y). Returns true if any squares get
        //deleted and returns false otherwise.

        //Returns true if any squares were deleted.

        //O(n) where n is the size of the list of squares
        if(squareList.size()==0){ //empty window
            return false;
        }
        int originalsize = squareList.size();
        Node<Square> iterate = squareList.getFirst();
        while(iterate!=null){ //iterate thru the list of squares to c if it contains the point
            if(iterate.data.contains(x, y) == true){ //if the square contains the point
                squareList.remove(iterate.data); //remove the square
                iterate = iterate.next;
            }
            else{
                iterate = iterate.next; //if not, just keep going thru the list
            }
        }
        if(originalsize > squareList.size()){
            return true;
        }
        return false;
    }

    /**
     *  Gets an iterator for the list of squares.
     *  Squares are returned in the order added.
     *
     *  @return the iterator requested
     */
    public Iterator<Square> elements() {
        //Note that this method uses your linked list!
        //so if the iterator doesn't work, that's on you...

        return new Iterator<>() {
            /**
             *  The current node pointed to by the
             *  iterator (containing the next value
             *  to be returned).
             */
            private Node<Square> current = getHead();

            /**
             * {@inheritDoc}
             */
            @Override
            public Square next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                Square ret = current.data;
                current = current.next;
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
     * Method to sort the squares in the square list by their id.
     */
    public void sortCreation() {
        //Sorts the squares in the window by their creation time
        //(lower ids were created first). This should use your
        //ThreeTenLinkedList.sort() method you write in Part 5,
        //so don't do this until you have (a) read part 5,
        //(b) looked at the example in WindowStack, and (c) are
        //sure you understand comparators.

        //O(n^2)


        //create a way to compare windows by area
        Comparator<Square> comp = new Comparator<>() {
            public int compare(Square s1, Square s2) {
                return (s1.id())-(s2.id());
            }
        };

        ThreeTenLinkedList.NodePair<Square> pair = new ThreeTenLinkedList.NodePair<>(getHead(), getTail());

        ThreeTenLinkedList.NodePair<Square> ret = ThreeTenLinkedList.sort(pair, comp);
        pair.head = ret.head;
        pair.tail = ret.tail;
    }

    /**
     * Method to sort the squares in the square list by their location.
     */
    public void sortLoc() {
        //Sorts the squares in the window by their location
        //in the window. Same rules as sorting the windows
        //in WindowStack. This should use your
        //ThreeTenLinkedList.sort() method you write in Part 5,
        //so don't do this until you have (a) read part 5,
        //(b) looked at the example in WindowStack, and (c) are
        //sure you understand comparators

        //O(n^2)

        Comparator<Square> comp = new Comparator<>() {
            public int compare(Square s1, Square s2) {
                if(s1.getUpperLeftX()!=s2.getUpperLeftX()){
                    return (s1.getUpperLeftX()-s2.getUpperLeftX());
                }
                else { //Exits to this branch if there's a tiebreaker for x-coordinates
                    return (s1.getUpperLeftY()-s2.getUpperLeftY());
                }
            }
        };

        ThreeTenLinkedList.NodePair<Square> pair = new ThreeTenLinkedList.NodePair<>(getHead(), getTail());

        //call the sort function with the comparator
        ThreeTenLinkedList.NodePair<Square> ret = ThreeTenLinkedList.sort(pair, comp);
        pair.head = ret.head;
        pair.tail = ret.tail;
    }
}

