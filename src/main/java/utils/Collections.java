package utils;

import database.model.DbUser;

import java.util.List;

public class Collections {

    public interface CountFunction<T> {
        int count(T t);
    }

    public static <T> int count(List<T> tList, CountFunction<T> countFunction) {
        if (tList == null) return 0;

        int sum = 0;
        for (T t : tList) {
            sum += countFunction.count(t);
        }
        return sum;
    }

    public static List<DbUser> safeSubList(List<DbUser> allUsers, int count) {
        return allUsers.subList(0, Math.min(count, allUsers.size()));
    }
}
