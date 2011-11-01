package contactpa.sso;

import com.google.inject.*;
import com.google.step2.discovery.DefaultHostMetaFetcher;
import com.google.step2.discovery.HostMetaFetcher;
import com.google.step2.hybrid.HybridOauthMessage;
import com.google.step2.openid.ax2.AxMessage2;
import com.google.step2.xmlsimplesign.*;

import org.openid4java.consumer.ConsumerAssociationStore;
import org.openid4java.message.Message;
import org.openid4java.message.MessageException;


public class GuiceModule extends AbstractModule {

    
    ConsumerAssociationStore associationStore;

    public GuiceModule(ConsumerAssociationStore associationStore) {
        this.associationStore = associationStore;
    }

    @Override
    protected void configure() {
        try {
            Message.addExtensionFactory(AxMessage2.class);
        } catch (MessageException e) {
            throw new CreationException(null);
        }

        try {
            Message.addExtensionFactory(HybridOauthMessage.class);
        } catch (MessageException e) {
            throw new CreationException(null);
        }

        bind(ConsumerAssociationStore.class)
                .toInstance(associationStore);

    }

    @Provides
    @Singleton
    public CertValidator provideCertValidator(DefaultCertValidator defaultValidator) {
        CertValidator hardCodedValidator = new CnConstraintCertValidator() {
            @Override
            protected String getRequiredCn(String authority) {
                // Trust Google for signing discovery documents
                return "hosted-id.google.com";
            }
        };

        return new DisjunctiveCertValidator(defaultValidator, hardCodedValidator);
    }

    @Provides
    @Singleton
    public HostMetaFetcher provideHostMetaFetcher(
            DefaultHostMetaFetcher fetcher1,
            GoogleHostedHostMetaFetcher fetcher2) {
        // For Marketplace apps, the default host-meta fetching strategy likely won't work.  Use
        // Google's location first.
        return new SerialHostMetaFetcher(fetcher2, fetcher1);
    }

}