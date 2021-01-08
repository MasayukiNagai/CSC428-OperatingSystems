public class Job {

    private int runningTime;
    private int arrivalTime;
    private int executedTime;
    private int completionTime;
    private int firstRunTime;
    private int turnaroundTime;
    private int responseTime;
    
    private int priority;
    private int jobId;

    public Job(int jobId, int runningTime, int arrivalTime, int priority){
        this.jobId = jobId;
        this.runningTime = runningTime;
        this.arrivalTime = arrivalTime;
        this.executedTime = 0;
        this.completionTime = -1;
        this.firstRunTime = -1;
        this.turnaroundTime = -1;
        this.responseTime = -1;
        this.priority = priority;
    }

    /**
     * @param currentTime current time 
     * @param executionTime execution time period given to this job
     * @return the remaining execution time
     */
    public int executeJob(int currentTime, int executionTime){
        if(this.executedTime == 0)
            this.firstRunTime = currentTime;
        this.executedTime += executionTime;
        if(this.executedTime >= this.runningTime){
            this.completionTime = currentTime;
            this.turnaroundTime = computeTurnaroundTime();
            this.responseTime = computeResponseTime();
        }
        return this.runningTime - this.executedTime;
    }

    public boolean isJobCompleted(){
        return (this.runningTime - this.executedTime) <= 0;
    }

    public int getPriority(){
        return this.priority;
    }

    public int getJobId(){
        return this.jobId;
    }

    public int getJobArrivalTime(){
        return this.arrivalTime;
    }

    public int getCompletionTime(){
        return this.completionTime;
    }

    public int getRunningTime(){
        return this.runningTime;
    }

    public int getRemainingTime(){
        return this.runningTime - this.executedTime;
    }

    public void decreasePriority(int degree){
        this.priority -= degree;
    }

    public int computeTurnaroundTime(){
        return this.completionTime - this.arrivalTime;
    }

    public int computeResponseTime(){
        return this.firstRunTime - this.arrivalTime;
    }

    public int getTurnaroundTime(){
        return this.turnaroundTime;
    }

    public int getResponseTime(){
        return this.responseTime;
    }


}
