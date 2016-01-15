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

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Anshul Verma (anshul.verma86@gmail.com)
 */
class VersionTemplateFile {

  private static final RELEASE_TEMPLATE_EXT = '.release-template'

  def inputFile
  def outputFile
  def lines = []

  VersionTemplateFile(String file, lineConfigs) {
    initializeInputAndOutputFile(file)

    lines = lineConfigs.collect { new VersionTemplateLine(it.key, it.value) }
    Collections.sort(lines)
  }

  private void initializeInputAndOutputFile(String file) {
    if (file.endsWith(RELEASE_TEMPLATE_EXT)) {
      inputFile = file
      outputFile = file[0..-(RELEASE_TEMPLATE_EXT.length() + 1)]
    } else if (Files.exists(Paths.get("${file}${RELEASE_TEMPLATE_EXT}"))) {
      inputFile = "${file}${RELEASE_TEMPLATE_EXT}"
      outputFile = file
    } else {
      inputFile = file
      outputFile = file
    }
  }
}