package org.thoughtslive.jenkins.plugins.jira.service;

import java.io.IOException;
import java.net.URL;

import org.thoughtslive.jenkins.plugins.jira.Site;
import org.thoughtslive.jenkins.plugins.jira.api.Comments;
import org.thoughtslive.jenkins.plugins.jira.api.Issue;
import org.thoughtslive.jenkins.plugins.jira.api.Issues;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.api.SearchResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JiraServiceTest {

	public static void main(String[] args) throws IOException {

		final Site config = new Site("JIRA2", new URL("http://192.168.1.176:8080/"), "BASIC", 10000);
		config.setUserName("jenkins");
		config.setPassword("jenkins$143");
		final JiraService service = new JiraService(config);

		// Component component = new Component();
		// component.setName("test-component");
		// component.setProject("TEST");
		// System.out.println(service.createComponent(component));
		System.out.println(service.getServerInfo().getData());
		// System.out.println(service.getComponent(10000));
		System.out.println(service.getComponentIssueCount(10000));
		//
		ResponseData<Issue> issue = service.getIssue("TEST-1");
		System.out.println(issue.getData());
		//
		 ObjectMapper mapper = new ObjectMapper();
		 String jsonInString1 = "{\"fields\": { \"project\": { \"id\":\"10000\" },\"summary\": \"New Issue\", \"issuetype\": {\"id\": \"3\"}}}";
		 String jsonInString2 = "{\"fields\": { \"project\": { \"id\": \"10000\" },\"summary\": \"something\'s wrong4\", \"issuetype\":	 {\"id\": \"3\"}}}";
		 Issue issue1 = mapper.readValue(jsonInString1, Issue.class);
		 Issue issue2 = mapper.readValue(jsonInString2, Issue.class);
		 System.out.println(issue1);
		 System.out.println(issue2);

		// ResponseData<Issue> newIssue = service.createIssue(issue1);
		// System.out.println(newIssue);

		// ResponseData<Issue> updateIssue = service.updateIssue("TEST-1",
		// issue2);
		// System.out.println(updateIssue);

		// User user = new User();
		// user.setName("naresh");
		//
		// ResponseData<Issue> issue3 = service.assignIssue("TEST-1", user);
		// System.out.println(issue3);

		// Comment comment = new Comment();
		// comment.setBody("TEST COMMENT FROM Basic API - Jenkins");
		// ResponseData<Comment> commentRes = service.updateComment("TEST-1",
		// 10000, comment);
		// System.out.println(commentRes);

		// Comment comment = new Comment();
		// comment.setBody("TEST COMMENT FROM API113");
		// ResponseData<Comment> commentRes = service.updateComment("TEST-1",
		// 10000, comment);
		// System.out.println(commentRes);

		// ResponseData<Comments> comments = service.getComments("TEST-1");
		// System.out.println(comments);

		// ResponseData<Comment> commentResponse = service.getComment("TEST-1",
		// 10001);
		// System.out.println(commentResponse.isSuccessful());
		// System.out.println(commentResponse.getData().getBody());

		// Issue[] newIssues = new Issue[2];
		// newIssues[0]=issue1;
		// newIssues[1]=issue2;
		// Issues issues = new Issues();
		// issues.setIssueUpdates(newIssues);
		// System.out.println(issues);
		// ResponseData<Issues> newIssues1 = service.createIssues(issues);
		// System.out.println(newIssues1);

		// final Notify notify = new Notify();
		// notify.setTextBody("Testing Email");
		// notify.setSubject("TEST EMAIL From Jira (Triggred from Jenkins)");
		// System.out.println(service.notifyIssue("TEST-1", notify));
		// System.out.println(service.getTransitions("TEST-1"));

		// ObjectMapper mapper = new ObjectMapper();
		// String jsonInString1 = "{\"transition\": {\"id\": \"41\"}}";
		// String jsonInString2 = "{\"fields\": { \"project\": { \"id\":
		// \"10000\" },\"summary\": \"something\'s wrong4\", \"issuetype\":
		// {\"id\": \"3\"}}}";
		// Issue issue1 = mapper.readValue(jsonInString1, Issue.class);
		// Issue issue2 = mapper.readValue(jsonInString2, Issue.class);
		// System.out.println(issue1);
		// System.out.println(issue2);

		// Notes: Need to have reporter
		// ResponseData<Issue> newIssue = service.transitionIssue("TEST-1",
		// issue1);
		// System.out.println(newIssue);

		// User user = new User();
		// user.setName("naresh");
		//
		// ResponseData<Issue> issue3 = service.assignIssue("TEST-1", user);
		// System.out.println(issue3);
		
		ResponseData<SearchResult> issues = service.searchIssues("PROJECT = TEST", 0, 1000);
		System.out.println(issues);
		
		System.out.println(service.getProjects().getData()[0]);
		System.out.println(service.getProject("TEST").getData());
		System.out.println(service.getIssueLinkTypes().getData().getIssueLinkTypes()[1]);
		System.out.println(service.linkIssues("Relates", "TEST-1", "TEST-2", "Testing Issue Linking").getError());

	}

}