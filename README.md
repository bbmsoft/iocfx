# IoCFX

[![Build Status](https://travis-ci.org/bbmsoft/iocfx.svg?branch=develop)](https://travis-ci.org/bbmsoft/iocfx)

## What is it?

IoCFX is a minimal Inversion of Control framework designed to improve the usability of JavaFX when running it from within OSGi.

## Why is it?

JavaFX is monolithic. It assumes that there is exactly one application running in the JVM that requires a UI and that the UI's lifecycle is bound to that of the JVM (i.e. when the UI terminates, the entire JVM needs to be restarted to bring it back up).

In an OSGi environment, the number of applications sharing the same JVM is potentially arbitrary and so is the number of bundles that want to bring their own UI. If every bundle, that wants to show a UI runs Application.launch(), chaos ensues.

IoCFX goes the other way round. It leaves managing the JavaFX Platform to the framework and just publishes its desire to be assigned a UI by the framework once it's ready.

## Why use it?

IoCFX was designed with maximum simplicity in mind. If you already have an OSGi framework running and want it to have a UI, let your UI component declare a dependency on javafx.stage.Stage and make sure that the net.bbmsoft.iocfx bundle is installed. Voilà, your UI bundle will be provided with a Stage instance as soon as it gets activated.

## Why not use it?

IoCFX is designed to be extremely simple. There are other projects out there that do quite similar things but offer more configurability or extra functionality. Not doing so is a deliberate choice in IoCFX. The goal here is to not add anything to your dependency tree that you don't actually use.
However if you do plan to use the additional flexibility other projects offer, they may of course be a better choice for you.

## Which Java versions will it run with?

#### Java 7 and older:

No.

#### Java 8

Yes, with a recent version of JavaFX

#### Java 9

So far I didn't encounter any problems with Oracle JDK 9

#### Java 10

Not tested yet, but I would expect it to work.

## How to build?

SSH:
```
git clone git@github.com:bbmsoft/iocfx.git
cd iocfx
mvn clean install
```

HTTPS:
```
git clone https://github.com/bbmsoft/iocfx.git
cd iocfx
mvn clean install
```

This obviously requires maven.

## How to run?

IoCFX is not executable by itself. However the repository includes a small executable example.

To run it, follow these simple steps:

#### With eclipse:

 - make sure you have the Bndtools Plugin 3.5.0+ installed
 - import all four projects contained in the repository into your workspace as maven projects
 - open iocfx-example/iocfx-example.bndrun and click 'Run OSGi'

#### Without eclipse:

I'm using Apache Felix here since that's the OSGi framework I'm most familiar with, but any other framework implementing OSGi  R6 should work as well.

I'm assuming you cloned th IoCFX repo into /tmp. Please adjust this path accordingly!

 - follow the 'How to build?' instructions
 - download Apache Felix from [the felix website](http://felix.apache.org/downloads.cgi) and start it according to its documentation
 - type ```install http://central.maven.org/maven2/org/apache/felix/org.apache.felix.scr/2.0.14/org.apache.felix.scr-2.0.14.jar``` and hit enter
 - start the bundle with the ID the shell just printed out by typing ```start <ID>``` and hitting enter
 - type ```install file:/tmp/iocfx/target/iocfx-2.0.0-SNAPSHOT.jar```
 - start the bundle with the ID the shell just printed out by typing ```start <ID>``` and hitting enter
 - type ```install file:/tmp/iocfx-example/target/iocfx-example-2.0.0-SNAPSHOT.jar```
 - start the bundle with the ID the shell just printed out by typing ```start <ID>``` and hitting enter
 
If you see two windows saying "Hello IoCFX!" popping up, everything went well.
