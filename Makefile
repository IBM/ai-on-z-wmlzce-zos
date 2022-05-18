JFLAGS = -g
JC = javac
SRC_DIR = ./com/ibm/wmlzscoring/caller
CALLER = com.ibm.wmlzscoring.caller.Caller
MODEL_JAR = ".:./commons-logging-1.1.3.jar:./httpclient-4.5.13.jar:./httpcore-4.4.14.jar:./javax.json-1.0.jar"
MODELCLASSPATH := -classpath $(MODEL_JAR):.

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(OUTPUT) $(MODELCLASSPATH) $*.java

CLASSES = \
	$(SRC_DIR)/Session.java \
	$(SRC_DIR)/Caller.java 

default: classes

classes: $(CLASSES:.java=.class)

run: 
	java $(MODELCLASSPATH) $(CALLER) $(HOSTIP) $(PORT) $(TOKEN) $(MODEL_DIR) $(PAYLOAD)

clean:
	$(RM) $(SRC_DIR/*.class
