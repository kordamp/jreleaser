/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
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
package org.jreleaser.tools;

import org.jreleaser.model.JReleaserContext;
import org.jreleaser.model.Scoop;
import org.jreleaser.model.tool.spi.ToolProcessorFactory;
import org.kordamp.jipsy.annotations.ServiceProviderFor;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
@ServiceProviderFor(ToolProcessorFactory.class)
public class ScoopToolProcessorFactory implements ToolProcessorFactory<Scoop, ScoopToolProcessor> {
    @Override
    public String getName() {
        return Scoop.NAME;
    }

    @Override
    public ScoopToolProcessor getToolProcessor(JReleaserContext context) {
        return new ScoopToolProcessor(context);
    }
}