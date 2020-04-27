package configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApiConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private MongoClientFactory mongo;

    @JsonProperty
    private String botToken;

    @JsonProperty
    private String guildId;

    public String getBotToken() {
        return botToken;
    }

    public String getGuildId() {
        return guildId;
    }

    public MongoClientFactory getMongo() {
        return mongo;
    }
}
