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
package net.anshulverma.gradle.release.info

import groovy.transform.TypeChecked

/**
 * @author Anshul Verma (anshul.verma86@gmail.com)
 */
@TypeChecked
enum PropertyName {

  SKIP_ALL_CHECKS('skipAllChecks'),
  SKIP_BRANCH_CHECK('skipBranchCheck'),
  SKIP_CLEAN_WORKSPACE_CHECK('skipCleanWorkspaceCheck'),
  SKIP_TEMPLATE_VALIDATION('skipTemplateValidation'),
  BINTRAY_USER('bintrayUser'),
  BINTRAY_KEY('bintrayKey'),
  RELEASE_TYPE('releaseType'),
  RELEASE_SETTINGS('releaseExt')

  private final name

  PropertyName(String name) {
    this.name = name
  }

  String getName() {
    return name
  }
}
