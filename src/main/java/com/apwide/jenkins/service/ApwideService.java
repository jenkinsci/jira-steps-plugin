package com.apwide.jenkins.service;

import static com.apwide.jenkins.api.ResponseHandler.buildErrorResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.login.SigningInterceptor;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.apwide.jenkins.api.DeployedVersion;
import com.apwide.jenkins.api.EnvironmentStatus;
import com.apwide.jenkins.api.ResponseData;
import com.apwide.jenkins.api.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ApwideService {

    private Site jiraSite;
    private ApwideEndPoints rest;

    public ApwideService(Site site) {
	this.jiraSite = site;

	final ConnectionPool CONNECTION_POOL = new ConnectionPool(5, 60, TimeUnit.SECONDS);

	OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(jiraSite.getTimeout(), TimeUnit.MILLISECONDS)
		.readTimeout(10000, TimeUnit.MILLISECONDS).connectionPool(CONNECTION_POOL).retryOnConnectionFailure(true)
		.addInterceptor(new SigningInterceptor(jiraSite)).build();

	final ObjectMapper mapper = new ObjectMapper();
	mapper.registerModule(new JodaModule());
	this.rest = new Retrofit.Builder().baseUrl(this.jiraSite.getUrl().toString()).addConverterFactory(JacksonConverterFactory.create(mapper))
		.addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient).build().create(ApwideEndPoints.class);
    }

    public ResponseData<Void> updateEnvironmentStatus(String applicationName, String categoryName, String statusId, String statusName) {
	try {
	    EnvironmentStatus status = new EnvironmentStatus(statusId, statusName);
	    Response<Void> resp = rest.updateStatus(applicationName, categoryName, status).execute();
	    return ResponseHandler.parseResponse(resp);
	} catch (Exception e) {
	    return buildErrorResponse(e);
	}
    }

    public ResponseData<Void> updateEnvironmentDeployedVersion(String applicationName, String categoryName, String versionName) {
	try {
	    DeployedVersion deployedVersion = new DeployedVersion(versionName);
	    Response<Void> resp = rest.updateDeployedVersion(applicationName, categoryName, deployedVersion).execute();
	    return ResponseHandler.parseResponse(resp);
	} catch (Exception e) {
	    return buildErrorResponse(e);
	}
    }
}
