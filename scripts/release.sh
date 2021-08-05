#!/bin/bash

# Check for uncommitted changes
[ ! -z "$(git status --porcelain)" ] && echo 'Found uncommitted changes' && exit 1

# Navigate to parent if we are inside scripts directory
[ -d .git ] || cd ..

# Release
mvn release:clean
mvn release:prepare
mvn release:perform

# Push commits
git push
