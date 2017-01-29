package org.thoughtslive.jenkins.plugins.jira.api.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.Comment;
import org.thoughtslive.jenkins.plugins.jira.api.Transition;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
@Builder
public class TransitionInput implements Serializable {

	private static final long serialVersionUID = 2345079536278547254L;

	@Data
	@ToString
	@NoArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
	@Builder
	public static class Update implements Serializable {

		private static final long serialVersionUID = -7324855138181589198L;

		@JsonProperty("comment")
		private List<CommentWrapper> comments;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@AllArgsConstructor(onConstructor = @__({@DataBoundConstructor}))
	@Builder
	public static class CommentWrapper implements Serializable {

		private static final long serialVersionUID = 7808277894848842311L;

		@JsonProperty("add")
		private Comment comment;
	}

	@JsonProperty("update")
	private Update update;

	@JsonProperty("transition")
	private Transition transition;
}