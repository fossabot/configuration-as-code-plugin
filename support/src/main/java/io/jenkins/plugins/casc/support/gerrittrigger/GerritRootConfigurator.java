package io.jenkins.plugins.casc.support.gerrittrigger;

import com.cloudbees.plugins.credentials.GlobalCredentialsConfiguration;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import hudson.Extension;
import hudson.PluginManager;
import hudson.ProxyConfiguration;
import hudson.model.UpdateCenter;
import hudson.model.UpdateSite;
import io.jenkins.plugins.casc.*;
import io.jenkins.plugins.casc.impl.attributes.MultivaluedAttribute;
import io.jenkins.plugins.casc.model.CNode;
import io.jenkins.plugins.casc.model.Mapping;


import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config;

import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import io.jenkins.plugins.casc.model.Sequence;
import io.jenkins.plugins.casc.plugins.Plugins;
import jenkins.model.Jenkins;
import jenkins.security.s2m.AdminWhitelistRule;
import org.antlr.v4.runtime.misc.NotNull;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


@Extension(optional = true)
@Restricted(NoExternalUse.class)
public class GerritRootConfigurator extends BaseConfigurator<PluginImpl> implements RootElementConfigurator<PluginImpl>  {


    /**
     * Build or identify the target component this configurator has to handle based on the provided configuration node.
     *
     * @param mapping configuration for target component. Implementation may consume some entries to create a fresh new instance.
     * @param context
     * @return instance to be configured, but not yet fully configured, see {@link #configure(Mapping, Object, boolean, ConfigurationContext)}
     * @throws ConfiguratorException
     */
    @Override
    protected PluginImpl instance(Mapping mapping, ConfigurationContext context) throws ConfiguratorException {
        return getTargetComponent(context);
    }

    /**
     * Retrieve the target component managed by this RootElementConfigurator
     *
     * @param context
     * @return
     */
    @Override
    public PluginImpl getTargetComponent(ConfigurationContext context) {
        return PluginImpl.getInstance();
    }

    /**
     * Get a configurator name.
     *
     * @return short name for this component when used in a configuration.yaml file
     */
    @Nonnull
    @Override
    public String getName() {
        return "gerrit";
    }

    /**
     * Target type this configurator can handle.
     */
    @Override
    public Class<PluginImpl> getTarget() {
        return PluginImpl.class;
    }

    @Override
    public Set<Attribute<PluginImpl, ?>> describe() {
        Set<Attribute<PluginImpl, ?>> attr =  new HashSet<>();
        attr.add(new MultivaluedAttribute<PluginImpl, GerritServer>("servers", GerritServer.class));
        return attr;
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
    public CNode describe(PluginImpl instance, ConfigurationContext context) throws Exception {
        return null;
    }

    @Nonnull
    @Override
    public PluginImpl configure(CNode c, ConfigurationContext context) throws ConfiguratorException {
        PluginImpl plugin = getTargetComponent(context);
        Mapping map = c.asMapping();
        final CNode serversNode = map.get("servers");
        List<GerritServer> gerritServerList = new ArrayList<GerritServer>();
        for(CNode srv : serversNode.asSequence()){
            Configurator<GerritServer> sc = context.lookup(GerritServer.class);
            GerritServer gerritServer = sc.configure(srv,context);
            gerritServerList.add(gerritServer);
        }
        plugin.setServers(gerritServerList);
        return plugin;

    }

}