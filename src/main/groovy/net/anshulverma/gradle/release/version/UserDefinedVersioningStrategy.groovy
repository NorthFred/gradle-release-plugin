/**
 * Copyright 2015 Anshul Verma. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.anshulverma.gradle.release.version

import net.anshulverma.gradle.release.repository.ProjectRepository
import net.anshulverma.gradle.release.repository.ProjectRepositoryWrapper
import org.gradle.api.Project

/**
 * @author Anshul Verma (anshul.verma86@gmail.com)
 */
class UserDefinedVersioningStrategy extends AbstractVersioningStrategy {

  ProjectRepository repository
  Closure currentVersionClosure

  UserDefinedVersioningStrategy(ProjectRepository repository, Closure currentVersionClosure) {
    this.repository = repository
    this.currentVersionClosure = currentVersionClosure
  }

  @Override
  SemanticVersion currentVersion(Project project) {
    currentVersionClosure.delegate = project
    def (major, minor, patch, suffix) = currentVersionClosure(new ProjectRepositoryWrapper(project, repository))
    try {
      new SemanticVersion(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch), suffix)
    } catch (NumberFormatException e) {
      throw new IllegalStateException('Invalid version format returned by versioning function. ' +
                                          'Your user defined version function should return an array of ' +
                                          'size 4 which should have numerical values in the first ' +
                                          'three positions.', e)
    }
  }
}
