package ard.piraso.replacer.spring.remoting;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.JSONEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.api.entry.ThrowableEntry;
import ard.piraso.api.spring.SpringPreferenceEnum;
import ard.piraso.server.ContextPreference;
import ard.piraso.server.GeneralPreferenceEvaluator;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoEntryPointContext;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Piraso aware HttpInvokerProxyFactoryBean implementation.
 */
public class PirasoHttpInvokerProxyFactoryBean extends HttpInvokerProxyFactoryBean {
    private static final Log LOG = LogFactory.getLog(PirasoSimpleHttpInvokerRequestExecutor.class);

    protected ContextPreference context = new PirasoEntryPointContext();

    protected GeneralPreferenceEvaluator pref = new GeneralPreferenceEvaluator();

    @Override
    protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, MethodInvocation originalInvocation) throws Exception {
        ElapseTimeEntry elapseTime = null;

        if(context.isMonitored()) {
            try {
                if(pref.isEnabled(SpringPreferenceEnum.REMOTING_ELAPSE_TIME_ENABLED)) {
                    elapseTime = new ElapseTimeEntry();
                    elapseTime.start();
                }

                if(pref.isEnabled(SpringPreferenceEnum.REMOTING_METHOD_CALL_ENABLED)) {
                    String declaringClass = originalInvocation.getMethod().getDeclaringClass().getName();

                    HttpRemoteInvocation remoteInvocation = new HttpRemoteInvocation(invocation);
                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    JSONEntry entry = new JSONEntry(originalInvocation.getMethod().toGenericString(), remoteInvocation);

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }
            } catch(Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }

        RemoteInvocationResult result = super.executeRequest(invocation, originalInvocation);

        if(context.isMonitored()) {
            try {
                String declaringClass = originalInvocation.getMethod().getDeclaringClass().getName();
                String methodName = originalInvocation.getMethod().getName();

                if(elapseTime != null) {
                    elapseTime.stop();

                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    MessageEntry entry = new MessageEntry("Elapse Time", elapseTime);

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }

                if(context.isMonitored() && pref.isEnabled(SpringPreferenceEnum.REMOTING_METHOD_CALL_ENABLED)) {
                    HttpRemoteInvocationResult remoteInvocationResult = new HttpRemoteInvocationResult(result);
                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    JSONEntry entry = new JSONEntry("Returned for " + methodName, remoteInvocationResult);

                    if(result.getException() != null) {
                        entry.setThrown(new ThrowableEntry(result.getException()));
                    }

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }
            } catch(Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }

        return result;
    }
}
