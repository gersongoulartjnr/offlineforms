#!/bin/bash
#
# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br
#

exec 5> debug_stop.txt
BASH_XTRACEFD="5"

parar_servicos(){
	sudo service apache2 stop
	sleep 5
	sudo service apache2 status
	sudo service rabbitmq-server stop
	sleep 5
	sudo service rabbitmq-server status
	#sudo /opt/cassandra/bin/stop-server 
	sudo /opt/solr42/tomcat/bin/shutdown.sh
	sleep 5
	sudo /opt/tomcat/bin/shutdown.sh 
}
