package app.utils;

import okhttp3.Headers;

/**
 * Created by Fadejimi on 7/12/18.
 */

public class RestUtil {
    public static final int getId(Headers headers) {
        String location = headers.get("location");
        String[] locArray = location.split("/");
        int id = Integer.parseInt(locArray[locArray.length - 1]);
        return id;
    }
}
