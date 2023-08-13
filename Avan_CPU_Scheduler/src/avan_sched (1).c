/* This is the only file you will be editing.
 * - Copyright of Starter Code: Prof. Kevin Andrea, George Mason University.  All Rights Reserved
 * - Copyright of Student Code: You!  
 * - Restrictions on Student Code: Do not post your code on any public site (eg. Github).
 * -- Feel free to post your code on a PRIVATE Github and give interviewers access to it.
 * - Date: Aug 2022
 */

/* Fill in your Name and GNumber in the following two comment fields
 * Name: Akram Beshir
 * GNumber: G01128864
 */

// System Includes
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <pthread.h>
#include <sched.h>
// Local Includes
#include "avan_sched.h"
#include "vm_support.h"
#include "vm_process.h"

/* Feel free to create any helper functions you like! */

/* Initialize the avan_header_t Struct
 * Follow the specification for this function.
 * Returns a pointer to the new avan_header_t or NULL on any error.
 */
avan_header_t *avan_create() {
	avan_header_t *z = NULL; //CREATES A struct NAMED avan_header_t OF TYPE avan_header; C IF INITIALIZING IT TO null IS NECESSARY
	z = malloc(sizeof( avan_header_t)); //ALLOCATING MEMORY FOR THE Avan Header struct
	if(z==NULL){ //IF ALLOCATION OF MEMORY FOR THE STRUCT WAS NOT SUCCESSFULL
		return NULL;//RETURN NULL
	}
	z->ready_queue = malloc(sizeof( queue_header_t)); //ALLOCATION OF MEMORY FOR THE QUEUE LISTS
	z->suspended_queue = malloc(sizeof( queue_header_t));
	z->terminated_queue = malloc(sizeof( queue_header_t));
	z->ready_queue->count = 0; //SET COUNT TO ZERO
	z->suspended_queue->count = 0;
	z->terminated_queue->count = 0;
	z->ready_queue->head = NULL; //SET THE HEAD TO NULL
	z->suspended_queue->head = NULL;
	z->terminated_queue->head = NULL;
	return z;
}

/* Adds a process into the appropriate singly linked list.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int avan_insert(avan_header_t *header, process_node_t *process) {
	pid_t p = process->pid; //STORING THE PROCESS PID WE WANT TO ADD IN ANOTHER VARIABLE
	process->flags = 0x00000000;
	process->flags |= 0x00000001; //SETTING THIS PROCESS FLAGS TO BE IN THE READY STATE
	if(header->ready_queue->head==NULL || p < header->ready_queue->head->pid){ //IF THIS IS THE FIRST PROCESS, OR IF IT HAS THE SMALLEST PID OUT OF THE OTHERS
		process->next = header->ready_queue->head;//THEN ADD IT TO THE FRONT
		header->ready_queue->head = process;
		header->ready_queue->count+=1;
		return 0;
	}
	else{
		process_node_t* iterator = header->ready_queue->head;
		while(iterator->next!=NULL && iterator->next->pid < p){//ITERATING THRU THE QUEUE UNTIL WE FIND A PID THAT'S LESS THAN OURS
			iterator = iterator->next; //KEEP ITERATING IF WE CAN'T FIND
		}
		process->next = iterator->next; //ADD IT IN IT'S RESPECTIVE POSITION
		iterator->next = process;
		header->ready_queue->count+=1; //INCREMENT COUNT
		return 0;
	}
	return -1;
}

/* Move the process with matching pid from Ready to Suspended queue.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error (such as process not found).
 */
int avan_suspend(avan_header_t *header, pid_t pid) {
	int found = 0;
	process_node_t* ptr = header->ready_queue->head;
        process_node_t* iterator_r = header->ready_queue->head;
	process_node_t* current = header->ready_queue->head;
	process_node_t* previous = header->ready_queue->head;

	while(ptr!=NULL){
		if(ptr->pid!=pid){ //ITERATING THRU THE QUEUE TO FIND OUR PID
			ptr = ptr->next;
		}
		else{
			found = 1; //CHANGE THE FOUND VARIABLE TO INDICATE WE FOUND IT
			break;
		}
	}
	if(found==1){
		;
	}
	else{ //IF THE PID ISN'T THERE, WE HAVE AN ERROR
		return -1;
	}

    	if(header->ready_queue->head->pid==pid){ //IF OUR PID IS IN THE BEGINNING
		iterator_r = header->ready_queue->head; //REMOVE IT
    		header->ready_queue->head = current->next;
		iterator_r->next = NULL; //iterator_r->next = NULL;
		header->ready_queue->count-=1; //DECREMENT THE COUNT
    	}
    	else{ //IF IT'S NOT AT THE BEGINNING, ITERATE THRU IT UNTIL U FIND IT
    		while(iterator_r->pid!=pid){
    			previous = current;
    			current = current->next;
    			iterator_r = iterator_r->next;
	  	}
    		previous->next = current->next;
		iterator_r->next = NULL;
		header->ready_queue->count-=1;
    	}
	iterator_r->next = NULL;
	iterator_r->flags <<= 0x00000001;//SET THE FLAGS FROM THE READY STATE TO BE IN THE SUSPENDED STATE USING BITWISE OPERATORS
	if(header->suspended_queue->head==NULL || pid < header->suspended_queue->head->pid){//IF THE QUEUE IS EMPTY OR OUR PROCESS HAS THE SMALLEST PID, WE'LL ADD IT TO THE FRONT
                iterator_r->next = header->suspended_queue->head;
                header->suspended_queue->head = iterator_r;
                header->suspended_queue->count+=1; //INCREMENT THE COUNT
                return 0;
        }
        else{
                process_node_t* iterator_s = header->suspended_queue->head;
                while(iterator_s->next!=NULL && iterator_s->next->pid < pid){//IF THE PID ISN'T THE SMALLEST, ITERATE THRU THE QUEUE TIL WE FIND ONE BIGGER THAN OURS 
                        iterator_s = iterator_s->next;
                }
		iterator_r->next = iterator_s->next;
                iterator_s->next = iterator_r;
                header->suspended_queue->count+=1; //INCREMENT THE COUNT
                return 0;
	}
	return -1;
}

/* Move the process with matching pid from Suspended to Ready queue.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error (such as process not found).
 */
int avan_resume(avan_header_t *header, pid_t pid) {
	int found = 0;
	process_node_t* ptr = header->suspended_queue->head;
	process_node_t* iterator_s = header->suspended_queue->head;
	process_node_t* current = header->suspended_queue->head;
	process_node_t* previous = header->suspended_queue->head;
	while(ptr!=NULL){
		if(ptr->pid!=pid){ //MAKE SURE OUR PID IS IN THE QUEUE
			ptr = ptr->next;
		}
		else{
			found = 1;//CHANGE THE FOUND VARIABLE TO SHOW WE FOUND IT
			break;
		}
	}
	if(found==1){
		;
	}
	else{
		return -1;
	}
    	if(header->suspended_queue->head->pid==pid){ //IF OUR PID IS THE FIRST ONE IN THE QUEUE, REMOVE IT
		iterator_s = header->suspended_queue->head;
    		header->suspended_queue->head = current->next;
		iterator_s->next = NULL;
		header->suspended_queue->count-=1; //DECREMENT THE COUNT
    	}
    	else{
    		while(iterator_s->pid!=pid){//IF IT'S NOT THE FIRST ONE, ITERATE THRU THE QUEUE TIL U FIND IT AND REMOVE IT
    			previous = current;
    			current = current->next;
    			iterator_s = iterator_s->next;
	  	}
    		previous->next = current->next;
		iterator_s->next = NULL;
		header->suspended_queue->count-=1;//DECREMENT THE COUNT
    	}
	iterator_s->next = NULL;
	iterator_s->flags >>= 0x00000001; //SETTING THE FLAGS FOR THE SUSPENDED STATE TO BE IN THE READY STATE USING BITWISE OPERATORS
	if(header->ready_queue->head==NULL||pid < header->ready_queue->head->pid){//IF THIS QUEUE IS EMPTY OR OUR PID IS THE SMALLEST
		iterator_s->next = header->ready_queue->head;//ADD TO THE FRONT
		header->ready_queue->head = iterator_s;
		header->ready_queue->count+=1;//INCREMENT
		return 0;
	}
	else{
		process_node_t* iterator_r = header->ready_queue->head;
		while(iterator_r->next!=NULL && iterator_r->next->pid < pid)//ITERATE THRU THE QUEUE TIL WE FIND A PID LARGER THAN OURS
			iterator_r = iterator_r->next;
		iterator_s->next = iterator_r->next;
		iterator_r->next = iterator_s;
		header->ready_queue->count+=1;//INCREMENT
		return 0;
	}
	return -1;
}

/* Insert the process in the Terminated Queue and add the Exit Code to it.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int avan_quit(avan_header_t *header, process_node_t *node, int exit_code) {
	if(node==NULL){//ACCOUNTING FOR A NULL NODE
		return -1;
	}
	pid_t pt = node->pid;
	node->flags = 0x00000000;
        node->flags |= 0x00000004; //SETTING THE FLAGS OF THIS PROCESS TO BE IN THE TERMINATED STATE USING BITWISE OPERATORS
	int shift = exit_code << 0x00000004; //PEFORMING A SHIFT TO THE LEFT BY 4 BITS TO MAKE THE LOWER 28 BITS AS THE UPPER 28 BITS AND TO MAKE SPACE FOR THE 4 BITS REPRESENTING THE STATE
	node->flags |= shift;
	if(header->terminated_queue->head==NULL||pt < header->terminated_queue->head->pid){//IF THE QUEUE IS EMPTY OR THE PID OF THE NODE IS THE MINIMUM OUT OF EVERYTHING
		node->next = header->terminated_queue->head; //ADD IT TO THE FRONT
		header->terminated_queue->head = node;
		header->terminated_queue->count+=1;
		return 0;
	}
	else{
		process_node_t* iterator_t = header->terminated_queue->head;
		while(iterator_t->next!=NULL && iterator_t->next->pid < pt) //GO THRU THE ENTIRE QUEUE TIL U FIND A PID GREATER THAN OUR NODES PID OR ADD IT AT THE END
			iterator_t = iterator_t->next;
		node->next = iterator_t->next;
		iterator_t->next = node;
		header->terminated_queue->count+=1;//INCREMENT THE COUNT
		return 0;
	}
        return -1;
}

/* Move the process with matching pid from Ready to Terminated and add the Exit Code to it.
 * Follow the specification for this function.
 * Returns its exit code (from flags) on success or a -1 on any error.
 */
int avan_terminate(avan_header_t *header, pid_t pid, int exit_code) {
	int found_r = 0;
	int found_s = 0;
	int shift = 0;
	process_node_t* ptr_r = header->ready_queue->head;
	process_node_t* ptr_s = header->suspended_queue->head;
        process_node_t* iterator_r = header->ready_queue->head;
	process_node_t* iterator_s = header->suspended_queue->head;
	process_node_t* currentone = header->ready_queue->head;
	process_node_t* previousone = header->ready_queue->head;
	process_node_t* currenttwo = header->suspended_queue->head;
	process_node_t* previoustwo = header->suspended_queue->head;

	while(ptr_r!=NULL){
		if(ptr_r->pid!=pid){ //CHECKING FOR OUR PID IN THE READY QUEUE
			ptr_r = ptr_r->next;
		}
		else{
			found_r = 1; //UPDATING OUR VARIABLE ACCORDINGLY TO SHOW THE PID WAS FOUND IN THE READY QUEUE
			break;
		}
	}
	while(ptr_s!=NULL){ //CHECKING FOR OUR PID IN THE SUSPENDED QUEUE
		if(ptr_s->pid!=pid){
			ptr_s = ptr_s->next;
		}
		else{
			found_s = 1; //UPDATING OUR VARIABLE ACCORDINGLY TO SHOW THE PID WAS FOUND IN THE SUSPENDED QUEUE
			break;
		}
	}
	if(found_r==1){
		if(header->ready_queue->head->pid==pid){//IF THE PROCESS WE WANT TO ADD IS IN THE BEGGINNING OF THE READY QUEUE
			iterator_r = header->ready_queue->head;
			header->ready_queue->head = currentone->next;
			iterator_r->next = NULL;
			header->ready_queue->count-=1;//DECREMENT THE COUNT
		}
		else{
			while(iterator_r->pid!=pid){//ITERATE THRU THE QUEUE TO FIND THE PROCESS WITH OUR PID
				previousone = currentone;
				currentone = currentone->next;
				iterator_r = iterator_r->next;
	      		}
			previousone->next = currentone->next;
			iterator_r->next = NULL;
			header->ready_queue->count-=1;//DECREMENT THE COUNT
		}
		iterator_r->next = NULL;
		iterator_r->flags <<= 0x00000002; //GOING FROM READY TO TERMINATED STATE REQUIRES A LEFT SHIFT OF 2 BITS
		shift = exit_code << 0x00000004; //APPLYING A 4 BIT LEFT SHIFT TO MOVE THE LOWER 28 BITS
		iterator_r->flags |= shift; //PUTTING THE LOWER 28 BITS OF THE EXIT CODE INTO THE UPPER 28 BITS OF THE FLAGS MEMBER WITH ITS LOWER 4 BITS REPRESENTING ITS STATE
		if(header->terminated_queue->head==NULL||pid < header->terminated_queue->head->pid){ //IF THE QUEUE IS EMPTY OR THE PID OF THE NODE IS THE MINIMUM OUT OF EVERYTHING
			iterator_r->next = header->terminated_queue->head; //ADD IT TO THE FRONT
			header->terminated_queue->head = iterator_r;
			header->terminated_queue->count+=1;
			return exit_code;
		}
		else{
			process_node_t* iterator_t = header->terminated_queue->head;
			while(iterator_t->next!=NULL && iterator_t->next->pid < pid) //OR GO THRU THE QUEUE UNTIL U FIND THE RIGHT PLACE FOR THE PROCESS TO BE IN BASED ON ITS PID
				iterator_t = iterator_t->next;
			iterator_r->next = iterator_t->next;
			iterator_t->next = iterator_r;
			header->terminated_queue->count+=1; //INCREMENT THE COUNT
			return exit_code;
		}
	}
	else if(found_s==1){
		if(header->suspended_queue->head->pid==pid){ //IF THE PROCESS WE WANT TO ADD IS IN THE BEGGINNING OF THE SUSPENDED QUEUE
			iterator_s = header->suspended_queue->head;
			header->suspended_queue->head = currenttwo->next;
			iterator_s->next = NULL;
			header->suspended_queue->count-=1; //DECREMENT THE COUNT
		}
		else{
			while(iterator_s->pid!=pid){//ITERATE THRU THE QUEUE TO FIND THE PROCESS WITH OUR PID
				previoustwo = currenttwo;
				currenttwo = currenttwo->next;
				iterator_s = iterator_s->next;
	      		}
			previoustwo->next = currenttwo->next;
			iterator_s->next = NULL;
			header->suspended_queue->count-=1;//DECREMENT THE COUNT
		}
		iterator_s->next = NULL;
		iterator_s->flags <<= 0x00000001;//GOING FROM SUSPENDED TO TERMINATED STATE REQUIRES A LEFT SHIFT OF 1 BIT
		shift = exit_code << 0x00000004; //APPLYING A 4 BIT LEFT SHIFT TO MOVE THE LOWER 28 BITS
		iterator_s->flags |= shift; //PUTTING THE LOWER 28 BITS OF THE EXIT CODE INTO THE UPPER 28 BITS OF THE FLAGS MEMBER WITH ITS LOWER 4 BITS REPRESENTING ITS STATE
		if(header->terminated_queue->head==NULL||pid < header->terminated_queue->head->pid){ //IF THE QUEUE IS EMPTY OR THE PID OF THE NODE IS THE MINIMUM OUT OF EVERYTHING
			iterator_s->next = header->terminated_queue->head; //ADD IT TO THE FRONT
			header->terminated_queue->head = iterator_s;
			header->terminated_queue->count+=1;
			return exit_code;
		}
		else{
			process_node_t* iterator_t = header->terminated_queue->head; //OR GO THRU THE QUEUE UNTIL U FIND THE RIGHT PLACE FOR THE PROCESS TO BE IN BASED ON ITS PID
			while(iterator_t->next!=NULL && iterator_t->next->pid < pid)
				iterator_t = iterator_t->next;
			iterator_s->next = iterator_t->next;
			iterator_t->next = iterator_s;
			header->terminated_queue->count+=1;
			return exit_code;
		}
	}
	else{
		return -1;
	}
}

/* Create a new process_node_t with the given information.
 * - Malloc and copy the command string, don't just assign it!
 * Follow the specification for this function.
 * Returns the process_node_t on success or a NULL on any error.
 */
process_node_t *avan_new_process(char *command, pid_t pid, int priority, int critical) {
	process_node_t *a = NULL; //MAKING A PROCESS NAMED process_node_t OF TYPE process_node
	a = malloc(sizeof( process_node_t)); //ALLOCATING MEMORY FOR THE PROCESS NODE
	if(a==NULL){ //IF ALLOCATION OF MEMORY FOR THE NEW PROCESS WAS NOT SUCCESSFUL
		return NULL; //RETURN NULL
	}
	strncpy(a->cmd, command, MAX_CMD);//STRING COPYING cmd INTO THE STRUCT
	a->pid = pid;
	a->priority = priority;
	a->flags = 0x00000000;
	a->skips = 0;
	if(critical==1){ //IF CRITICAL IS TRUE, THE LOWER 4 BITS OF THE FLAGS MEMBER IN THE READY STATE => BINARY:1001 DECIMAL:9 HEX:0x00000009
		a->flags |= 0x00000009;
	}
	else{ //IF CRITICAL IS FALSE, WE JUST SET IT TO BE IN THE READY STATE
		a->flags |= 0x00000001;
	}
	return a;//RETURNS THE POINTER TO THE PROCESS CREATED
}

/* Schedule the next process to run from Ready Queue.
 * Follow the specification for this function.
 * Returns the process selected or NULL if none available or on any errors.
 */
process_node_t *avan_select(avan_header_t *header) {
	process_node_t* ptr_crit = header->ready_queue->head;
	process_node_t* iterator_crit = header->ready_queue->head;
	process_node_t* ptr_starve = header->ready_queue->head;
	process_node_t* iterator_starve = header->ready_queue->head;
	process_node_t* current_r = header->ready_queue->head;
	process_node_t* previous_r = header->ready_queue->head;
	process_node_t* increment = header->ready_queue->head;
	process_node_t* first = header->ready_queue->head;
	process_node_t* iterator_p = header->ready_queue->head;
	int crit_found = 0;
	int starve_found = 0;
	int starve = 0;
	int low_pid = 0;
	int pri = 0;
	int tie = 0;
	int fir = 0;
	
	if(iterator_crit==NULL){//IF THE READY QUEUE IS EMPTY
		return NULL;
	}
	while(ptr_crit!=NULL){
		if(ptr_crit->flags!=0x00000009){ //GO THRU THE QUEUE TO FIND A PROCESS WITH THE CRITICAL FLAG
			ptr_crit = ptr_crit->next;
		}
		else{
			crit_found++; //CHANGE THIS VARIABLE TO INDICATE WE FOUND A CRITICAL PROCESS
			break;
		}
	}
	if(crit_found==1){
		if(header->ready_queue->head->flags==0x00000009){//IF THE CRITICAL PROCESS IS IN THE FRONT OF THE READY QUEUE
			iterator_crit = header->ready_queue->head;
			header->ready_queue->head = current_r->next;
			iterator_crit->next = NULL;
			header->ready_queue->count-=1; //DECREMENT THE COUNT
			iterator_crit->skips = 0; //SET THE SKIPS FOR THIS PROCESS TO BE 0 SINCE WE'RE PICKING IT
			while(header->ready_queue->head!=NULL){
				header->ready_queue->head->skips += 1; //GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
				header->ready_queue->head = header->ready_queue->head->next;
			}
			header->ready_queue->head = increment;
			return iterator_crit;
		}
		else{
			while(iterator_crit->flags!=0x00000009){//ITERATING THRU TO QUEUE TO FIND OUR CRITICAL PROCESS AND REMOVE IT
				previous_r = current_r;
				current_r = current_r->next;
				iterator_crit = iterator_crit->next;
			}
			previous_r->next = current_r->next;
			iterator_crit->next = NULL;
			header->ready_queue->count-=1;//DECREMENT THE COUNT
			iterator_crit->skips = 0;//SET THE SKIPS FOR THIS PROCESS TO BE 0 SINCE WE'RE PICKING IT
			while(header->ready_queue->head!=NULL){
                                header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
                                header->ready_queue->head = header->ready_queue->head->next;
                        }
                        header->ready_queue->head = increment;
			return iterator_crit;
		}
	}
	while(ptr_starve!=NULL){//IF THERE ARE NO CRITICAL PROCESSES, WE CHECK FOR STARVING PROCESSES
		if(!(ptr_starve->skips>=MAX_SKIPS)){
			ptr_starve = ptr_starve->next;
		}
		else{
			starve_found = 1;//CHANGE THIS VARIABLE TO SHOW THERE ARE STARVING PROCESSES
			starve++; //THERE COULD BE MULTIPLE PROCESSES SO WE CHANGE THIS VARIABLE AS WELL TO SHOW THAT
		}
	}
	if(starve_found==1 && starve==1){//IF THERE'S ONLY ONE STARVING PROCESS
		if(header->ready_queue->head->skips>=MAX_SKIPS){ //AND IF ITS AT THE FRONT OF THE QUEUE
			iterator_starve = header->ready_queue->head;
			header->ready_queue->head = current_r->next;
			iterator_starve->next = NULL;
			header->ready_queue->count-=1;//DECREMENT THE COUNT
			iterator_starve->skips = 0; //SET THE SKIPS FOR THIS PROCESS TO BE 0 SINCE WE'RE PICKING IT
			while(header->ready_queue->head!=NULL){
				header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
				header->ready_queue->head = header->ready_queue->head->next;
			}
			header->ready_queue->head = increment;
			return iterator_starve;
		}
		else{
			while(!(iterator_starve->skips>=MAX_SKIPS)){ //FIND WHERE THE STARVING PROCESS IS IN THE QUEUE AND REMOVE IT
				previous_r = current_r;
				current_r = current_r->next;
				iterator_starve = iterator_starve->next;
			}
			previous_r->next = current_r->next;
			iterator_starve->next = NULL;
			header->ready_queue->count-=1;//DECREMENT THE COUNT
			iterator_starve->skips = 0;//SET THE SKIPS FOR THIS PROCESS TO BE 0 SINCE WE'RE PICKING IT
			while(header->ready_queue->head!=NULL){
				header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
				header->ready_queue->head = header->ready_queue->head->next;
			}
			header->ready_queue->head = increment;
			return iterator_starve;
		}
	}
	else if(starve_found==1 && starve>1){//IF THERE ARE MULTIPLE STARVING PROCESSES
		while(iterator_starve!=NULL){
			if(iterator_starve->skips>=MAX_SKIPS && fir==0){//WE ITERATE THRU THE QUEUE TO COMPARE THEIR PIDS USING low_pid AND FINDING THE SMALLEST ONE OUT OF THEM
				low_pid = iterator_starve->pid; //WE UPDATE THE LOWEST PID TO BE THE ONE FOR THE FIRST STARVING PROCESS WE FIND
				fir++;
				iterator_starve = iterator_starve->next;
			}
			else if(iterator_starve->skips>=MAX_SKIPS && fir>0){
				if(low_pid>iterator_starve->pid){ //IF WE FIND A PID SMALLER THAN THE ONE FOR OUR FIRST STARVING PROCESS, WE UPDATE IT ACCORDINGLY
					low_pid=iterator_starve->pid;
					iterator_starve = iterator_starve->next;
				}
				else{
					iterator_starve = iterator_starve->next;//WE KEEP GOING THRU THE QUEUE TO FIND STARVING PROCESSES AND COMPARE THEIR PIDS
				}
			}
			else{
				iterator_starve = iterator_starve->next;
			}
		}
		iterator_starve = first;
		if(header->ready_queue->head->pid==low_pid){ //IF THE STARVING PROCESS WITH THE LOWEST PID IS IN THE FRONT OF THE QUEUE
                        iterator_starve = header->ready_queue->head; //REMOVE IT
                        header->ready_queue->head = current_r->next;
                        iterator_starve->next = NULL;
                        header->ready_queue->count-=1;//DECEREMENT THE COUNT
                        iterator_starve->skips = 0; //SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
                        while(header->ready_queue->head!=NULL){
                                header->ready_queue->head->skips += 1;
                                header->ready_queue->head = header->ready_queue->head->next;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
                        }
                        header->ready_queue->head = increment;
                        return iterator_starve;
                }
                else{
                        while(iterator_starve->pid!=low_pid){//IF THE STARVING PROCESS WITH THE LOWEST PID IS NOT IN THE FRONT, ITERATE THRU THE QUEUE TO FIND IT AND REMOVE IT
                                previous_r = current_r;
                                current_r = current_r->next;
                                iterator_starve = iterator_starve->next;
                        }
                        previous_r->next = current_r->next;
                        iterator_starve->next = NULL;
                        header->ready_queue->count-=1;//DECEREMENT THE COUNT
                        iterator_starve->skips = 0;//SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
                        while(header->ready_queue->head!=NULL){
                                header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
                                header->ready_queue->head = header->ready_queue->head->next;
                        }
                        header->ready_queue->head = increment;
                        return iterator_starve;
                }

	}
	if(iterator_p->next==NULL){
		pri = iterator_p->priority; //IF THERE'S ONLY ONE PROCESS IN THE QUEUE, THAT WILL BE OUR 'BEST' PROCESS BECAUSE THERE ARE NO OTHER PROCESSES TO COMPARE PRIORITY NUMBERS
	}
	else{
		pri = iterator_p->priority;//SET OUR PRIORITY VARIABLE TO BE THE PRIORITY NUMBER OF THE FIRST PROCESS IN THE QUEUE
		while(iterator_p->next!=NULL){
			if(pri>iterator_p->next->priority){ //IF WE FIND A PROCESS WITH A PRIORITY NUMBER LOWER THAN THE ONE WE HAVE
				pri = iterator_p->next->priority; //WE UPDATE OUR VARIABLE TO BE THAT NEW PRIORITY NUMBER
				iterator_p = iterator_p->next;
			}
			else if(pri==iterator_p->next->priority){ //IF WE FIND MATCHING PRIORITY NUMBERS
				tie++; //WE INCREMENT THIS VARIABLE TO INDICATE WE HAVE A TIE
				iterator_p = iterator_p->next;
			}
			else{
				iterator_p = iterator_p->next;
			}
		}
		iterator_p = first;
		if(tie==0){ //IF THERE ARE NO MATCHING PRIORITY NUMBERS
			if(header->ready_queue->head->priority==pri){ //REMOVING THE PROCESS WITH THE LOWEST PRIORITY NUMBER AT THE BEGINNING
				iterator_p = header->ready_queue->head;
				header->ready_queue->head = current_r->next;
				iterator_p->next = NULL;
				header->ready_queue->count-=1;//DECEREMENT THE COUNT
				iterator_p->skips = 0;//SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
				while(header->ready_queue->head!=NULL){
					header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
					header->ready_queue->head = header->ready_queue->head->next;
				}
				header->ready_queue->head = increment;
				return iterator_p;
			}
			else{
				while(iterator_p->priority!=pri){ //REMOVING THE PROCESS WITH THE LOWEST PRIORITY NUMBER ELSEWHERE
					previous_r = current_r;
					current_r = current_r->next;
					iterator_p = iterator_p->next;
				}
				previous_r->next = current_r->next;
				iterator_p->next = NULL;
				header->ready_queue->count-=1;//DECEREMENT THE COUNT
				iterator_p->skips = 0;//SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
				while(header->ready_queue->head!=NULL){
					header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
					header->ready_queue->head = header->ready_queue->head->next;
				}
				header->ready_queue->head = increment;
				return iterator_p;
			}
		}
		else{ //IF THERE ARE MATCHING PRIORITY NUMBERS
			while(iterator_p!=NULL){
				if(iterator_p->priority==pri && fir==0){ //WE COMPARE THEIR PIDS INSTEAD
					low_pid = iterator_p->pid;
					fir++;
					iterator_p = iterator_p->next;
				}
				else if(iterator_p->priority==pri && fir>0){ //IF WE FIND A PROCESS WITH A LOWER PID
					if(low_pid>iterator_p->pid){
						low_pid=iterator_p->pid; //WE UPDATE IT ACCORDINGLY
						iterator_p = iterator_p->next;
					}
					else{
						iterator_p = iterator_p->next;
					}
				}
				else{
					iterator_p = iterator_p->next;
				}
			}
			iterator_p = first;
			if(header->ready_queue->head->pid == low_pid){ //REMOVING THE PROCESS WITH THE LOWEST PID AT THE FRONT
				iterator_p = header->ready_queue->head;
				header->ready_queue->head = current_r->next;
				iterator_p->next = NULL;
				header->ready_queue->count-=1;//DECEREMENT THE COUNT
                                iterator_p->skips = 0;//SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
                                while(header->ready_queue->head!=NULL){
                                        header->ready_queue->head->skips += 1; //GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
                                        header->ready_queue->head = header->ready_queue->head->next;
                                }
                                header->ready_queue->head = increment;
                                return iterator_p;
			}
			else{
				while(iterator_p->pid!=low_pid){//REMOVING THE PROCESS WITH THE LOWEST PID ELSEWHERE
                                        previous_r = current_r;
                                        current_r = current_r->next;
                                        iterator_p = iterator_p->next;
                                }
                                previous_r->next = current_r->next;
                                iterator_p->next = NULL;
                                header->ready_queue->count-=1;//DECEREMENT THE COUNT
                                iterator_p->skips = 0;//SINCE WE FINALLY CHOSE THE PROCESS, SKIPS SHOULD BE 0
                                while(header->ready_queue->head!=NULL){
                                        header->ready_queue->head->skips += 1;//GO THRU ALL THE PROCESSES IN THE QUEUE AND INCREMENT THEIR SKIPS MEMBER BY 1
                                        header->ready_queue->head = header->ready_queue->head->next;
                                }
                                header->ready_queue->head = increment;
                                return iterator_p;
                        }
		}
	}
	return NULL;


}

/* Returns the number of items in a given queue_header_t
 * Follow the specification for this function.
 * Returns the number of processes in the list or -1 on any errors.
 */
int avan_get_size(queue_header_t *ll) {
	if(ll==NULL){ //ACCOUNT FOR A NULL QUEUE
		return -1;
	}
	int len = ll->count; //THE COUNT VARIABLE REPRESENTS THE NUMBER OF ITEMS IN A QUEUE
	return len;
}

/* Frees all allocated memory in the avan_header_tr */
void avan_cleanup(avan_header_t *header) { //ITERATING THRU ALL THE NODES IN THE QUEUES AND FREEING THEM
	while(header->ready_queue->head!=NULL){
		process_node_t *iterator_r = header->ready_queue->head;
		header->ready_queue->head = header->ready_queue->head->next;
		free(iterator_r);
	}
	while(header->suspended_queue->head!=NULL){
                process_node_t *iterator_s = header->suspended_queue->head;
                header->suspended_queue->head = header->suspended_queue->head->next;
                free(iterator_s);
        }
	while(header->terminated_queue->head!=NULL){
                process_node_t *iterator_t = header->terminated_queue->head;
                header->terminated_queue->head = header->terminated_queue->head->next;
                free(iterator_t);
        }
	free(header->ready_queue); //FREEING THE QUEUES
	free(header->suspended_queue);
	free(header->terminated_queue);
	free(header); //FREEING THE HEADER

}
