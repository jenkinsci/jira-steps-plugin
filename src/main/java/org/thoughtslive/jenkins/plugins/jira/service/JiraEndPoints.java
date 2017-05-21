package org.thoughtslive.jenkins.plugins.jira.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * JIRA Restful Endpoints.
 * 
 * @author Naresh Rayapati
 *
 */
public interface JiraEndPoints {

  // Server Info
  @GET("rest/api/2/serverInfo")
  Call<Map<String, Object>> getServerInfo();

  // Components
  @GET("rest/api/2/component/{id}")
  Call<Object> getComponent(@Path("id") String id);

  @POST("rest/api/2/component")
  Call<Object> createComponent(@Body Object component);

  @PUT("rest/api/2/component/{id}")
  Call<Void> updateComponent(@Path("id") String id, @Body Object component);

  @GET("rest/api/2/component/{id}/relatedIssueCounts")
  Call<Object> getComponentIssueCount(@Path("id") String id);

  // Issue
  @GET("rest/api/2/issue/{issueIdOrKey}")
  Call<Object> getIssue(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue")
  Call<Object> createIssue(@Body Object issue);

  @PUT("rest/api/2/issue/{issueIdOrKey}")
  Call<Object> updateIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body Object issue);

  @POST("rest/api/2/issue/bulk")
  Call<Object> createIssues(@Body Object issues);

  @PUT("rest/api/2/issue/{issueIdOrKey}/assignee")
  Call<Void> assignIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body Object user);

  @GET("rest/api/2/issue/{issueIdOrKey}/comment")
  Call<Object> getComments(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/comment")
  Call<Object> addComment(@Path("issueIdOrKey") String issueIdOrKey, @Body Object comment);

  @PUT("rest/api/2/issue/{issueIdOrKey}/comment/{id}")
  Call<Object> updateComment(@Path("issueIdOrKey") String issueIdOrKey, @Path("id") String id,
      @Body Object comment);

  @GET("rest/api/2/issue/{issueIdOrKey}/comment/{id}")
  Call<Object> getComment(@Path("issueIdOrKey") String issueIdOrKey, @Path("id") String id);

  @POST("rest/api/2/issue/{issueIdOrKey}/notify")
  Call<Void> notifyIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body Object notify);

  @GET("rest/api/2/issue/{issueIdOrKey}/transitions?expand=transitions.fields")
  Call<Object> getTransitions(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/transitions")
  Call<Void> transitionIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body Object issue);

  @GET("rest/api/2/issue/{issueIdOrKey}/watchers")
  Call<Object> getIssueWatches(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/watchers")
  Call<Void> addIssueWatcher(@Path("issueIdOrKey") String issueIdOrKey, @Body String user);

  // Issue Links
  @POST("rest/api/2/issueLink")
  Call<Void> createIssueLink(@Body Object issueLink);

  @GET("rest/api/2/issueLink/{linkId}")
  Call<Object> getIssueLink(@Path("linkId") String linkId);

  // Issue Link Types
  @GET("rest/api/2/issueLinkType")
  Call<Object> getIssueLinkTypes();

  // Project
  @GET("rest/api/2/project?expand=lead,description")
  Call<Object> getProjects();

  @GET("rest/api/2/project/{projectIdOrKey}")
  Call<Object> getProject(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/statuses")
  Call<Object> getProjectStatuses(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/components")
  Call<Object> getProjectComponents(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/versions")
  Call<Object> getProjectVersions(@Path("projectIdOrKey") String projectId);

  // Search
  @POST("rest/api/2/search")
  Call<Object> searchIssues(@Body Object search);

  // Version
  @POST("rest/api/2/version")
  Call<Object> createVersion(@Body Object version);

  @GET("rest/api/2/version/{id}")
  Call<Object> getVersion(@Path("id") String id);

  @PUT("rest/api/2/version/{id}")
  Call<Void> updateVersion(@Path("id") String id, @Body Object version);
  
  // Fields
  @GET("rest/api/2/field")
  Call<Object> getFields();

  @POST("rest/api/2/field")
  Call<Object> createField(@Body Object field);

}
