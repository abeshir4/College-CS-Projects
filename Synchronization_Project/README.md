BEFORE REVIEWING THIS CODE, IT IS IMPORTANT TO KNOW THE FOLLOWING:

This assignment will familiarize you with an operating system called OS/161 and also with System/161 which is the machine simulator on which OS/161 runs. OS/161 is the operating system that I had to use in this programming assignment.

The OS/161 distribution contains a full operating system source tree, including some utility programs and libraries.

This project was created to put into practice my knowledge on synchronization of threads. The language used for this project was C.

Project Summary:

You must solve this problem using the locks that you implemented int the synch.c source file along with

thread_sleep() and thread_wakeup() functions.

Other solutions (condition variables, semaphores, etc.) are not acceptable.

Your solution shouldn’t use busy waiting either.

Traffic through the main intersection in the town of Podunk, KS has increased over the past few years. Until now the intersection has been a three-way stop but now the impending gridlock has forced the residents of Podunk to admit that they need a more efficient way for traffic to pass through the intersection. Your job is to design and implement a solution using the synchronization primitives (locks) that you have developed in the previous part. For the purpose of this problem we will model the intersection as shown above, dividing it into three portions (AB, BC, CA) and identifying each portion with the names of the lanes entering/leaving the intersection through that section. (Just to clarify: Podunk is in the US, so we're driving on the right side of the road.) Turns are represented by a progression through one or two portions of the intersection (for simplicity assume that U-turns do not occur in the intersection). So if a car approaches from Route-A, depending on where it is going, it proceeds through the intersection as follows:

● Right: AB

● Left: AB-BC

In addition to “Cars”, there are also “Trucks” in Podunk. Trucks have a lower priority than cars when entering the intersection. If a lane in a given route has both cars and trucks in it, then the cars should cross the intersection before the trucks in that route. When no more cars are waiting, then the trucks on that lane can cross the intersection. It is fine if some trucks suffer starvation (i.e., cross last). Note that, cars have higher priority than trucks approaching the intersection through the same route (Route-A, Route-B, or Route-C). For example, if there are six vehicles (four cars and two trucks) approaching the intersection through Route-A, the two trucks should not start to cross before any of the four cars.

The file you are to work with is stoplight.c

Implement (mutual exclusion) locks for OS/161. The interface for the lock structure is defined in synch.h. Stub code is provided in synch.c.
