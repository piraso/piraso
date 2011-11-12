package ard.piraso.server;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Piraso aware {@link XmlWebApplicationContext} class.
 * This supports cascading of {@link ContextLoggerBeanProcessor} to child contexts.
 */
public class PirasoWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory factory = super.createBeanFactory();

        if(getParent() != null) {
            String[] postProcessorNames = getParent().getBeanNamesForType(ContextLoggerBeanProcessor.class, true, false);

            for (String ppName : postProcessorNames) {
                BeanPostProcessor pp = getParent().getBean(ppName, BeanPostProcessor.class);
                factory.addBeanPostProcessor(pp);
            }
        }

        return factory;
    }
}
