package africa.absa.ursa.minor;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import io.confluent.ksql.api.client.StreamedQueryResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{

    public static String KSQLDB_SERVER_HOST = "pksqlc-71x0j.af-south-1.aws.confluent.cloud";
    public static int KSQLDB_SERVER_HOST_PORT = 443;

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        System.out.println("Starting");
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT)
                .setKeyAlias("KETKOOWMYEL6YP5I")
                .setKeyPassword("8ZHtnuTvL0sxVE9xZeQFzALtaL38Bh3JMqm/1G9bMliXRS4g+sYTivbcR5LtTKmc");
        Client client = Client.create(options);

        // Send requests with the client by following the other examples
        client.streamQuery("select * from DUCKHUNTSCORE EMIT CHANGES;")
                .thenAccept(streamedQueryResult -> {
                    System.out.println("Query has started. Query ID: " + streamedQueryResult.queryID());

                    RowSubscriber subscriber = new RowSubscriber();
                    streamedQueryResult.subscribe(subscriber);
                }).exceptionally(e -> {
                    System.out.println("Request failed: " + e);
                    return null;
                }).get();
        // Terminate any open connections and close the client
        client.close();
    }
}
