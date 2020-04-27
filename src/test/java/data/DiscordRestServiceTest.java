package data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import data.model.Role;
import okhttp3.OkHttpClient;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DiscordRestServiceTest {

    @Test
    public void getGuildRoles() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create(mapper);

        Retrofit discordRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(DiscordRestService.discordBaseUrl)
                .addConverterFactory(jacksonConverterFactory)
                .build();

        DiscordRestService guildInvitesRest = discordRetrofit.create(DiscordRestService.class);

        try {
            List<Role> guildRoles = guildInvitesRest.getGuildRoles("401435746415935488", "Bot NDAxNDkxMjgyNzM4MDIwMzUy.DTzheg.tfdPRGnRRD8T1Z7bBmcGN61oOVQ").execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}