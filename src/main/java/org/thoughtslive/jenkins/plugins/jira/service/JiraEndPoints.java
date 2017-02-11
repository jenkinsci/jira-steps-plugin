package org.thoughtslive.jenkins.plugins.jira.service;

import java.util.Map;

import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.Comments;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.Count;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.IssueLink;
import org.thoughtslive.jenkins.plugins.jira.api.IssueLinkTypes;
import org.thoughtslive.jenkins.plugins.jira.api.Notify;
import org.thoughtslive.jenkins.plugins.jira.api.Project;
import org.thoughtslive.jenkins.plugins.jira.api.SearchResult;
import org.thoughtslive.jenkins.plugins.jira.api.Status;
import org.thoughtslive.jenkins.plugins.jira.api.Transitions;
import org.thoughtslive.jenkins.plugins.jira.api.User;
import org.thoughtslive.jenkins.plugins.jira.api.Version;
import org.thoughtslive.jenkins.plugins.jira.api.Watches;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssues;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssueInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.IssuesInput;
import org.thoughtslive.jenkins.plugins.jira.api.input.TransitionInput;

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
  Call<Component> getComponent(@Path("id") int id);

  @POST("rest/api/2/component")
  Call<Component> createComponent(@Body Component component);

  @PUT("rest/api/2/component/{id}")
  Call<Void> updateComponent(@Path("id") int id, @Body Component component);

  @GET("rest/api/2/component/{id}/relatedIssueCounts")
  Call<Count> getComponentIssueCount(@Path("id") int id);

  // Issue
  @GET("rest/api/2/issue/{issueIdOrKey}")
  Call<Issue> getIssue(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue")
  Call<BasicIssue> createIssue(@Body IssueInput issue);

  @PUT("rest/api/2/issue/{issueIdOrKey}")
  Call<BasicIssue> updateIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body IssueInput issue);

  @POST("rest/api/2/issue/bulk")
  Call<BasicIssues> createIssues(@Body IssuesInput issues);

  @PUT("rest/api/2/issue/{issueIdOrKey}/assignee")
  Call<Void> assignIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body User user);

  @GET("rest/api/2/issue/{issueIdOrKey}/comment")
  Call<Comments> getComments(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/comment")
  Call<Comment> addComment(@Path("issueIdOrKey") String issueIdOrKey, @Body Comment comment);

  @PUT("rest/api/2/issue/{issueIdOrKey}/comment/{id}")
  Call<Comment> updateComment(@Path("issueIdOrKey") String issueIdOrKey, @Path("id") int id,
      @Body Comment comment);

  @GET("rest/api/2/issue/{issueIdOrKey}/comment/{id}")
  Call<Comment> getComment(@Path("issueIdOrKey") String issueIdOrKey, @Path("id") int id);

  @POST("rest/api/2/issue/{issueIdOrKey}/notify")
  Call<Void> notifyIssue(@Path("issueIdOrKey") String issueIdOrKey, @Body Notify notify);

  @GET("rest/api/2/issue/{issueIdOrKey}/transitions?expand=transitions.fields")
  Call<Transitions> getTransitions(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/transitions")
  Call<Void> transitionIssue(@Path("issueIdOrKey") String issueIdOrKey,
      @Body TransitionInput issue);

  @GET("rest/api/2/issue/{issueIdOrKey}/watchers")
  Call<Watches> getIssueWatches(@Path("issueIdOrKey") String issueIdOrKey);

  @POST("rest/api/2/issue/{issueIdOrKey}/watchers")
  Call<Void> addIssueWatcher(@Path("issueIdOrKey") String issueIdOrKey, @Body User user);

  // Issue Links
  @POST("rest/api/2/issueLink")
  Call<Void> createIssueLink(@Body IssueLink issueLink);

  @GET("rest/api/2/issueLink/{linkId}")
  Call<IssueLink> getIssueLink(@Path("linkId") int linkId);

  // Issue Link Types
  @GET("rest/api/2/issueLinkType")
  Call<IssueLinkTypes> getIssueLinkTypes();

  // Project
  @GET("rest/api/2/project?expand=lead,description")
  Call<Project[]> getProjects();

  @GET("rest/api/2/project/{projectIdOrKey}")
  Call<Project> getProject(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/statuses")
  Call<Status[]> getProjectStatuses(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/components")
  Call<Component[]> getProjectComponents(@Path("projectIdOrKey") String projectId);

  @GET("rest/api/2/project/{projectIdOrKey}/versions")
  Call<Version[]> getProjectVersions(@Path("projectIdOrKey") String projectId);

  // Search
  @POST("rest/api/2/search")
  Call<SearchResult> searchIssues(@Body SearchResult search);

  // Version
  @POST("rest/api/2/version")
  Call<Version> createVersion(@Body Version version);

  @GET("rest/api/2/version/{id}")
  Call<Version> getVersion(@Path("id") int id);

  @PUT("rest/api/2/version/{id}")
  Call<Void> updateVersion(@Path("id") int id, @Body Version version);
}
