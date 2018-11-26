# service-idl
This project holds the definition of your service's public interface (The methods your service will expose to other clients and services).

## Instructions
Define the interface for your service using ProtoBuf IDL in the `src/main/proto` directory.

An example service interface has been provided for you: [service.proto](https://github.com/netifi/proteus-quickstart/blob/master/service-idl/src/main/proto/io/netifi/proteus/quickstart/service/protobuf/service.proto)

## Documentation
Documentation on the IDL format can be found here: [http://docs.netifi.com/protobuf_rsocket/](http://docs.netifi.com/protobuf_rsocket/)

```bash
docker run \
-p 8001:8001 \
-p 7001:7001 \
-p 9000:9000 \
-e BROKER_SERVER_OPTS="'-Dnetifi.authentication.0.accessKey=9007199254740991'  \
'-Dnetifi.broker.console.enabled=true' \
'-Dnetifi.authentication.0.accessToken=kTBDVtfRBO4tHOnZzSyY5ym2kfY=' \
'-Dnetifi.broker.admin.accessKey=9007199254740991' \
'-Dnetifi.broker.admin.accessToken=kTBDVtfRBO4tHOnZzSyY5ym2kfY='" \
netifi/proteus:1.5.2
```

First part live coding: https://github.com/reactor/lite-rx-api-hands-on/ 
Seconde part live coding: https://github.com/bclozel/spring-reactive-university