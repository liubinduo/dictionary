proc="dictionary.jar"

JAVA_OPTS="-server -Xms1024M -Xmx2048M -Xss232k -Xmn512m -XX:SurvivorRatio=1 -XX:+AggressiveOpts -XX:+UseBiasedLocking -XX:CMSInitiatingOccupancyFraction=90 -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+DisableExplicitGC -XX:MaxTenuringThreshold=0 -XX:CMSFullGCsBeforeCompaction=100 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -Djava.awt.headless=true"

start() {

echo -n $"Starting $proc:"
for name in ./libs/*.jar
do
  APP_CLASS="$APP_CLASS:$name"
done


JAVA_CMD="$JAVA_OPTS -classpath $CLASSPATH:$APP_CLASS:$proc com.v1ok.dictionary.StartApplication --spring.profiles.active=dev"

echo "$JAVA_CMD"

# nohup $JAVA_CMD > start.log
java $JAVA_CMD
}

stop() {
        echo -n $"Stopping $proc:"
        aaa=$(ps aux | grep $proc |grep -v "grep" | awk '{print $2}')
        #aaa=$(ps -ef | grep  "jar $proc"  | awk '{print $2}')
	echo -n
        if [ -z $aaa ]; then
                echo "can't find this process"
                exit 1
        else
                kill -9 $aaa
	fi
        
}
case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
esac
