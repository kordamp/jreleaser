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
package org.kordamp.jreleaser.tools;

import org.kordamp.jreleaser.model.Chocolatey;
import org.kordamp.jreleaser.model.Distribution;
import org.kordamp.jreleaser.model.JReleaserModel;
import org.kordamp.jreleaser.model.Project;
import org.kordamp.jreleaser.util.Logger;
import org.kordamp.jreleaser.util.OsUtils;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.kordamp.jreleaser.templates.TemplateUtils.trimTplExtension;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public class ChocolateyToolProcessor extends AbstractToolProcessor<Chocolatey> {
    public ChocolateyToolProcessor(Logger logger, JReleaserModel model, Chocolatey tool) {
        super(logger, model, tool);
    }

    @Override
    protected boolean doPackageDistribution(Distribution distribution, Map<String, Object> context) throws ToolProcessingException {
        if (!OsUtils.isWindows()) {
            getLogger().debug("Tool {} must run on Windows", getToolName());
            return false;
        }

        return true;
    }

    @Override
    protected Set<String> resolveByExtensionsFor(Distribution.DistributionType type) {
        Set<String> set = new LinkedHashSet<>();
        set.add(".zip");
        return set;
    }

    @Override
    protected void fillToolProperties(Map<String, Object> context, Distribution distribution) throws ToolProcessingException {
        // noop
    }

    @Override
    protected void writeFile(Project project, Distribution distribution, String content, Map<String, Object> context, String fileName)
        throws ToolProcessingException {
        fileName = trimTplExtension(fileName);

        Path outputDirectory = (Path) context.get(Constants.KEY_PREPARE_DIRECTORY);
        Path outputFile = "binary.nuspec".equals(fileName) ?
            outputDirectory.resolve(distribution.getExecutable().concat(".nuspec")) :
            outputDirectory.resolve(fileName);

        writeFile(content, outputFile);
    }
}