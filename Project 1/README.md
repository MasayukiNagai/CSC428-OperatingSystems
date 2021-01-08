# Basic Multi-Level Feedback Queue (MLFQ) CPU Scheduling Simulator
This is a basic CPU scheduling simulator that implements Multi-Level Feedback Queue algorithm. 

## Usage

```sh
$ javac MLFQ.java
$ java MLFQ
```



## Rules

* Rule 1: If Priority(A) > Priority(B), A runs (B doesn’t)
* Rule 2: If Priority(A) = Priority(B), A & B run in RR.

<img src="/Users/moon/DePauw/Courses/CSC428 Operating Systems/CSC428-OperatingSystems/Project 1/MLFQ_example.png" alt="MLFQ_example" style="zoom:70%;" />
[*Operating Systems: Three Easy Pieces](http://pages.cs.wisc.edu/~remzi/OSTEP/)

## Output 

While the simulation is running, it prints each second:

* Any job that arrives.
* The job currently running on the CPU.
* Any jobs waiting to be run.

When the simulation ends, it prints information for each job:

* The job number
* When the job arrived
* When the job finished
* The job's turnaround time
* The job's response time

 ## Reporting

* Test case 1:  The chance of arrival is 25%, the minimum length is 3, the maximum length is 5, the end time is 100, and the time quantum is 3.
* Test case 2:  The chance of arrival is 75%, the minimum length is 5, the maximum length is 10, the end time is 100, and the time quantum is 5.

