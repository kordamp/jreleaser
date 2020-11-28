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
package org.kordamp.jreleaser.app;

import org.kordamp.jreleaser.app.internal.JReleaserModelPrinter;
import org.kordamp.jreleaser.model.JReleaserModel;
import picocli.CommandLine;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
@CommandLine.Command(name = "config",
    description = "Displays current configuration")
public class Config extends AbstractModelCommand {
    @Override
    protected void consumeModel(JReleaserModel jreleaserModel) {
        new JReleaserModelPrinter(parent.out).print(jreleaserModel.asMap());
    }
}