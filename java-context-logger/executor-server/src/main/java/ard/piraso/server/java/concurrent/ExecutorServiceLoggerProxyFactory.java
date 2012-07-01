package ard.piraso.server.java.concurrent;

import ard.piraso.api.entry.RequestEntry;
import ard.piraso.api.entry.ResponseEntry;
import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.GeneralPreferenceEvaluator;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import ard.piraso.server.logger.AbstractLoggerProxyFactory;

import java.util.concurrent.ExecutorService;

/**
 * Proxy factory for {@link ExecutorService}
 */
public class ExecutorServiceLoggerProxyFactory extends AbstractLoggerProxyFactory<ExecutorService, GeneralPreferenceEvaluator> {

    public ExecutorServiceLoggerProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<ExecutorService>(ExecutorService.class), new GeneralPreferenceEvaluator());

        factory.addMethodListener("submit|execute", new SubmitHandler());
    }

    private class SubmitHandler extends RegexMethodInterceptorAdapter<ExecutorService> {

        @Override
        public void beforeCall(RegexMethodInterceptorEvent<ExecutorService> evt) {
            Object[] args = evt.getInvocation().getArguments();

            if(args.length > 0) {
                ExecutionEntryPoint entryPoint = new ExecutionEntryPoint(id.getGroupIds().getFirst());
                RequestEntry request = new RequestEntry(entryPoint.getPath());
                ResponseEntry response = new ResponseEntry();

                request.setServerName(entryPoint.getRemoteAddr());

                PirasoContext context = PirasoContextHolder.getContext();

                if(context != null) {
                    context = context.createChildContext(id);
                } else {
                    context = new PirasoContext(entryPoint);
                }

                Object[] replacement = new Object[args.length];

                for(int i = 0; i < args.length; i++) {
                    if(Runnable.class.isInstance(args[i])) {
                        replacement[i] = new PirasoContextRunnableWrapper((Runnable) args[i], context, request, response);
                    } else {
                        replacement[i] = args[i];
                    }
                }

                evt.setReplacedArguments(replacement);
            }
        }
    }
}
