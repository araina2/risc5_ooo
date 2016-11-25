
TOP = ..
SBT ?= java -Xmx8G -Xss16M -XX:MaxPermSize=512M -XX:-UseGCOverheadLimit -jar $(TOP)/sbt-launch.jar

# Set the MODULE_GEN environment variable to the name of the module generator class
test :
	$(SBT) "run Test ${MODULE_GEN} --backend --genHarness --compile --test"
