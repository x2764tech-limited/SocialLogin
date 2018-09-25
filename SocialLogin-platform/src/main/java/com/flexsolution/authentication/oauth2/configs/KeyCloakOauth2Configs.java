package com.flexsolution.authentication.oauth2.configs;

import com.flexsolution.authentication.oauth2.dto.AccessToken;
import com.flexsolution.authentication.oauth2.dto.UserMetadata;
import com.flexsolution.authentication.oauth2.model.Oauth2ConfigModel;
import com.google.gson.Gson;
import lombok.Data;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.http.MediaType;
import org.springframework.social.oauth2.OAuth2Version;

import java.io.IOException;

public class KeyCloakOauth2Configs extends AbstractOauth2Configs {

    private final Log logger = LogFactory.getLog(this.getClass());
    private String accessTokenUrl;
    private String authorizationUrl;
    private static final String KEYCLOAK = "keycloak";
    public static final String AVATAR_FILE_NAME = KEYCLOAK + "_avatar.jpeg";
    private String userDataUrl;

    @Override
    String getAccessTokenURL() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    @Override
    QName getClientIdQName() {
        return Oauth2ConfigModel.PROP_LINKED_IN_CLIENT_ID;
    }

    @Override
    QName getSecretKeyQName() {
        return Oauth2ConfigModel.PROP_LINKED_IN_SECRET_KEY;
    }

    @Override
    QName getQNameForEnableField() {
        return Oauth2ConfigModel.PROP_LINKED_IN_OAUTH2_SIGN_IN_ENABLED;
    }

    @Override
    String getAuthorizationURL() {
        return authorizationUrl;
    }

    public void setAuthorisationUrl(String authorisationUrl) {
        this.authorizationUrl = authorisationUrl;
    }

    @Override
    String getUserDataUrl() {
        return userDataUrl;
    }

    @Override
    public String getUserNamePrefix() {
        return KEYCLOAK;
    }

    @Override
    public String getAvatarName() {
        return AVATAR_FILE_NAME;
    }

    @Override
    public String getApiName() {
        return KEYCLOAK;
    }

    @Override
    public UserMetadata getUserMetadata(AccessToken accessToken) {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {
            HttpGet get = new HttpGet(getUserDataUrl());
            get.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            get.setHeader(HttpHeaders.AUTHORIZATION, OAuth2Version.BEARER.getAuthorizationHeaderValue(accessToken.getAccess_token()));

            try (CloseableHttpResponse response = httpclient.execute(get)) {

                int statusCode = response.getStatusLine().getStatusCode();

                HttpEntity entity = response.getEntity();

                if (statusCode != Status.STATUS_OK) {
                    throw new WebScriptException(Status.STATUS_UNAUTHORIZED, entity.toString());
                }

                String responseString = EntityUtils.toString(entity, CharEncoding.UTF_8);

                logger.debug(responseString);

                return new Gson().fromJson(responseString, KeyCloakUserMetadata.class).toUserMetadata();

            }
        } catch (IOException e) {
            throw new WebScriptException(Status.STATUS_UNAUTHORIZED, e.toString());
        }
    }

    public void setUserDataUrl(String userDataUrl) {
        this.userDataUrl = userDataUrl;
    }

    @Data
    private class KeyCloakUserMetadata {
        private String family_name;
        private String given_name;
        private String email;
        private String preferred_username;

        UserMetadata toUserMetadata() {
            UserMetadata userMetadata = new UserMetadata();
            userMetadata.setId(this.preferred_username);
            userMetadata.setFirstName(given_name);
            userMetadata.setLastName(family_name);
            userMetadata.setEmailAddress(email);
            return userMetadata;
        }
    }
}


