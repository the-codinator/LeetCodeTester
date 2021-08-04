#!/bin/bash

# Check for uncommitted changes
[ ! -z "$(git status --porcelain)" ] && echo 'Found uncommitted changes' && exit 1

# Release
mvn release:clean
mvn release:prepare
mvn release:perform

# Push commits
git push
