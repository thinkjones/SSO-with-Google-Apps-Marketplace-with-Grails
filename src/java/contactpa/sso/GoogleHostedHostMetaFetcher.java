package contactpa.sso;

import com.google.inject.Inject;
import com.google.step2.discovery.UrlHostMetaFetcher;
import com.google.step2.http.HttpFetcher;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;


@Component
public class GoogleHostedHostMetaFetcher extends UrlHostMetaFetcher {

  private static final String SOURCE_PARAM = "step2.hostmeta.google.source";
  private static final String DEFAULT_SOURCE = "https://www.google.com";
  private static final String HOST_META_PATH = "/accounts/o8/.well-known/host-meta";
  private static final String DOMAIN_PARAM = "hd";
		  
  @Inject
  public GoogleHostedHostMetaFetcher(HttpFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected URI getHostMetaUriForHost(String host) throws URISyntaxException {
    String source = System.getProperty(SOURCE_PARAM, DEFAULT_SOURCE);
    String uri = source + HOST_META_PATH + "?" + DOMAIN_PARAM + "=" + host;
    return new URI(uri);
  }
}
