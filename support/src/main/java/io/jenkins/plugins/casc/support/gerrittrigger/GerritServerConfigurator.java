package io.jenkins.plugins.casc.support.gerrittrigger;


import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config;
import hudson.Extension;
import io.jenkins.plugins.casc.*;
import io.jenkins.plugins.casc.impl.attributes.MultivaluedAttribute;
import io.jenkins.plugins.casc.model.CNode;
import io.jenkins.plugins.casc.model.Mapping;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@Extension
@Restricted(NoExternalUse.class)
public class GerritServerConfigurator extends BaseConfigurator<GerritServer> {

    /**
     * Target type this configurator can handle.
     */
    @Override
    public Class<GerritServer> getTarget() {
        return GerritServer.class;
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
    public CNode describe(GerritServer instance, ConfigurationContext context) throws Exception {
        return null;
    }

    @Override
    public Set<Attribute<GerritServer, ?>> describe() {
        Set<Attribute<GerritServer, ?>> attr =  new HashSet<>();
        attr.add(new MultivaluedAttribute<GerritServer, Config>("config", Config.class));
        return attr;
    }

    /**
     * Build or identify the target component this configurator has to handle based on the provided configuration node.
     *
     * @param mapping configuration for target component. Implementation may consume some entries to create a fresh new instance.
     * @param context
     * @return instance to be configured, but not yet fully configured, see {@link #configure(Mapping, Object, boolean, ConfigurationContext)}
     * @throws ConfiguratorException
     */
    @Override
    protected GerritServer instance(Mapping mapping, ConfigurationContext context) throws ConfiguratorException {
        return new GerritServer(mapping.getScalarValue("name"));
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
    protected void configure(Mapping config, GerritServer instance, boolean dryrun, ConfigurationContext context) throws ConfiguratorException {
        final CNode configNode = config.get("config");
        Configurator<Config> cf = context.lookup(Config.class);
        Config srvConf = cf.configure(configNode, context);
        instance.setConfig(srvConf);
    }

    @Override
    public GerritServer check(CNode c, ConfigurationContext context) throws ConfiguratorException {
        final CNode cn = c.asMapping().get("config");
        Configurator<Config> cf = context.lookup(Config.class);
        Config srvConf = cf.configure(cn, context);
        GerritServer gs = new GerritServer(c.asMapping().getScalarValue("name"));
        gs.setConfig(srvConf);
        return gs;
    }
}
