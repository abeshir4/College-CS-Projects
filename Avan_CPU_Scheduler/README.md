Introduction

You will finish writing functions for the Avan CPU Scheduler. Finish the function definitions in
src/avan_sched.c to implement a CPU scheduler for the TRILBY Virtual Machine (VM).

Problem Background

TRILBY-VM is a lightweight process-level virtual machine that allows users to interlace Linux
programs with custom execution techniques. These machines are useful for testing complicated
interactions between processes (programs being run) by manually scheduling them in certain
orders or allow the user to run processes with custom priority orderings.

So, what is CPU scheduling and how does it work?

The idea is to pick a process and run it for a very, very short amount of time. Then, you can put it
back in a ready queue (linked list) and then pick another from that queue to run. As long as you
let each process run for a tiny amount of time, and keep swapping them out, then it seems like
your Operating System is running many different programs at the same time! This is the main idea
of multitasking. You will be writing the Avan CPU Scheduler.

Project Overview

Like industry, this project involves a lot of code written by other people. You will only be
finishing a few functions of code to add one feature to the project.
You will be finishing code in src/avan_sched.c to create, add, remove, and find nodes on three
singly linked lists in C. Each list represents a queue to manage processes in this VM. You will
also be using some bitwise operators in C to work with process flags.
Your code (avan_sched.c) works with pre-written files to implement several of these
operations. You will be maintaining three singly linked lists (Ready Queue, Suspended Queue,
and Terminated Queue). These structs are defined in inc/avan_sched.h.
You can add additional helper functions, but you cannot change how we compile it.

Summary of Functions for the Avan Scheduler to be finished:

avan_header_t *avan_create();
• Creates the Avan Header struct, which has pointers to all three Linked List Queues.

int avan_insert(avan_header_t *header, process_node_t *process);
• Inserts the Process node in ascending PID order in of the Ready Queue Linked List.

int avan_suspend(avan_header_t *header, pid_t pid);
• Remove a Process from the Ready Queue and Add it (in PID Order) to the Suspended Queue

int avan_resume(avan_header_t *header, pid_t pid);
• Remove a given Process from the Suspended Queue and Add (in PID Order) to the Ready Queue

int avan_quit(avan_header_t *header, process_node_t *process, int exit_code);
• Inserts the Process (in PID Order) to Terminated Queue

int avan_terminate(avan_header_t *header, pid_t pid, int exit_code);
• Remove a Process from the Ready or Suspended Queues and Add (in PID Order) to Terminated Queue

process_node_t *avan_new_process(char *command, pid_t pid, int priority, int critical);
• Create a new Process node from the arguments and return it.

process_node_t *avan_select(avan_header_t *header);
• Remove and return the best Process in your Ready Queue Linked List. (Details in Section 4)

int avan_get_size(queue_header_t *ll);
• Return the number of Nodes in the given Queue Linked List

void avan_cleanup(avan_header_t *header);
• Free the Avan Header, the Three Linked List Queues, and all of their Process Nodes.
• TRILBY-VM will run with no Memory Leaks
