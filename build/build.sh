#!/bin/bash
set -e # Any subsequent(*) commands which fail will cause the shell script to exit immediately
set -x

MVN="mvn --settings build/settings.xml -Dlogback.configurationFile=build/logback-ci.xml org.jacoco:jacoco-maven-plugin:prepare-agent "

if [[ $TRAVIS_PULL_REQUEST == "false" ]]
then
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/pubring.gpg.enc -out $BUILD_DIR/pubring.gpg -d
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/secring.gpg.enc -out $BUILD_DIR/secring.gpg -d
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $BUILD_DIR/deploy_key.pem.enc -out $BUILD_DIR/deploy_key.pem -d

	if [ -e "release.version" ] && [ $TRAVIS_BRANCH == "master" ]
	then
		echo "Performing a release..."
		RELEASE_VERSION=$(cat release.version)

		eval "$(ssh-agent -s)"
		chmod 600 $BUILD_DIR/deploy_key.pem
		ssh-add $BUILD_DIR/deploy_key.pem
		git config --global user.name "TraneIO CI"
		git config --global user.email "ci@trane.io"
		git config --global push.default matching
		git remote set-url origin git@github.com:traneio/ndbc.git
		git fetch --unshallow
		git checkout master || git checkout -b master
		git reset --hard origin/master

		git rm release.version
		git commit -m "[skip ci] [release] remove release.version"
		git push

		$MVN -B clean release:prepare -DreleaseVersion=$RELEASE_VERSION
		$MVN release:perform
	elif [[ $TRAVIS_BRANCH == "master" ]]
	then
		echo "Publishing a snapshot..."
		$MVN clean package deploy
	else
		echo "Publishing a branch snapshot..."
		$MVN clean versions:set -DnewVersion=$TRAVIS_BRANCH-SNAPSHOT
		$MVN package deploy
	fi
else
	echo "Nothing to publish"
fi

set +x
