// TO DO: add your implementation and JavaDocs.

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The 310 Hashset.
 * @author Akram Beshir
 * @param <T> generic of T type
 */
class ThreeTenHashSet<T> {
    // This is the class that you need to write to implement a set
    // using a hash table with _separate chaining_.

    // Underlying storage table -- you MUST use this for credit!
    // Do NOT change the name or type
    /**
     * hashtable.
     */
    private LinkedList<T>[] table;
    /**
     * hashtable for rehash.
     */
    private LinkedList<T>[] tabnew;

    // ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
    /**
     * number of items.
     */
    private int numofitems;
    /**
     * HashSet Storage.
     */
    private int storage;
    /**
     * the load.
     */
    private int load;
    //private T[] give;
    //private SimpleList<T> give;
    /**
     * an object.
     */
    private Object an;
    //private ThreeTenHashSet<T> a;

    /**
     * ThreeTenHashSet constructor.
     * @param initLength the storage of the hashset
     */
    @SuppressWarnings("unchecked")
    public ThreeTenHashSet(int initLength){
        // Create a hash table where the storage is with initLength
        // Initially the table is empty
        // You can assume initLength is >= 2

        // O(1)
        table = (LinkedList<T>[]) new LinkedList[initLength];//.get(initLength);
        numofitems = 0;
        storage = initLength;
    }

    /**
     * capacity of hashset.
     * @return capacity
     */
    public int capacity() {
        // return the storage length
        // O(1)

        return storage; //default return; change or remove as needed
    }

    /**
     * the number of items in the table.
     * @return size
     */
    public int size() {
        // return the number of items in the table
        // O(1)

        return numofitems; //default return; change or remove as needed
    }

    /**
     * Add an item to the set.
     * @param value item to be added
     * @return if the item was added
     */
    public boolean add(T value) {
        // Add an item to the set
        // - return true if you successfully add value;
        // - return false if the value can not be added
        //    (i.e. the value already exists or is null)

        // NOTES:
        // - Always add value to the tail of the chain.
        // - If load of table is at or above 2.0, rehash() to double the length.

        // Time complexity not considering rehash():
        // O(N) worst case, where N is the number of values in table
        // O(N/M) average case where N is the number of values in table and M is the table length
        if(value==null){
            return false;
        }
        load = numofitems/storage;
        if(load>=2){
            rehash(storage*2);
        }
        int xhc = value.hashCode() % storage;
        for(int i = 0; i < storage; i++) {
            if(table[i]==null){
                ;
            }
            else{
                Iterator ab = table[i].iterator();
                while (ab.hasNext() == true) {
                    if(ab.next().equals(value)){
                        return false;
                    }
                }
            }
        }
        if(xhc<0){
            xhc = xhc * -1;
        }
        if(table[xhc]==null){
            table[xhc] = new LinkedList<>();
        }
        table[xhc].addLast(value);
        numofitems++;
        return true; //default return; change or remove as needed
    }

    /**
     * removes a value from the set.
     * @param value the value to be removed
     * @return if the value was removed
     */
    public boolean remove(T value) {
        // Removes a value from the set
        // - return true if you remove the item
        // - return false if the item is not present

        // O(N) worst case, where N is the number of values in table
        // O(N/M) average case where N is the number of values in table and M is the table length
        if(value==null){
            return false;
        }
        for(int i = 0; i < storage; i++) {
            if(table[i]==null){
                ;
            }
            else{
                Iterator ab = table[i].iterator();
                while (ab.hasNext() == true) {
                    if(ab.next().equals(value)){
                        table[i].remove(value);
                        numofitems--;
                        return true;
                    }
                }
            }
        }
        return false; //default return; change or remove as needed
    }

    /**
     * Checks for a value in the set.
     * @param value the value to be checked
     * @return Return whether the value is in the set
     */
    public boolean contains(T value) {
        // Return true if value is in the set
        // Return false otherwise

        // O(N) worst case, where N is the number of values in table
        // O(N/M) average case where N is the number of values in table and M is the table length
        if(value==null){
            return false;
        }
        for(int i = 0; i < storage; i++) {
            if(table[i]==null){
                ;
            }
            else{
                Iterator ab = table[i].iterator();
                while (ab.hasNext() == true) {
                    if(ab.next().equals(value)){
                        return true;
                    }
                }
            }
        }
        return false; //default return; change or remove as needed
    }

    /**
     * Gets a value from the set.
     * @param value The value we want to get
     * @return Return the item if found or null if value is not present
     */
    public T get(T value) {
        // Return null if value is not present in set.
        // Return the item _FROM THE HASH TABLE_ if it was found
        //  - do not return the incoming parameter, while "equivalent" they
        // may not be the same)

        // O(N) worst case, where N is the number of values in table
        // O(N/M) average case where N is the number of values in table and M is the table length


        // NOTE: HashMap uses a ThreeTenHashSet of Pair<Key,Value>. In that class,
        // this method is used in the following way:
        //
        // - HashMap passes in a Pair<Key,Value> to search for
        // - The key is "real", the value may be a "dummy" or null
        // - The Pair<Key,--> passed in and the Pair<Key,Value> in the hash table
        //   will match with .equals() -- see equals() in the Pair class
        // - If this method finds the Pair<Key,-->, the returned value must be the
        //   **actual** hash table entry, which includes both matching key and a valid
        //   non-null value.
        //
        // Because of this, get() in this class need to be careful too, and it *must*
        // return the value from the hash table and not the parameter.
        if(value==null){
            return null;
        }
        for(int i = 0; i < storage; i++) {
            if(table[i]==null){
                ;
            }
            else{
                Iterator ab = table[i].iterator();
                while (ab.hasNext() == true) {
                    an = ab.next();
                    if(an.equals(value)){
                        return (T) an;
                    }
                }
            }
        }
        return null; //default return; change or remove as needed
    }

    /**
     * Rehash to table size newCapacity.
     * @param newCapacity the new capacity
     * @return If the rehash was a success
     */
    @SuppressWarnings("unchecked")
    public boolean rehash(int newCapacity) {
        // Rehash to table size newCapacity
        // - If the new capacity is no greater than the current capacity,
        //   do not rehash and return false;
        // - otherwise, return true after resizing

        // You can assume the newCapacity is always < Integer.MAX_VALUE - 50.

        // O(N+M) where N is the number of values in table and M is the table size
        // Hint: Take advantage of the iterator of SimpleList to meet big-O requirements.
        if(newCapacity<=storage){
            return false;
        }
        tabnew = (LinkedList<T>[]) new LinkedList[newCapacity];
        for(int i = 0; i < storage; i++) {
            if(table[i]==null){
                ;
            }
            else{
                Iterator ab = table[i].iterator();
                while (ab.hasNext() == true) {
                    /*if(ab.next().equals(value)){
                        return false;
                    }*/
                    an = ab.next();
                    int xhc = an.hashCode() % newCapacity;
                    if(xhc<0){
                        xhc = xhc * -1;
                    }
                    if(tabnew[xhc]==null){
                        tabnew[xhc] = new LinkedList<>();
                    }
                    tabnew[xhc].addLast((T) an);
                }
            }
        }
        table = tabnew;
        tabnew = null;
        storage = newCapacity;
        return true; //default return; change or remove as needed

    }

    // Provided: do not change but you will need to add JavaDoc

    /**
     * String representation of ThreeTenHashSet.
     * @return String representation of ThreeTenHashSet
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("ThreeTenHashSet (non-empty entries):\n");
        for(int i = 0; i < table.length; i++) {
            if(table[i] != null && table[i].size()!=0) {
                s.append(i);
                s.append(" :");
                s.append(table[i]);
                s.append("\n");
            }
        }
        return s.toString().trim();
    }

    // Provided: do not change but you will need to add JavaDoc

    /**
     * A better String representation of ThreeTenHashSet.
     * @return better String representation of ThreeTenHashSet
     */
    public String toStringDebug() {
        StringBuilder s = new StringBuilder("ThreeTenHashSet (all entries):\n");
        for(int i = 0; i < table.length; i++) {
            s.append(i);
            s.append(" :");
            s.append(table[i]);
            s.append("\n");
        }
        return s.toString().trim();
    }

    // Provided: do not change but you will need to add JavaDoc

    /**
     * return all items in set as a list.
     * @return all items in set as a list
     */
    public LinkedList<T> allValues(){
        // return all items in set as a list
        LinkedList<T> all = new LinkedList<>();
        for(int i = 0; i < table.length; i++) {
            if (table[i]!=null){
                for (T value: table[i])
                    all.addLast(value);
            }
        }
        return all;
    }

    //----------------------------------------------------
    // example testing code... make sure you pass all ...
    // and edit this as much as you want!
    //----------------------------------------------------

    /**
     * Main method.
     * @param args args
     */
    public static void main(String[] args) {

        // Again, these are limited sample tests.  Showing all "yays"
        // does NOT guarantee your code is 100%.
        // You must do more testing.

        ThreeTenHashSet<String> names = new ThreeTenHashSet<>(5);

        //basic table w/o collision: add / size / capacity
        if(names.add("Alice") && names.add("Bob") && !names.add("Alice")
                && names.size() == 2 && names.capacity() == 5) 	{
            System.out.println("Yay 1");
        }
        //System.out.println(names.toString());
        //System.out.println("-----------------------");
        //System.out.println(names.toStringDebug());
        //System.out.println("-----------------------");

        //remove / contains / get
        if(!names.remove("Alex") && names.remove("Bob") && names.contains("Alice")
                && !names.contains("Bob") && names.get("Bob")==null) {
            System.out.println("Yay 2");
        }
        //System.out.println(names.toString());
        //System.out.println("-----------------------");

        //table with collision
        ThreeTenHashSet<Integer> nums = new ThreeTenHashSet<>(5);
        for(int i = 0; i <7 ; i++) {
            nums.add(i);
        }
        String expectedString =
                "ThreeTenHashSet (non-empty entries):\n0 :[0,5]\n1 :[1,6]\n2 :[2]\n3 :[3]\n4 :[4]";
        String allValueString = "[0,5,1,6,2,3,4]";
        if (nums.size()==7 && nums.toString().equals(expectedString)
                && nums.allValues().toString().equals(allValueString)){
            System.out.println("Yay 3");
        }
        //System.out.println(nums.size());
        //System.out.println(nums.toString() + "\n==\n" + expectedString + nums.toString().equals(expectedString));
        //System.out.println(nums.allValues().toString().equals(allValueString));
        //System.out.println(nums.allValues().toString() + "\n==\n" + allValueString + nums.allValues().toString().equals(allValueString));
        //rehash
        String rehashedString =
                "ThreeTenHashSet (non-empty entries):\n0 :[0]\n1 :[1]\n2 :[2]\n3 :[3]\n4 :[4]\n5 :[5]\n6 :[6]";
        if (!nums.rehash(3) && nums.rehash(11) && nums.capacity()==11
                && nums.toString().equals(rehashedString)){
            System.out.println("Yay 4");
        }
        //System.out.println(nums.toString());


    }
}

