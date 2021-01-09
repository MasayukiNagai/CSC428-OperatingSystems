# Least-Recently Used (LRU) Page Algorithm Simulator
This is a basic simulator that implements the Least-Recently Used (LRU) page algorithm. 

## Usage

```sh
$ javac LRU.java
$ java LRU
```

## Input

Users will be asked to enter the following on stdin

* How many pages the process will have in its virtual memory
* How many frames of physical memory are allocated for the process
* How many page references the user would like to simulate

## Output 

* The number of page faults generated during the simulation, as a number and as a percentage of total page references.