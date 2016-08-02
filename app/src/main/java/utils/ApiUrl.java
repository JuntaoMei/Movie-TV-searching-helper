package utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by juntao on 7/27/16.
 */
public class ApiUrl {

    /*
     * TmdbApi API Base URL
     */
    private static final String TMDB_API_BASE = "https://api.themoviedb.org/3/";

    private final static String key = "&api_key=16f1d4452bf58e6649c066f3a9bc7b63";
    private static final String APPEND_TO_RESPONSE = "append_to_response";

    private final String baseUrl;

    private final Map<String, String> params = new HashMap<String, String>();

//
//    public ApiUrl(String apiKey, Object... urlElements) {
//        this(urlElements);
//
//        assert StringUtils.isNotBlank(apiKey);
//        addParam(PARAM_API_KEY, apiKey);
//
//    }


    public ApiUrl(Object... urlElements) {
        StringBuilder baseUrlBuilder = new StringBuilder(TMDB_API_BASE);

        for (int i = 0; i < urlElements.length; i++) {
            baseUrlBuilder.append(urlElements[i]);

            if (i < urlElements.length - 1) {
                baseUrlBuilder.append("/");
            }
        }

        baseUrl = baseUrlBuilder.toString();
    }


    public String getBaseUrl() {
        return baseUrl;
    }
    public URL buildUrl() {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        try {

            if (params.size() > 0) {
                List<String> keys = new ArrayList<String>(params.keySet());
                urlBuilder.append(URLEncoder.encode(params.get("title"), "UTF-8"));
                for (int i = 0; i < keys.size(); i++) {
                    urlBuilder.append("&");
                    String paramName = keys.get(i);

                    if (paramName.equals("title")) {
                        continue;
                    }
                    urlBuilder.append(paramName).append("=");
                    //urlBuilder.append(paramName).append("=");
                    urlBuilder.append(URLEncoder.encode(params.get(paramName), "UTF-8"));
                }
            }
            urlBuilder.append(key);
            return new URL(urlBuilder.toString());

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public void addParam(String name, Object value) {
        addParam(name, value.toString());
    }


    public void addParam(String name, String value) {
        if (params.containsKey(name)) {
            throw new RuntimeException("paramater '" + name + "' already defined");
        }

        name = StringUtils.trimToEmpty(name);
        if (name.isEmpty()) {
            throw new RuntimeException("parameter name can not be empty");
        }

        value = StringUtils.trimToEmpty(value);
        if (value.isEmpty()) {
            return;
        }

        params.put(name, value);
    }


    /**
     * Add argument
     *
     * @param key
     * @param value
     */
    public void addParam(String key, int value) {
        addParam(key, Integer.toString(value));
    }


    /**
     * Add argument
     *
     * @param key
     * @param value
     */
    public void addParam(String key, boolean value) {
        addParam(key, Boolean.toString(value));
    }


    /**
     * Convenience wrapper around addArgument
     *
     * @param appendToResponse Comma separated, any movie method
     */
    public void appendToResponse(String... appendToResponse) {
        if (appendToResponse == null || appendToResponse.length == 0) {
            return;
        }

        addParam(APPEND_TO_RESPONSE, StringUtils.join(appendToResponse, ","));
    }

}
