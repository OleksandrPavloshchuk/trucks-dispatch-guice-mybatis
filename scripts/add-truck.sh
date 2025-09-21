#!/bin/bash

set -x

JSON=$(printf '{"truck":{"name":"%s","capacity":%f}}' "$1" "$2")

curl -v \
  -X POST \
  -H "Content-Type: application/json" \
  -d $JSON \
  http://localhost:8080/trucks