/*
 * Copyright © 2021 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package io.cdap.wrangler.proto.workspace.v2;

import io.cdap.wrangler.api.Row;

import java.util.List;
import java.util.Objects;

/**
 * Full information about the workspace, including sample data
 */
public class WorkspaceDetail {
  private final Workspace workspace;
  private final List<Row> sample;

  public WorkspaceDetail(Workspace workspace, List<Row> sample) {
    this.workspace = workspace;
    this.sample = sample;
  }

  public Workspace getWorkspace() {
    return workspace;
  }

  public List<Row> getSample() {
    return sample;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WorkspaceDetail detail = (WorkspaceDetail) o;
    return Objects.equals(workspace, detail.workspace) &&
             Objects.equals(sample, detail.sample);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workspace, sample);
  }
}
