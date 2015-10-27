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
package net.anshulverma.gradle.release.tasks

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author Anshul Verma (anshul.verma86@gmail.com)
 */
@TypeChecked
@Slf4j
class TaskRegistry {

  final static TaskRegistry INSTANCE = new TaskRegistry()

  final Map<TaskType, TaskContext> taskMap = [:]

  def register(AbstractReleaseTask task,
               TaskType taskType,
               TaskType parent,
               TaskType[] dependencies,
               TaskType[] dependents) {

    def taskContext = TaskContext.builder()
                                 .task(task)
                                 .parent(parent)
                                 .type(taskType)
                                 .dependencies(dependencies)
                                 .dependents(dependents)
                                 .build()
    if (taskMap[taskType]) {
      throw new IllegalArgumentException(
          "task of type $taskType has already been registered with context ${taskMap[taskType]}")
    }
    taskMap << [(taskType): taskContext]
  }

  def reset() {
    taskMap.clear()
    log.info('task registry cleared')
  }

  def resolveDependencies(Project project) {
    taskMap.each { TaskType taskType, TaskContext taskContext ->
      if (taskContext.dependencies) {
        taskContext.dependencies.each { TaskType dependency ->
          resolveDependency(project, taskContext, dependency)
        }
      }
      taskContext.dependents.each { TaskType dependent ->
        resolveDependent(project, taskContext, dependent)
      }
    }
  }

  private void resolveDependency(Project project, TaskContext taskContext, TaskType dependencyType) {
    taskContext.task.dependsOn(getDependencies(project, dependencyType))
  }

  private void resolveDependent(Project project, TaskContext taskContext, TaskType dependentType) {
    project.getTasksByName(dependentType.taskName, true).each { task ->
      if (taskContext.parent == TaskType.NULL || isInvoked(project, taskContext.parent)) {
        task.dependsOn(taskContext.task)
      }
    }
  }

  private boolean isInvoked(Project project, TaskType taskType) {
    project.gradle.startParameter.taskNames.contains(taskType.taskName)
  }

  private Collection<Task> getDependencies(Project project, TaskType taskType) {
    if (taskMap[taskType]) {
      return [taskMap[taskType].task]
    }
    def dependencies = project.getTasksByName(taskType.taskName, true)
    if (dependencies.empty) {
      throw new IllegalStateException("unable to find tasks named '$taskType.taskName' in '$project.name'")
    }
    return dependencies
  }
}
