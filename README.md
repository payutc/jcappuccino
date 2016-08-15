jcappuccino
===========

Standalone interface for access to hardware using Websockets on TCP port 9191.
Currently supports Card Readers using PC/SC.

First of all, check that `pcscd` is running and detects your card reader (eventually run `pcscd -df` to see its output).

Run the jcappuccino (Mac OS or generix Linux/Unix) :

Run the build for the Fablab version using:

Run the jcappuccino (Ubuntu starting 14.04 on 64bit, due to a JVM bug) :

    $ java -Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1 -jar jcappuccino.jar
