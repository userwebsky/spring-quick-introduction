package pl.robertprogramista.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Aspect logging the request for audit purposes
 */
@Aspect
@Component
public class AuditLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    private HttpServletRequest request;

    /**
     * The constructor into which HttpServletRequest is injected
     * @param request current request
     */
    public AuditLogAspect(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Log request data for audit logs
     * @param joinPoint request processing intersection point
     * @return the result of proceeding
     * @throws Throwable exception
     */
    @Around("@annotation(AuditLog)")
    public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        boolean notOnlyArgs = false;

        if (method.isAnnotationPresent(AuditLog.class)) {
            AuditLog auditLog = method.getAnnotation(AuditLog.class);
            notOnlyArgs =  auditLog.notOnlyArgs();
        }

        logger.info("Args: {}", joinPoint.getArgs());

        if(notOnlyArgs) {
            logger.info("More info: IP:{}, URI:{}, Method:{}",
                    request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
        }

        return joinPoint.proceed();
    }
}
