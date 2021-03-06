package io.jenkins.plugins.casc.impl.attributes;

import hudson.util.PersistedList;
import io.jenkins.plugins.casc.Attribute;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.Beta;

import java.util.Collection;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
@Restricted(Beta.class)
public class PersistedListAttribute<Owner, Type> extends Attribute<Owner, Collection<Type>> {

    public PersistedListAttribute(String name, Class<Type> type) {
        super(name, type);
        multiple(true);
    }

    @Override
    public void setValue(Owner target, Collection<Type> o) throws Exception {
        getValue(target).replaceBy(o);
    }

    @Override
    public PersistedList getValue(Owner o) throws Exception {
        return (PersistedList) super.getValue(o);
    }

}
