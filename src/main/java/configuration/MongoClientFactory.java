package configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.MongoClientURI;

import javax.validation.constraints.NotNull;
import java.net.UnknownHostException;

public class MongoClientFactory {

    @NotNull
    @JsonProperty
    private MongoClientURI uri;

    public ManagedMongoClient build() throws UnknownHostException {
        return new ManagedMongoClient(uri);
    }
}
