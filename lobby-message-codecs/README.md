## Message Codecs

This project contains the codecs for messages sent between application components. The binary format used is the [Simple Binary Encoding](https://github.com/real-logic/simple-binary-encoding). 

SBE is the fastest amongst its peers at the cost of not being as flexible. However, given the messages in this project are very simple (notably, they don't feature variable sized fields) SBE is a perfect fit.
