#!/bin/bash

# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br

exec 5> debug_configurate.txt
BASH_XTRACEFD="5"
set -x

project_clone(){
	cd 
	git clone http://git.code.sf.net/p/maritaca/code maritaca-code 
}

project_build(){
	cd 
	cd maritaca-code/server/ 
	mvn clean install -U -Dmaven.test.skip=true 
}

pcts_copy_folders(){
	cd 

	#ARQUIVOS DE CONFIGURAÇÃO DO SOLR
	cp -r tomcat/ solr42/ 
	mkdir -p solr42/solr/data 
	mkdir -p solr42/tomcat/conf/Catalina/localhost 
	cp maritaca-code/doc/scripts/solr.xml solr42/tomcat/conf/Catalina/localhost/solr.xml 
	cp solr42/dist/solr-4.2.0.war solr42/solr/solr.war 
	cp -r -p solr42/example/solr/* solr42/solr/data/. 

	#ARQUIVO - VARIAVEIS DE AMBIENTE
	sudo cp -r -p maritaca-code/doc/scripts/configuration.properties /opt/ 

	#COPIA DO maritaca.war gerado após build de projeto
	sudo cp -r -p maritaca-code/server/web/target/maritaca.war tomcat/webapps/ 

	#DIRETÓROS HADOOP
	mkdir -p hadoop_mounted/user/maritaca/{apk,audio,picture,video} 

	#DIRETÓRIO APPS
	mkdir -p maritaca-code/client/apps 
	
	#COPIA DOS ARQUIVOS COM SUAS RESPECTIVAS PERMISSÕES PARA A PASTA /OPT
	sudo cp -r -p tomcat/ /opt/ 
	sudo cp -r -p solr42/ /opt/ 
	sudo cp -r -p cassandra/ /opt/ 
	sudo cp -r -p hadoop_mounted/* /var/www/ 
	sudo cp -r -p androidSDK/ /opt/
	sudo cp -r -p maritaca-code/ /opt/
}

configurate_cassandra(){
	cd 
	sudo /opt/cassandra/bin/cassandra
	sleep 10
	sudo /opt/cassandra/bin/cassandra-cli -h localhost -port 9160 -f maritaca-code/doc/scripts/cassandra_keyspace.txt 
	#sudo /opt/cassandra/bin/stop-server 
}

configurate_rabbitMQ(){
	cd
	echo "@@@@____configurate_rabbit____MQ@@@@" >> debug_configure.txt 
	sudo rabbitmqctl add_user maritaca maritaca >> debug_configure.txt 
	sudo rabbitmqctl add_vhost Maritaca >> debug_configure.txt
	sudo rabbitmqctl set_permissions -p Maritaca maritaca ".*" ".*" ".*" >> debug_configure.txt
	echo "@@@@____configurate_rabbit____MQ@@@@"
}

configurate_solr42(){
	perl -i -pe 's/<Server port="8005" shutdown="SHUTDOWN">/<Server port="8105" shutdown="SHUTDOWN">/' /opt/solr42/tomcat/conf/server.xml 
	perl -i -pe 's/<Connector port="8080" protocol="HTTP/<Connector port="8983" protocol="HTTP/' /opt/solr42/tomcat/conf/server.xml 
	perl -i -pe 's/<Connector port="8009" protocol="AJP/<Connector port="8109" protocol="AJP/' /opt/solr42/tomcat/conf/server.xml 
	perl -i -pe 's/{solr.data.dir:}/{solr.data.dir:\/opt\/solr42\/solr\/data}/' /opt/solr42/solr/data/collection1/conf/solrconfig.xml 
}

#configurate_tomcat(){

#}

configurate_androidSDK(){
	cd /opt/ 
	androidSDK/tools/android list sdk 
	androidSDK/tools/android update sdk --no-ui 
}

##################################   CODE    ################################## 

project_build

pcts_copy_folders

configurate_cassandra
configurate_rabbitMQ
configurate_solr42
#configurate_tomcat
configurate_androidSDK
