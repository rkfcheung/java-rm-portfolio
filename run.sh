#!/usr/bin/env bash

THIS_DIR=$(cd $(dirname $0); pwd)
CSV_FILE=$1

if [[ -f "${CSV_FILE}" ]]; then
  echo "Using ${CSV_FILE} ..."
  export PORTFOLIO_FILE=${CSV_FILE}

  "${THIS_DIR}"/gradlew clean bootRun
else
  echo "ERROR: File not found: ${CSV_FILE}"
  exit 1
fi

