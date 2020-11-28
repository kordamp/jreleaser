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
package org.kordamp.jreleaser.gradle.plugin.internal.dsl

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.kordamp.jreleaser.gradle.plugin.dsl.BrewPackager
import org.kordamp.jreleaser.gradle.plugin.dsl.ChocolateyPackager
import org.kordamp.jreleaser.gradle.plugin.dsl.Packagers
import org.kordamp.jreleaser.gradle.plugin.dsl.ScoopPackager
import org.kordamp.jreleaser.gradle.plugin.dsl.SnapPackager

import javax.inject.Inject

/**
 *
 * @author Andres Almiray
 * @since 0.1.0
 */
@CompileStatic
class PackagersImpl implements Packagers {
    final BrewPackagerImpl brew
    final ChocolateyPackagerImpl chocolatey
    final ScoopPackagerImpl scoop
    final SnapPackagerImpl snap

    @Inject
    PackagersImpl(ObjectFactory objects) {
        brew = objects.newInstance(BrewPackagerImpl, objects)
        chocolatey = objects.newInstance(ChocolateyPackagerImpl, objects)
        scoop = objects.newInstance(ScoopPackagerImpl, objects)
        snap = objects.newInstance(SnapPackagerImpl, objects)
    }

    @Override
    void brew(Action<? super BrewPackager> action) {
        action.execute(brew)
    }

    @Override
    void chocolatey(Action<? super ChocolateyPackager> action) {
        action.execute(chocolatey)
    }

    @Override
    void scoop(Action<? super ScoopPackager> action) {
        action.execute(scoop)
    }

    @Override
    void snap(Action<? super SnapPackager> action) {
        action.execute(snap)
    }

    org.kordamp.jreleaser.model.Packagers toModel() {
        org.kordamp.jreleaser.model.Packagers packagers = new org.kordamp.jreleaser.model.Packagers()
        if (brew.set) packagers.brew = brew.toModel()
        if (chocolatey.set) packagers.chocolatey = chocolatey.toModel()
        if (scoop.set) packagers.scoop = scoop.toModel()
        if (snap.set) packagers.snap = snap.toModel()
        packagers
    }
}