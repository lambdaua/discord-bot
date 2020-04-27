package dictionary;

public enum Role {
    UNKNOWN("", -1),
    NONE("", 0),
    BASIC("Basic", 5),
    PREFERRED("Preferred", 10),
    BRONZE("Bronze", 25),
    SILVER("Silver", 50),
    GOLD("Gold", 100),
    DIAMOND("Diamond", 250),
    PLATINUM("Platinum", 500);

    private final String name;
    private final int count;

    Role(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public Role getNextRole() {
        Role[] values = Role.values();
        int ordinal = this.ordinal();
        try {
            return values[ordinal + 1];
        } catch (Exception e) {
            return null;
        }
    }

    public static Role from(String name) {
        if (name.equals(UNKNOWN.name)) {
            return UNKNOWN;
        }
        if (name.equals(NONE.name)) {
            return NONE;
        }
        if (name.equals(BASIC.name)) {
            return BASIC;
        }
        if (name.equals(PREFERRED.name)) {
            return PREFERRED;
        }
        if (name.equals(BRONZE.name)) {
            return BRONZE;
        }
        if (name.equals(SILVER.name)) {
            return SILVER;
        }
        if (name.equals(GOLD.name)) {
            return GOLD;
        }
        if (name.equals(DIAMOND.name)) {
            return DIAMOND;
        }
        if (name.equals(PLATINUM.name)) {
            return PLATINUM;
        }

        throw new IllegalStateException("There's no role for name: " + name);
    }

    public static Role forInvites(int count) {
        if (isBetween(count, 0, 5)) {
            return Role.NONE;
        } else if (isBetween(count, 5, 10)) {
            return Role.BASIC;
        } else if (isBetween(count, 10, 25)) {
            return Role.PREFERRED;
        } else if (isBetween(count, 25, 50)) {
            return Role.BRONZE;
        } else if (isBetween(count, 50, 100)) {
            return Role.SILVER;
        } else if (isBetween(count, 100, 250)) {
            return Role.GOLD;
        } else if (isBetween(count, 250, 500)) {
            return Role.DIAMOND;
        } else if (count >= 500) {
            return Role.PLATINUM;
        }

        return Role.PLATINUM;
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}

