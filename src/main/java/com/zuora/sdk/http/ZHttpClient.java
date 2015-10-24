/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.http;

import com.zuora.sdk.common.ZConfig;
import com.zuora.sdk.common.ZConstants;
import com.zuora.sdk.utils.ZLogger;
import com.zuora.sdk.utils.ZUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


public class ZHttpClient {

    private static CloseableHttpClient instance;

    /**
     * See: http://www.baeldung.com/httpclient-guide for migration
     * The proxy settings and SSL support has NOT been tested so far
     */
    ZHttpClient() throws IOException {

        HttpClientBuilder clientBuilder = HttpClients.custom();

        if (Boolean.valueOf(((String) ZConfig.getInstance().getVal("ssl.verify.peer")).toLowerCase())) {

            PoolingHttpClientConnectionManager manager = getBasicConnectionManager();
            clientBuilder.setConnectionManager(manager);

        } else {

            PoolingHttpClientConnectionManager manager = getFriendlyHttpClientConnectionManager(clientBuilder);
            clientBuilder.setConnectionManager(manager);
        }

        configHttpClient(clientBuilder);
    }

    private PoolingHttpClientConnectionManager getBasicConnectionManager() {

        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(Integer.parseInt((String) ZConfig.getInstance().getVal("http.max.connection.pool.size")));
        manager.setDefaultMaxPerRoute(Integer.parseInt((String) ZConfig.getInstance().getVal("http.max.connection.pool.size")));
        manager.closeIdleConnections(15, TimeUnit.SECONDS);
        return manager;
    }

    // ZHttpClient is a singleton shared by all ZClients
    public synchronized static CloseableHttpClient getInstance() {
        if (instance == null) {
            try {
                new ZHttpClient();
            } catch (IOException e) {
                ZLogger.getInstance().log("Could not construct a ZHttpClient instance ", ZConstants.LOG_BOTH);
            }
        }
        return instance;
    }

    // Return a verify_none httpclient
    private PoolingHttpClientConnectionManager getFriendlyHttpClientConnectionManager(HttpClientBuilder clientBuilder) {

        try {

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();

            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            clientBuilder.setSSLContext(sslContext);

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            pccm.setMaxTotal(Integer.parseInt((String) ZConfig.getInstance().getVal("http.max.connection.pool.size")));
            pccm.setDefaultMaxPerRoute(Integer.parseInt((String) ZConfig.getInstance().getVal("http.max.connection.pool.size")));
            pccm.closeIdleConnections(60, TimeUnit.SECONDS);

            return pccm;

        } catch (GeneralSecurityException e) {
            ZLogger.getInstance().log(e.getMessage(), ZConstants.LOG_BOTH);
            ZLogger.getInstance().log(ZUtils.stackTraceToString(e), ZConstants.LOG_BOTH);
            String errorMessage = "Fatal Error in creating friendlyHTTPClient";
            ZLogger.getInstance().log(errorMessage, ZConstants.LOG_BOTH);
            throw new RuntimeException(errorMessage);
        }
    }

    // set all the nuts and bolts for httpclient
    private void configHttpClient(HttpClientBuilder builder) {

        int timeout = Integer.parseInt((String) ZConfig.getInstance().getVal("http.connect.timeout"));
        int socketTimeout = Integer.parseInt((String) ZConfig.getInstance().getVal("http.receive.timeout"));

        builder = setProxyDetailsIfPresent(builder);


        builder = builder.setUserAgent((String) ZConfig.getInstance().getVal("http.user.agent"));

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(socketTimeout)
                .build();

        builder.setDefaultRequestConfig(config);

        ZLogger.getInstance().log("HTTPClient Settings: " + builder.toString(), ZConstants.LOG_API);

        instance = builder.build();
    }

    private HttpClientBuilder setProxyDetailsIfPresent(HttpClientBuilder builder) {

        // get settings for proxy
        boolean proxyUsed = Boolean.valueOf(((String) ZConfig.getInstance().getVal("proxy.used")).toLowerCase());
        String urlString = (String) ZConfig.getInstance().getVal("proxy.url");

        // if proxy is used and proxy url is specified ...
        if (proxyUsed && urlString != null && !urlString.equals("")) {
            // decode URL
            try {
                URL url = new URL(urlString);
                String proxyProtocol = url.getProtocol();
                String proxyHost = url.getHost();
                int proxyPort = url.getPort();

                // add authenticating proxy support if in use
                if (Boolean.valueOf(((String) ZConfig.getInstance().getVal("proxy.auth")).toLowerCase())) {

                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
                            new UsernamePasswordCredentials((String) ZConfig.getInstance().getVal("proxy.user"),
                                    (String) ZConfig.getInstance().getVal("proxy.password")));

                    builder.setDefaultCredentialsProvider(credentialsProvider);
                }

                HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyProtocol);
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
                builder = builder.setRoutePlanner(routePlanner);

                // proxy url is malformed ... giving up
            } catch (MalformedURLException e) {
                ZLogger.getInstance().log("Proxy URL string " + urlString + " is malformed.", ZConstants.LOG_BOTH);
                ZLogger.getInstance().log("Unable to use Proxy. Proxy config is not used.", ZConstants.LOG_BOTH);
            }
        }
        return builder;
    }

    // Clean up all TCP connections before being collected
    public void finalize() {

        try {
            super.finalize();
        } catch (Throwable ex) {
            // NOOP
        }

        try {
            instance.close();
        } catch (IOException e) {
            // NOOP
        }
    }
}
