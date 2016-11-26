package se.tuxflux.gibson.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static se.tuxflux.gibson.AwsCredentials.*;


@Configuration
@ConditionalOnClass(JestClient.class)
@EnableConfigurationProperties(JestProperties.class)
@AutoConfigureAfter(GsonAutoConfiguration.class)
public class JestConfig {
  private static final String SERVICE = "es";
  private static final String REGION = "eu-west-1";
  private final AWSCredentialsProvider awsCredentialsProvider = new StaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
  private final AWSSigner awsSigner = new AWSSigner(awsCredentialsProvider, REGION, SERVICE, () -> LocalDateTime.now(ZoneOffset.UTC));
  private final AWSSigningRequestInterceptor requestInterceptor = new AWSSigningRequestInterceptor(awsSigner);
  private final JestProperties properties;
  private final ObjectProvider<Gson> gsonProvider;


  @Bean(destroyMethod = "shutdownClient")
  @ConditionalOnMissingBean
  public JestClient jestClient() {
    final JestClientFactory factory = new JestClientFactory() {
      @Override
      protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
//        builder.addInterceptorLast(requestInterceptor);
        return builder;
      }

      @Override
      protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
//        builder.addInterceptorLast(requestInterceptor);
        return builder;
      }
    };

    factory.setHttpClientConfig(createHttpClientConfig());
    return factory.getObject();
  }

  public JestConfig(JestProperties properties, ObjectProvider<Gson> gsonProvider) {
    this.properties = properties;
    this.gsonProvider = gsonProvider;
  }


  protected HttpClientConfig createHttpClientConfig() {
    HttpClientConfig.Builder builder = new HttpClientConfig.Builder(ES_ROOT);
    if (StringUtils.hasText(this.properties.getUsername())) {
      builder.defaultCredentials(this.properties.getUsername(),
          this.properties.getPassword());
    }
    String proxyHost = this.properties.getProxy().getHost();
    if (StringUtils.hasText(proxyHost)) {
      Integer proxyPort = this.properties.getProxy().getPort();
      Assert.notNull(proxyPort, "Proxy port must not be null");
      builder.proxy(new HttpHost(proxyHost, proxyPort));
    }
    Gson gson = this.gsonProvider.getIfUnique();
    if (gson != null) {
      builder.gson(gson);
    }
    return builder.connTimeout(this.properties.getConnectionTimeout())
        .readTimeout(this.properties.getReadTimeout()).build();
  }
}