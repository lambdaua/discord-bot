import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mongodb.client.MongoDatabase;
import configuration.ApiConfiguration;
import configuration.ManagedMongoClient;
import data.DiscordRestService;
import data.model.Role;
import database.UserRepository;
import database.UserService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import messenger.DiscordMessenger;
import messenger.MessagesReceiver;
import messenger.Messenger;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class ApiApplication extends Application<ApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new ApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.addBundle(new AssetsBundle("/assets", "/files"));

        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false))
        );

        bootstrap.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            ManagedMongoClient mongoClient = configuration.getMongo().build();
            environment.lifecycle().manage(mongoClient);
            MongoDatabase database = mongoClient.getDatabase("discord-db");

            String guildId = configuration.getGuildId();
            String botToken = configuration.getBotToken();

            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create(mapper);

            Retrofit discordRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(DiscordRestService.discordBaseUrl)
                    .addConverterFactory(jacksonConverterFactory)
                    .build();

            DiscordRestService guildInvitesRest = discordRetrofit.create(DiscordRestService.class);

            List<Role> guildRoles = guildInvitesRest.getGuildRoles(guildId, "Bot " + botToken).execute().body();

            UserRepository userRepository = new UserRepository(database);
            UserService userService = new UserService(userRepository, guildRoles, guildInvitesRest);

            Messenger discordMessenger = new DiscordMessenger();

            JDA jdaMessages = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .buildBlocking();

            jdaMessages.addEventListener(new MessagesReceiver(discordMessenger, userService,
                    guildInvitesRest, guildId, botToken));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
