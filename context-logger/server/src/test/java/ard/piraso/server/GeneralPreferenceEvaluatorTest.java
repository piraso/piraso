package ard.piraso.server;

import ard.piraso.api.GeneralPreferenceEnum;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Test for {@link GeneralPreferenceEvaluator} class.
 */
public class GeneralPreferenceEvaluatorTest {

    private PirasoContext context;

    private GeneralPreferenceEvaluator evaluator;

    @Before
    public void setUp() throws Exception {
        context = mock(PirasoContext.class);
        PirasoContextHolder.setContext(context);

        evaluator = new GeneralPreferenceEvaluator();
    }

    @Test
    public void testIsStackTraceEnabled() throws Exception {
        assertFalse(evaluator.isStackTraceEnabled());

        verify(context, times(1)).isEnabled(GeneralPreferenceEnum.STACK_TRACE_ENABLED.getPropertyName());
    }

    @Test
    public void testIsLoggingScopedEnabled() throws Exception {
        assertFalse(evaluator.isLoggingScopedEnabled());

        verify(context, times(1)).isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName());
    }

    @Test
    public void testRequestOnScope() throws Exception {
        evaluator.requestOnScope();

        verify(context, times(1)).requestOnScope();
    }
}
