all: sheepdog/sim/Sheepdog.class sheepdog/sim/Point.class sheepdog/sim/Player.class sheepdog/manual/Player.class sheepdog/dumb/Player.class

sheepdog/sim/Point.class: sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Player.class: sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Sheepdog.class: sheepdog/sim/Player.java sheepdog/sim/Point.java sheepdog/sim/Sheepdog.java
	javac $^

sheepdog/dumb/Player.class: sheepdog/dumb/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/manual/Player.class: sheepdog/manual/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^


.PHONY: rungui
rungui: all
	java sheepdog.sim.Sheepdog dumb 1 50 5 false true


.PHONY: clean
clean:
	$(RM) sheepdog/sim/*.class
	$(RM) sheepdog/manual/*.class
	$(RM) sheepdog/dumb/*.class
