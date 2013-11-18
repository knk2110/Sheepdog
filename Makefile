all: sheepdog/sim/Sheepdog.class sheepdog/sim/Point.class sheepdog/sim/Player.class sheepdog/manual/Player.class sheepdog/g8_v1/Player.class sheepdog/g8_v2/Player.class sheepdog/g8_v3/Player.class

sheepdog/sim/Point.class: sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Player.class: sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Sheepdog.class: sheepdog/sim/Player.java sheepdog/sim/Point.java sheepdog/sim/Sheepdog.java
	javac $^

sheepdog/g8_v1/Player.class: sheepdog/g8_v1/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/g8_v2/Player.class: sheepdog/g8_v2/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/g8_v3/Player.class: sheepdog/g8_v3/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/manual/Player.class: sheepdog/manual/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^


.PHONY: rungui
rungui: all
	java sheepdog.sim.Sheepdog g8_v4 3 300 150 false true


.PHONY: clean
clean:
	$(RM) sheepdog/sim/*.class
	$(RM) sheepdog/manual/*.class
	$(RM) sheepdog/g8_v1/*.class
	$(RM) sheepdog/g8_v2/*.class
	$(RM) sheepdog/g8_v3/*.class
