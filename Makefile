SRCDIR = src
BINDIR = bin

MAIN_CLASS = SchedulingSimulation
MAIN_CLASSFILE = $(BINDIR)/$(MAIN_CLASS).class

# Default parameters
NUM_PATRONS = 100
SCHEDULING_ALGORITHM = 0

$(MAIN_CLASSFILE): $(wildcard $(SRCDIR)/*.java)
	javac -d "$(BINDIR)" -sourcepath "$(SRCDIR)" $^

run: $(MAIN_CLASSFILE)
	java -cp "$(BINDIR)" barScheduling.SchedulingSimulation $(NUM_PATRONS) $(SCHEDULING_ALGORITHM)

clean:
	rm -rf $(BINDIR)/*.class

.PHONY: run clean
