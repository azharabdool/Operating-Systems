SRCDIR = src
BINDIR = bin

MAIN_CLASS = SchedulingSimulation
MAIN_CLASSFILE = $(BINDIR)/$(MAIN_CLASS).class

$(MAIN_CLASSFILE): $(wildcard $(SRCDIR)/*.java)
	javac -d "$(BINDIR)" -sourcepath "$(SRCDIR)" $^

run: $(MAIN_CLASSFILE)
	java -cp "$(BINDIR)" barScheduling.SchedulingSimulation


clean:
	rm -rf $(BINDIR)/*.class
