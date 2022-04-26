package com.cloudbees.workflow.rest.external;

import java.util.List;

public class BuildResponse {
  private int count;
  private List<BuildExt> builds;

  public BuildResponse(int count, List<BuildExt> builds) {
    this.count = count;
    this.builds = builds;
  }

  public int getCount() {
    return count;
  }

  public List<BuildExt> getBuilds() {
    return builds;
  }
}