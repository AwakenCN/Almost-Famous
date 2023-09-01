#!/bin/bash
cd `dirname $0`
echo "Begin to gen the lua proto."
java -cp libs/MsgGenerator-1.0.0.jar:libs/freemarker-2.3.9.jar:libs/protobuf-java-3.21.6.jar com.ljoy.warz.tools.msg.ProtoGen ../CCB/WarZResources/lua/proto com.elex.cok.gameengine ./ lua
