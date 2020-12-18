#!/bin/bash
set -e # Any subsequent(*) commands which fail will cause the shell script to exit immediately

LOGBACK_CONFIG="-Dlogback.configurationFile=file://$BUILD_DIR/logback-ci.xml"
MVN="mvn --settings build/settings.xml -Drelease.arguments="$LOGBACK_CONFIG" $LOGBACK_CONFIG org.jacoco:jacoco-maven-plugin:prepare-agent "


openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/pubring.gpg.enc -out $BUILD_DIR/pubring.gpg -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/secring.gpg.enc -out $BUILD_DIR/secring.gpg -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/deploy_key.pem.enc -out $BUILD_DIR/deploy_key.pem -d

echo "Performing a release..."

eval "$(ssh-agent -s)"
chmod 600 $BUILD_DIR/deploy_key.pem
ssh-add $BUILD_DIR/deploy_key.pem
git config --global user.name "TraneIO CI"
git config --global user.email "ci@trane.io"
git config --global push.default matching
git remote set-url origin git@github.com:traneio/ndbc.git

#git fetch --unshallow
#git checkout master || git checkout -b master
#git reset --hard origin/master

#git rm release.version
#git commit -m "[skip ci] [release] remove release.version"
#git push

set -x
$MVN -B clean release:prepare release:perform
git push

set +x
