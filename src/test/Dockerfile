#https://github.com/openfaas-incubator/faas-memory
#export OPENFAAS_URL=127.0.0.1:8083
FROM golang:1.15

ENV GOPATH=/go

EXPOSE 8083

RUN go get -u github.com/openfaas-incubator/faas-memory
WORKDIR $GOPATH/src/github.com/openfaas-incubator/faas-memory
CMD make start
