/* This is the only file you should update and submit. */

/* Fill in your Name and GNumber in the following two comment fields
 * Name: Akram Beshir
 * GNumber: G01128864
 */

#include <sys/wait.h>
#include "taskctl.h"
#include "parse.h"
#include "util.h"

/* Constants */
#define DEBUG 1

 
// uncomment if you want to use any of these:

#define NUM_PATHS 2
#define NUM_INSTRUCTIONS 10

//static const char *task_path[] = { "./", "/usr/bin/", NULL };
//static const char *instructions[] = { "quit", "help", "list", "purge", "exec", "bg", "kill", "suspend", "resume", "pipe", NULL};


void handler(int sig) {
	log_kitc_ctrl_c();
	//exit(0);
}

void handle(int sig) {
        log_kitc_ctrl_z();
	//signal(SIGTSTP, handle);
        //exit(0);
}


/* The entry of your task controller program */
int main() {
    char cmdline[MAXLINE];        /* Command line */
    char *cmd = NULL;
    /* Intial Prompt and Welcome */
    log_kitc_intro();
    log_kitc_help();

    //int num = 0;
    //bool built = false;

    typedef struct process_node {
	    int numb; //process number
	    int state; //status of the process
	    int exitc; //exit code
	    int pi; // PID of the Process you're Tracking
	    char cm[MAXLINE]; // Name of the Process being run
	    struct process_node *next; // Pointer to next Process Node in a linked list.
    } process_node_t;

    typedef struct queue_header {
	    int count; // How many items are in this linked list?
	    process_node_t *head; // Points to FIRST node of linked list.  No Dummy Nodes.
    } queue_header_t;

    queue_header_t *z = NULL;
    z = malloc(sizeof(queue_header_t));
    z->count = 0;
    z->head = NULL;

    /*process_node_t *a = NULL;
    a = malloc(sizeof(process_node_t));
    a->status = LOG_STATE_READY;
    a->exitc = 0;
    a->pi = 0;*/
    /* Shell looping here to accept user command and execute */
    while (1) {
        char *argv[MAXARGS+1];        /* Argument list */
        Instruction inst;           /* Instruction structure: check parse.h */

        /* Print prompt */
        log_kitc_prompt();

        /* Read a line */
        // note: fgets will keep the ending '\n'
	errno = 0;
        if (fgets(cmdline, MAXLINE, stdin) == NULL) {
            if (errno == EINTR) {
                continue;
            }
            exit(-1);
        }

        if (feof(stdin)) {  /* ctrl-d will exit text processor */
          exit(0);
        }

        /* Parse command line */
        if (strlen(cmdline)==1)   /* empty cmd line will be ignored */
          continue;     

        cmdline[strlen(cmdline) - 1] = '\0';        /* remove trailing '\n' */

        cmd = malloc(strlen(cmdline) + 1);          /* duplicate the command line */
        snprintf(cmd, strlen(cmdline) + 1, "%s", cmdline);

        /* Bail if command is only whitespace */
        if(!is_whitespace(cmd)) {
            initialize_command(&inst, argv);    /* initialize arg lists and instruction */
            parse(cmd, &inst, argv);            /* call provided parse() */

            if (DEBUG) {  /* display parse result, redefine DEBUG to turn it off */
                debug_print_parse(cmd, &inst, argv, "main (after parse)");
	    }

            /* After parsing: your code to continue from here */
            /*================================================*/

	    //{instruct = 0x605bb0 "ls", num = 0, num2 = 0, infile = 0x0, outfile = 0x0}

	    if(strcmp(cmd, "help") == 0){ //IF THE BUILT-IN INSTRUCTION IS "help"
                            log_kitc_help(); //Outputs the Help: All the Built-in Commands
            }
            else if(strcmp(cmd, "quit") == 0){ //IF THE BUILT-IN INSTRUCTION IS "quit"
		    log_kitc_quit(); //Displays the goodbye message
		    exit(0); //exit the task controller
            }
	    else if(strcmp(cmd, "list") == 0){ //IF THE BUILT-IN INSTRUCTION IS "list"
		    log_kitc_num_tasks(z->count); //display number of tasks
		    process_node_t *iterate = z->head;
                    while(iterate!=NULL){ //Iterate through the entire linked list and dsiplay info about each task
			    log_kitc_task_info(iterate->numb, iterate->state, iterate->exitc, iterate->pi, iterate->cm);
                            iterate=iterate->next;
		    }
	    }
	    else if(strncmp(cmd, "purge", 5) == 0){ ////IF THE BUILT-IN INSTRUCTION IS "purge"
		    //bool exist = false;
		    int i = 5;//Start at the index after the built-in command
		    while(cmdline[i]==' '){ //Keep iterating until we're not at a space
			    i++;
		    }
		    char *x = &cmdline[i]; //Get the task number
		    int d = atoi(x); //Convert to an int
		    process_node_t* ptr = z->head;
		    process_node_t *iterate = z->head;
		    process_node_t* current = z->head;
		    process_node_t* previous = z->head;
		    while(ptr!=NULL && ptr->numb!=d){ //Keep iterating until you find the task number or reach the end of the list
			    ptr = ptr->next;
		    }
		    if(ptr==NULL){ //If the task number doesn't exist
			    log_kitc_task_num_error(d);//Display the Output for when the given task number is not found
		    }
		    else if(ptr->state==LOG_STATE_RUNNING||ptr->state==LOG_STATE_SUSPENDED){//If the task is busy
			    log_kitc_status_error(d, ptr->state);// Outputs a notification of an error due to action in an incompatible state.
		    }
		    else{
			    if(z->head->numb==d){//If the task is at the front
				    iterate = z->head;
				    z->head = current->next;//Set the head to be the task after the one to be purged
				    iterate->next = NULL;//purge the task
				    z->count-=1;
				    log_kitc_purge(d);//Outputs a notification of a task deletion
			    }
			    else{
				    while(iterate->numb!=d){//Iterate until you find your task
					    previous = current;
					    current = current->next;
					    iterate = iterate->next;
				    }
				    previous->next = current->next;
				    iterate->next = NULL;//delete the task
                                    z->count-=1;
				    log_kitc_purge(d);//Outputs a notification of a task deletion
			    }
		    }
	    }
	    else if(strncmp(cmd, "exec", 4) == 0){ //IF BUILT IN COMMAND IS "exec"
		    char path[] = "./"; //Have both paths ready to check
		    char path2[] = "/usr/bin/";
		    int i = 4;//Start at the index after the built-in command
                    while(cmdline[i]==' '){ //Keep iterating until we're not at a space
                            i++;
                    }
                    char *x = &cmdline[i]; //Get the task number
                    int d = atoi(x); //Convert to an int
		    process_node_t *ptr = z->head; //Start at the beginning of the linked list of tasks
		    while(ptr!=NULL && ptr->numb!=d){ //Keep iterating until you find the task number or reach the end of the list
                            ptr = ptr->next;
                    }
		    if(ptr==NULL){ //If the task number doesn't exist
                            log_kitc_task_num_error(d); //Display the Output for when the given task number is not found
                    }
		    else if(ptr->state==LOG_STATE_RUNNING||ptr->state==LOG_STATE_SUSPENDED){ //If the task is busy
                            log_kitc_status_error(d, ptr->state); // Outputs a notification of an error due to action in an incompatible state.
                    }
		    else{
			    int a,b;
			    int ye = 1;
			    for(int i = 0; i<strlen(ptr->cm); i++){//Checks how many spaces are in the full command
		    		    if(ptr->cm[i]==' '){
				    	    ye++;
				    }
			    }
			    ye++;
			    char *words[ye];//Sets up an array of strings for each command
			    char delim[] = " ";
			    char *ptrh = strtok(ptr->cm, delim);
			    int j = 0;
		    	    while(ptrh != NULL){
				    words[j] = ptrh; //Puts each command into the array
				    ptrh = strtok(NULL, delim);
				    j++;
			    }
			    words[j] = NULL;
			    a = strlen(path) + strlen(words[0]);//length of path summed with length of the argv[0]
		    	    char process[a]; //Set up a character array of that length
			    strcpy(process, path); //Copy the path
			    strcat(process, words[0]); //Concatenate argv[0]
			    b = strlen(path2) + strlen(words[0]); //length of path2 summed with length of the argv[0]
		    	    char process2[b];//Set up a character array of that length
			    strcpy(process2, path2);//Copy the path2
			    strcat(process2, words[0]);//Concatenate argv[0]
			    int exitnum = 0;
			    int status;
			    struct sigaction sa = {0};
			    sa.sa_handler = handler;
			    sigaction(SIGINT, &sa, NULL); //Signal handler to handle ctrl-c
			    struct sigaction s = {0};
                            s.sa_handler = handle;
                            sigaction(SIGTSTP, &s, NULL); //Signal handler to handle ctrl-z
			    pid_t pid = fork();
			    if(pid==0){
				    if(execv(process, words)!=-1){ //Check path 1 for command
					    execv(process, words); //Excecute if successful
				    }
				    else if(execv(process2, words)!=-1){ //Check path 2 for command
					    execv(process2, words); //Excecute if successful
				    }
				    else{
					    //ptr->exitc = 1;
					    exitnum = 1;
					    log_kitc_exec_error(ptr->cm); // Output when the command is not found
					    exit(exitnum);
					    //ptr->exitc = 1;
				    }
			    }
			    else{
				    //ptr->pi = getpid();
				    log_kitc_status_change(ptr->numb, pid, LOG_FG, ptr->cm, LOG_START);//Output when a task changes state
				    ptr->state = LOG_STATE_RUNNING; //Change the state to running
				    waitpid(pid, &status, 0); //Wait until the process is done
				    ptr->pi = pid;
				    /*if(execv(process, words)!=-1 && (execv(process2, words)!=-1)){
					    ptr->exitc = 1;
				    }*/
				    if(WIFEXITED(status)){ //If terminated normally
					    log_kitc_status_change(ptr->numb, pid, LOG_FG, ptr->cm, LOG_TERM);
					    ptr->state = LOG_STATE_FINISHED;
				    }
				    else if(WIFSIGNALED(status)){ //If killed by a signal
					    log_kitc_status_change(ptr->numb, pid, LOG_FG, ptr->cm, LOG_TERM_SIG);
                                            ptr->state = LOG_STATE_FINISHED;
				    }
			    }
			    if(exitnum==1){
				    ptr->exitc = 1;
			    }
			    else{
				    ;
			    }
		    }
	    }
	    else if(strncmp(cmd, "bg", 2) == 0){ //IF BUILT IN COMMAND IS "bg"
                    char path[] = "./";  //Get paths ready
                    char path2[] = "/usr/bin/";
                    int i = 2;
                    while(cmdline[i]==' '){
                            i++;
                    }
                    char *x = &cmdline[i]; 
                    int d = atoi(x); //get task number
                    process_node_t *ptr = z->head;
                    while(ptr!=NULL && ptr->numb!=d){ //go thru list til u find the task or reach the end of the list
                            ptr = ptr->next;
                    }
                    if(ptr==NULL){
                            log_kitc_task_num_error(d); //Task was not found
                    }
                    else if(ptr->state==LOG_STATE_RUNNING||ptr->state==LOG_STATE_SUSPENDED){ //If task is busy
                            log_kitc_status_error(d, ptr->state); //Outputs a notification of an error due to action in an incompatible state.
                    }
		    else{
                            int a,b;
                            int ye = 1;
                            for(int i = 0; i<strlen(ptr->cm); i++){
                                    if(ptr->cm[i]==' '){
                                            ye++;
                                    }
                            }
                            ye++;
                            char *words[ye];//Sets up an array of strings for each command
                            char delim[] = " ";
                            char *ptrh = strtok(ptr->cm, delim);
                            int j = 0;
                            while(ptrh != NULL){
                                    words[j] = ptrh;
                                    ptrh = strtok(NULL, delim);
                                    j++;
                            }
                            words[j] = NULL;
                            a = strlen(path) + strlen(words[0]); //Length of path 1 summed with argv[0]
                            char process[a];//Sets up a char array of that length
                            strcpy(process, path); //Copy the path
                            strcat(process, words[0]); //Concatenate the argv[0]
                            b = strlen(path2) + strlen(words[0]);
                            char process2[b];
                            strcpy(process2, path2);
                            strcat(process2, words[0]);
                            int exitnum = 0;
			    struct sigaction sa = {0};
                            sa.sa_handler = handler;
                            sigaction(SIGINT, &sa, NULL);
                            struct sigaction s = {0};
                            s.sa_handler = handle;
                            sigaction(SIGTSTP, &s, NULL);
                            int status;
                            pid_t pid = fork();
                            if(pid==0){
                                    if(execv(process, words)!=-1){//Check the path
                                            execv(process, words);
                                    }
                                    else if(execv(process2, words)!=-1){//Check the other path
                                            execv(process2, words);
                                    }
                                    else{
                                            //ptr->exitc = 1;
                                            exitnum = 1;
                                            log_kitc_exec_error(ptr->cm);//Output when the command is not found
                                            exit(exitnum);
                                            //ptr->exitc = 1;
                                    }
                            }
			    else{
                                    //ptr->pi = getpid();
                                    log_kitc_status_change(ptr->numb, pid, LOG_BG, ptr->cm, LOG_START);
				    ptr->state = LOG_STATE_RUNNING;
                                    //waitpid(pid, &status, 0);
                                    ptr->pi = pid;
				    //int abel = kill(ptr->pi, 17);
				    //waitpid(pid, &status, 0);
                                    /*if(execv(process, words)!=-1 && (execv(process2, words)!=-1)){
                                            ptr->exitc = 1;
                                    }*/
                                    if(WIFEXITED(status)){
                                            log_kitc_status_change(ptr->numb, pid, LOG_FG, ptr->cm, LOG_TERM);
                                            ptr->state = LOG_STATE_FINISHED;
                                    }
                                    else if(WIFSIGNALED(status)){
                                            log_kitc_status_change(ptr->numb, pid, LOG_FG, ptr->cm, LOG_TERM_SIG);
                                            ptr->state = LOG_STATE_FINISHED;
                                    }
				    //waitpid(pid, &status, 0);
                            }
                            if(exitnum==1){
                                    ptr->exitc = 1;
                            }
                            else{
                                    ;
                            }
                    }
            }
	    else if(strncmp(cmd, "pipe", 4) == 0){ //IF BUILT IN COMMAND IS "pipe"
		    bool found1 = true;
		    bool found2 = true;
		    int i = 4;
                    while(cmdline[i]==' '){
                            i++;
                    }
                    char *x = &cmdline[i];
                    int d = atoi(x);
		    i++;
		    while(cmdline[i]==' '){
                            i++;
                    }
		    char *y = &cmdline[i];
                    int e = atoi(y);
		    if(d==e){ //If the task numbers are the same
			    log_kitc_pipe_error(d); //Outputs a notification of an error piping a program's output to itself
		    }
		    process_node_t* ptr = z->head;
                    while(ptr!=NULL && ptr->numb!=d){ //Iterate til task 1 is found
                            ptr = ptr->next;
                    }
                    if(ptr==NULL){ //if task 1 isn't found
			    found1 = false;
                            log_kitc_task_num_error(d);
                    }
                    else if(ptr->state==LOG_STATE_RUNNING||ptr->state==LOG_STATE_SUSPENDED){ //If task one is busy
			    found1 = false;
                            log_kitc_status_error(d, ptr->state);
                    }
		    process_node_t* ptr2 = z->head;
		    while(ptr2!=NULL && ptr2->numb!=e){ //Iterate til task 2 is found
                            ptr2 = ptr2->next;
                    }
                    if(ptr2==NULL){ //If task 2 is not found
                            found2 = false;
                            log_kitc_task_num_error(e);
                    }
                    else if(ptr2->state==LOG_STATE_RUNNING||ptr2->state==LOG_STATE_SUSPENDED){ //If task two is busy
                            found2 = false;
                            log_kitc_status_error(d, ptr2->state);
                    }
		    if(found1!=false && found2!=false){
			    log_kitc_pipe(d,e);
		    }
		    
	    }

	    else if(strncmp(cmd, "kill", 4) == 0){ //IF BUILT IN COMMAND IS "kill"
                    //bool exist = false;
                    int i = 4;
                    while(cmdline[i]==' '){
                            i++;
                    }
                    char *x = &cmdline[i];
                    int d = atoi(x);//Get task number
                    process_node_t* ptr = z->head;
                    while(ptr!=NULL && ptr->numb!=d){ //Iterate til task is found
                            ptr = ptr->next;
                    }
                    if(ptr==NULL){ //If task number not found
                            log_kitc_task_num_error(d);
                    }
                    else if(ptr->state==LOG_STATE_READY||ptr->state==LOG_STATE_FINISHED||ptr->state==LOG_STATE_KILLED){//if task is busy
                            log_kitc_status_error(d, ptr->state);
                    }
		    else{
			    kill(ptr->pi, 2);//2 is the id for the SIGINT signal
			    log_kitc_sig_sent(LOG_CMD_KILL, ptr->numb, ptr->pi);
		    }
	    }
	    else if(strncmp(cmd, "suspend", 7) == 0){ //IF BUILT IN COMMAND IS "suspend"
                    //bool exist = false;
                    int i = 7;
                    while(cmdline[i]==' '){
                            i++;
                    }
                    char *x = &cmdline[i];
                    int d = atoi(x);//Get task number
                    process_node_t* ptr = z->head;
                    while(ptr!=NULL && ptr->numb!=d){ //Iterate til task is found
                            ptr = ptr->next;
                    }
                    if(ptr==NULL){ //If task not found
                            log_kitc_task_num_error(d);
                    }
                    else if(ptr->state==LOG_STATE_READY||ptr->state==LOG_STATE_FINISHED||ptr->state==LOG_STATE_KILLED){//If task is busy
                            log_kitc_status_error(d, ptr->state);
                    }
                    else{
                            kill(ptr->pi, 20);//20 is the id for the SIGTSTP signal
                            log_kitc_sig_sent(LOG_CMD_SUSPEND, ptr->numb, ptr->pi);
                    }
            }
	    else if(strncmp(cmd, "resume", 6) == 0){ //IF BUILT IN COMMAND IS "resume"
                    //bool exist = false;
                    int i = 6;
                    while(cmdline[i]==' '){
                            i++;
                    }
                    char *x = &cmdline[i];
                    int d = atoi(x);//Get task number
                    process_node_t* ptr = z->head;
                    while(ptr!=NULL && ptr->numb!=d){ //Iterate til task is found
                            ptr = ptr->next;
                    }
                    if(ptr==NULL){ //If task not found
                            log_kitc_task_num_error(d);
                    }
                    else if(ptr->state==LOG_STATE_READY||ptr->state==LOG_STATE_FINISHED||ptr->state==LOG_STATE_KILLED){//If task is busy
                            log_kitc_status_error(d, ptr->state);
                    }
                    else{
                            kill(ptr->pi, 18);//18 is the id for the SIGCONT signal
                            log_kitc_sig_sent(LOG_CMD_RESUME, ptr->numb, ptr->pi);
                    }
            }
	    else{ //NOT A BUILT IN COMMAND
		    process_node_t *a = NULL; //Making a new process
		    a = malloc(sizeof(process_node_t)); //Allocating memory for such process
		    a->state = LOG_STATE_READY; //New processes are always in the ready state
                    a->exitc = 0; //New processes have an exit code of 0
                    a->pi = 0; //New processes have a pid of 0
		    //a->cm[MAXLINE] = *cmd;
                    strncpy(a->cm, cmd, MAXLINE);
		    if(z->head==NULL||z->head->numb!=1){ //If the new process is our 1st process or if task number 1 is not taken
			    a->numb = 1;
			    a->next = z->head;
			    z->head = a;
			    z->count+=1;
		    }
		    else{
			    process_node_t *iterate = z->head;
			    int xo = 2;
			    while(iterate->next!=NULL && iterate->next->numb==xo){ //Iterate thru list til u find the right place to add task so list will be in sequential order of task #'s
				    iterate = iterate->next;
				    xo++;
			    }
			    a->numb = xo;
			    a->next = iterate->next;
			    iterate->next = a;
			    z->count+=1;
		    }
		    log_kitc_task_init(a->numb, a->cm);//Output when activating a new task
	    }

        }  // end if(!is_whitespace(cmd))


	free(cmd);
	cmd = NULL;
        free_command(&inst, argv);
    }  // end while(1)

    return 0;
}  // end main()

