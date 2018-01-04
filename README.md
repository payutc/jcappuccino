jcappuccino
===========

Standalone interface for access to hardware using Websockets on TCP (default port : 9191).

Currently supports Card Readers using PC/SC (with Java `javax.smartcardio` library).

First of all, check that `pcscd` is running and detects your card reader (eventually run `pcscd -df` to see its output).

### Usages :

Run the jcappuccino (Mac OS or generic Linux/Unix) :

    $ java -jar jcappuccino.jar

Run the jcappuccino (Ubuntu from version 14.04 on 64bit, due to a JVM bug) :

    $ java -Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1 -jar jcappuccino.jar

### Options :

| Option                   | Description                                                                      |
| ------------------------ |:--------------------------------------------------------------------------------:|
| `ws-port=9191`           | specify port to launch ws server (default: 9191)                                 |
| `wss-port=9192`          | launch a wss server on 9192 port, `ws-port` and `wss-port` should be different   |
| `no-reader-simulation`   | returning error message instead of simulate a card reader                        |
| `card-id`                | set the simulated card ID (default: 123456AB)                                    |
| `limit-localhost`        | limit access to 127.0.0.1                                                        |

Exemple :

    $ java -Dwss-port=9192 -Dno-reader-simulation -jar jcappuccino.jar

Be careful to place all `-D` options before the `-jar` option.
