package uk.gov.justice.services.core.accesscontrol;

import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Optional;

import jakarta.inject.Inject;

/**
 * Interceptor to apply access control to local framework components.
 */
public class LocalAccessControlInterceptor implements Interceptor {

    @Inject
    private AccessControlService accessControlService;

    @Inject
    private AccessControlFailureMessageGenerator accessControlFailureMessageGenerator;

    @Override
    public InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain) {

        checkAccessControl(interceptorContext.getComponentName(), interceptorContext.inputEnvelope());

        return interceptorChain.processNext(interceptorContext);
    }

    private void checkAccessControl(final String component, final JsonEnvelope jsonEnvelope) {

        final Optional<AccessControlViolation> accessControlViolation = accessControlService.checkAccessControl(component, jsonEnvelope);

        if (accessControlViolation.isPresent()) {
            final String errorMessage = accessControlFailureMessageGenerator.errorMessageFrom(
                    jsonEnvelope,
                    accessControlViolation.get());

            throw new AccessControlViolationException(errorMessage);
        }
    }
}
