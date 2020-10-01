#!/bin/bash
fileproto=~/ressources/
prosrc=/usr/local/protobuf-master/src
grppath=/usr/local/grpc-java-master/compiler/build/exe/java_plugin/protoc-gen-grpc-java

protoc --proto_path=$fileproto --proto_path=$prosrc --plugin=$grppath --grpc-java_out=$HOME/ressources/ --java_out=$HOME/ressources/ $fileproto/messages.proto
