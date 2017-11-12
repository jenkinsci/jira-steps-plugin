package org.thoughtslive.jenkins.plugins.jira.service

import org.thoughtslive.jenkins.plugins.jira.Site;
 

// This is just a utility class for local testing, nothing to do with any other changes.
class JiraServiceIT {

  static main(args) {
    final Site config = new Site("JIRA2", new URL("http://192.168.1.181:8080/"), "BASIC", 10000);
    config.setUserName('jenkins')
    config.setPassword('jenkins$123');
    def service = new JiraService(config);
    
    println service.addComment('TEST-27', 'Testing From Groovy Class').code;
    
    println service.addIssueWatcher('TEST-27', 'naresh').code;
    
    println service.getIssue("TEST-27").code
    
    println service.assignIssue('TEST-27', 'jenkins').code
    
    println service.updateComment('TEST-27', '10604', 'changing it to something else again').code

    def component = [id: '10100', name: 'test-comp']
    println service.updateComponent(component).code
    
    def issue = [fields: [summary: 'newSummary001',
                          customfield_10000: 'Testing']]
    println service.updateIssue('TEST-27', issue).error

    def version = [id: '10300', name: 'testingT']
    println service.updateVersion('10300', version).code
    
    println service.getComments('TEST-27').code
    
    println service.getComment('TEST-27', '10604').code
    
    println service.getComponentIssueCount('10100').code
    
    println service.getComponent('10100').code
    
    println service.getIssueLinkTypes().code
    
    println service.getIssue('TEST-27').code
    
    println service.getIssueWatches('TEST-27').code
    
    println service.getProjects().code
    
    println service.getProject('TEST').code
    
    println service.getVersion('10300').code
    
    println service.searchIssues('PROJECT=TEST', 0, 20).code
    
    println service.linkIssues('Duplicate', 'TEST-27', 'TEST-33', 'Linked Issue Comment').code
    
    println service.createComponent([name: 'TcOMP', project: 'TEST']).code
    
	println service.createIssue([fields: [summary: "test",
	                                      description: 'desc',
	                                      project: [key: 'TEST'],
	                                      issuetype: [name: 'Task']]]).code

	println service.createIssues([issueUpdates: [[fields: [summary: "test",
	                                                 description: 'desc',
	                                                 project: [key: 'TEST'],
	                                                 issuetype: [name: 'Task']]]]]).code
	                                                 
	println service.createVersion([name: 'test-vvv1', project: 'TEST']).code
	
    println service.getIssueTransitions('TEST-27').code
	
	println service.notifyIssue('TEST-27', [subject: 'HELLO', textBody: 'ABC']).error
	
	def transitionInput = [
        transition: [
            id: 31
        ]
    ]

	println service.transitionIssue('TEST-27', transitionInput).code                                             

	transitionInput = [
        transition: [
            id: 11
        ]
    ]
	
	println service.transitionIssue('TEST-27', transitionInput).code
	
	println service.getFields().code

	println service.getProjectVersions('TEST').code
	
	println service.getProjectComponents('TEST').code                                            

	println service.getProjectStatuses('TEST').code
	
	println service.userSearch('jenkins', 0, 10).code    

	println service.assignableUserSearch('jenkins', 'TEST', '', 0, 10).code   
	
	println service.linkIssues('Duplicate', 'TEST-27', 'TEST-33', 'Linked Issue Comment').code
	
	def issueLinkId =  service.getIssue('TEST-27').data.fields.issuelinks[0].id

	println service.getIssueLink(issueLinkId).code
	
	println service.deleteIssueLink(issueLinkId).code
	 
  }

}
