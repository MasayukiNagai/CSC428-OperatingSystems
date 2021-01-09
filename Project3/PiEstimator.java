import java.util.Random;

class PiEstimator implements Runnable{

    private int numArrowsToThrow;
    private double estPi;

    public PiEstimator(int numArrowsToThrow){
        this.numArrowsToThrow = numArrowsToThrow;
    }

    static class Coordinate{
        private double x;
        private double y;
        private Random randX;
        private Random randY;

        public Coordinate(){
            randX = new Random();
            randY = new Random();
            x = 2 * randX.nextDouble() - 1;
            y = 2 * randY.nextDouble() - 1;
        }
        
        public double calcDistanceFromOrigin(){
            return Math.sqrt(x*x + y*y);
        }
    }

    public double estimatePi(){
        int numDartsHittingDartboard = 0;
        for(int i = 0; i < this.numArrowsToThrow; i++){
            Coordinate dart = new Coordinate();
            if(dart.calcDistanceFromOrigin() <= 1)
                numDartsHittingDartboard++;
        }
        double pi = 4 * (double)numDartsHittingDartboard/numArrowsToThrow;
        this.estPi = pi;
        return pi;
    }

    public double getEstPi(){
        return this.estPi;
    }

    public void run(){
        double estPi = estimatePi();
        if(ThreadExecuter.getVerbose()){
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": " + estPi);
        }
    }

}