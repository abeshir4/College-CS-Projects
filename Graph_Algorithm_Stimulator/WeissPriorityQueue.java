//TODO:
//  (1) Update this code to meet the style and JavaDoc requirements.
//			Why? So that you get experience with the code for a heap!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards). We've done this for you in
//			WeissCollection and WeissAbstractCollection.
//  (2) Implement getIndex() method and the related map integration
//			 -- see project description
//  (3) Implement update() method -- see project description

//import java.util.Arrays;
import java.util.Iterator;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

//You may use the JCF HashMap or the HashMap from Project 3
//that depends on your ThreeTenHashSet.

//To use the JCF class, uncomment this line:
//import java.util.HashMap;

//To use your code, just copy over HashMap and ThreeTenHashSet
//from Project 3 and DON'T uncomment the line above.


/**
 * PriorityQueue class implemented via the binary heap.
 * From your textbook (Weiss)
 * @param <AnyT> anytpe
 */
public class WeissPriorityQueue<AnyT> extends WeissAbstractCollection<AnyT>
{
    //you may not have any other class variables, only this one
    //if you make more class variables your priority queue class
    //will receive a 0, no matter how well it works
    /**
     * Default capacity.
     */
    private static final int DEFAULT_CAPACITY = 100;

    //you may not have any other instance variables, only these four
    //if you make more instance variables your priority queue class
    //will receive a 0, no matter how well it works
    /**
     * Number of elements in the heap.
     */
    private int currentSize;   // Number of elements in heap
    /**
     * The heap in array form.
     */
    private AnyT [ ] array; // The heap array
    /**
     * The comparator.
     */
    private Comparator<? super AnyT> cmp;
    /**
     * The Hashmap.
     */
    private HashMap<AnyT, Integer> indexMap;
    //--------------------------------------------------------
    // testing code goes here... edit this as much as you want!
    //--------------------------------------------------------

    /**
     * The main function.
     * @param args args
     */
    public static void main(String[] args) {
        /**
         * Student.
         */
        class Student {
            String gnum;
            String name;
            Student(String gnum, String name) { this.gnum = gnum; this.name = name; }

            /**
             * The equals method.
             * @param o object
             * @return if it is equal
             */
            public boolean equals(Object o) {
                if(o instanceof Student) return this.gnum.equals(((Student)o).gnum);
                return false;
            }
            public String toString() { return name + "(" + gnum + ")"; }
            public int hashCode() { return gnum.hashCode(); }
        }

        Comparator<Student> comp = new Comparator<>() {
            public int compare(Student s1, Student s2) {
                return s1.name.compareTo(s2.name);
            }
        };


        //TESTS FOR INDEXING -- you'll need more testing...

        WeissPriorityQueue<Student> q = new WeissPriorityQueue<>(comp);
        q.add(new Student("G00000000", "Robert"));
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //-1, no index
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //1, at root
        System.out.println();

        q.add(new Student("G00000001", "Cindi"));
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //1, at root
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //2, lower down
        System.out.println();

        q.remove(); //remove Cindi
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //-1, no index
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //1, at root
        System.out.println();
        System.out.println();


        //TESTS FOR UPDATING -- you'll need more testing...

        q = new WeissPriorityQueue<>(comp);
        q.add(new Student("G00000000", "Robert"));
        q.add(new Student("G00000001", "Cindi"));

        for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
        System.out.println();
        for(Student s : q) System.out.print(s.name + " "); //Cindi Robert
        System.out.println();

        Student bobby = new Student("G00000000", "Bobby");
        q.update(bobby);

        for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
        System.out.println();
        for(Student s : q) System.out.print(s.name + " ");  //Bobby Cindi
        System.out.println();

        bobby.name = "Robert";
        q.update(bobby);

        for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
        System.out.println();
        for(Student s : q) System.out.print(s.name + " "); //Cindi Robert
        System.out.println();
        //System.out.println();

        //you'll need more testing...
    }

    //you implement this

    /**
     * The index of a certain item.
     * @param x The item
     * @return The index of that item
     */
    public int getIndex(AnyT x) {
        //average case O(1)

        //returns the index of the item in the heap,
        //or -1 if it isn't in the heap

        if(indexMap.contains(x)){
            return indexMap.getValue(x);
        }
        return -1;
    }

    //you implement this

    /**
     * The updating of a certain item.
     * @param x The item to be updated
     * @return if the updating was a success
     */
    public boolean update(AnyT x) {
        //O(lg n) average case
        //or O(lg n) worst case if getIndex() is guarenteed O(1)
        if(getIndex(x)==-1){
            return false;
        }
        AnyT an;
        if(array[getIndex(x)].equals(x)){

            array[getIndex(x)] = x;

            indexMap.update(x, getIndex(x));

            LinkedList<AnyT> hell = indexMap.getKeys();
            //indexMap.getKeys()

            hell.sort(cmp);



            array[0] = hell.get(0);
            Iterator travel = hell.iterator();
            int loc = 0;
            while(travel.hasNext()){
                loc++;
                an = (AnyT) travel.next();
                array[loc] = an;
                indexMap.update(an, loc);
            }
        }
        return true; //dummy return, make sure to replace this!
    }

    /**
     * Construct an empty PriorityQueue.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( )
    {
        currentSize = 0;
        cmp = null;
        array = (AnyT[]) new Object[ DEFAULT_CAPACITY + 1 ];
        indexMap = new HashMap<AnyT, Integer>(16);
    }

    /**
     * Construct an empty PriorityQueue with a specified comparator.
     * @param c
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( Comparator<? super AnyT> c )
    {
        currentSize = 0;
        cmp = c;
        array = (AnyT[]) new Object[ DEFAULT_CAPACITY + 1 ];
        indexMap = new HashMap<AnyT, Integer>(16);
    }


    /**
     * Construct a PriorityQueue from another Collection.
     * @param coll
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( WeissCollection<? extends AnyT> coll )
    {
        cmp = null;
        currentSize = coll.size( );
        array = (AnyT[]) new Object[ ( currentSize + 2 ) * 11 / 10 ];
        indexMap = new HashMap<AnyT, Integer>(16);

        int i = 1;
        for( AnyT item : coll )
            array[ i++ ] = item;
        buildHeap( );
    }

    /**
     * Compares lhs and rhs using comparator if
     * provided by cmp, or the default comparator.
     * @param lhs lhs
     * @param rhs rhs
     * @return int
     */
    @SuppressWarnings("unchecked")
    private int compare( AnyT lhs, AnyT rhs )
    {
        if( cmp == null )
            return ((Comparable)lhs).compareTo( rhs );
        else
            return cmp.compare( lhs, rhs );
    }

    /**
     * Adds an item to this PriorityQueue.
     * @param x any object.
     * @return true.
     */
    public boolean add( AnyT x )
    {
        if( currentSize + 1 == array.length )
            doubleArray( );

        // Percolate up
        int hole = ++currentSize;
        array[ 0 ] = x;

        for( ; compare( x, array[ hole / 2 ] ) < 0; hole /= 2 ) {
            array[ hole ] = array[ hole / 2 ];
        }

        array[ hole ] = x;
        indexMap.add(x, hole);
        //System.out.println(indexMap.getKeys() + ": KEYS/ add");
        for(int i = 0; i < currentSize; i++){
            indexMap.update(indexMap.getKeys().get(i), i+1);
        }

        return true;
    }

    /**
     * Returns the number of items in this PriorityQueue.
     * @return the number of items in this PriorityQueue.
     */
    public int size( )
    {
        return currentSize;
    }

    /**
     * Make this PriorityQueue empty.
     */
    public void clear( )
    {
        currentSize = 0;
    }

    /**
     * Returns an iterator over the elements in this PriorityQueue.
     * The iterator does not view the elements in any particular order.
     * @return iterator
     */
    public Iterator<AnyT> iterator( )
    {
        return new Iterator<AnyT>( )
        {
            int current = 0;

            public boolean hasNext( )
            {
                return current != size( );
            }

            @SuppressWarnings("unchecked")
            public AnyT next( )
            {
                if( hasNext( ) )
                    return array[ ++current ];
                else
                    throw new NoSuchElementException( );
            }

            public void remove( )
            {
                throw new UnsupportedOperationException( );
            }
        };
    }

    /**
     * Returns the smallest item in the priority queue.
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public AnyT element( )
    {
        if( isEmpty( ) )
            throw new NoSuchElementException( );
        return array[ 1 ];
    }

    /**
     * Removes the smallest item in the priority queue.
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public AnyT remove( )
    {
        AnyT minItem = element( );
        array[ 1 ] = array[ currentSize-- ];
        percolateDown( 1 );
        indexMap.remove(minItem);
        //indexMap.add(x, hole);
        //System.out.println(indexMap.getKeys() + ": KEYS/ rem");
        //System.out.println("curr: " + currentSize);
        /*for(int i = 0; i < indexMap.getKeys().size(); i++){
            System.out.print("Key: " + indexMap.getKeys().get(i) + "; Index: ");
            System.out.println(indexMap.getValue(indexMap.getKeys().get(i)));
        }*/
        for(int i = 0; i < currentSize; i++){
            //System.out.println(indexMap.);
            indexMap.update(indexMap.getKeys().get(i), i+1);
        }
        return minItem;
    }


    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap( )
    {
        for( int i = currentSize / 2; i > 0; i-- )
            percolateDown( i );
    }

    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown( int hole )
    {
        int child;
        AnyT tmp = array[ hole ];

        for( ; hole * 2 <= currentSize; hole = child )
        {
            child = hole * 2;
            if( child != currentSize &&
                    compare( array[ child + 1 ], array[ child ] ) < 0 )
                child++;
            if( compare( array[ child ], tmp ) < 0 ) {
                array[ hole ] = array[ child ];
            }
            else
                break;
        }
        array[ hole ] = tmp;
    }

    /**
     * Internal method to extend array.
     */
    @SuppressWarnings("unchecked")
    private void doubleArray( )
    {
        AnyT [ ] newArray;

        newArray = (AnyT []) new Object[ array.length * 2 ];
        for( int i = 0; i < array.length; i++ )
            newArray[ i ] = array[ i ];
        array = newArray;
    }
}

