package io.jenkins.plugins.casc.support.gerrittrigger;

import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config;
import io.jenkins.plugins.casc.*;
import io.jenkins.plugins.casc.model.CNode;
import io.jenkins.plugins.casc.model.Mapping;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class GerritConfigCofigurator extends BaseConfigurator<Config> {
    /**
     * Build or identify the target component this configurator has to handle based on the provided configuration node.
     *
     * @param mapping configuration for target component. Implementation may consume some entries to create a fresh new instance.
     * @param context
     * @return instance to be configured, but not yet fully configured, see {@link #configure(Mapping, Object, boolean, ConfigurationContext)}
     * @throws ConfiguratorException
     */
    @Override
    protected Config instance(Mapping mapping, ConfigurationContext context) throws ConfiguratorException {
        return new Config();
    }

    /**
     * Target type this configurator can handle.
     */
    @Override
    public Class<Config> getTarget() {
        return Config.class;
    }

    /**
     * @param clazz
     * @return <code>true</code> if this configurator can handle type <code>clazz</code>. Implementation has to be
     * consistent with {@link #getTarget()}
     */
    @Override
    public boolean canConfigure(Class clazz) {
        if(clazz == Config.class) return true;
        return false;
    }

    /**
     * Describe a component as a Configuration Nodes {@link CNode} to be exported as yaml.
     * Only export attributes which are <b>not</b> set to default value.
     *
     * @param instance
     * @param context
     */
    @CheckForNull
    @Override
    public CNode describe(Config instance, ConfigurationContext context) throws Exception {
        return null;
    }

    /**
     * Run configuration process on the target instance
     *
     * @param config   configuration to apply. Can be partial if {@link #instance(Mapping, ConfigurationContext)} did already used some entries
     * @param instance target instance to configure
     * @param dryrun   only check configuration is valid regarding target component. Don't actually apply changes to jenkins master instance
     * @param context
     * @throws ConfiguratorException something went wrong...
     */
    @Override
    protected void configure(Mapping config, Config instance, boolean dryrun, ConfigurationContext context) throws ConfiguratorException {
        instance.setGerritAuthKeyFile(new File(config.getScalarValue("gerritAuthKeyFile")));
        instance.setGerritUserName(config.getScalarValue("gerritUserName"));
    }

    @Nonnull
    @Override
    public Config configure(CNode c, ConfigurationContext context) throws ConfiguratorException {
        Mapping map = c.asMapping();
        Config conf = this.instance(map,context);
        configure(map,conf,false,context);
        return conf;
    }
}
