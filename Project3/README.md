# Parallel Monte Carlo Simulation
This implements a Monte Carlo simulator that estimates the value of π, using threads.

## Usage

```sh
$ javac PiEstimator.java
$ java PiEstimator
```

## How

<img src="https://github.com/MasayukiNagai/CSC428-OperatingSystems/blob/main/Project3/Figure.png" alt="MLFQ_example" style="zoom:100%;" />

Randomly throw darts at the dashboard above, assuming that the darts will all land on the square and that they are evenly distriubted across the square as well. Then, we get the following:

<img src="https://github.com/MasayukiNagai/CSC428-OperatingSystems/blob/main/Project3/PiCalculation.png" alt="MLFQ_example" style="zoom:70%;" />

## Input

Users will be asked to enter the following on stdin:

* The number of threads to run (an integer between 1 and 20)
* The number of arrows to "throw" in EACH thread (a positive integer)

## Output 

* An estimate value of π by averaging the result from all the threads. 
