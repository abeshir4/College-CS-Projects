You’re going to build a window manager! This program will work like a traditional window manager, but very limited.
Things your application will be able to do by the end:

• Draw on the window/region with “squares”

• Remove squares from windows.

• Move windows into the foreground.

• Remove windows.

• Add new windows.

There are five major components to this project:
1. Implementing squares.
2. Implementing collections of squares.
3. Implementing windows.
4. Implementing stacks of windows.
5. Implementing the menus (sorting windows/squares).

Part 1: Squares

Squares are represented by the (x,y) position of the upper left corner, and their size. Squares also have a randomly
generated color.
1. You need to be able to determine if a given (x,y) position is within the square’s boundaries. This will, eventually,
allow you to handle “clicks” on a given square.
2. You need to draw the squares.

Part 2: Lists of Squares

Each window is going to need a list of squares. An outline of this class has been provided for you and you are going to complete this provided code using a linked list class of your own design made out of the nodes defined in the provided Node class.

Part 3: Windows

1. You need to be able to determine if a given (x,y) position is within the window’s boundaries. This will,
eventually, allow you to handle “clicks” on a given window.
2. You need to track whether or not a window is the currently selected window. This is just a field that controls how
windows are drawn (with or without a thick border)

Part 4: Window Stacks

The window stack is actually even cooler that the windows themselves, so we left that to you. An outline of this class has
been provided for you and you are going to complete this provided code using linked lists.

Part 5: Sorting Windows and Squares

While you were working on Part 2-4, you probably came across bits of code related to sorting. You may also have
wondered how the menu buttons were going to work. That’s this part of the project. The sorting code is spread throughout
the following files:

• ThreeTenLinkedList – There are two static methods in this class, isSorted() and sort(). These are
generic static methods that accept a NodePair (a head/tail combo variable) and a comparator.

• WindowStack – The window stack needs to sort windows! Sorting by size is 99% done for you a demonstration
of how to write a comparator and call your sort() method from ThreeTenLinkedList. Sorting by location is
on you, you’ll have to do the same thing, but write a different comparator.

• SquareList – The square list will do the sorting of squares. Sorting by creation time (using the square’s IDs)
and sorting by the square’s location in the window will be similar to how you did the sorting in WindowStack,
but now for a list of squares.
There is a lot more information on sorting, suggested ways to sort linked lists, and requirements for the sorts in the
ThreeTenLinkedList class.

-This project should compile with the command: javac *.java

-This project should run with the command: java WindowManager
