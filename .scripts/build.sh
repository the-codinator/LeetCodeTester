#!/bin/bash

# Navigate to parent if we are inside scripts directory
[ -d .git ] || cd ..

# Clean compile with tests
mvn clean package
