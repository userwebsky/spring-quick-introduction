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

@Aspect
@Component
public class AuditLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    private HttpServletRequest request;

    public AuditLogAspect(final HttpServletRequest request) {
        this.request = request;
    }

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
