FROM azul/zulu-openjdk-centos:8

MAINTAINER liubinduo <liubinduo@parkbao.com>

VOLUME /tmp

ADD target/libs/*.jar /home/dictonary/libs/
ADD target/dictionary-1.0-SNAPSHOT.jar /home/dictonary/dictionary.jar
ADD server.sh /home/dictonary/

WORKDIR /home/dictonary/

RUN chmod +X server.sh
CMD echo "runing...."
CMD ["sh","server.sh","start"]
