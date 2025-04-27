package dto;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;

public abstract class JsonConvert {
    private static final Gson
        gson =
        new Gson()
            .newBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();
    
    public static Gson getGson() {
        return gson;
    }
    
    private JsonConvert() {
        throw new UnsupportedOperationException(
            "JsonConvert es una clase de utilidad y no se puede crear instancias.");
    }
}
