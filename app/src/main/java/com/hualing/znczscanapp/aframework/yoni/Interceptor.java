package com.hualing.znczscanapp.aframework.yoni;

public interface Interceptor {
	boolean intercept(RequestParams params, NetResponse result);
}
