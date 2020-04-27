package controller;

import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

@Path("/discord")
public class DiscordController {

    private Deque<JsonNode> messages = new ArrayDeque<>();

    @POST
    @Path("/webhook")
    public Response receive(JsonNode jsonNode) {
        if (messages.size() >= 200) {
            for (int i = 0; i < 100; i++) {
                messages.removeLast();
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public Queue<JsonNode> messages() {
        return messages;
    }
}
