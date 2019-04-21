#!/bin/bash
#
# Author: Vitor Lima
# Email:  vitor.lima@unifesp.br
#

exec 5> debug_download.txt
BASH_XTRACEFD="5"
set -x

pcts_download(){
	cd 

	if [ -e "cassandra.tgz" ] ; then
		echo "Cassandra Já Baixado"
	else
		wget -O cassandra.tgz http://ftp.unicamp.br/pub/apache/cassandra/1.2.19/apache-cassandra-1.2.19-bin.tar.gz 
	fi

	if [ -e "tomcat7.tgz" ] ; then
		echo "Tomcat Já Baixado"
	else	
		wget -O tomcat7.tgz http://ftp.unicamp.br/pub/apache/tomcat/tomcat-7/v7.0.61/bin/apache-tomcat-7.0.61.tar.gz 
	fi

	if [ -e "solr.tgz" ] ; then
		echo "Solr Já Baixado"
	else
		wget -O solr.tgz http://archive.apache.org/dist/lucene/solr/4.2.0/solr-4.2.0.tgz 
	fi

	if [ -e "android.tgz" ] ; then
		echo "Android Já Baixado"
	else
		wget -O android.tgz http://dl.google.com/android/android-sdk_r22.3-linux.tgz 
	fi
}

pcts_extract(){
	cd 
	tar zxvf cassandra.tgz 
	tar zxvf tomcat7.tgz 
	tar zxvf solr.tgz 
	tar zxvf android.tgz 
}

pcts_rename_folder(){
	cd 
	mv apache-cassandra-1.2.19/ cassandra 
	mv apache-tomcat-7.0.61/ tomcat 
	mv solr-4.2.0/ solr42 
	mv android-sdk-linux/ androidSDK 
}

pcts_download
pcts_extract
pcts_rename_folder
