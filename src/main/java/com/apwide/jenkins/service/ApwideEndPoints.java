package com.apwide.jenkins.service;

import com.apwide.jenkins.api.DeployedVersion;
import com.apwide.jenkins.api.EnvironmentStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApwideEndPoints {

    @PUT("rest/apwide/tem/1.1/status-change")
    Call<Void> updateStatus(@Query("application") String applicationName, @Query("category") String categoryName, @Body EnvironmentStatus status);

    @PUT("rest/apwide/tem/1.1/deployment")
    Call<Void> updateDeployedVersion (@Query("application") String applicationName, @Query("category") String categoryName, @Body DeployedVersion deployedVersion);

}
