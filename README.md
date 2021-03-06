# gradle-release-plugin

[![Build Status](https://travis-ci.org/anshulverma/gradle-release-plugin.svg?branch=master)](https://travis-ci.org/anshulverma/gradle-release-plugin)
[![Coverage Status](https://coveralls.io/repos/anshulverma/gradle-release-plugin/badge.svg?branch=master&service=github)](https://coveralls.io/github/anshulverma/gradle-release-plugin?branch=master)
[![Download](https://api.bintray.com/packages/anshulverma/gradle-plugins/gradle-release-plugin/images/download.svg)](https://bintray.com/anshulverma/gradle-plugins/gradle-release-plugin/_latestVersion)

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc/generate-toc again -->
**Table of Contents**

- [gradle-release-plugin](#gradle-release-plugin)
    - [Usage](#usage)
    - [Tasks](#tasks)
        - [Snapshot task](#snapshot-task)
        - [Release task](#release-task)
    - [Configuration](#configuration)
        - [Override versioning strategy](#override-versioning-strategy)
            - [`repository` argument](#repository-argument)
        - [Update version info in other files](#update-version-info-in-other-files)
    - [Options](#options)
    - [Multimodule projects](#multimodule-projects)
    - [Building](#building)
    - [Contributing](#contributing)
    - [Author](#author)
    - [License](#license)

<!-- markdown-toc end -->

A simplistic approach to a project release cycle following the most common practice.

This plugin can be used to build artifacts for `java` and `groovy` based projects to `bintray` or
any other repository where you would like to host your contents.

The `gradle-release-plugin` uses itself for releasing itself. You are welcome to take a look at the
build scripts for a working example.

## Usage

You need to add a `buildScript` dependency to enable this plugin:

``` groovy
buildscript {
  repositories {
    jcenter() // also available in mavenCentral()
  }

  dependencies {
    classpath 'net.anshulverma.gradle:gradle-release-plugin:0.5.7'
  }
}
```

Then apply the plugin:

``` groovy
apply plugin: 'net.anshulverma.gradle.release'
```

## Tasks

This plugin adds several tasks to your build. Two of these are most relevant:

### Snapshot task

Executed using `./gradlew snapshot`, this task will publish the build artifact to a remote OSS
repository.

The `snapshot` task performs several checks before publishing any content:

- `checkCleanWorkspace` - This task checks if the workspace is clean or not. In other words it makes
sure that there are no uncommitted changes in your repository.

- `checkRepositoryBranch` - This task checks if the current branch is in sync with the remote
repository. This is required so that you can make sure you don't publish any change by mistake that
does not exist in your codebase.

- `check` - We also need to make sure that the current build passes before publishing any artifacts.

- `showPublishInfo` - This is more of a informational task that prints the version information
before publishing any artifacts. You can execute it directly as well to check what the next version
will be (details on how to control versioning can be found in the options section below).

Once all of these checks are completed, then the `publish` task is executed which uploads the
artifacts to any pre-configured repositories.

### Release task

Once a release artifact is ready to be pushed, execute `./gradlew release` to publish that artifact
to any configured repositories and to `bintray`.

Just as in the `snapshot` task, this task also depends on `checkCleanWorkspace`,
`checkRepositoryBranch`, `check`, `showPublishInfo` and `publish` tasks. In addition to these tasks
`release` task also depends on `bintrayUpload` which uploads the build artifacts to `bintray` and
(optionally) `jcenter`.

## Configuration

Since, this plugin configures versioning of your projects the configuration should be applied to the
project before this plugin is applied. To do this, declare a `releaseExt` property like this:

``` groovy
ext {
  releaseExt = {
    // ... configuration settings for release plugin ... //
  }
}

apply plugin: 'net.anshulverma.gradle.release'
```

### Override versioning strategy

As per the current implementation, only git based repositories are supported and semantic version is
parsed from the latest git tag. If this is not acceptable in your case, consider overriding
versioning strategy:

``` groovy
ext {
  releaseExt = {
    versioning = { repository ->
      // this should return an array of size 4 where first three are numbers
      repository.tag.split(/\./)
    }
  }
}
```

#### `repository` argument

The repository argument in the closure above has several methods:

|method name|return type|description|
|---|:-:|:-:|
|`currentBranch`|string|name of the current repository branch|
|`synced`|boolean|`true` if the repository is synced to remote|
|`status`|string|status of repository|
|`tag`|string|latest repository tag|
|`upstream`|string|name of the upstream for current branch|

### Update version info in other files

In some cases you want to tie together several files with the same
version tag as the root project. This can be achieved by defining
`versionedFiles` like this:

``` groovy
ext {
  releaseExt = {
    versionedFiles << [
      'src/main/resources/sample.properties': [
          // line number -> template text for that line
          3: 'version=$currentVersionWithSuffix'
      ],
      'INSTALL.txt': [
          9 : 'Install sample version $currentVersion by $author',
          37: 'Download file: "http://example.com/file-$currentVersion-$releaseType.tar.gz'
      ],
      // this file is itself a template that get evaluated by the release plugin
      'README.txt.release-template': []
    ]
  }
}
```

The task which updates the files above runs before every `snapshot` or
`release` task.

By default this task will look for `<file-name>.release-template`
files. If it does not find one then it defaults to using the one you
specified.

## Options

> boolean option types can be derived from string values like "true" and "false"

|name|type|description|default value|
|---|:-:|:-:|--:|
|`skipCleanWorkspaceCheck`|boolean|Skip the `checkCleanWorkspace` task to disable checking if there are no uncommited changes|false|
|`skipBranchCheck`|boolean|Skip the `checkRepositoryBranch` task to disable checking if the branch is in sync with remote|false|
|`skipAllChecks`|boolean|Skip both of the above checks|false|
|`releaseType`|string|Possible values: `"major"`, `"minor"`, `"patch"`. Specify the type of release based on the commits that have gone in since last release|`"patch"`|
|`bintrayUser`|string|The user name to use for `bintray` authentication|`null`|
|`bintrayKey`|string|The key id for `bintray` authentication|`null`|

## Multimodule projects

Multimodule projects are also supported. You don't have to do anything special to configure it.

You can apply the plugin just to the root project if you like. In this case tasks for all sub
projects will automatically be configured by the plugin itself.

## Building

You are welcome to suggest changes or work on them yourself and issue a pull request. To make sure
your changes pass all requirements, please run the `check` task:

``` bash
$ ./gradlew check
```

## Contributing

1. Fork it ( https://github.com/anshulverma/gradle-release-plugin/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request

## Author

[Anshul Verma](http://anshulverma.net/) ::
[anshulverma](https://github.com/anshulverma) ::
[@anshulverma](http://twitter.com/anshulverma)

## License

Copyright (c) 2014 Anshul Verma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
