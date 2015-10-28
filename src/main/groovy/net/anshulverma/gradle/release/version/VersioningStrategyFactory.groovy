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

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import net.anshulverma.gradle.release.repository.GitProjectRepository
import net.anshulverma.gradle.release.repository.ProjectRepository
import org.gradle.api.Project

/**
 * @author Anshul Verma (anshul.verma86@gmail.com)
 */
@TypeChecked
@Slf4j
class VersioningStrategyFactory {

  static VersioningStrategy get(Project project) {
    // only git based repository implemented at the moment
    get(project, new GitProjectRepository())
  }

  static VersioningStrategy get(Project project, ProjectRepository repository) {
    log.info("building git based versioning strategy for $project.name")
    new GitTagVersioningStrategy(repository)
  }
}
