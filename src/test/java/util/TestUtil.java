package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestUtil {

    public static boolean isJSONEqual(String json_lhs, String json_rhs) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json_lhs).equals(objectMapper.readTree(json_rhs));
    }

}
