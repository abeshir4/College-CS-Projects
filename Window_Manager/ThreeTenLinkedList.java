//TODO: Linked list implementation (optional)
//      JavaDocs (not optional)
//      Static sorting methods (not optional)

import java.util.Comparator;

/**
 * A linked list class that will be used for our square list and window stack.
 * @author Katherine Russell
 * @author Akram Beshir
 * @param <T> generic type T
 */
class ThreeTenLinkedList<T> {
    //You may, but are not required to, implement some or
    //all of this generic linked list class to help you with
    //this project. If you do, you MUST use the provided
    //Node class for this linked list class. This means
    //that it must be a doubly linked list (and links in
    //both directions MUST work).

    //Alternatively, you may implement this project using
    //only the Node class itself (i.e. use "bare nodes"
    //in the classes that require linked lists).

    //Either way, you MUST do all your own work. Any other
    //implementations you have done in the past, anything
    //from the book, etc. should not be in front of you,
    //and you certainly shouldn't copy and paste anything
    //from any other source.

    //This is the only class where you are allowed to
    //implement public methods.

    //In "Part 5" of the project, you will also be implementing
    //the following static methods:
    /**
     * the head of the list.
     */
    private Node<T> head;
    /**
     * the tail of the list.
     */
    private Node<T> tail;
    /**
     * the length of the list.
     */
    private int count;

    /**
     * The linked list constructor.
     */
    public ThreeTenLinkedList(){
        head = null;
        tail = null;
        count = 0;
    }

    /**
     * Method for keeping track of the list length.
     * @return The length of the list
     */
    public int size(){

        return count;
    }

    /**
     * Adds an item to the end of the list.
     * @param item THe item we want to add
     */
    public void append(T item) {
        Node<T> addNode = new Node<>(item);
        if (count == 0) { //FOR THE FIRST ITEM TO BE ADDED
            head = addNode; //SET THE head & tail TO POINT TO THAT NODE
            tail = addNode;
            head.prev = null;
            tail.next = null;
        }
        else {
            tail.next = addNode; //THE LAST ITEMS NEXT REFERENCE SHOULD POINT TO THE NEWLY ADDED NODE
            addNode.prev = tail;
            tail = addNode; //SET THE TAIL TO BE THE NEW ADDED NODE
            tail.next = null;
        }
        count++; //INCREMENT COUNT
    }

    /**
     * Adds an item to the front of the list.
     * @param item the item we want to add
     */
    public void addFront(T item){
        Node<T> addNode = new Node<>(item);
        if (count == 0) { //FOR THE FIRST ITEM TO BE ADDED
            head = addNode; //SET THE head & tail TO POINT TO THAT NODE
            tail = addNode;
            head.prev = null;
            tail.next = null;
        }
        else {
            head.prev = addNode; //THE FIRST ITEMS PREVIOUS REFERENCE SHOULD POINT TO THE NEWLY ADDED NODE
            addNode.next = head;
            head = addNode; //SET THE HEAD TO BE THE NEW ADDED NODE
            head.prev = null;
        }
        count++; //INCREMENT COUNT
    }

    /**
     * Keeps track of the head.
     * @return The front node of the list
     */
    public Node<T> getFirst(){
        Node<T> getNode = head;
        return getNode; //RETURN the front node
    }

    /**
     * Keeps track of the tail.
     * @return The back node of the list
     */
    public Node<T> getLast(){
        Node<T> getNode = tail;
        return getNode; ////RETURN the back node
    }

    /**
     * Removes an item from the list.
     * @param item The item we want to delete
     */
    public void remove(T item){
        if (item == head.data){ //IF THE ITEM TO BE REMOVED IS THE FIRST ONE IN THE LIST
            head = head.next; //THE NEW HEAD WILL BE THE ITEM AFTER THE FIRST ONE
            if (count==1){
                tail = null;
            }
            else{
                head.prev = null;
            }
        }
        else{
            Node<T> itr = head; //AN ITERATOR NODE TO GO THRU ALL NODES IN THE LIST TO FIND THE ITEM
            while (itr.next.next != null && itr.next.data != item) {
                itr = itr.next; //Goes to the next node til the item is found
            }
            itr.next = itr.next.next;
            if (item == tail.data){ //IF ITEM IS FOUND AT THE LAST NODE
                tail = itr;
            }
            else{
                itr.next.prev = null;
                itr.next.prev = itr;
            }
        }
        count--; //decrement count
    }

    /**
     * Method to see if the list is sorted.
     * @param <X> generic type X
     * @param pairs head and tail pair
     * @param comp comparator used to determine if list is sorted
     * @return whether or not the list is sorted
     */
    static <X> boolean isSorted(NodePair<X> pairs, Comparator<X> comp) {
        //Determine if the provided list is sorted based on the comparator.

        //For an empty linked list (e.g. the head-tail pair contains two nulls)
        //return true (an empty list is sorted).

        //For a null comparator or null pairs, throw an IllegalArgumentException.

        //O(n)

        if(comp==null || pairs==null){
            throw new IllegalArgumentException();
        }
        if(pairs.head==null && pairs.tail==null){ //empty list
            return true;
        }
        if(pairs.head==pairs.tail){ //only one item in the list
            return true;
        }
        Node<X> itrtwo = pairs.head;
        while(itrtwo.next != null){ //iterate thru the list
            if(comp.compare(itrtwo.data, itrtwo.next.data)>0){ //compare two values in the list for right order
                return false;
            }
            else{
                itrtwo = itrtwo.next; //keep iterating if the list elements are in the right order
            }
        }
        return true;
    }

    /**
     * Method to see if the list is sorted.
     * @param <X> generic type X
     * @param pairs head and tail pair
     * @param comp comparator used to determine if list is sorted
     * @return the head and tail pair of the sorted list
     */
    static <X> NodePair<X> sort(NodePair<X> pairs, Comparator<X> comp) {

        //Using the comparator, sort the linked list. It is recommended that
        //you sort by moving *values* around rather than moving nodes.
        //Two simple sorting algorithms which will work well here (and
        //meet the big-O requirements if implemented correctly) are the
        //insertion sort (see textbook Ch8.3) and the selection sort.

        //Insertion sort quick summary: Go to each element in the linked list,
        //shift it "left" into the correct position.
        //Example: 1,3,0,2
        // 1 is at the start of the list, leave it alone
        // 3 is bigger than 1, leave it alone
        // 0 is smaller than 3, move it left: 1,0,3,2
        // 0 is smaller than 1, move it to the left: 0,1,3,2
        // 0 is at the start of the list, stop moving it left
        // 2 is smaller than 3, move it to the left: 0,1,2,3
        // 2 is bigger than 1, stop moving it to the left

        //Selection sort quick summary: Go to each index in the linked list,
        //find the smallest thing from that index and to the "right",
        //and swap it into that index.
        //Example: 1,3,0,2
        // index 0: the smallest thing from index 0 to the end is 0, swap it into the right place: 0,3,1,2
        // index 1: the smallest thing from index 1 to the end is 1, swap it into the right place: 0,1,3,2
        // index 2: the smallest thing from index 2 to the end is 2, swap it into the right place: 0,1,2,3
        // index 3: there is only one item from index 3 to the end, so this is in the right places

        //Regardless of the method you choose, your sort should be a stable sort.
        //This means that if there are two equal values, they do not change their
        //order relative to each other.
        //Example: 1, 2, 1
        //The first "1" (which I'll call "1a") should be sorted before
        //the second "1" (1b), so that the output is "1a, 1b, 2" and
        //never "1b, 1a, 2". The easiest way to test this is to put two
        //equal items in the list, sort, and confirm using == that the
        //correct object is in the correct place.

        //For an empty linked list (e.g. the head-tail pair contains two nulls)
        //return the original pairs back to the user.

        //For a null comparator or null pairs, throw an IllegalArgumentException.

        //O(n^2)

        if(comp==null || pairs==null){
            throw new IllegalArgumentException();
        }
        if(pairs.head==null && pairs.tail==null){
            return pairs; //DOUBLE CHECHK WITH TA ABOUT ORIGINAL PAIRS
        }
        if(pairs.head==pairs.tail){
            return pairs;
        }
        Node<X> cur = pairs.head;
        cur = cur.next;
        Node<X> dot = pairs.head;
        dot = dot.next;
        while(cur!=null){ //WHILE ITERATING THRU THE LIST
            X tmp = cur.data; //FOR EVERY ITEM IN THE LIST
            while(cur.prev!=null && comp.compare(tmp, cur.prev.data)<0){ //COMPARE THAT ITEM TO ALL ITEMS BEFORE IT
                cur.data = cur.prev.data; //MOVE VALUES IF NOT SORTED IN THE RIGHT ORDER
                cur= cur.prev;
            }
            cur.data = tmp;
            dot = dot.next;
            cur = dot; //HELPS KEEP TRACK OF WHERE WE ARE IN THE LINKED LIST
        }
        return pairs;
    }

    //Which uses the following nested class:

    /**
     * Nested class.
     * @param <Y> generic type Y
     */
    public static class NodePair<Y> {
        /**
         * The node pairs head.
         */
        public Node<Y> head;
        /**
         * The node pairs tail.
         */
        public Node<Y> tail;

        /**
         * The node pairs constructor.
         * @param head head of this node pair
         * @param tail tail of this node pair
         */
        public NodePair(Node<Y> head, Node<Y> tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    //You may also use the above nested class elsewhere in your
    //project if you'd find that helpful.
}

