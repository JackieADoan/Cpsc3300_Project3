import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CacheSimulator{
	private final static int MAX_SET = 8;
    private final static int MAX_NUM = 8192;
    private final static int MIN_LINE = 4;

    public static void main(String[] args){
        Scanner scan = null;
        if (args.length == 0) {
            scan = new Scanner(System.in);
        }
        else {
            File input = new File(args[0]);
            try {
                scan = new Scanner(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        assert scan != null;
        scan.skip(".*:");
        int sets = scan.nextInt();
		//Reads in MAX_NUM
        if (sets > MAX_NUM){
            System.err.println("Too Many Sets! (MAX=" + MAX_NUM + ")");
			System.exit(0);
        }
        else if ((sets & -(sets)) != sets){
            System.err.println("Number of Sets has to be a power of 2!");
			System.exit(0);
        }
        scan.nextLine();
        scan.skip(".*:");
        int set_size = scan.nextInt();
		//Reads in MAX_SET
        if (set_size > MAX_SET){
            System.err.println("Set Size has a MAX size of " + MAX_SET + "!");
			System.exit(0);
        }
        scan.nextLine();
        scan.skip(".*:");
        int line_size = scan.nextInt();
		//Reads in MIN_LINE
        if (line_size < MIN_LINE){
            System.err.println("Line Size has a MIN size of " + MIN_LINE + "!");
			System.exit(0);
        }
        else if ((line_size & -(line_size)) != line_size){
            System.err.println("Line Size has to be a power of 2!");
			System.exit(0);
        }
        scan.nextLine();
		//Creates the Cache.Cache
        Cache cache = new Cache(sets, set_size, sets * line_size * set_size, line_size);
        StAcc stAcc = null;
        long mem_addr = -1;
		//Reads in Reads and Writes till end
        while (scan.hasNextLine()){
            String strs[] = scan.nextLine().split(":");
            stAcc = StAcc.getAccess(strs[0]);
            if (stAcc == null){
                System.err.println("Incorrect Access Parameter Usage");
				System.exit(0);
            }
            mem_addr = Long.decode("0x" + strs[1]);
            cache.makeEntry(stAcc, mem_addr, cache.getTag(mem_addr), cache.getIndex(mem_addr), cache.getOffset(mem_addr));
        }
        cache.printCache();
    }
}
