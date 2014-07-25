#!/usr/bin/bash
find src -nowarn | grep .java$ >> sourcefiles
javac -d bin/ @sourcefiles
rm sourcefiles
