/* 
 * stoplight.c
 *
 * 31-1-2003 : GWA : Stub functions created for CS161 Asst1.
 *
 * NB: You can use any synchronization primitives available to solve
 * the stoplight problem in this file.
 */


/*
 * 
 * Includes
 *
 */

#include <types.h>
#include <lib.h>
#include <test.h>
#include <thread.h>
#include <synch.h>
#include <machine/spl.h>


/*
 *
 * Constants
 *
 */

/*
 * Number of vehicles created.
 */

#define NVEHICLES 20
struct lock *lock_AB;
struct lock *lock_BC;
struct lock *lock_CA;

int carquan;
int truckquan;
int turningleft;
int vehiclescan;
/*
 *
 * Function Definitions
 *
 */

/*
 * turnleft()
 *
 * Arguments:
 *      unsigned long vehicledirection: the direction from which the vehicle
 *              approaches the intersection.
 *      unsigned long vehiclenumber: the vehicle id number for printing purposes.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      This function should implement making a left turn through the 
 *      intersection from any direction.
 *      Write and comment this function.
 */

static
void
turnleft(unsigned long vehicledirection,
		unsigned long vehiclenumber,
		unsigned long vehicletype)
{
	/*
	 * Avoid unused variable warnings.
	 */

	//(void) vehicledirection;
	//(void) vehiclenumber;
	//(void) vehicletype;
	
	if(vehicletype != 1 && vehicledirection == 0){	//CAR COMING FROM ROUTE A
		//kprintf("Car %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);

		int spl;
		spl = splhigh();
		carquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){ //if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_AB->occup); //put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_AB->occup);

		lock_acquire(lock_AB);
		//kprintf("Car %ld from Route A to turn LEFT to Route C ENTERED AB\n", vehiclenumber);
		lock_acquire(lock_BC);
		kprintf("Car %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route A to turn LEFT to Route C ENTERED AB\n", vehiclenumber);
		kprintf("Car %ld from Route A to turn LEFT to Route C ENTERED BC\n", vehiclenumber);
		kprintf("Car %ld from Route A to turn LEFT to Route C LEFT BC\n", vehiclenumber);
		lock_release(lock_AB);
		lock_release(lock_BC);
		spl = splhigh();
		thread_wakeup(lock_AB->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Car %ld from Route A to turn LEFT to Route C LEFT BC\n", vehiclenumber);
	}
	else if(vehicletype != 1 && vehicledirection == 1){	//CAR COMING FROM ROUTE B
		//kprintf("Car %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);

		int spl;
		spl = splhigh();
		carquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){//if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_BC->occup);//put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_BC->occup);

		lock_acquire(lock_BC);
		//kprintf("Car %ld from Route B to turn LEFT to Route A ENTERED BC\n", vehiclenumber);
		lock_acquire(lock_CA);
		kprintf("Car %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route B to turn LEFT to Route A ENTERED BC\n", vehiclenumber);
		kprintf("Car %ld from Route B to turn LEFT to Route A ENTERED CA\n", vehiclenumber);
		kprintf("Car %ld from Route B to turn LEFT to Route A LEFT CA\n", vehiclenumber);
		lock_release(lock_BC);
		lock_release(lock_CA);
		spl = splhigh();
		thread_wakeup(lock_BC->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Car %ld from Route B to turn LEFT to Route A LEFT CA\n", vehiclenumber);
	}
	else if(vehicletype != 1 && vehicledirection == 2){	//CAR COMING FROM ROUTE C
		//kprintf("Car %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);

		int spl;
		spl = splhigh();
		carquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){//if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_CA->occup);//put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_CA->occup);

		lock_acquire(lock_CA);
		//kprintf("Car %ld from Route C to turn LEFT to Route B ENTERED CA\n", vehiclenumber);
		lock_acquire(lock_AB);
		kprintf("Car %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route C to turn LEFT to Route B ENTERED CA\n", vehiclenumber);
		kprintf("Car %ld from Route C to turn LEFT to Route B ENTERED AB\n", vehiclenumber);
		kprintf("Car %ld from Route C to turn LEFT to Route B LEFT AB\n", vehiclenumber);
		lock_release(lock_CA);
		lock_release(lock_AB);
		spl = splhigh();
		thread_wakeup(lock_CA->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Car %ld from Route C to turn LEFT to Route B LEFT AB\n", vehiclenumber);
	}
	else if(vehicletype == 1 && vehicledirection == 0){	//TRUCK COMING FROM ROUTE A
		//kprintf("Truck %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);
		
		int spl;
		spl = splhigh();
		truckquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){//if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_AB->occup);//put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_AB->occup);

		lock_acquire(lock_AB);
		//kprintf("Truck %ld from Route A to turn LEFT to Route C ENTERED AB\n", vehiclenumber);
		lock_acquire(lock_BC);
		kprintf("Truck %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route A to turn LEFT to Route C ENTERED AB\n", vehiclenumber);
		kprintf("Truck %ld from Route A to turn LEFT to Route C ENTERED BC\n", vehiclenumber);
		kprintf("Truck %ld from Route A to turn LEFT to Route C LEFT BC\n", vehiclenumber);
		lock_release(lock_AB);
		lock_release(lock_BC);
		spl = splhigh();
		thread_wakeup(lock_AB->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Truck %ld from Route A to turn LEFT to Route C LEFT BC\n", vehiclenumber);
	}
	else if(vehicletype == 1 && vehicledirection == 1){	//TRUCK COMING FROM ROUTE B
		//kprintf("Truck %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);
		
		int spl;
		spl = splhigh();
		truckquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){//if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_BC->occup);//put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_BC->occup);

		lock_acquire(lock_BC);
		//kprintf("Truck %ld from Route B to turn LEFT to Route A ENTERED BC\n", vehiclenumber);
		lock_acquire(lock_CA);
		kprintf("Truck %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route B to turn LEFT to Route A ENTERED BC\n", vehiclenumber);
		kprintf("Truck %ld from Route B to turn LEFT to Route A ENTERED CA\n", vehiclenumber);
		kprintf("Truck %ld from Route B to turn LEFT to Route A LEFT CA\n", vehiclenumber);
		lock_release(lock_BC);
		lock_release(lock_CA);
		spl = splhigh();
		thread_wakeup(lock_BC->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Truck %ld from Route B to turn LEFT to Route A LEFT CA\n", vehiclenumber);
	}
	else{                                                  //TRUCK COMING FROM ROUTE C
		//kprintf("Truck %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);
		
		int spl;
		spl = splhigh();
		truckquan++;
		turningleft++;//counter to see how many cars want to turn left at intersection
		while(turningleft==3){//if that counter reaches 3 (posibility of deadlock)
			thread_sleep(lock_CA->occup);//put the vehicle thread to sleep
			turningleft--;
		}
		splx(spl);
		//thread_wakeup(lock_CA->occup);

		lock_acquire(lock_CA);
		//kprintf("Truck %ld from Route C to turn LEFT to Route B ENTERED CA\n", vehiclenumber);
		lock_acquire(lock_AB);
		kprintf("Truck %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route C to turn LEFT to Route B ENTERED CA\n", vehiclenumber);
		kprintf("Truck %ld from Route C to turn LEFT to Route B ENTERED AB\n", vehiclenumber);
		kprintf("Truck %ld from Route C to turn LEFT to Route B LEFT AB\n", vehiclenumber);
		lock_release(lock_CA);
		lock_release(lock_AB);
		spl = splhigh();
		thread_wakeup(lock_CA->occup);//Wake up the vehicle thread that was put to sleep
		splx(spl);
		//kprintf("Truck %ld from Route C to turn LEFT to Route B LEFT AB\n", vehiclenumber);
	}
}


/*
 * turnright()
 *
 * Arguments:
 *      unsigned long vehicledirection: the direction from which the vehicle
 *              approaches the intersection.
 *      unsigned long vehiclenumber: the vehicle id number for printing purposes.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      This function should implement making a right turn through the 
 *      intersection from any direction.
 *      Write and comment this function.
 */

static
void
turnright(unsigned long vehicledirection,
		unsigned long vehiclenumber,
		unsigned long vehicletype)
{
	/*
	 * Avoid unused variable warnings.
	 */

	//(void) vehicledirection;
	//(void) vehiclenumber;
	//(void) vehicletype;
	
	if(vehicletype != 1 && vehicledirection == 0){	//CAR COMING FROM ROUTE A
		//kprintf("Car %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_AB);
		kprintf("Car %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route A to turn RIGHT to Route B ENTERED AB\n", vehiclenumber);
		kprintf("Car %ld from Route A to turn RIGHT to Route B LEFT AB\n", vehiclenumber);
		lock_release(lock_AB);
	}
	else if(vehicletype != 1 && vehicledirection == 1){	//CAR COMING FROM ROUTE B
		//kprintf("Car %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_BC);
		kprintf("Car %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route B to turn RIGHT to Route C ENTERED BC\n", vehiclenumber);
		kprintf("Car %ld from Route B to turn RIGHT to Route C LEFT BC\n", vehiclenumber);
		lock_release(lock_BC);
	}
	else if(vehicletype != 1 && vehicledirection == 2){	//CAR COMING FROM ROUTE C
		//kprintf("Car %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_CA);
		kprintf("Car %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
		kprintf("Car %ld from Route C to turn RIGHT to Route A ENTERED CA\n", vehiclenumber);
		kprintf("Car %ld from Route C to turn RIGHT to Route A LEFT CA\n", vehiclenumber);
		lock_release(lock_CA);
	}
	else if(vehicletype == 1 && vehicledirection == 0){	//TRUCK COMING FROM ROUTE A
		//kprintf("Truck %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_AB);
		kprintf("Truck %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route A to turn RIGHT to Route B ENTERED AB\n", vehiclenumber);
		kprintf("Truck %ld from Route A to turn RIGHT to Route B LEFT AB\n", vehiclenumber);
		lock_release(lock_AB);
	}
	else if(vehicletype == 1 && vehicledirection == 1){	//TRUCK COMING FROM ROUTE B
		//kprintf("Truck %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_BC);
		kprintf("Truck %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route B to turn RIGHT to Route C ENTERED BC\n", vehiclenumber);
		kprintf("Truck %ld from Route B to turn RIGHT to Route C LEFT BC\n", vehiclenumber);
		lock_release(lock_BC);
	}
	else{                                                  //TRUCK COMING FROM ROUTE C
		//kprintf("Truck %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
		lock_acquire(lock_CA);
		kprintf("Truck %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
		kprintf("Truck %ld from Route C to turn RIGHT to Route A ENTERED CA\n", vehiclenumber);
		kprintf("Truck %ld from Route C to turn RIGHT to Route A LEFT CA\n", vehiclenumber);
		lock_release(lock_CA);
	}

}


/*
 * approachintersection()
 *
 * Arguments: 
 *      void * unusedpointer: currently unused.
 *      unsigned long vehiclenumber: holds vehicle id number.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      Change this function as necessary to implement your solution. These
 *      threads are created by createvehicles().  Each one must choose a direction
 *      randomly, approach the intersection, choose a turn randomly, and then
 *      complete that turn.  The code to choose a direction randomly is
 *      provided, the rest is left to you to implement.  Making a turn
 *      or going straight should be done by calling one of the functions
 *      above.
 */

static
void
approachintersection(void * unusedpointer,
		unsigned long vehiclenumber)
{
	int vehicledirection, turndirection, vehicletype;

	/*
	 * Avoid unused variable and function warnings.
	 */

	(void) unusedpointer;
	//(void) vehiclenumber;
	//(void) turnleft;
	//(void) turnright;

	/*
	 * vehicledirection is set randomly.
	 */

	vehicledirection = random() % 3;
	turndirection = random() % 2;
	vehicletype = random() % 2;

	//NEW CODE HERE
	/*
	if(turndirection != 0 && vehicletype != 0){ //MAKING A RIGHT TURN IF vehicledirection == 1 / TRUCK if vehicletype == 1
		truckquan++; //Increment the number of trucks present
		if(vehicledirection == 0){ //Coming from ROUTE A
			kprintf("Truck %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
		else if(vehicledirection == 1){ //Coming from Route B
			kprintf("Truck %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
		else{ //Coming from Route C
			kprintf("Truck %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
	}
	else if(turndirection == 0 && vehicletype != 0){ //MAKING A LEFT TURN IF vehicledirection == 0 / TRUCK if vehicletype == 1
		truckquan++; //Increment the number of trucks present
		if(vehicledirection == 0){ //Coming from ROUTE A
			kprintf("Truck %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
		else if(vehicledirection == 1){ //Coming from Route B
			kprintf("Truck %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
		else{ //Coming from Route C
			kprintf("Truck %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
	}
	else if(turndirection != 0 && vehicletype == 0){ //MAKING A RIGHT TURN IF vehicledirection == 1 / CAR if vehicletype == 0
		carquan++; //Increment the number of cars present
		if(vehicledirection == 0){ //Coming from ROUTE A
			kprintf("Car %ld from Route A to turn RIGHT to Route B APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
		else if(vehicledirection == 1){ //Coming from Route B
			kprintf("Car %ld from Route B to turn RIGHT to Route C APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
		else{ //Coming from Route C
			kprintf("Car %ld from Route C to turn RIGHT to Route A APPROACHES the intersection\n", vehiclenumber);
			turnright(vehicledirection, vehiclenumber, vehicletype);
		}
	}
	else{ //MAKING A LEFT TURN IF vehicledirection == 0  / CAR if vehicletype == 0
		carquan++; //Increment the number of cars present
		if(vehicledirection == 0){ //Coming from ROUTE A
			kprintf("Car %ld from Route A to turn LEFT to Route C APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
		else if(vehicledirection == 1){ //Coming from Route B
			kprintf("Car %ld from Route B to turn LEFT to Route A APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
		else{ //Coming from Route C
			kprintf("Car %ld from Route C to turn LEFT to Route B APPROACHES the intersection\n", vehiclenumber);
			turnleft(vehicledirection, vehiclenumber, vehicletype);
		}
	}*/

	if(turndirection != 0){//TURN RIGHT IF turndirection==1
		turnright(vehicledirection, vehiclenumber, vehicletype);
	}
	else{//TURN LEFT IF turndirection==0
		turnleft(vehicledirection, vehiclenumber, vehicletype);
	}
	int spl = splhigh();
	vehiclescan = vehiclescan + 1;
	splx(spl);

}


/*
 * createvehicles()
 *
 * Arguments:
 *      int nargs: unused.
 *      char ** args: unused.
 *
 * Returns:
 *      0 on success.
 *
 * Notes:
 *      Driver code to start up the approachintersection() threads.  You are
 *      free to modiy this code as necessary for your solution.
 */

int
createvehicles(int nargs,
		char ** args)
{
	int index, error;

	lock_AB = lock_create("AB"); //3 different locks created for 3 different portions
	lock_BC = lock_create("BC");
	lock_CA = lock_create("CA");
	/*
	 * Avoid unused variable warnings.
	 */

	(void) nargs;
	(void) args;

	/*
	 * Start NVEHICLES approachintersection() threads.
	 */

	for (index = 0; index < NVEHICLES; index++) {

		error = thread_fork("approachintersection thread",
				NULL,
				index,
				approachintersection,
				NULL
				);

		/*
		 * panic() on error.
		 */

		if (error) {

			panic("approachintersection: thread_fork failed: %s\n",
					strerror(error)
				 );
		}
	}

	while(NVEHICLES>vehiclescan){
		thread_yield();
	}

	return 0;
}
