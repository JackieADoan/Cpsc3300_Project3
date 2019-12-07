import java.util.*;

public class Cache{
    private static final int MEM_ADDR_SIZE = 32;
    private int line_size;
    private int num_sets;
    private int connects;
    private int cache_size;
    private int hits;
    private int misses;
    private int accesses;
    private int offset_size;
    private int tag_size;
    private int index_size;
    private LinkedList<Sets> sets;
    private LinkedList<CacheItem> entries;

	//Initialize the Cache.Cache
    public Cache(int setCount, int conn, int cacheSize, int lineSize){
        misses = 0;
        sets = new LinkedList<>();
        num_sets = setCount;
        hits = 0;
        line_size = lineSize;
        connects = conn;
        cache_size = cacheSize;
        entries = new LinkedList<>();
        accesses = 0;
        
        offset_size = (int) (Math.log(line_size)/(Math.log(2)));
        index_size = (int) (Math.log(num_sets)/Math.log(2));
        tag_size = MEM_ADDR_SIZE - conn - index_size;
    }
    //Gets Address Offset
    public long getOffset(long addr){
        return (addr << Long.SIZE - offset_size) >>> (Long.SIZE - offset_size);
    }
	//Gets Address Index
    public long getIndex(long addr){
        addr >>>= offset_size;
        return (addr << Long.SIZE - index_size) >>> (Long.SIZE - index_size);
    }
    //Places the new instruction into the cache
    public void makeEntry(StAcc stAcc, long address, long tag, long index, long offset){
        accesses++;
        Sets set = null;
        for (int i = 0; i < sets.size(); i++){
            Sets temp_set = sets.get(i);
            if (temp_set.index == index){
                set = temp_set;
                i = sets.size();
            }
        }
        if (set == null){
            set = new Sets(index);
            sets.add(set);
        }
        Sets.BlockChain square = set.insert(tag, stAcc);
        if (square.status == StAcc.miss){
            misses++;
        }
        else{
            hits++;
        }
        entries.add(new CacheItem(stAcc, address, tag, offset, index, square.status, square.memory));
    }
    //Gets Address Tag
    public long getTag(long addr){
        addr >>>= (offset_size + index_size);
        return (addr << Long.SIZE - index_size) >>> (Long.SIZE - index_size);
    }
	//Initializes a Set in a Cache
    private class Sets{
        private LinkedList<BlockChain> chain;
		private long index;
		//Sets Initializer
        private Sets(long in){
            index = in;
            chain = new LinkedList<>();
        }
		//Insert into a block of cache
        private BlockChain insert(long tag, StAcc acc){
            BlockChain temp  = null;
            BlockChain square = null;
            boolean dirty = (acc == StAcc.W);
			
            for (int i = 0; i < chain.size(); i++){
                temp = chain.get(i);
                if (temp.tag == tag){
                    square = temp;
                    i = chain.size();
                }
            }
			//Determines how many times the memory has been referenced
            //And do LRU Eviction as necessary
            int memory = 0;
            if (square == null){
                memory++;
                if (chain.size() == connects){
                    if (chain.remove(0).dirtyBit){
                        memory++;
                    }
                    square = new BlockChain(tag, StAcc.miss, memory, dirty);
                }
                else{
                    square = new BlockChain(tag, StAcc.miss, memory, dirty);
                }
                chain.add(square);
            }
            else{
                chain.remove(square);
                chain.add(square);
                if (!square.dirtyBit){
                    square.dirtyBit = dirty;
                }
                square.status = StAcc.hit;
                square.memory = memory;
            }
            return square;
        }
		//Block of cache in the cache Set
        private class BlockChain{
            private long tag;
            private StAcc status;
            private int memory;
            private boolean dirtyBit;
			//initialize the square of cache
            private BlockChain(long tip, StAcc stat, int memR, boolean dirty){
                tag = tip;
                status = stat;
                memory = memR;
                dirtyBit = dirty;
            }

            public String toString(){
                return "" + dirtyBit;
            }
        }
    }
	//Cache Entry Class
    private class CacheItem{
        private StAcc stAcc;
        private long address;
        private long tag;
        private long index;
        private long offset;
        private StAcc stat;
        private long memory;
		//Initialize an item in the cache
        public CacheItem(StAcc acc, long addr, long tip, long off, long in, StAcc status, long memR){
            stAcc = acc;
            address = addr;
            tag = tip;
            offset = off;
            stat = status;
            memory = memR;
            index = in;
        }

        public String toString(){
            return String.format(Locale.ENGLISH, "%6s %8x %7d %5d %6d %6s %7d\n", stAcc, address, tag, index, offset, stat, memory);
        }
    }
	//Trying to output the results of the Cache
    public void printCache(){
        System.out.printf("Cache Configuration\n\n\t%d %d-way set " + "associative entries\n\tof line size %d " + "bytes\n\n", num_sets, connects, line_size);
        System.out.printf("%-6s %-8s %-7s %-5s %-6s %-6s %-7s\n", "Access", "Address", "  Tag", "Index", "Offset", "Status", "memory");
        System.out.printf("%6s %8s %7s %5s %6s %6s %7s\n", "------", "--------", "-------", "-----", "------", "------", "-------");
        StringBuilder build = new StringBuilder();

        for (CacheItem entry : entries){
            build.append(entry.toString());
        }

        System.out.println(build);
        System.out.println("\nSimulation Summary Statistics\n" + "-----------------------------");
        System.out.printf("Total hits       : %-6d\n" + "Total misses     : %-6d\n" + "Total accesses   : %-6d\n" + "Hit ratio        : %-6f\n" + "Miss ratio       : %-6f\n", hits, misses, accesses, ((double) hits)/accesses, ((double) misses)/accesses);
    }
}
