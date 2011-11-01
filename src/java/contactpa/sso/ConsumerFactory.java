package contactpa.sso;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.step2.ConsumerHelper;
import org.openid4java.consumer.ConsumerAssociationStore;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.springframework.stereotype.Component;


@Component
public class ConsumerFactory {

    
    ConsumerHelper helper;

    public ConsumerFactory() {
        this(new InMemoryConsumerAssociationStore());
    }

    public ConsumerFactory(ConsumerAssociationStore store) {
        Injector injector = Guice.createInjector(new GuiceModule(store));
        helper = injector.getInstance(ConsumerHelper.class);
    }

    public ConsumerHelper getConsumerHelper() {
        return helper;
    }

}