jcappuccino
===========

Standalone interface for access to hardware using Websockets on TCP port 9191 (localhost only).

Currently supports Card Readers using PC/SC (with Java `javax.smartcardio` library).

First of all, check that `pcscd` is running and detects your card reader (eventually run `pcscd -df` to see its output).

Run the jcappuccino (Mac OS or generic Linux/Unix) :

    $ java -jar jcappuccino.jar

Run the jcappuccino (Ubuntu from version 14.04 on 64bit, due to a JVM bug) :

    $ java -Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1 -jar jcappuccino.jar

