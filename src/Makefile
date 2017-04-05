JFLAGS = -g -cp .
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = TreeNode.java LeafNode.java Heaps.java DaryHeap.java PairingHeap.java PriorityQueueHeap.java InputBuffer.java encoder.java decoder.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
