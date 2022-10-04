package com.evolution.quicktrack.base;

import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;

public interface BaseContractor {
    ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
}
