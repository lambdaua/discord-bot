package data;

import data.model.Invite;
import data.model.Role;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface DiscordRestService {
    String discordBaseUrl = "https://discordapp.com/api/";

    @GET("guilds/{guild_id}/invites")
    Call<List<Invite>> getGuildInvites(@Path("guild_id") String guildId,
                                       @Header("Authorization") String botToken);

    @GET("guilds/{guild_id}/roles")
    Call<List<Role>> getGuildRoles(@Path("guild_id") String guildId,
                                   @Header("Authorization") String botToken);

    @PUT("guilds/{guild_id}/members/{user_id}/roles/{role_id}")
    Call<Response<Void>> addRole(@Path("guild_id") String guildId,
                                 @Path("user_id") long userId,
                                 @Path("role_id") String roleId,
                                 @Header("Authorization") String botToken);

    @DELETE("guilds/{guild_id}/members/{user_id}/roles/{role_id}")
    Call<Response<Void>> removeRole(@Path("guild_id") String guildId,
                            @Path("user_id") long userId,
                            @Path("role_id") String roleId,
                            @Header("Authorization") String botToken);
}
