# http://tecadmin.net/steps-to-install-java-on-centos-5-6-or-rhel-5-6/

cd /opt/
sudo wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/7u75-b13/jdk-7u75-linux-x64.tar.gz"
tar xzf jdk-7u75-linux-x64.tar.gz

cd /opt/jdk1.7.0_75/
sudo alternatives --install /usr/bin/java java /opt/jdk1.7.0_75/bin/java 2
#sudo alternatives --config java

sudo alternatives --install /usr/bin/jar jar /opt/jdk1.7.0_75/bin/jar 2
sudo alternatives --install /usr/bin/javac javac /opt/jdk1.7.0_75/bin/javac 2
sudo alternatives --set jar /opt/jdk1.7.0_75/bin/jar
sudo alternatives --set javac /opt/jdk1.7.0_75/bin/javac

java -version

export JAVA_HOME=/opt/jdk1.7.0_75
export JRE_HOME=/opt/jdk1.7.0_75/jre
export PATH=$PATH:/opt/jdk1.7.0_75/bin:/opt/jdk1.7.0_75/jre/bin

#!/bin/bash
#
# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br

#Pré-Requisito: Ter o git instalado
#sudo yum install -y git

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
	sudo yum update 
}

softwares_install(){
	sudo yum install -y git
	sudo yum install -y maven
	sudo yum install -y ant 
	sudo yum install -y xvfb 
	sudo yum install -y lynx
	#https://www.rabbitmq.com/install-rpm.html
	sudo yum install -y erlang #Repositório p/ rabbit
	sudo yum install -y rabbitmq-server 
	sudo yum install -y ffmpeg 
	sudo yum install -y httpd #Apache
}


repository_add
repository_update
softwares_install

#ffmpeg
#http://www.diolinux.com.br/2015/01/o-que-fazer-depois-de-instalar-o-fedora.html
sudo yum localinstall --nogpgcheck http://download1.rpmfusion.org/free/fedora/rpmfusion-free-release-$(rpm -E %fedora).noarch.rpm http://download1.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-$(rpm -E %fedora).noarch.rpm -y

#cassandra
#http://www.liquidweb.com/kb/how-to-install-cassandra-2-on-fedora-20/
cd 
sudo cp maritaca-code/doc/scripts/datastax.repo /etc/yum.repos.d/

