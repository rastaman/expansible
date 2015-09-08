#!/bin/sh
mvn -Pgenerate-sources clean generate-sources && rm -rf ../generated/* && cp -rfp target/generated-sources/swagger/* ../generated
