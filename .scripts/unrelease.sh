#!/bin/bash

PROJECT_GROUP=org.codi
PROJECT_NAME=leetcode-tester

# Check for version parameter
[ -z "$1" ] && echo 'Please provide version to delete: "./unrelease.sh 0.0.1"' && exit 1

# Clean any release files
mvn release:clean

# Delete git release tag and branch
git tag --delete "$PROJECT_NAME-$1"
git push --delete origin "$PROJECT_NAME-$1"

# Get package id
PACKAGE_ID=$(curl "https://api.github.com/user/packages/maven/$PROJECT_GROUP.$PROJECT_NAME/versions" \
  -H "Authorization: Bearer $GITHUB_PAT" \
  -H 'Accept: application/vnd.github.v3+json' \
  2>/dev/null | jq -r ".[] | select(.name == \"$1\") | .id")

[ -z "$PACKAGE_ID" ] && echo 'Package not found in repository' && exit 1

# Delete package

curl -X DELETE "https://api.github.com/user/packages/maven/$PROJECT_GROUP.$PROJECT_NAME/versions/$PACKAGE_ID" \
  -H "Authorization: Bearer $GITHUB_PAT" \
  -H 'Accept: application/vnd.github.v3+json' \
  2>/dev/null

echo 'Please update version in pom.xml & delete release commits on git'
