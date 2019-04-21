#!/bin/bash
#
# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br

#Pré-Requisito: Ter o git instalado
#sudo apt-get install -y git

cd 
exec 5> debug_install.txt
BASH_XTRACEFD="5"
set -x

################################## VARIABLES ##################################
JAVA_REPOSITORY="ppa:webupd8team/java"
FFMPEG_REPOSITORY="ppa:mc3man/trusty-media"
################################## FUNCTIONS ################################## 

repository_local(){
	cd 
	sudo cp -r -p maritaca-code/doc/scripts/apt.conf /etc/apt/ 
}

repository_add(){
	sudo add-apt-repository -y $JAVA_REPOSITORY 
	sudo add-apt-repository -y $FFMPEG_REPOSITORY 
}

repository_update(){
	sudo apt-get update 
}

softwares_install(){
	sudo apt-get install -y oracle-java6-installer # Java 7 da erro no sdk android
	sudo apt-get install -y oracle-java6-set-default
	sudo apt-get install -y libjna-java
	sudo apt-get install -y python-software-properties # Dependencias cassandra
	sudo apt-get install -y curl # Dependencias cassandra
	sudo apt-get install -y ant
	sudo apt-get install -y xvfb
	sudo apt-get install -y maven
	sudo apt-get install -y lynx 
	sudo apt-get install -y rabbitmq-server 
	sudo apt-get install -y ffmpeg 
	sudo apt-get install -y lib32z1 lib32ncurses5 lib32bz2-1.0 lib32stdc++6 #Dependencias do android SDK
	sudo apt-get install -y apache2 
}

#repository_local
repository_add
repository_update
softwares_install
