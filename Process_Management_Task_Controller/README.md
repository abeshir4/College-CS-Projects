1. Introduction

For this assignment, you are going to use C to implement a task controller called the KI Task
Controller. Once running, KI maintains a list of several tasks, which can be executed, inspected,
or otherwise managed (e.g. by killing or suspending a running task). This assignment will help
you to get familiar with the principles of process management in a Unix-like operating system.

2. Project Overview

A typical command shell receives line-by-line instructions from the user in a terminal. In this
project, the shell is the interface to our task controller. The shell would support a set of built-in
instructions, which will then be interpreted by the shell, and acted on accordingly. In some
cases, the instructions would be requests for the system to execute other programs. In that case,
the shell would fork a new child process and execute the program in the context of the child.
The task controller also has the responsibility to maintain a list of tasks of interest, and to keep
them organized. The user is able to enter programs, then execute them in the foreground (wait
until the task completes) or background (allow the process to run while moving on to other
things). The user can also control existing tasks by temporarily suspending them, killing them,
or deleting them from the list altogether. Finally, the user will have some additional capabilities,
like redirecting the input or output of a process to or from a file, or piping the output of one task
to another. The task controller will allow the user to check the exit code of completed tasks. The
shell provides some built-in instructions to help manage and list tasks, as well as some
instructions to execute and control running commands.

For this assignment, your implementation should be able to perform the following:

• Accept a single line of instruction from the user and perform the instruction.

o The instruction may involve creating, deleting or listing tasks.

o The instruction may involve reading from or writing to a file.

o The instruction may involve loading and running a user-specified program.

• The system must support any arbitrary number of simultaneous running processes.

o KI is able to both wait for a processes to finish, or let them run in the background.

• Perform basic management of tasks, whether they are in ready mode, running, or complete;

• Use file redirects and pipes to read input from or send output to a file or another process.

• Use signals to suspend/resume or terminate running processes, and track child activity. 

I was required to create the file called: "taskctl.c"
