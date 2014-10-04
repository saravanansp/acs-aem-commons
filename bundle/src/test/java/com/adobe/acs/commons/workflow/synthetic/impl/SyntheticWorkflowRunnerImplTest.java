/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.adobe.acs.commons.workflow.synthetic.impl;

import com.adobe.acs.commons.workflow.synthetic.impl.testprocesses.ReadDataWorkflowProcess;
import com.adobe.acs.commons.workflow.synthetic.impl.testprocesses.SetDataWorkflowProcess;
import com.adobe.acs.commons.workflow.synthetic.impl.testprocesses.WFArgsWorkflowProcess;
import com.adobe.acs.commons.workflow.synthetic.impl.testprocesses.WFDataWorkflowProcess;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SyntheticWorkflowRunnerImplTest {

    @Mock
    ResourceResolver resourceResolver;

    @Mock
    Session session;

    SyntheticWorkflowRunnerImpl swr = new SyntheticWorkflowRunnerImpl();

    @Before
    public void setUp() {
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
    }

    @Test
    public void testStart_WFData() throws Exception {
        Map<Object, Object> map = new HashMap<Object, Object>();

        map.put("process.label", "test");
        swr.bindWorkflowProcesses(new WFDataWorkflowProcess(), map);

        Map<String, Map<String, Object>> metadata = new HashMap<String, Map<String, Object>>();

        swr.start(resourceResolver,
                "/content/test",
                new String[] {"test"},
                metadata, false, false);
    }

    @Test
    public void testStart_PassingDataBetweenProcesses() throws Exception {
        Map<Object, Object> map = new HashMap<Object, Object>();

        map.put("process.label", "set");
        swr.bindWorkflowProcesses(new SetDataWorkflowProcess(), map);

        map.put("process.label", "read");
        swr.bindWorkflowProcesses(new ReadDataWorkflowProcess(), map);


        Map<String, Map<String, Object>> metadata = new HashMap<String, Map<String, Object>>();

        swr.start(resourceResolver,
                "/content/test",
                new String[] {"set", "read"},
                metadata, false, false);
    }


    @Test
    public void testStart_ProcessArgs() throws Exception {
        Map<Object, Object> map = new HashMap<Object, Object>();

        map.put("process.label", "wf-args");
        swr.bindWorkflowProcesses(new WFArgsWorkflowProcess(), map);

        /** WF Process Metadata */

        Map<String, Map<String, Object>> metadata = new HashMap<String, Map<String, Object>>();

        Map<String, Object> wfArgs = new HashMap<String, Object>();
        wfArgs.put("hello", "world");

        metadata.put("wf-args", wfArgs);

        swr.start(resourceResolver,
                "/content/test",
                new String[]{ "wf-args" },
                metadata, false, false);
    }
}