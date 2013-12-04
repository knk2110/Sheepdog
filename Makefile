all: sheepdog/sim/Sheepdog.class sheepdog/sim/Point.class sheepdog/sim/Player.class sheepdog/manual/Player.class sheepdog/g8_final/Player.class

sheepdog/sim/Point.class: sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Player.class: sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/sim/Sheepdog.class: sheepdog/sim/Player.java sheepdog/sim/Point.java sheepdog/sim/Sheepdog.java
	javac $^

sheepdog/g8_final/Player.class: sheepdog/g8_final/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/g8_v8/Player.class: sheepdog/g8_v8/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^

sheepdog/manual/Player.class: sheepdog/manual/*.java sheepdog/sim/Player.java sheepdog/sim/Point.java
	javac $^


.PHONY: rungui
rungui: all
	java sheepdog.sim.Sheepdog g8_final 8 5 5 false true


.PHONY: clean
clean:
	$(RM) sheepdog/sim/*.class
	$(RM) sheepdog/manual/*.class
	$(RM) sheepdog/g8_final/*.class
	$(RM) sheepdog/g8_v8/*.class
