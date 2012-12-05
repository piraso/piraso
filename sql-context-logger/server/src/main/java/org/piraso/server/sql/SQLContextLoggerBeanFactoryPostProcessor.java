package org.piraso.server.sql;

import org.piraso.server.AbstractContextLoggerBeanProcessor;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

public class SQLContextLoggerBeanFactoryPostProcessor extends AbstractContextLoggerBeanProcessor<FactoryBean> {
    public SQLContextLoggerBeanFactoryPostProcessor() {
        super(FactoryBean.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isSupported(Object o) {
        return super.isSupported(o) && DataSource.class.isAssignableFrom(((FactoryBean) o).getObjectType());
    }

    @Override
    public FactoryBean createProxy(FactoryBean o, String id) {
        return SQLContextLogger.createFactory(o, id);
    }
}
