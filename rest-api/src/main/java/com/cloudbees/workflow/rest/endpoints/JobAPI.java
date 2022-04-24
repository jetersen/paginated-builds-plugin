/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.workflow.rest.endpoints;

import com.cloudbees.workflow.rest.AbstractAPIActionHandler;
import com.cloudbees.workflow.rest.external.JobExt;
import com.cloudbees.workflow.rest.external.BuildExt;
import com.cloudbees.workflow.util.ModelUtil;
import com.cloudbees.workflow.util.ServeJson;
import hudson.Extension;
import hudson.model.Job;
import hudson.model.Run;

import org.kohsuke.stapler.QueryParameter;
import hudson.model.Fingerprint.RangeSet;
import hudson.model.Fingerprint.Range;

import java.util.List;
import java.util.ArrayList;

/**
 * API Action handler to return Job info.
 * <p>
 * Bound to {@code ${{rootURL}/job/<jobname>/wfapi/*}}
 * </p>
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@Extension
public class JobAPI extends AbstractAPIActionHandler {

    public static String getUrl(Job job) {
        return ModelUtil.getFullItemUrl(job.getUrl()) + URL_BASE + "/";
    }

    private static final int DEFAULT_PAGE_SIZE = 100;

    public static String getBuildsUrl(Job job) {
        return getUrl(job) + "builds";
    }

    /**
     * Get all Workflow Job runs/builds since the specified run/build name.
     * @param since The run/build name at which to stop returning (inclusive),
     *              or null/empty if all runs/builds are to be returned.
     * @param fullStages Return the stageNodes within each stage
     * @return The runs list.
     */
    @ServeJson
    public List<BuildExt> doBuilds(@QueryParameter int start, @QueryParameter int size) {
        ArrayList<BuildExt> builds = new ArrayList<>();
        size = size == 0 ? DEFAULT_PAGE_SIZE : size;
        List<Run> rawBuilds = getJob().getBuilds(RangeSet.fromString(start + "-" + (size + start - 1), false));
        for (Run build : rawBuilds) {
            builds.add(BuildExt.create(build));
        }

        return builds;
    }
}
