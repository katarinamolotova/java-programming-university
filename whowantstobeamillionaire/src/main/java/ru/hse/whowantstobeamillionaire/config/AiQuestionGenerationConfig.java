package ru.hse.whowantstobeamillionaire.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Configuration
public class AiQuestionGenerationConfig {

    @Bean
    RestClient.Builder aiQuestionGenerationRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    ObjectMapper aiQuestionGenerationObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    RestClient aiQuestionGenerationRestClient(
            RestClient.Builder restClientBuilder,
            AiQuestionGenerationProperties properties
    ) {
        return restClientBuilder.clone()
                .baseUrl(properties.getBaseUrl())
                .requestFactory(createUnsafeRequestFactory(properties))
                .build();
    }

    @Bean
    RestClient aiQuestionGenerationAuthRestClient(
            RestClient.Builder restClientBuilder,
            AiQuestionGenerationProperties properties
    ) {
        return restClientBuilder.clone()
                .requestFactory(createUnsafeRequestFactory(properties))
                .build();
    }

    private ClientHttpRequestFactory createUnsafeRequestFactory(AiQuestionGenerationProperties properties) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(properties.getConnectTimeoutMs()))
                    .sslContext(sslContext)
                    .build();

            JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Duration.ofMillis(properties.getReadTimeoutMs()));
            return requestFactory;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create SSL context", e);
        }
    }
}