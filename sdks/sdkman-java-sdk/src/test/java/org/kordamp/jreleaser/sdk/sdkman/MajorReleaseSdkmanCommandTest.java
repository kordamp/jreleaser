/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.jreleaser.sdk.sdkman;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kordamp.jreleaser.sdk.sdkman.ApiEndpoints.ANNOUNCE_ENDPOINT;
import static org.kordamp.jreleaser.sdk.sdkman.ApiEndpoints.DEFAULT_ENDPOINT;
import static org.kordamp.jreleaser.sdk.sdkman.ApiEndpoints.RELEASE_ENDPOINT;
import static org.kordamp.jreleaser.sdk.sdkman.Stubs.verifyPost;
import static org.kordamp.jreleaser.sdk.sdkman.Stubs.verifyPut;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public class MajorReleaseSdkmanCommandTest {
    @RegisterExtension
    WireMockExtension api = new WireMockExtension(options().dynamicPort());

    @Test
    public void testMajorReleaseWithAnnouncement() throws SdkmanException {
        // given:
        stubFor(post(urlEqualTo(RELEASE_ENDPOINT))
                .willReturn(okJson("{\"status\": 201, \"message\":\"success\"}")));
        stubFor(post(urlEqualTo(ANNOUNCE_ENDPOINT))
                .willReturn(okJson("{\"status\": 201, \"message\":\"success\"}")));
        stubFor(put(urlEqualTo(DEFAULT_ENDPOINT))
                .willReturn(okJson("{\"status\": 201, \"message\":\"success\"}")));

        MajorReleaseSdkmanCommand command = MajorReleaseSdkmanCommand.builder()
                .apiHost(api.baseUrl())
                .consumerKey("CONSUMER_KEY")
                .consumerToken("CONSUMER_TOKEN")
                .candidate("jreleaser")
                .version("1.0.0")
                .url("https://host/jreleaser-1.0.0.zip")
                .https(false)
                .build();

        // when:
        command.execute();

        // then:
        verifyPost(RELEASE_ENDPOINT, "{\n" +
                "   \"candidate\": \"jreleaser\",\n" +
                "   \"version\": \"1.0.0\",\n" +
                "   \"platform\": \"UNIVERSAL\",\n" +
                "   \"url\": \"https://host/jreleaser-1.0.0.zip\"\n" +
                "}");
        verifyPost(ANNOUNCE_ENDPOINT, "{\n" +
                "   \"candidate\": \"jreleaser\",\n" +
                "   \"version\": \"1.0.0\"\n" +
                "}");
        verifyPut(DEFAULT_ENDPOINT, "{\n" +
                "   \"candidate\": \"jreleaser\",\n" +
                "   \"version\": \"1.0.0\"\n" +
                "}");
    }

    @Test
    public void testError() {
        // given:
        stubFor(post(urlEqualTo(RELEASE_ENDPOINT))
                .willReturn(aResponse().withStatus(500)));

        MajorReleaseSdkmanCommand command = MajorReleaseSdkmanCommand.builder()
                .apiHost(api.baseUrl())
                .consumerKey("CONSUMER_KEY")
                .consumerToken("CONSUMER_TOKEN")
                .candidate("jreleaser")
                .version("1.0.0")
                .url("https://host/jreleaser-1.0.0.zip")
                .https(false)
                .build();

        // expected:
        assertThrows(SdkmanException.class, command::execute);
    }
}