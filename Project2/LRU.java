import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

class LRU{

    private int numFrames;
    private Hashtable<Integer, Integer> frames;
    private int numPageFaults;
    private int numReferencesSoFar;

    public LRU(int numFrames){
        this.numFrames = numFrames;
        this.frames = new Hashtable<>();
        this.numPageFaults = 0;
        this.numReferencesSoFar = 0;
    }

    public void updateFrames(int reference){
        for(Integer page: this.frames.keySet()){
            if(page == reference){
                updateWhenThePageIsReferenced(reference);
                return;
            }
        }
        
        // The referenced page is NOT in memory
        // Increment the number of page faults
        this.numPageFaults++;

        // Check if the frames of physical memory are full
        if(this.frames.size() >= this.numFrames)
            removeAPage();
        
        // Add a page to memory
        addAPage(reference);
    }

    public void updateWhenThePageIsReferenced(int page){
        this.frames.put(page, this.numReferencesSoFar);
    }

    public void removeAPage(){
        int pageToReplace = -1;
        for(Integer page: this.frames.keySet()){
            if(pageToReplace > 0){
                if(this.frames.get(pageToReplace) > this.frames.get(page))
                    pageToReplace = page;
            }
            // only the first time
            else
                pageToReplace = page;
        }
        this.frames.remove(pageToReplace);
    }

    public void addAPage(int page){
        this.frames.put(page, this.numReferencesSoFar);
    }

    public void displayInfo(int pageReference){
        System.out.println("#" + this.numReferencesSoFar + " - Page Reference: " + pageReference);
        for(Integer page: this.frames.keySet()){
            System.out.print("|" + page + "(" + this.frames.get(page) + ")");
        }
        System.out.println("|");
    }

    public static void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the simulator! We are using the LRU algorithm.");
        System.out.print("How many virtual pages are in the process: ");
        int numVirtualPages = scanner.nextInt();
        System.out.print("How many frames are allocated to the process: ");
        int numFrames = scanner.nextInt();
        System.out.print("How many page references would you like to simulate: ");
        int numTotalReferences = scanner.nextInt();
        scanner.close();

        LRU myLRU = new LRU(numFrames);
        
        Random rand = new Random();
        while(myLRU.numReferencesSoFar < numTotalReferences){
            int pageReference = rand.nextInt(numVirtualPages);
            myLRU.updateFrames(pageReference);
            myLRU.numReferencesSoFar++;
            // myLRU.displayInfo(pageReference);
        }

        float percentPageFaults = 100 * ((float)myLRU.numPageFaults/numTotalReferences);
        DecimalFormat df = new DecimalFormat("##.#");
        System.out.println("The algorithm produced " + myLRU.numPageFaults + " page faults, or " + df.format(percentPageFaults) + "%.");
    }

    public static void main(String[] args){
        run();
    }
}