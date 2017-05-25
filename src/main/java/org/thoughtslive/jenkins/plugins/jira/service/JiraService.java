package org.thoughtslive.jenkins.plugins.jira.service;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.empty;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.parseResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.IssueLink;
import org.thoughtslive.jenkins.plugins.jira.api.IssueLinkType;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.SearchResult;
import org.thoughtslive.jenkins.plugins.jira.api.User;
import org.thoughtslive.jenkins.plugins.jira.api.input.BasicIssue;
import org.thoughtslive.jenkins.plugins.jira.login.SigningInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Service to interact with jira instance/site.
 * 
 * @author Naresh Rayapati.
 *
 */
public class JiraService {

  private final Site jiraSite;
  private final JiraEndPoints jiraEndPoints;

  public JiraService(final Site jiraSite) {
    this.jiraSite = jiraSite;

    final ConnectionPool CONNECTION_POOL = new ConnectionPool(5, 60, TimeUnit.SECONDS);

    OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(jiraSite.getTimeout(), TimeUnit.MILLISECONDS)
        .readTimeout(10000, TimeUnit.MILLISECONDS).connectionPool(CONNECTION_POOL)
        .retryOnConnectionFailure(true).addInterceptor(new SigningInterceptor(jiraSite)).build();

    final ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    this.jiraEndPoints = new Retrofit.Builder().baseUrl(this.jiraSite.getUrl().toString())
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient).build()
        .create(JiraEndPoints.class);
  }

  /**
   * @return JIRA Server info.
   */
  public ResponseData<Map<String, Object>> getServerInfo() {
    try {
      return parseResponse(jiraEndPoints.getServerInfo().execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Queries Component by id.
   * 
   * @param id component id.
   * @return component.
   */
  public ResponseData<Object> getComponent(final String id) {
    try {
      return parseResponse(jiraEndPoints.getComponent(id).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Creates component.
   * 
   * @param component an instance of {@link Component}
   * @return created component.
   */
  public ResponseData<Object> createComponent(final Object component) {
    try {
      return parseResponse(jiraEndPoints.createComponent(component).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Updates component.
   * 
   * @param component actual component
   * @return updated component.
   */
  public ResponseData<Void> updateComponent(final Component component) {
    try {
      return parseResponse(jiraEndPoints.updateComponent(component.getId(), component).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Component issue count.
   * 
   * @param id component id.
   * @return count.
   */
  public ResponseData<Object> getComponentIssueCount(final String id) {
    try {
      return parseResponse(jiraEndPoints.getComponentIssueCount(id).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Queries the issues by given id.
   * 
   * @param issueIdOrKey issue id or key.
   * @return issue.
   */
  public ResponseData<Object> getIssue(final String issueIdOrKey) {
    try {
      return parseResponse(jiraEndPoints.getIssue(issueIdOrKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  /**
   * Creates issue based on given {@link Issue}
   * 
   * @param issue
   * @return issue.
   */
  public ResponseData<Object> createIssue(final Object issue) {
    try {
      return parseResponse(jiraEndPoints.createIssue(issue).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> updateIssue(final String issueIdOrKey, final Object issue) {
    try {
      return parseResponse(jiraEndPoints.updateIssue(issueIdOrKey, issue).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> assignIssue(final String issueIdorKey, final String userName) {
    try {
      return parseResponse(
          jiraEndPoints.assignIssue(issueIdorKey, User.builder().name(userName).build()).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> createIssues(final Object issues) {
    try {
      return parseResponse(jiraEndPoints.createIssues(issues).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getComments(final String issueIdorKey) {
    try {
      return parseResponse(jiraEndPoints.getComments(issueIdorKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> addComment(final String issueIdorKey, final String comment) {
    try {
      return parseResponse(jiraEndPoints
          .addComment(issueIdorKey, Comment.builder().body(comment).build()).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> updateComment(final String issueIdorKey, final String commentId, final String comment) {
    try {
      return parseResponse(
          jiraEndPoints.updateComment(issueIdorKey, commentId, Comment.builder().body(comment).build()).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getComment(final String issueIdorKey, final String commentId) {
    try {
      return parseResponse(jiraEndPoints.getComment(issueIdorKey, commentId).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> notifyIssue(final String issueIdorKey, final Object notify) {
    try {
      return parseResponse(jiraEndPoints.notifyIssue(issueIdorKey, notify).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getIssueTransitions(final String issueIdorKey) {
    try {
      return parseResponse(jiraEndPoints.getTransitions(issueIdorKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> transitionIssue(final String idOrKey, final Object issue) {
    try {
      return parseResponse(jiraEndPoints.transitionIssue(idOrKey, issue).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getIssueWatches(final String issueIdorKey) {
    try {
      return parseResponse(jiraEndPoints.getIssueWatches(issueIdorKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> addIssueWatcher(final String issueIdorKey, final String userName) {
    try {
      return parseResponse(jiraEndPoints
          .addIssueWatcher(issueIdorKey, userName).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> searchIssues(final String jql, final int startAt,
      final int maxResults) {
    final SearchResult searchInput =
        SearchResult.builder().jql(jql).startAt(startAt).maxResults(maxResults).build();
    try {
      return parseResponse(jiraEndPoints.searchIssues(searchInput).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getProjects() {
    try {
      return parseResponse(jiraEndPoints.getProjects().execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getProject(final String projectIdOrKey) {
    try {
      return parseResponse(jiraEndPoints.getProject(projectIdOrKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getProjectVersions(final String projectIdOrKey) {
    try {
      return parseResponse(jiraEndPoints.getProjectVersions(projectIdOrKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getProjectComponents(final String projectIdOrKey) {
    try {
      return parseResponse(jiraEndPoints.getProjectComponents(projectIdOrKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getProjectStatuses(final String projectIdOrKey) {
    try {
      return parseResponse(jiraEndPoints.getProjectStatuses(projectIdOrKey).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getVersion(final String id) {
    try {
      return parseResponse(jiraEndPoints.getVersion(id).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> createVersion(final Object version) {
    try {
      return parseResponse(jiraEndPoints.createVersion(version).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> updateVersion(final String id, final Object version) {
    try {
      return parseResponse(jiraEndPoints.updateVersion(id, version).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Object> getIssueLinkTypes() {
    try {
      return parseResponse(jiraEndPoints.getIssueLinkTypes().execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }

  public ResponseData<Void> linkIssues(final String name, final String inwardIssueKey,
      final String outwardIssueKey, final String comment) {
    Comment linkComment = null;
    if (!empty(comment)) {
      linkComment = Comment.builder().body(comment).build();
    }

    final IssueLink issueLink = IssueLink.builder().type(IssueLinkType.builder().name(name).build())
        .comment(linkComment).inwardIssue(BasicIssue.builder().key(inwardIssueKey).build())
        .outwardIssue(BasicIssue.builder().key(outwardIssueKey).build()).build();

    try {
      return parseResponse(jiraEndPoints.createIssueLink(issueLink).execute());
    } catch (Exception e) {
      return buildErrorResponse(e);
    }
  }
  
  public ResponseData<Object> getFields() {
    try {
        return parseResponse(jiraEndPoints.getFields().execute());
    } catch (Exception e) {
        return buildErrorResponse(e);
    }
  }
}
