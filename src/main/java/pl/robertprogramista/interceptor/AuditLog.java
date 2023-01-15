package pl.robertprogramista.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Audit log class
 */
@Component
public class AuditLog implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuditLog.class);

    /**
     * Pre Handle
     * @param request current request
     * @param response current response
     * @param handler handler
     * @return boolean
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UUID uuid = UUID.randomUUID();
        response.setHeader("uuid", uuid.toString());
        logger.info(getDateTime() + " Start..." + response.getHeaders("uuid"));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * After completion
     * @param request current request
     * @param response current response
     * @param handler handler
     * @param ex  exception
     * @throws Exception  exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info(getDateTime() + " End..." + response.getHeaders("uuid"));
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * Returns a current datetime as a string
     * @return datetime as a string
     */
    private String getDateTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    }
}
