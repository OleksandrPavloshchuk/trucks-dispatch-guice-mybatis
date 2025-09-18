#!/bin/bash

set -x

curl -v \
  -X POST \
  -H "Content-Type: application/json" \
  -d "[]" \
  http://localhost:8080/