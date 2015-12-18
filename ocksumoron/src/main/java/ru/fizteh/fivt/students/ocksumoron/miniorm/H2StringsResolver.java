package ru.fizteh.fivt.students.ocksumoron.miniorm;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class H2StringsResolver {
    private static Map<Class, String> h2Strings;
    static {
        h2Strings = new HashMap<>();
        h2Strings.put(Integer.class, "INTEGER");
        h2Strings.put(Boolean.class, "BOOLEAN");
        h2Strings.put(Byte.class, "TINYINT");
        h2Strings.put(Short.class, "SMALLINT");
        h2Strings.put(Long.class, "BIGINT");
        h2Strings.put(Double.class, "DOUBLE");
        h2Strings.put(Float.class, "FLOAT");
        h2Strings.put(Time.class, "TIME");
        h2Strings.put(Date.class, "DATE");
        h2Strings.put(Timestamp.class, "TIMESTAMP");
        h2Strings.put(Character.class, "CHAR");
        h2Strings.put(String.class, "CLOB");
        h2Strings.put(UUID.class, "UUID");
    }

    public static String resolve(Class clazz) {
        if (h2Strings.containsKey(clazz)) {
            return h2Strings.get(clazz);
        }
        if (clazz.isArray()) {
            return "ARRAY";
        }
        return "OTHER";
    }
}
