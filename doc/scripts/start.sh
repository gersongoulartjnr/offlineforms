#!/bin/bash
#
# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br
#

exec 5> debug_start.txt
BASH_XTRACEFD="5"

iniciar_servicos(){
	#sudo service apache2 start #para quando apache não inicia automaticamente com SO
	sudo service apache2 status
	sleep 5
	#sudo service rabbitmq-server start #para quando rabbitmq-server não inicia automaticamente com SO
	sudo service rabbitmq-server status
	sleep 5
	sudo /opt/cassandra/bin/cassandra
	sleep 5
	sudo /opt/solr42/tomcat/bin/startup.sh
	sleep 5 
	sudo /opt/tomcat/bin/startup.sh
	sleep 5
	java -jar /opt/maritaca-code/services/rabbit-consumer/target/rabbit-consumer-1.0.jar &
	sleep 5
	lynx http://localhost:8080/maritaca
}

iniciar_servicos
