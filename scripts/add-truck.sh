#!/bin/bash

JSON=$(printf '{"name":"%s","capacity":%f}' "$1" "$2")

curl -X POST \
  -H "Content-Type: application/json" \
  -d $JSON \
  http://localhost:8080/td/truck