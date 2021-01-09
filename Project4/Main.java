import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Main {
    static int MAX = 10;

    String buffer[] = new String[MAX];

    int fill = 0;
    int use = 0;
    boolean terminateProducer = false;
    boolean terminateConsumer = false;

    Semaphore empty = new Semaphore(MAX);
    Semaphore full = new Semaphore(0);
    Semaphore lock = new Semaphore(1);

    public void put(String word) {
        buffer[fill] = word;
        fill = (fill + 1) % MAX;
    }

    public String get() {
        String temp = buffer[use];
        use = (use + 1) % MAX;
        return temp;
    }

    public void setTerminateProducer(){
        terminateProducer = true;
    }

    public void setTerminateConsumer(){
        terminateConsumer = true;
    }

    public class ProdThread extends Thread {
        public void run() {
            System.out.print("Enter a file name to open for input: ");
            Scanner scanner = new Scanner(System.in);
            File text = new File(scanner.nextLine());
            scanner.close();
            try {
                scanner = new Scanner(text);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNext()) {
                try {
                    empty.acquire();
                    // System.out.println("P (full, empty) = (" + full.availablePermits() + ", " + empty.availablePermits() + ")");
                    put(scanner.next());
                    // System.out.println("P(" + fill + ", " + use + "): " + Arrays.toString(buffer));
                    full.release();
                } 
                catch (InterruptedException e) {
                    System.out.println("Problem with Producer Thread.");
                }
            }
            System.out.println("Finished reading the file.");
            setTerminateProducer();
            scanner.close();
        }
    }

    public class ConsThread extends Thread{
        private HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
        private String name;
        public ConsThread(String name){
            this.name = name;
        }
        
        public void run(){
            String temp;
            while(!(terminateConsumer)){
                try{
                    if(full.tryAcquire()){
                        // System.out.println(name+ "(full, empty) = (" + full.availablePermits() + ", " + empty.availablePermits() + ")");
                        lock.acquire();
                        temp = get();    
                        lock.release(); 
                        empty.release();
                        int count = wordCounts.containsKey(temp) ? wordCounts.get(temp) : 0;
                        wordCounts.put(temp, count+1);
                    }
                    else{
                        if(terminateProducer){
                            setTerminateConsumer();
                        }
                    }
                }
                catch(InterruptedException e){
                    System.out.println("Problem with Consumer Thread");
                }
            }
        }
        public HashMap<String, Integer> getWordCounts(){
            return wordCounts;
        }
    }

    public void begin() throws FileNotFoundException, InterruptedException {
        ProdThread p = new ProdThread();
        ConsThread c1 = new ConsThread("c1");
        ConsThread c2 = new ConsThread("c2");

        p.start();
        c1.start();
        c2.start();

        p.join();
        c1.join();
        c2.join();

        HashMap<String, Integer> c1_wordCounts = c1.getWordCounts();
        // System.out.println(c1_wordCounts);
        HashMap<String, Integer> c2_wordCounts = c2.getWordCounts();
        // System.out.println(c2_wordCounts);

        Set<String> allwords = new HashSet<String>(c1_wordCounts.keySet());
        allwords.addAll(new HashSet<String>(c2_wordCounts.keySet()));

        for(String word: allwords){
            int count = c1_wordCounts.containsKey(word) ? c1_wordCounts.get(word) : 0;
            count += c2_wordCounts.containsKey(word) ? c2_wordCounts.get(word) : 0;
            System.out.println(word + " " + count);
        }
    }


    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Main m = new Main();
        m.begin();
    }

}