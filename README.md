# IoCFX

[![Build Status](https://travis-ci.org/bbmsoft/iocfx.svg?branch=develop)](https://travis-ci.org/bbmsoft/iocfx)

## What is it?

IoCFX is a minimal Inversion of Control framework designed to improve the usability of JavaFX when running it from within OSGi or a similar framework/container/plugin host.

## Why is it?

JavaFX is monolithic. It assumes that there is exactly one application running in the JVM that requires a UI and that the UI's lifecycle is bound to that of the JVM (i.e. when the UI terminates, the entire JVM needs to be restarted to bring it back up).

In an OSGi environment, the number of applications sharing the same JVM is potentially arbitrary and so is the number of bundles that want to bring their own UI. If every bundle, that wants to show a UI runs Application.launch(), chaos ensues.

IoCFX goes the other way round. It leaves managing the JavaFX Platform to the framework and just publishes its desire to be assigned a UI by the framework once it's ready.

## Why use it?

IoCFX was designed with maximum simplicity in mind. If you already have an OSGi framework running and want it to have a UI, let your UI bundle provide a StageConsumer instance and make sure that the net.bbmsoft.iocfx bundle is installed. Voilà, your UI bundle will be provided with a Stage instance as soon as it gets activated. By letting your StageConsumer also implement the StageConfig interface, you can customize the StageStyle and the behavior of your bundle when the stage is closed. That's it.

If you don't have an OSGi framework running, your UI plugin can register itself to a StageConsumerRegistry singleton, which will have the same result. This of course creates an additional compile time dependency that you can avoid when working with OSGi.

## Why not use it?

IoCFX is designed to be extremely simple. There are other projects out there that do quite similar things but offer more configurability or extra functionality. Not doing so is a deliberate choice in IoCFX. The goal here is to not add anything to your dependency tree that you don't actually use.
However if you do plan to use the additional flexibility other projects offer, they may of course be a better choice for you.

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

IoCFX is not executable by itself. However the repository includes two small executable examples.

To run them, follow these simple steps:

 - make sure you have eclipse with Bndtools 3.4.0+ installed
 - follow the 'How to build?' instructions
 - import iocfx-example into eclipse as a maven project
 - open iocfx-example.bndrun and click 'Run OSGi'
 - alternatively, if you want to try the non-OSGi version, find NonOsgiUI.java in the package explorer and run it as a plain old Java Application (Right click > Run As > Java Application)

If you see two windows saying "Hello IoCFX!" popping up (one in case of NonOsgiUI.java), everything went well.
