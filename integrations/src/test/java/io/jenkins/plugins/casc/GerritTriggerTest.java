package io.jenkins.plugins.casc;

import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GerritTriggerTest {
    @Rule
    public JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("GerritTriggerTest.yml")
    public void check_if_config_loaded() {

        final PluginImpl gerrit_plugin = PluginImpl.getInstance();
        Assert.assertNotNull(gerrit_plugin);
        Assert.assertNotNull(gerrit_plugin.getFirstServer());
    }
}
