package networking.httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * Created by liumengjun on 2017-07-14.
 */
public class HttpClientTool {
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String DEFAULT_CONTENT_TYPE = CONTENT_TYPE_JSON;

    public static String post(String url, Map<String, Object> textMap, Map<String, File> fileMap) throws IOException {
        final boolean hasFile = fileMap != null && !fileMap.isEmpty();

        HttpEntity he = null;
        if (hasFile) {
            MultipartEntityBuilder meb = MultipartEntityBuilder.create();
            textMap.forEach((name, obj) -> {
                meb.addTextBody(name, obj == null ? "" : obj.toString());
            });
            fileMap.forEach((name, file) -> {
                String mineType = URLConnection.guessContentTypeFromName(file.getName());
                meb.addBinaryBody(name, file, ContentType.create(mineType), file.getName());
            });
            he = meb.build();
        } else {
            he = new StringEntity(JSON.toJSONString(textMap));
        }
        String contentType = hasFile ? he.getContentType().getValue() : DEFAULT_CONTENT_TYPE;

        HttpPost post = new HttpPost(url);
        post.addHeader("Date", Instant.now().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)));
        post.addHeader("Authorization", "KEY:ENCODED-SECRET");
        post.addHeader("Content-Type", contentType);
        post.setHeader("Accept", CONTENT_TYPE_JSON);
        post.setEntity(he);
        HttpClient hc = HttpClients.createDefault();
        HttpResponse res = hc.execute(post);
        int code = res.getStatusLine().getStatusCode();
        if (code >= 200 && code < 300) {
            return EntityUtils.toString(res.getEntity());
        }
        return null;
    }
}
