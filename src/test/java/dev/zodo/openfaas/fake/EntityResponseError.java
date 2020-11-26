package dev.zodo.openfaas.fake;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EntityResponseError {
    private final int statusCode;
    private final String error;
}
