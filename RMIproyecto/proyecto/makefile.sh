#!/bin/bash

cd servidor/

echo "COMPILAMOS EL SERVIDOR: "

javac -Xlint *.java

cp *.class ../cliente

cd ../cliente

echo "COMPILAMOS EL CLIENTE: "

javac *.java

cd ../servidor

echo "EJECUTAMOS EL SERVIDOR: "

rmiregistry 54321 &

java -Djava.security.policy=servidor.permisos ServidorComercio 54321
