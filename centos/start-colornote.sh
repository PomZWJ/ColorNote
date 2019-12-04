#!/bin/sh
java -Dloader.path="lib,resources" -jar /usr/local/JavaProject/clolorNote/ColorNote.jar
echo $! > /var/run/clolorNote.pid
