package numble.mybox.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import numble.mybox.common.presentation.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer{

  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  public WebFluxConfig(
      CurrentUserArgumentResolver currentUserArgumentResolver) {
    this.currentUserArgumentResolver = currentUserArgumentResolver;
  }

  @Override
  public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
    WebFluxConfigurer.super.configureArgumentResolvers(configurer);
    configurer.addCustomResolver(currentUserArgumentResolver);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowedHeaders("*");
  }

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    var partReader = new DefaultPartHttpMessageReader();
    partReader.setMaxParts(3);
    // Configure the maximum amount of disk space allowed for file parts
    partReader.setMaxDiskUsagePerPart(30L* 10000L * 1024L); // 307,2 MO
    partReader.setEnableLoggingRequestDetails(true);
    MultipartHttpMessageReader multipartReader = new
        MultipartHttpMessageReader(partReader);
    multipartReader.setEnableLoggingRequestDetails(true);
    configurer.defaultCodecs().multipartReader(multipartReader);

    configurer.defaultCodecs().maxInMemorySize(512 * 1024);
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient()))
        .build();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn ->
            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
  }

}
