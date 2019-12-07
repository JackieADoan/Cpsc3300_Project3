SOURCE = CacheSimulator.java \
	Cache.java \
	StAcc.java \

all:
	javac $(SOURCE)

run:
	java CacheSimulator < in.dat

clean:
	rm -rf *.class
