/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leaf.oauth2.security.jackson2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.Builder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.io.IOException;

/**
 *
 * @author Joe Grandja
 * @since 0.1.2
 */
final class OAuth2ClientAuthenticationTokenDeserializer extends JsonDeserializer<OAuth2ClientAuthenticationToken> {

    @Override
    public OAuth2ClientAuthenticationToken deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode root = mapper.readTree(parser);
        return deserialize(parser, mapper, root);
    }

    private OAuth2ClientAuthenticationToken deserialize(JsonParser parser, ObjectMapper mapper, JsonNode root)
            throws JsonParseException {
        /*OAuth2ClientAuthenticationToken token = new OAuth2ClientAuthenticationToken(
                JsonNodeUtils.findStringValue(root, "clientId"),

                );*/
        return null;
    }

}
