# Jackie Doan
Code is compiled with "MAKE" and "MAKE ALL"

The input file for the current directory is in.dat but can be changed
 if necessary

Code is run with "MAKE RUN", "java CacheSimulator in.dat",
 or "java CacheSimulator < in.dat"

The Code is in Java. The code is split up into 3 files one file to have
the variables for the hits and access, one file as a viewer, and one file
as the cache and its processes.


# Creating a Write back Cache
1. You should take an input file specifying cache configuration (Number of sets, associativity, line size), and then a list of Reads/Writes and addresses. 
Values don't matter for writes. 

2. You should track all relevant info for the access, including if it is a miss or hit and requires a mem access. 

3. Also create a summary stats (hits/misses/accesses/ratios) and print it. 

4. Sample Input File looks like below

    sets:256

    set_size:4

    line_size:4

    W:160

    R:15

    R:430

    W:430 

    Here "sets, set_size, and line_size" are ordered params. Meaning the string before ":" does not matter.

5. Number of sets should be a power of 2. 

6. Number of sets will be at most 2^13.

7. Line size should also be power of 2 and at least 4 (bytes).

8. It should use an LRU eviction policy.


# Note
1. This project can be pretty easy if you use good object-oriented style.
2. You can work on group of 2.

# Deadline - November 30, 2019

# References
Please refer to the cache.png for more details on output format and call.
