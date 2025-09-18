#!/bin/bash

set -x

JSON=$(printf '{"shipment":{"name":"%s","weight":%f}}' "$1" "$2")

curl -v \
  -X POST \
  -H "Content-Type: application/json" \
  -d $JSON \
  http://localhost:8080/