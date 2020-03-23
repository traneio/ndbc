#!/bin/bash
set -e # Any subsequent(*) commands which fail will cause the shell script to exit immediately

LOGBACK_CONFIG="-Dlogback.configurationFile=file://$BUILD_DIR/logback-ci.xml"
MVN="mvn --settings build/settings.xml -Drelease.arguments="$LOGBACK_CONFIG" $LOGBACK_CONFIG org.jacoco:jacoco-maven-plugin:prepare-agent "

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

		set -x
		$MVN -B clean release:prepare -DreleaseVersion=$RELEASE_VERSION
		$MVN release:perform
		set +x
	elif [[ $TRAVIS_BRANCH == "master" ]]
	then
		echo "Publishing a snapshot..."
		set -x
		$MVN clean install sonar:sonar package deploy
		set +x
	else
		echo "Publishing a branch snapshot..."
		set -x
		$MVN clean versions:set -DnewVersion=$TRAVIS_BRANCH-SNAPSHOT
		$MVN install sonar:sonar package deploy
		set +x
	fi
else
        echo "Pull request build..."
	$MVN install sonar:sonar
fi
