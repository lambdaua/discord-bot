package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import database.model.DbUser;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String USERS = "users";
    public static final String USER_ID = "user_id";
    public static final String INVITES = "invites";
    public static final String NAME = "name";
    public static final String INVITE_CODE = "code";
    public static final String INVITE_USES = "uses";
    public static final String ROLE = "role";

    private MongoCollection<Document> users;

    public UserRepository(MongoDatabase database) {
        this.users = database.getCollection(USERS);
    }

    public void insert(DbUser dbUser) {
        Document document = new Document();

        document.append(USER_ID, dbUser.getUserId())
                .append(NAME, dbUser.getName())
                .append(ROLE, dbUser.getRole());

        users.insertOne(document);
    }

    public void addInvite(long userId, String code, int invitesUses) {
        Bson filter = Filters.eq(USER_ID, userId);

        Document invite = new Document().append(INVITE_CODE, code)
                .append(INVITE_USES, invitesUses);

        Bson update = Updates.push(INVITES, invite);
        users.updateOne(filter, update);
    }

    public void updateInvite(long userId, String code, int invitesUses) {
        Bson filter = Filters.eq(USER_ID, userId);

        Document user = users.find(filter).first();
        List<Document> invites = (ArrayList<Document>) user.get(INVITES);

        Document invite = invites.stream().filter(in -> in.get(INVITE_CODE).equals(code)).findFirst().get();
        invite.replace(INVITE_USES, invitesUses);

        users.replaceOne(filter, user);
    }

    public void updateRole(long userId, String role) {
        Bson filter = Filters.eq(USER_ID, userId);

        Document user = users.find(filter).first();
        user.replace(ROLE, role);

        users.replaceOne(filter, user);
    }

    public DbUser get(long userId) {
        Bson filter = Filters.eq(USER_ID, userId);

        Document document = users.find(filter).first();

        return DbUser.from(document);
    }

    public List<DbUser> getAll() {
        return users.find().map(DbUser::from).into(new ArrayList<>());
    }
}
