import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class IO {
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(60)).build();

  private static CompletableFuture<String> fetch(String uri) {
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uri)).header("Accept", "application/json")
        .build();

    return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
      int successCode = response.statusCode();
      String body = response.body();
      if (successCode == 200) {
        return body;
      } else {
        throw new RuntimeException("Api request failed with status code" + successCode);
      }
    });
  }
}