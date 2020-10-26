#!/usr/bin/env bash

javapackager -deploy -native pkg -name MySQLDataGrip -BappVersion=1.0 -Bicon=AppIcon.icns -srcdir . -srcfiles ../target/assembly-1.0-SNAPSHOT-jar-with-dependencies.jar -appclass org.client.bootstrap.Bootstrap -outdir ../target -outfile 123
