<img src="doc/image/quasar.png" align="left" width="128px" height="128px"/>
<img align="left" width="0" height="128px" hspace="10"/>

[![letgo](https://img.shields.io/badge/letgo-quasar-blue.svg?style=flat-square)](http://letgo.com)
[![Build Status](https://travis-ci.org/sergigp/quasar.svg?branch=master)](https://travis-ci.org/sergigp/quasar)

> /ˈkweɪzɑːr/ Is an active galactic nucleus of very high luminosity :dizzy:

Quasar is a library to do [CQRS](https://martinfowler.com/bliki/CQRS.html) in Scala.
<br> <br>

**TBD: Currently this doc is deprecated. See the code to see how it works.**

## Async?
Quasar provides two interfaces `CommandBus` and `QueryBus` that accepts a type parameter `P`. Quasar also provides Async implementations of each bus powered by Scala `Future`.

## Install
Add the bintray resolver and the dependency in your `build.sbt`

```
resolvers += Resolver.bintrayRepo("sergigp", "maven")

libraryDependencies ++= Seq("com.sergigp.quasar" %% "quasar" % "0.7")
```

## Usage

You can visit our [doc](doc/index.md) to learn how to use Quasar

logo designed by [FreePik](https://www.flaticon.com/authors/freepik)
