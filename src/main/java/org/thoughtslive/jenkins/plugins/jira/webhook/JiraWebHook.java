package org.thoughtslive.jenkins.plugins.jira.webhook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;

import hudson.Extension;
import hudson.model.RootAction;
import hudson.model.UnprotectedRootAction;
import jenkins.model.Jenkins;

@Extension
public class JiraWebHook implements UnprotectedRootAction {

  public static final String URL_PATH = "jira-steps-webhook";

  private static final String EVENT_ISSUE_UPDATED = "jira:issue_updated";

  private static final Logger LOG = Logger.getLogger(JiraWebHook.class.getName());

  @VisibleForTesting
  public final Map<String, List<JiraIssueChangeListener>> listeners = new HashMap<>();

  @Override
  public String getIconFileName() {
    return null;
  }

  @Override
  public String getDisplayName() {
    return null;
  }

  @Override
  public String getUrlName() {
    return URL_PATH;
  }

  public void registerWebhook(JiraIssueChangeListener listener) {
    LOG.fine("Registering hook for " + listener.getIssueIdOrKey());
    synchronized (listeners) {
      List<JiraIssueChangeListener> issueListeners = listeners.get(listener.getIssueIdOrKey());

      if (issueListeners == null) {
        issueListeners = new ArrayList<>();
        listeners.put(listener.getIssueIdOrKey(), issueListeners);
      }

      issueListeners.add(listener);
    }
  }

  public void unregisterWebhook(JiraIssueChangeListener listener) {
    LOG.fine("Unregistering hook for " + listener.getIssueIdOrKey());
    synchronized (listeners) {
      List<JiraIssueChangeListener> issueListeners = listeners.get(listener.getIssueIdOrKey());

      if (issueListeners == null) {
        LOG.fine("Removing unregistered JiraIssueChangeListener");
      } else {
        issueListeners.remove(listener);
        if (issueListeners.isEmpty()) {
          listeners.remove(listener.getIssueIdOrKey());
        }
      }
    }
  }

  private void notifyListeners(List<JiraIssueChangeListener> listeners, String field, String fieldValue) {
    // use ListIterator in order to remove elements
    for (ListIterator<JiraIssueChangeListener> it = listeners.listIterator(); it.hasNext();) {
      JiraIssueChangeListener listener = it.next();
      if (listener.onIssueUpdated(field, fieldValue)) {
        it.remove();
      }
    }
  }

  private void notifyUpdatedFields(String issueKey, JsonNode items) {
    synchronized (listeners) {
      List<JiraIssueChangeListener> issueListeners = listeners.get(issueKey);

      if (issueListeners == null) {
        return;
      }

      for (Iterator<JsonNode> i = items.iterator(); i.hasNext();) {
        JsonNode item = i.next();
        JsonNode field = item.get("field");
        JsonNode toString = item.get("toString");

        if (field != null && toString != null) {
          LOG.fine("Jira issue field changed: " + field.asText() + " --> " + toString.asText());

          notifyListeners(issueListeners, field.asText(), toString.asText());
        }
      }

      // if there is no listener waiting for this issue, remove entry
      if (listeners.get(issueKey).isEmpty()) {
        listeners.remove(issueKey);
      }
    }
  }

  private int onIssueUpdatedEvent(JsonNode notification) {
    JsonNode issue = notification.get("issue");

    if (issue == null) {
      return StaplerResponse.SC_BAD_REQUEST;
    }

    JsonNode issueKey = issue.get("key");

    if (issueKey == null) {
      return StaplerResponse.SC_BAD_REQUEST;
    }

    LOG.info("Jira issue updated: " + issueKey);

    JsonNode changelog = notification.get("changelog");

    if (changelog == null) {
      return StaplerResponse.SC_BAD_REQUEST;
    }

    JsonNode items = changelog.get("items");

    if (items == null) {
      return StaplerResponse.SC_BAD_REQUEST;
    }

    notifyUpdatedFields(issueKey.asText(), items);

    return StaplerResponse.SC_NO_CONTENT;
  }

  @RequirePOST
  public void doNotify(StaplerRequest request, StaplerResponse response) throws IOException {
    int sc;

    LOG.fine("Handling notify hook");

    ObjectMapper mapper = new ObjectMapper();

    JsonNode notification = mapper.readTree(request.getReader());

    LOG.finest(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(notification));

    JsonNode webhookEvent = notification.get("webhookEvent");

    if (webhookEvent == null) {
      response.setStatus(StaplerResponse.SC_BAD_REQUEST);
      return;
    }

    if (!EVENT_ISSUE_UPDATED.equals(webhookEvent.asText())) {
      response.setStatus(StaplerResponse.SC_NOT_IMPLEMENTED);
      return;
    }

    sc = onIssueUpdatedEvent(notification);

    response.setStatus(sc);
  }

  public static JiraWebHook get() {
    return Jenkins.getInstance().getExtensionList(RootAction.class).get(JiraWebHook.class);
  }
}
