#!/bin/bash

2>&1       # Redirecting stderr to stdout 
set -e     # Exit on program error

PACKAGE_NAME="$1"
APP_NAME="$2"
MARITACA_MOBILE_PATH="$3"
MARITACA_MOBILE_APPS="$4"
URI_SERVER="$5"
APP_DESCRIPTION="$6"
FORM_ID="$7"
echo "creating variables ..."

PROJECT_NAME="maritaca-mobile"
DIR_MARITACA_MOBILE=$MARITACA_MOBILE_PATH/$PROJECT_NAME

DIR_NEW_PROJECT=$MARITACA_MOBILE_APPS/$PACKAGE_NAME
KEY_STORE_PATH=$DIR_NEW_PROJECT/$PROJECT_NAME/maritaca.keystore
SRC_PATH="src/br/unifesp/maritaca/mobile"
GEN_PATH="gen/br/unifesp/maritaca/mobile"

NEW_SRC_PACKAGE_PATH=$DIR_NEW_PROJECT/$PROJECT_NAME/$SRC_PATH

NEW_GEN_PACKAGE_PATH=$DIR_NEW_PROJECT/$PROJECT_NAME/$GEN_PATH

if [ ! -d $DIR_NEW_PROJECT ]; then
	mkdir $DIR_NEW_PROJECT
else
	rm -rf $DIR_NEW_PROJECT
	mkdir $DIR_NEW_PROJECT
fi
#
cp -r $DIR_MARITACA_MOBILE/ $DIR_NEW_PROJECT
#src:
mkdir -p $NEW_SRC_PACKAGE_PATH/$PACKAGE_NAME
cd $NEW_SRC_PACKAGE_PATH
ls -1 | grep -v ^$PACKAGE_NAME | xargs -I{} mv {} $NEW_SRC_PACKAGE_PATH/$PACKAGE_NAME
#gen:
if [ -d $NEW_GEN_PACKAGE_PATH ]; then  
	mkdir -p $NEW_GEN_PACKAGE_PATH/$PACKAGE_NAME
	cd $NEW_GEN_PACKAGE_PATH
	ls -1 | grep -v ^$PACKAGE_NAME | xargs -I{} mv {} $NEW_GEN_PACKAGE_PATH/$PACKAGE_NAME
fi
#
cd $DIR_NEW_PROJECT/$PROJECT_NAME
find . -name "*.java" -o -name "*.xml" | xargs sed -i 's/br\.unifesp\.maritaca\.mobile/br\.unifesp\.maritaca\.mobile\.'$PACKAGE_NAME'/g'
find . -name "Constants.java" | xargs sed -i 's!String FORM_ID.*!String FORM_ID="'$FORM_ID'";!'
find . -name "Constants.java" | xargs sed -i 's!String URI_SERVER.*!String URI_SERVER="'$URI_SERVER'";!'
find . -name "ant.properties" | xargs sed -i 's;key\.store=.*;key\.store='$KEY_STORE_PATH';'
find . -name "project.properties" | xargs sed -i 's;android\.library\.reference\.1=.*;android\.library\.reference\.1='\.\.\/\.\.\/\.\.\/library';'
find . -name "strings.xml" | xargs sed -i 's/<string name=\"app_name\">Maritaca Mobile/<string name=\"app_name\">'"$APP_NAME"'/g'
find . -name "strings.xml" | xargs sed -i 's/<string name=\"app_description\">Description/<string name=\"app_description\">'"$APP_DESCRIPTION"'/g'
