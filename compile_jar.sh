#!/bin/bash

javac src/TimeProfiler/TimeProfiler.java src/TimeProfiler/TimeResult.java -d jarFile
jar cvfm TimeProfiler.jar MANIFEST.MF -C jarFile .
rm -rf jarFile