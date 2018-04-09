/*
 * MIT License
 *
 * Copyright (c) Copyright (c) 2017-2017, Greatmancode
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.greatmancode.legendarybotapi.utils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * OKHttp interceptor to do Battle.Net queries
 */
public class BattleNetAPIInterceptor implements Interceptor {

    /**
     * List containing all available battle.net API keys
     */
    private String usKey;
    private String euKey;
    private OAuth2AccessToken usToken;
    private OAuth20Service usService;
    private long usTokenExpire;
    private OAuth20Service euService;
    private OAuth2AccessToken euToken;
    private long euTokenExpire;

    /**
     * Build an instance of the Interceptor
     */
    public BattleNetAPIInterceptor() {
        try {
            usKey = System.getenv("US_KEY");
            String usSecret = System.getenv("US_SECRET");
            euKey = System.getenv("EU_KEY");
            String euSecret = System.getenv("EU_SECRET");

            if (usKey == null && euKey == null) {
                throw new IllegalArgumentException("Blizzard API requires at least one API key.");
            }

            //Copy over the secret/key to the other region
            if (usKey == null) {
                usKey = euKey;
                usSecret = euSecret;
            } else if (euKey == null) {
                euKey = usKey;
                euSecret = usSecret;
            }

            //Ensure both secrets are provided
            if (usSecret == null || euSecret == null) {
                throw new IllegalArgumentException("Blizzard API requires at least one API secret.");
            }

            if (usService == null) {
                usService = new ServiceBuilder(usKey)
                        .apiSecret(usSecret)
                        .build(new OAuthBattleNetApi("us"));
                usToken = usService.getAccessTokenClientCredentialsGrant();
                usTokenExpire = System.currentTimeMillis() + (usToken.getExpiresIn() * 1000);
            }

            if (euService == null) {
                euService = new ServiceBuilder(euKey)
                        .apiSecret(euSecret)
                        .build(new OAuthBattleNetApi("eu"));
                euToken = euService.getAccessTokenClientCredentialsGrant();
                euTokenExpire = System.currentTimeMillis() + (euToken.getExpiresIn() * 1000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl url = null;
        if (chain.request().url().host().equals("us.api.battle.net") && chain.request().url().encodedPath().contains("/data/")) {
            //Data mode US
            try {
                refreshToken();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            url = chain.request().url().newBuilder()
                    .addQueryParameter("locale", "en_US")
                    .addQueryParameter("access_token", usToken.getAccessToken())
                    .build();

        } else if (chain.request().url().host().equals("eu.api.battle.net") && chain.request().url().encodedPath().contains("/data/")) {
            //Data mode EU
            try {
                refreshToken();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            url = chain.request().url().newBuilder()
                    .addQueryParameter("locale", "en_US")
                    .addQueryParameter("access_token", euToken.getAccessToken())
                    .build();
        } else if (chain.request().url().host().equals("us.api.battle.net")) {
            url = chain.request().url().newBuilder()
                    .addQueryParameter("apikey", usKey)
                    .addQueryParameter("locale", "en_US")
                    .build();
        } else if (chain.request().url().host().equals("eu.api.battle.net")) {
            url = chain.request().url().newBuilder()
                    .addQueryParameter("apikey", euKey)
                    .addQueryParameter("locale", "en_US")
                    .build();
        }

        Request request = chain.request().newBuilder().url(url).build();
        Response response = chain.proceed(request);
        return response;
    }

    private void refreshToken() throws InterruptedException, ExecutionException, IOException {
        if (usToken == null) {
            usToken = usService.getAccessTokenClientCredentialsGrant();
        } else {
            //We do a time check.
            if (System.currentTimeMillis() > usTokenExpire) {
                usToken = usService.getAccessTokenClientCredentialsGrant();
            }
        }
        if (euToken == null) {
            euToken = euService.getAccessTokenClientCredentialsGrant();
        } else {
            //We do a time check.
            if (System.currentTimeMillis() > euTokenExpire) {
                euToken = euService.getAccessTokenClientCredentialsGrant();
            }
        }
    }
}
