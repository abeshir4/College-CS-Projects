// TO DO: add your implementation and JavaDocs.
/**
 * A class for performing operations with a Sorted Array.
 * @author Akram Beshir
 * @param <T> the generic array we'll be working with
 */
public class MySortedArray<T extends Comparable<T>> {

	//default initial capacity / minimum capacity
	/**
	 * minimum capacity.
	 */
	private static final int DEFAULT_CAPACITY = 2;
	
	//underlying array for storage -- you MUST use this for credit!
	//Do NOT change the name or type
	/**
	 * data array.
	 */
	private T[] data;
	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	/**
	 * placeholder array for expanding and shrinking.
	 */
	private T[] placeholder; //PLACEHOLDER ARRAY SO WE DON'T LOSE ANY VALUES FOR THE SHIFTED ARRAY
	/**
	 * placeholder array for the delete method.
	 */
	private T[] deletedItem; //AN ARRAY OF SIZE 1 TO STORE WHATEVER ITEM WE WANT TO DELETE
	/**
	 * size.
	 */
	private int count; //KEEPS TRACK OF THE AMOUNT OF THINGS BEING STORED IN  THE ARRAY
	/**
	 * capacity.
	 */
	private int storage; //KEEPS TRACK OF THE CAPACITY
	/**
	 * detects a duplicate.
	 */
	private boolean duplicate; //MAKES SURE IF THERE ARE ANY DUPLICTAE ITEMS IN THE ARRAY
	/**
	 * duplicate index.
	 */
	private int duplicateFound; //INDEX WHERE THE DUPLICATE WAS FOUND;
	/**
	 * index for the added value with duplicates.
	 */
	private int repeatedValueIndex; //INDEX WHERE THE REPEATED VALUE WILL BE ADDED
	/**
	 * index for the added value with no duplicates.
	 */
	private int addedValueIndex; //INDEX WHERE THE VALUE (NON-REPEATING) WILL BE ADDED
	/**
	 * checks if the value added is the max.
	 */
	private boolean max; //CHECKS IF THE VARIABLE TO BE ADDED IS THE MAX

	/**
	 * constructor that's referring to the array we'll be working with starting of with a capacity of 2.
	 */
	@SuppressWarnings("unchecked")
	public MySortedArray() {
		// Constructor	
		// Initial capacity of the storage should be DEFAULT_CAPACITY
		// Hint: Can't remember how to make an array of generic Ts? It's in the textbook...
		count = 0; //WHENEVER A NEW ARRAY IS CREATED, THERE ARE NO ITEMS IN IT
		data = (T[]) new Comparable[DEFAULT_CAPACITY];
		storage = DEFAULT_CAPACITY; //SETTING THE INITIAL CAPACITY OF THE STORAGE TO BE DEFAULT_CAPACITY
		//placeholder = (T[]) new Comparable[storage];
	}

	/**
         * constructor that's referring to the array we'll be working with.
         * @param initialCapacity the capacity that we'll work with
         */
	@SuppressWarnings("unchecked")
	public MySortedArray(int initialCapacity) {
		// Constructor
		// Initial capacity of the storage should be initialCapacity
		// Throw IllegalArgumentException if initialCapacity is smaller than 
		// 2 (which is the DEFAULT_CAPACITY). 
		// Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Capacity must be at least 2!"
		count = 0;
		if(initialCapacity < DEFAULT_CAPACITY){
			throw new IllegalArgumentException("Capacity must be at least 2!"); //INITIAL CAPACITY SHOULD BE AT LEAST 2
		}
		else{
			data = (T[]) new Comparable[initialCapacity]; 
		}
		storage = initialCapacity;
		//placeholder = (T[]) new Comparable[storage];
	}
	/**
         * Get the array size.
         * @return the array size
         */
	public int size() {	
		// Report the current number of elements
		// O(1)	
		return count; //SIZE

	}  
	/**
         * Get the capacity.
         * @return the array capacity
         */	
	public int capacity() { 
		// Report max number of elements before the next expansion
		// O(1)
		return storage; //CAPACITY
	}

	/**
         * Stores the value to be added in the sorted array.
         * @param value the value to be added
         */
	public void add(T value){
		// Insert the given value into the array and keep the array _SORTED_ 
		// in ascending order. 

		// If the array already contains value(s) that are equal to the new value,
		// make sure that the new value is added at the end of the group. Check examples
		// in project spec and main() below.
		
		// Remember to use .compareTo() to perform order/equivalence checking.
				
		// Note: You _can_ append an item (and increase size) with this method.
		// - You must call doubleCapacity() if no space is available. 
		// - Check below for details of doubleCapacity().
		// - For the rare case that doubleCapacity() fails to increase the max 
		//   number of items allowed, throw IllegalStateException.
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Cannot add: capacity upper-bound reached!"

		
		// Note: The value to be added cannot be null.
		// - Throw IllegalArgumentException if value is null. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Cannot add: null value!"

		// O(N) where N is the number of elements in the storage
		//placeholder = null;
		if((count == data.length)&&(count*2>Integer.MAX_VALUE)){ //IF THERE'S NO MORE SPACE IN THE ARRAY AND DOUBLING CAPACITY FAILS
			throw new IllegalStateException("Cannot add: capacity upper-bound reached!");                            //THINK ABOUT doubleCapacity()==false
		}
		else if(value==null){ //IF THE VALUE YOU WANT TO ADD IS NULL
			throw new IllegalArgumentException("Cannot add: null value");
		}
		else if((count == data.length)&&(count*2<=Integer.MAX_VALUE)){ //IF THERE'S NO MORE SPACE IN THE ARRAY AND THE UPPER-BOUND HASN'T BEEN REACHED YET
			doubleCapacity();
		}
		for(int i = 0; i<count; i++){
			if(value.compareTo(data[i])<0){ //IF YOU FIND A VALUE IN THE SORTED ARRAY THAT IS GREATER THAN THE ONE YOU'RE TRYING TO ADD
				max = false; //THEN THE VALUE YOU'RE ADDING CAN'T BE THE LARGEST ITEM IN THE SORTED ARRAY
				duplicate = false;
				addedValueIndex = i; //SET THIS TO BE THE INDEX WHERE YOU MADE THIS DISCOVERY
				break; //NO MORE LOOPING IS NECESSARY WHEN WE FIND THAT INDEX
			}
			else if(value.compareTo(data[i])==0 && value.compareTo(data[count-1])==0){ //IF THE VALUE YOU'RE ADDING IS ALREADY IN THE SORTED ARRAY AND IS THE SAME AS THE LAST ITEM
				max = true; //THEN IT'S THE LARGEST ITEM IN THE SORTED ARRAY
				duplicate = true; //AND THERE'S A DUPLICATE OF THAT ITEM
				repeatedValueIndex = count; //SET THIS TO BE THE INDEX AFTER THE LAST ITEM
				break; //NO MORE LOOPING IS NECESSARY WHEN WE FIND THAT INDEX
			}
			else if(value.compareTo(data[i])==0){ //IF THE VALUE YOU'RE ADDING IS IN THE SORTED ARRAY AND ISN'T THE SAME AS THE LAST ITEM
				max = false; //THEN IT CAN'T BE THE MAX
				duplicate = true; //BUT A DUPLICATE STILL EXISTS
				duplicateFound = i; //SET THIS TO BE THE INDEX WHERE YOU MADE THIS DISCOVERY
				break; //NO MORE LOOPING IS NECESSARY WHEN WE FIND THAT INDEX
			}
			else if(value.compareTo(data[count-1])>=0){ //IF THE LAST ITEM IN THE SORTED ARRAY IS LESS THAN THE VALUE YOU WANT TO ADD
				max = true; //THEN YOUR VALUE IS THE MAX
				duplicate = false;
				addedValueIndex = count; //SET THIS TO BE THE INDEX AFTER THE LAST ITEM
				break; //NO MORE LOOPING IS NECESSARY WHEN WE FIND THAT INDEX
			}
		}
		if(max==false && duplicate==true){ //DOUBLE CHECK THE BIG-O FOR THIS
			for(int i = duplicateFound;i<count;i++){
				if(value.compareTo(data[i])!=0){ //AT WHATEVER INDEX YOU DON'T FIND ANY MORE DUPLICATES
					repeatedValueIndex = i;  //SAVE THAT INDEX
					break;
				}
			}
			placeholder = (T[]) new Comparable[storage]; //PLACEHOLDER ARRAY
			for(int i = 0;i<count+1;i++){
				if(i>repeatedValueIndex){
					placeholder[i] = data[i-1]; //ADDING ALL THE VALUES GREATER THAN OUR VALUE AND SHIFTING THEM BY AN EXTRA SPACE
				}
				else if(i==repeatedValueIndex){ //ADDING THE NEW VALUE AT THE INDEX WE WANT IT AT
					placeholder[i] = value;
				}
				else{
					placeholder[i] = data[i];
				}
			}
			data = placeholder;
			placeholder = null;
		}
		else if(max==true && duplicate==true){
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0;i<count;i++){
				placeholder[i] = data[i];
			}
			placeholder[repeatedValueIndex] = value;
			data = placeholder;
			placeholder = null;
		}
		else if(max==true && duplicate==false){
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0;i<count;i++){
				placeholder[i] = data[i];
			}
			placeholder[addedValueIndex] = value;
			data = placeholder;
			placeholder = null;
		}
		else{
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0;i<count+1;i++){
				if(i>addedValueIndex){
					placeholder[i] = data[i-1];
				}
				else if(i==addedValueIndex){
					placeholder[i] = value;
				}
				else{
					placeholder[i] = data[i];
				}
			}
			data = placeholder;
			placeholder = null;
		}
		count++;
	}

	/**
         * Gets a value at a certain index.
         * @param index the index we'll be obtaining our item from
         * @return the value at that index
         */
	public T get(int index) {
		// Return the item at the given index
		
		// O(1)
				
		// For an invalid index, throw an IndexOutOfBoundsException.
		// Use this code to produce the correct error message for
		// the exception (do not use a different message):
		//	  "Index " + index + " out of bounds!"
		if(index<0||index>=count){ //INVALID INDEX
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		return data[index];//GIVES THE ITEM AT THIS INDEX
	}

	/**
         * Replaces an item at a specific index with a different value.
         * @param index the index we want to replace
         * @param value the value we want to replace it with
         * @return if the operation was a success
         */
	public boolean replace(int index, T value) {
		// Change the item at the given index to be the given value.
		
		// For an invalid index, throw an IndexOutOfBoundsException. 
		// Use the same error message as get(index).
		// Note: You _cannot_ add new items with this method.
		
		// For a valid index, if value is null, throw IllegalArgumentException.
		// Use the exact same error message as add(value).
				
		// The array must keep to be sorted in ascending order after updating. 
		// For a valid index, return false if setting the value at index violates 
		// the required order hence can not be performed; no change should be made 
		// to the array.  Otherwise, change the item and return true. 
		
		// O(1)
		if(index<0||index>=count){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!"); //INVALID INDEX
		}
		else if(data[index]==null){
			throw new IllegalArgumentException("Cannot add: null value"); //NULL OBJECT
		}
		else if(index==0&&value.compareTo(data[index+1])>0){//IF THE VALUE U WANT TO REPLACE IS GREATER THAN THE ONE AFTER IT
			return false;
		}
		else if((index==0 && value.compareTo(data[index+1])==0) || (index==0 && value.compareTo(data[index+1])<0)){//IF THE VALUE U WANT TO REPLACE IS THE SAME AS THE ONE AFTER IT
			data[index] = value;
			return true;
		}
		else if(index==count-1 && value.compareTo(data[index-1])<0){ ////IF THE VALUE U WANT TO REPLACE AT THE END IS LESS THAN THE ONE BEFORE IT
			return false;
		}
		else if((index==count-1 && value.compareTo(data[index-1])==0) || (index==count-1 && value.compareTo(data[index-1])>0)){ //IF THE VALUE U WANT TO REPLACE IS SAME AS THE ONE BEFORE IT
			data[index] = value;
			return true;
		}
		else if((value.compareTo(data[index+1])<=0) && (value.compareTo(data[index-1])>=0)){//IF THE VALUE U WANT TO REPLACE AT THE INDEX IS LESS THAN THE ONE AFTER IT & GREATER THAH THE ONE BEFORE IT
			data[index] = value;
			return true;
		}
		else{
			return false;
		}
	}

	/**
         * Stores a value to be added in the array at a certain index.
         * @param index the index we want to add our value
         * @param value the value we want to add in the index
         * @return if the operation was a success
         */
	public boolean add(int index, T value) {
		// Insert the given value at the given index. Shift elements if needed.
		// Double capacity if no space available. 

		// For an invalid index, throw an IndexOutOfBoundsException. 
		// Use the same error message as get(index).
		// Note: You _can_ append items with this method, which is different from replace().
		
		// For a valid index, if value is null, throw IllegalArgumentException.
		// Use the exact same error message as add(value). See add(value) above.

		// The array must keep to be sorted in ascending order after updating. 
		// For a valid index, return false if inserting the value at index violates 
		// the required order hence can not be performed; no change should be made 
		// to the array.  Otherwise, insert the value and return true. 
		
		// You must call doubleCapacity() if no space is available. 
		// Throw IllegalStateException if doubleCapacity() fails.
		// Use the exact same error message as add(value). See add(value) above.

		// O(N) where N is the number of elements in the storage
		if(value==null){ //IF THE VALUE IS NULL
			throw new IllegalArgumentException("Cannot add: null value");
		}
		else if(index>count||index<0){ //invalid index
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		else if(count==0 && index==0){ //ADDING YOUR FIRST VALUE, SHOULD BE AT THE FIRST INDEX
			data[index] = value;
			count++;
			return true;
		}
		else if(count==0 && index!=0){
			return false;
		}

		for(int i = 0;i<count;i++){
			if(value.compareTo(data[i])==0&&i==index){ //ADDING THE VALUE BEFORE A VALUE THAT'S THE SAME
				add(value); 
				break;
			}
			else if(value.compareTo(data[i])<0&&i==index){ //ADDING THE VALUE BEFORE A VALUE THAT'S LARGER
				add(value);
				break;
			}
			else if(value.compareTo(data[i])>0&&i==index){ //ADDING THE VALUE BEFORE A VALUE THAT'S SMALLER. NO! NO!
				return false;
			}
		}
		if(index==count&&value.compareTo(data[index-1])>0){ //IF YOU WANT TO ADD AT THE VERY END AFTER FINDING OUT THAT THE VALUE YOU HAVE IS THE MAX
			add(value);
		}
		else if(index==count&&value.compareTo(data[index-1])<0){
			return false;
		}	
		return true;
	} 
	/**
         * Deletes an item at a specific index.
         * @param index the index we want to replace
         * @return the item at that index
         */
	public T delete(int index) {
		// Remove and return the element at the given index. Shift elements
		// to remove the gap. Throw an exception when there is an invalid
		// index (see replace(), get(), etc. above).
		
		// After deletion, if the number of elements falls below 1/3 
		// of the capacity, you must call halveCapacity() to shrink the storage.
		// - Check halveCapacity() below for details.
		// - If the capacity would fall below the DEFAULT_CAPACITY, 
		//   shrink to the DEFAULT_CAPACITY. This should be implemented by
		//   halveCapacity().
		
		// O(N) where N is the number of elements currently in the storage
		if(index<0||index>=count){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		else if(data[index]==null||count==0){ //WHAT IF THERE ARE NO ELEMENTS IN THE ARRAY?
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds!");
		}
		deletedItem = (T[]) new Comparable[1];
		placeholder = (T[]) new Comparable[count-1];
		for(int i = 0;i<count;i++){ //SHIFT THE ARRAY ACCORDINGLY AFTER REMVOVING ITEM
			if(i<index){
				placeholder[i]=data[i]; 
			}
			else if(i>index){
				placeholder[i-1] = data[i];
			}
			else{
				deletedItem[0] = data[i];
			}
		}
		data = placeholder;
		count--;
		if(count<(storage/3.0)){ //HALVING CAPACITY WHEN ARRAY BECOMES VACANT
			halveCapacity();
		}
		return deletedItem[0];
	}  

	/**
         * Doubles the capacity of our array if we run out of storage.
         * @return if the operation was a success
         */
	@SuppressWarnings("unchecked")
	public boolean doubleCapacity(){
		// Double the max number of items allowed in data storage.
		// Remember to copy existing items to the new storage after expansion.

		// - Out of abundance of caution, we will use (Integer.MAX_VALUE - 50)
		//   as the upper-bound of our capacity.
		// - If double the current capacity would go beyond this upper-bound,
		//   use this upper-bound value as the new capacity.
		// - If the current capacity is already this upper-bound (Integer.MAX_VALUE - 50), 
		//   do not expand and return false.
		
		// Return true for a successful expansion.

		// O(N) where N is the number of elements in the array
		if(count*2<=Integer.MAX_VALUE - 50){  //IF UPPER BOUND CAPACITY IS NOT REACHED
			storage = count * 2; //DOUBLE THE STORAGE
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0; i < count; i++){
				placeholder[i] = data[i];
			}
			data = placeholder;
			return true;
		}
		else if(count*2>Integer.MAX_VALUE - 50){
			storage = Integer.MAX_VALUE - 50;
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0; i < count; i++){
				placeholder[i] = data[i];
			}
			data = placeholder;
			return true;
		}
		return false; //default return, remove/change as needed
	}

	/**
         * Reduces the array capacity if most of its spaces aren't occupied.
         * @return if the operation was a success
         */
	@SuppressWarnings("unchecked")
	public boolean halveCapacity(){
		// Reduce the max number of items allowed in data storage by half.
		// - If the current capacity is an odd number, _round down_ to get the 
		//   new capacity;
		// - If the new capacity would fall below the DEFAULT_CAPACITY, 
		//   shrink to the DEFAULT_CAPACITY;
		// - If the new capacity (after necessary adjustment to DEFAULT_CAPACITY) 
		//   cannot hold all existing items, do not shrink and return false;
		// - Return true for a successful shrinking.

		// Remember to copy existing items to the new storage after shrinking.
		
		// O(N) where N is the number of elements in the array
		if(storage/2<DEFAULT_CAPACITY){ 
			storage = DEFAULT_CAPACITY;
		}
		if(count>storage/2){
			return false;
		}
		else{
			storage/=2;
			placeholder = (T[]) new Comparable[storage];
			for(int i = 0; i < count; i++){
				placeholder[i] = data[i];
			}
			data = placeholder;
			return true;
		}

	}
	//******************************************************
	//*******     BELOW THIS LINE IS TESTING CODE    *******
	//*******      Edit it as much as you'd like!    *******
	//*******		Remember to add JavaDoc			 *******
	//******************************************************

	/**
         * A string that describes the current status of the array.
         * @return the string representation of the array
         */
	public String toString() {
		//This method is provided for debugging purposes
		//(use/modify as much as you'd like), it just prints
		//out the MySortedArray for easy viewing.
		StringBuilder s = new StringBuilder("MySortedArray with " + size()
			+ " items and a capacity of " + capacity() + ":");
		for (int i = 0; i < size(); i++) {
			s.append("\n  ["+i+"]: " + get(i));
		}
		return s.toString();
		
	}

	/**
         * Main method of the class.
         * @param args args
         */
	public static void main(String[] args){
		//These are _sample_ tests. If you're seeing all the "yays" that's
		//an excellend first step! But it might not mean your code is 100%
		//working... You may edit this as much as you want, so you can add
		//own tests here, modify these tests, or whatever you need!

		//create a MySortedArray of integers
		MySortedArray<Integer> nums = new MySortedArray<>();
		if((nums.size() == 0) && (nums.capacity() == 2)){
			System.out.println("Yay 1");
		}

		//append some numbers 
		for(int i = 0; i < 3; i++) {
			nums.add(i,i*2);
		}
		//uncomment to check the array details
		//System.out.println(nums.toString());
		
		
		if(!nums.add(nums.size(),1) && nums.size() == 3 && nums.get(2) == 4 && nums.capacity() == 4){
			System.out.println("Yay 2");
		}
		//System.out.println(nums.toString());
		
		
		//add more numbers, your insertion need to keep the array sorted
		nums.add(1);
		nums.add(-1);
		nums.add(5);
		if (nums.size() == 6 && nums.get(0)==-1 && nums.get(2) == 1 && nums.get(5) == 5){
			System.out.println("Yay 3");
		}
		//System.out.println(nums.toString());
		
		//add with index
		if (nums.add(4,2) && nums.add(3,2) && nums.get(3) == nums.get(4) 
			&& nums.get(4) == nums.get(5) && nums.get(5)== 2){ 	
			System.out.println("Yay 4");		
		}
		//System.out.println(nums.toString());
		
		//replace with index
		if (nums.replace(5,3) && nums.get(5)==3 && nums.replace(6,5) && nums.get(6)==5
			&& !nums.replace(1,2) && nums.get(1) == 0){
			System.out.println("Yay 5");				
		}
		//System.out.println(nums.toString());
		
		MySortedArray<String> names = new MySortedArray<>();
		
		//insert some strings
		names.add("alice");
		names.add("charlie");
		names.add("bob");
		names.add("adam");
		//System.out.println(names.toString());

		//delete
		if (names.add(4,"emma") && names.delete(3).equals("charlie")){
			System.out.println("Yay 6");
		}
		
		names.delete(0);
		names.delete(0);
		
		//shrinking
		if (names.size()==2 && names.capacity() == 4){
			System.out.println("Yay 7");
		}
		//System.out.println(names.toString());
		
		//insert equal values: sorted by insertion order
		String dylan1 = new String("dylan");
		String dylan2 = new String("dylan");
		names.add(dylan1);
		names.add(dylan2);
		if (names.size()==4 && names.get(1) == dylan1 && names.get(2) == dylan2
			&& names.get(1)!=names.get(2)){
			System.out.println("Yay 8");		
		}
		//System.out.println(names.toString());
		
		// exception checking example
		// make sure you write more testings by yourself
		try{
			names.get(-1);
		}
		catch(IndexOutOfBoundsException ex){
			if (ex.getMessage().equals("Index -1 out of bounds!"))
				System.out.println("Yay 9");
		}
		
		// call doubleCapacity() and halveCapacity() directly
		if (names.doubleCapacity() && names.capacity() == 8 
			&& names.halveCapacity() && names.capacity() == 4
			&& !names.halveCapacity() && names.capacity() == 4){
			System.out.println("Yay 10");
		
		}
		//System.out.println(names.toString());

	}
	

}
