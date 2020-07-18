# Getting Started
SpringBoot Web and Netty TCP Server/Client and Custom Fixedlength parser

# Springboot web request netty TCP client example
This is an example of receiving a response after request using netty tcp client when requesting url.

Client port : 8012

Need other TCP server(Copy this project then Change TCP server port and Run)

```
curl http://localhost:8080/send/fixed
curl http://localhost:8080/send/variable
```

# Netty TCP Server
Server port : 8011

* telnet connect > `telnet localhost 8011`
* input fixed example message > `fixed  001 1000  smssName`
* input variable fixed example message > `varia  001 1000---28               5.256    2 nam1       20200706 nam2       20200704  s28sName`
