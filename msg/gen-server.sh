#!/bin/bash
echo "Begin to gen the server proto."
java -cp libs/MsgGenerator-1.0.0.jar:libs/freemarker-2.3.9.jar:libs/protobuf-java-3.21.6.jar com.ljoy.warz.tools.msg.ProtoGen ../warz-game/src/main/java com.im30.warz.gameengine ./ java