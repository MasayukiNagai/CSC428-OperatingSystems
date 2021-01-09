import java.util.Scanner;

public class ThreadExecuter {

    private static boolean verbose = true;

    public static void run() throws InterruptedException {
        System.out.println("This program statistically simulate the value of pi");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of threads to run: ");
        int numThreadsToRun = scanner.nextInt();
        System.out.print("Enter the number of arrows to throw in each thread: ");
        int numArrowsToThrow = scanner.nextInt();
        scanner.close();
        Thread[] myThreads = new Thread[numThreadsToRun];
        PiEstimator[] estimators = new PiEstimator[numThreadsToRun];
        for(int i = 0; i < numThreadsToRun; i++){
            estimators[i] = new PiEstimator(numArrowsToThrow);
            myThreads[i] = new Thread(estimators[i]);
            myThreads[i].start();
        }
        double sumEstPi = 0;
        for(int i = 0; i < numThreadsToRun; i++){
            myThreads[i].join();
            sumEstPi += estimators[i].getEstPi();
        }
        double averageEstPi = sumEstPi/numThreadsToRun;
        System.out.println("The estimated Pi value is " + averageEstPi);
        double rse = Math.abs(averageEstPi - Math.PI);
        System.out.println("The root squared error of the estimaion is : " + rse);
    }

    public static boolean getVerbose(){
        return verbose;
    }

    public static void main(String[] args) throws InterruptedException {
        run();
    }
}
