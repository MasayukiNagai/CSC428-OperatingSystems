import java.util.ArrayList;
// import java.util.Collections;
import java.util.Random;

public class MLFQ {

    private double CHANCE_OF_ARRIVAL = 0.25;  // This percentage represents the chance that a job will arrive in a particular second.
    private int MIN_TIME = 3;  // This number represents the minimum time a new process can take.
    private int MAX_TIME = 5;  // This number represents the maximum time a new process can take. 
    private int END_TIME = 20;  // This number represents thte ending time for the simulation.
    private int TIME_QUANTUM = 3;  // This number represents the time quantum for the algorithm. 

    private int numQueues = 5;  // This number represents the number of queues (priority levels)

    private int currentTime;
    private int currentQuantumPeriod;
    private ArrayList<Job> arrivedJobs;
    private ArrayList<ArrayList<Job>> waitingJobs;

    private int jobId;;

    public MLFQ(){
        this.currentTime = 0;
        this.currentQuantumPeriod = 0;
        jobId = 1;
        enforceRestrictions();
        this.arrivedJobs = new ArrayList<Job>();
        this.waitingJobs = new ArrayList<ArrayList<Job>>();
        for(int i = 0; i < numQueues; i++)
            this.waitingJobs.add(new ArrayList<Job>());
    }

    public MLFQ(double chance, int min_time, int max_time, int end, int quantum){
        this.CHANCE_OF_ARRIVAL = chance;
        this.MIN_TIME = min_time;
        this.MAX_TIME = max_time;
        this.END_TIME = end;
        this.TIME_QUANTUM = quantum;
        this.currentTime = 0;
        this.currentQuantumPeriod = 0;
        jobId = 1;
        enforceRestrictions();

        this.arrivedJobs = new ArrayList<Job>();
        this.waitingJobs = new ArrayList<ArrayList<Job>>();
        for(int i = 0; i < numQueues; i++)
            this.waitingJobs.add(new ArrayList<Job>());
    }

    public void enforceRestrictions(){
        if(this.CHANCE_OF_ARRIVAL < 0){
            System.out.println("Warning: CHANCE_OF_ARRIVAL cannot be less than 0. It's set to 0.");
            this.CHANCE_OF_ARRIVAL = 0;
        }
        else if(this.CHANCE_OF_ARRIVAL > 1){
            System.out.println("Warning: CHANCE_OF_ARRIVAL cannot be greater than 0. It's set to 1.");
            this.CHANCE_OF_ARRIVAL = 1;
        }
        
        if(this.MIN_TIME > this.MAX_TIME){
            System.out.println("Warning: MIN_TIME must be smaller than MAX_TIME. They are swapped.");        
            int temp = this.MIN_TIME;
            this.MIN_TIME = this.MAX_TIME;
            this.MAX_TIME = temp;
        }
    }

    public Job generateJob(){
        Random r = new Random();
        if (r.nextDouble() <= this.CHANCE_OF_ARRIVAL){
            int runningTime = this.MIN_TIME + r.nextInt(MAX_TIME - MIN_TIME + 1);
            int priority = numQueues;
            Job newJob = new Job(this.jobId, runningTime, currentTime, priority);
            return newJob;
        }
        return null;
    }

    public Job getNextJob(){
        for(ArrayList<Job> PriorityQueue: this.waitingJobs){
            if(PriorityQueue.size() != 0)
                return PriorityQueue.remove(0);
        }
        return null;
    }

    public boolean isAnyJobWaiting(){
        for(ArrayList<Job> PriorityQueue: this.waitingJobs){
            if(PriorityQueue.size() != 0)
                return true;
        }
        return false;
    }

    public ArrayList<Integer> getAllWaitingJobsId(){
        ArrayList<Integer> allWaitingJobsId = new ArrayList<Integer>();
        for(ArrayList<Job> PriorityQueue: this.waitingJobs){
            for(Job waitingJob: PriorityQueue)
                allWaitingJobsId.add(waitingJob.getJobId());
        }
        // Collections.sort(allWaitingJobsId);
        return allWaitingJobsId;
    }

    public void putJobBackToWaitingLine(Job job){
        if (job.getPriority() > 1){
            job.decreasePriority(1);
        }
        this.waitingJobs.get(numQueues - job.getPriority()).add(job);   
    }

    public void reportStatus(int currentTime, Job arrivedJob, Job currentJob){
        System.out.println("---------------------------------------------------------");
        System.out.printf("Second %d:\n", currentTime);
        // report on a job that has just arrived 
        if(arrivedJob != null)
            System.out.printf("Job #%d arrives, requiring %d seconds to complete.\n", arrivedJob.getJobId(), arrivedJob.getRunningTime());
        
        // report on a job that is currently running on the CPU
        if(currentJob != null)
            System.out.printf("Job #%d is running on the CPU, with %d seconds left.\n", currentJob.getJobId(), (currentJob.getRemainingTime()+1));
        else
            System.out.println("No job is running on the CPU right now.");
        
        // report on waiting jobs
        ArrayList<Integer> allWaitingJobsId = getAllWaitingJobsId();
        if(allWaitingJobsId.size() != 0){
            String report = "Jobs #";
            for(int i = 0; i < allWaitingJobsId.size(); i++){
                if(i != allWaitingJobsId.size() - 1)
                    report += allWaitingJobsId.get(i).toString() + ", ";
                else
                    report += allWaitingJobsId.get(i).toString() + " are waiting.";
            }
            System.out.println(report);
        }
        else
            System.out.println("No job is waiting to be executed.");
    }

    public void reportInfoForEachJob(){
        for(Job job: this.arrivedJobs){
            if(job.getTurnaroundTime() >= 0){
                System.out.printf("Job #%d arrived at time %d and finished at time %d.\n", job.getJobId(), job.getJobArrivalTime(), job.getCompletionTime());
                System.out.printf("Its turn around time was %d and response time was %d.\n", job.getTurnaroundTime(), job.getResponseTime());
            }
            else{
                System.out.printf("Job #%d arrived at time %d and has NOT finished yet.\n", job.getJobId(), job.getJobArrivalTime());
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    public void reportSimulationInfo(){
        System.out.println("CHANCE_OF_ARRIVAL: " + CHANCE_OF_ARRIVAL);
        System.out.println("MIN_TIME: " + MIN_TIME);
        System.out.println("MIN_TIME: " + MAX_TIME);
        System.out.println("END_TIME: " + END_TIME);
        System.out.println("TIME_QUANTUM: " + TIME_QUANTUM);
        System.out.println("---------------------------------------------------------");
    }

    public void run() throws InterruptedException {
        System.out.println("Start MLFQ CPU Scheduling Simulation");
        reportSimulationInfo();
        Job currentJob = null;
        long millis;
        while(this.currentTime < this.END_TIME){
            millis = System.currentTimeMillis();
            Thread.sleep(1000 - millis % 1000);

            // check if a new job has arrived
            Job arrivedJob = generateJob();
            if (arrivedJob != null){
                this.arrivedJobs.add(arrivedJob);
                this.jobId++;
                this.waitingJobs.get(0).add(arrivedJob);
            }

            // decide which job to run for the next one second
            if(currentJob == null || currentJob.isJobCompleted()){
                // if there is no job running or if the job has been completed,
                // get the next job 
                // quantum period is reset
                currentJob = getNextJob();
                this.currentQuantumPeriod = TIME_QUANTUM-1;
            }
            else{
                if(this.currentQuantumPeriod == 0){
                    // if the currentQuantomPeriod is over, put the job back to the wailiting line
                    // get the next job
                    // quantum peri od is reset 
                    putJobBackToWaitingLine(currentJob);
                    currentJob = getNextJob();
                    this.currentQuantumPeriod = TIME_QUANTUM-1;
                }
                else{
                    // otherwise, keep running the same job
                    // decrese quantum period
                    this.currentQuantumPeriod--;
                }
            }

            // execute a job if there is
            if(currentJob != null){
                int executionTime = 1;
                currentJob.executeJob(currentTime, executionTime);
            }

            //report
            reportStatus(currentTime, arrivedJob, currentJob);
            this.currentTime++;
        }
        System.out.printf("========== The Simulation is over at Second %d ==========\n", this.currentTime);
        reportInfoForEachJob();
    }

    public void setCHANCE_OF_ARRIVAL(double chance){
        this.CHANCE_OF_ARRIVAL = chance;
    }

    public void setMIN_TIME(int min_time){
        this.MIN_TIME = min_time;
    }

    public void setMAX_TIME(int max_time){
        this.MAX_TIME = max_time;
    }

    public void setEND_TIME(int end_time){
        this.END_TIME = end_time;
    }

    public void setTIME_QUANTUM(int time_quantum){
        this.TIME_QUANTUM = time_quantum;
    }

    public static void testCase0() throws InterruptedException{
        MLFQ simulator = new MLFQ();
        simulator.setCHANCE_OF_ARRIVAL(0.25);
        simulator.setMIN_TIME(3);
        simulator.setMAX_TIME(5);
        simulator.setEND_TIME(100);
        simulator.setTIME_QUANTUM(3);
        simulator.enforceRestrictions();
        simulator.run();
    }

    public static void testCase1() throws InterruptedException{
        MLFQ simulator = new MLFQ();
        simulator.setCHANCE_OF_ARRIVAL(0.25);
        simulator.setMIN_TIME(3);
        simulator.setMAX_TIME(5);
        simulator.setEND_TIME(100);
        simulator.setTIME_QUANTUM(3);
        simulator.enforceRestrictions();
        simulator.run();
    }

    public static void testCase2() throws InterruptedException{
        MLFQ simulator = new MLFQ();
        simulator.setCHANCE_OF_ARRIVAL(0.75);
        simulator.setMIN_TIME(3);
        simulator.setMAX_TIME(10);
        simulator.setEND_TIME(250);
        simulator.setTIME_QUANTUM(5);
        simulator.enforceRestrictions();
        simulator.run();
    }

    // If you change the values of the 5 constants above,
    // just run this test funtion 
    public static void test() throws InterruptedException{
        MLFQ simulator = new MLFQ();
        simulator.run();
    }

    public static void main(String[] args) throws InterruptedException{
        test();
        // testCase1();
        // testCase2();
    }
}