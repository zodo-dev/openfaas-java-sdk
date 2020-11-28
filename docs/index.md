# Welcome to the openfaas-java-sdk wiki!

After added dependency in project, get next steps  

## Simple usage

## extend abstract class RemoteFunctionImp or implement interface RemoteFunction

```java
public class CalculatorRemoteFunction extends RemoteFunctionImp<CalculatorData, ResultData> {
    public CalculatorRemoteFunction(String uri, String callbackEndpoint) {
        super(uri, "calculator", ResultData.class, callbackEndpoint);
    }
}
```
[CalculatorRemoteFunction.java](https://github.com/zodo-dev/openfaas-java-sdk/blob/main/src/test/java/dev/zodo/openfaas/api/CalculatorRemoteFunction.java)

The args `uri` and `callbackEndpoint` are optional and can be set before calling function.

## Call sync function

```java
SyncResponse<ResultData> res = calculatorRemoteFunction.call(objCalculatorData);
```
[ApiTest.java](https://github.com/zodo-dev/openfaas-java-sdk/blob/38f77373fb6be339b349e654bacf0d14d4e86cbc/src/test/java/dev/zodo/openfaas/api/ApiTest.java#L107)

After execution, `res` object receive status of the remote execution as http status code `res.getStatusCode()`, execution time `res.getDurationSeconds` and the function result as ResultaData in `res.getBody()`.

## Call sync function as java future

```java
CompletableFuture<SyncResponse<ResultData>> future = calculatorRemoteFunction.callFuture(objCalculatorData);
```

Same as calling sync function, but return type is CompletableFuture<SyncResponse<ResultData>>

## Call async function

```java
AsyncResponse asyncResult = calculatorRemoteFunction.asyncCall(objCalculatorData);
```
[ApiTest.java](https://github.com/zodo-dev/openfaas-java-sdk/blob/38f77373fb6be339b349e654bacf0d14d4e86cbc/src/test/java/dev/zodo/openfaas/api/ApiTest.java#L163)

After execution, `res` object receive status of remote execution as http status code `res.getStatusCode()`, start time `res.getStartTime` and the function call id as string unique identification `res.getCallId()`.
If callback endpoint url has provided, after execution the result has send to provided webhook in `callbackEndpoint`.

## Callback webhook

```java
@Path("/api")
public class CallbackWebhookResource extends CallbackAsyncEndpoint<ResultData> {

}
```
[CallbackWebhookResource.java](https://github.com/zodo-dev/openfaas-java-sdk/blob/38f77373fb6be339b349e654bacf0d14d4e86cbc/src/test/java/dev/zodo/openfaas/api/callback/CallbackWebhookResource.java)

Extend class CallbackAsyncEndpoint and configure root path with annotation `@Path`.

```java
@Component
public class OpenfaasCallbackListener implements OpenfaasCallbackEvent<ResultData> {

    public static AsyncCallbackResponse<ResultData> asyncResponseReceived;

    @Override
    public void consume(AsyncCallbackResponse<ResultData> asyncResponse) {
        log.info("## Webhook: Receive event: {}", asyncResponse.getFunctionName());
        asyncResponseReceived = asyncResponse;
    }
}
```
Implement interface OpenfaasCallbackEvent with expected receive data in webhook. This interface has provided by CDI (Spring in sample). On receive webhook event, received value well be passed to consume method. 

[OpenfaasCallbackListener.java](https://github.com/zodo-dev/openfaas-java-sdk/blob/38f77373fb6be339b349e654bacf0d14d4e86cbc/src/test/java/dev/zodo/openfaas/api/callback/OpenfaasCallbackListener.java)
