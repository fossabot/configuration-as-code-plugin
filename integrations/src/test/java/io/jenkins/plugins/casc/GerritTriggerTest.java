package io.jenkins.plugins.casc;

import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.amazonaws.services.sagemaker.model.TrainingInputMode.File;
import static org.hamcrest.CoreMatchers.equalTo;

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

    @Test
    @ConfiguredWithCode("GerritTriggerTest.yml")
    public void check_if_first_server_configured()
    {
        final PluginImpl gerrit_plugin = PluginImpl.getInstance();
        Assert.assertNotNull(gerrit_plugin);

        Assert.assertEquals("More than 1 server configured!",
                gerrit_plugin.getServers().size(),1);

        GerritServer server = gerrit_plugin.getFirstServer();
        Assert.assertNotNull(server);

        Config server_config = (Config) server.getConfig();
        Assert.assertNotNull(server_config);

        Assert.assertThat(server.getName(),equalTo("gerrit-test-server"));

        Assert.assertThat(server_config.getGerritUserName(),equalTo("admin"));
        Assert.assertThat(
                server_config.getGerritAuthKeyFile().toString(),
                equalTo("/var/jenkins_home/gerrit_key.pem/gerritAuthKeyFile")
        );
        
    }
}
