package com.ndebugs.nhttpx.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class HTTPConnection {

    private HttpURLConnection connection;
    private int bufferSize = 8192;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int open(String url, Map<String, String> params, HTTPMethod method) throws IOException {
        String postQuery = null;
        if (params != null && !params.isEmpty()) {
            String query = toQueryString(params);
            if (method == HTTPMethod.GET) {
                url += '?' + query;
            } else if (method == HTTPMethod.POST) {
                postQuery = query;
            }
        }

        URL obj = URI.create(url).toURL();
        connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod(method.toString());
        if (postQuery != null) {
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postQuery.getBytes());
                os.flush();
            }
        }

        return connection.getResponseCode();
    }

    public byte[] getResponseBytes() throws IOException {
        ByteArrayOutputStream out;
        try (InputStream in = connection.getInputStream()) {
            out = new ByteArrayOutputStream(in.available());
            int length;
            byte[] buffer = new byte[bufferSize];
            while ((length = in.read(buffer)) > -1) {
                out.write(buffer, 0, length);
            }
        }

        return out.toByteArray();
    }

    public String toQueryString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&');
            }

            String value = URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name());
            sb.append(e.getKey()).append('=').append(value);
        }
        return sb.toString();
    }
}
