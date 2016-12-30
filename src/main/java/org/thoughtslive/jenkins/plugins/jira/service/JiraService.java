package org.thoughtslive.jenkins.plugins.jira.service;

import static org.thoughtslive.jenkins.plugins.jira.util.Common.buildErrorResponse;
import static org.thoughtslive.jenkins.plugins.jira.util.Common.parseResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.Comments;
import org.thoughtslive.jenkins.plugins.jira.api.Component;
import org.thoughtslive.jenkins.plugins.jira.api.Count;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.Issues;
import org.thoughtslive.jenkins.plugins.jira.api.Notify;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.Transitions;
import org.thoughtslive.jenkins.plugins.jira.api.User;
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

		OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(jiraSite.getTimeout(), TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS)
				.connectionPool(CONNECTION_POOL).retryOnConnectionFailure(true).addInterceptor(new SigningInterceptor(jiraSite)).build();

		final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
		this.jiraEndPoints = new Retrofit.Builder().baseUrl(this.jiraSite.getUrl().toString()).addConverterFactory(JacksonConverterFactory.create(mapper))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient).build().create(JiraEndPoints.class);
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
	 * @param id
	 *            component id.
	 * @return component.
	 */
	public ResponseData<Component> getComponent(final int id) {
		try {
			return parseResponse(jiraEndPoints.getComponent(id).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	/**
	 * Creates component.
	 * 
	 * @param component
	 *            an instance of {@link Component}
	 * @return created component.
	 */
	public ResponseData<Component> createComponent(final Component component) {
		try {
			return parseResponse(jiraEndPoints.createNewComponent(component).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	/**
	 * Updates component.
	 * 
	 * @param id
	 *            component id.
	 * @param component.
	 * @return updated component.
	 */
	public ResponseData<Component> updateComponent(final int id, final Component component) {
		try {
			return parseResponse(jiraEndPoints.updateComponent(id, component).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	/**
	 * Component issue count.
	 * 
	 * @param id
	 *            component id.
	 * @return count.
	 */
	public ResponseData<Count> getComponentIssueCount(final int id) {
		try {
			return parseResponse(jiraEndPoints.getComponentIssueCount(id).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	/**
	 * Queries the issues by given id.
	 * 
	 * @param issueIdOrKey
	 *            issue id or key.
	 * @return issue.
	 */
	public ResponseData<Issue> getIssue(final String issueIdOrKey) {
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
	public ResponseData<Issue> createIssue(final Issue issue) {
		try {
			return parseResponse(jiraEndPoints.createIssue(issue).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Issue> updateIssue(final String issueIdorKey, final Issue issue) {
		try {
			return parseResponse(jiraEndPoints.updateIssue(issueIdorKey, issue).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Issue> assignIssue(final String issueIdorKey, final User user) {
		try {
			return parseResponse(jiraEndPoints.assignIssue(issueIdorKey, user).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Issues> createIssues(final Issues issues) {
		try {
			return parseResponse(jiraEndPoints.createIssues(issues).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Comments> getComments(final String issueIdorKey) {
		try {
			return parseResponse(jiraEndPoints.getComments(issueIdorKey).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Comment> addComment(final String issueIdorKey, final Comment comment) {
		try {
			return parseResponse(jiraEndPoints.addComment(issueIdorKey, comment).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Comment> updateComment(final String issueIdorKey, final int commentId, final Comment comment) {
		try {
			return parseResponse(jiraEndPoints.updateComment(issueIdorKey, commentId, comment).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Comment> getComment(final String issueIdorKey, final int commentId) {
		try {
			return parseResponse(jiraEndPoints.getComment(issueIdorKey, commentId).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Notify> notifyIssue(final String issueIdorKey, final Notify notify) {
		try {
			return parseResponse(jiraEndPoints.notifyIssue(issueIdorKey, notify).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Transitions> getTransitions(final String issueIdorKey) {
		try {
			return parseResponse(jiraEndPoints.getTransitions(issueIdorKey).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Issue> transitionIssue(final String issueIdorKey, final Issue issue) {
		try {
			return parseResponse(jiraEndPoints.transitionIssue(issueIdorKey, issue).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}

	public ResponseData<Issues> searchIssues(final String jql, final int startAt, final int maxResults, final String[] fields) {
		try {
			return parseResponse(jiraEndPoints.searchIssues(jql, startAt, maxResults, fields).execute());
		} catch (Exception e) {
			return buildErrorResponse(e);
		}
	}
}