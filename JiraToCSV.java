import com.atlassian.jira.rest.client.api.RestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.jersey.JerseyRestClient;
import com.atlassian.util.concurrent.Promise;
import com.opencsv.CSVWriter;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JiraToCSV {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String jiraUrl = "https://your-jira-instance.atlassian.net";
        String username = "your-username";
        String token = "your-personal-access-token";
        String jql = "your-jql-query";
        String csvFile = "issues.csv";

        // Create Jira REST client
        RestClient restClient = new JerseyRestClient(URI.create(jiraUrl));
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(URI.create(jiraUrl), new UsernamePasswordCredentials(username, token));
        restClient.getAuthenticationContext().setCredentialsProvider(credentialsProvider);

        // Execute JQL query
        Promise<Iterable<Issue>> searchResultPromise = restClient.getSearchClient().searchJql(jql);
        Iterable<Issue> issues = searchResultPromise.get();

        // Create CSV file
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));

        // Write header row (adjust fields as needed)
        String[] header = {"key", "summary", "status", "assignee", "created"};
        writer.writeNext(header);

        // Write issue data
        for (Issue issue : issues) {
            String[] data = {
                    issue.getKey(),
                    issue.getFields().getSummary(),
                    issue.getFields().getStatus().getName(),
                    issue.getFields().getAssignee() != null ? issue.getFields().getAssignee().getDisplayName() : "",
                    issue.getFields().getCreated().toString()
            };
            writer.writeNext(data);
        }

        writer.close();
        restClient.close();
    }
}