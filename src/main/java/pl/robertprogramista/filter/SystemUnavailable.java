package pl.robertprogramista.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Blocks requests to the system. The system will be unavailable
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class SystemUnavailable implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SystemUnavailable.class);

    @Value("${isSystemAvailable}")
    private Boolean isSystemAvailable;

    /**
     * Blocks requests to the system.
     * @param request current request
     * @param response current response
     * @param chain filter chain
     * @throws IOException IO exceptions
     * @throws ServletException Servlet exceptions
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isSystemAvailable) {
            chain.doFilter(request, response);
        } else {
            logger.info("Request to {} from address: {}", ((HttpServletRequest)request).getRequestURL().toString(), request.getRemoteAddr());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.reset();
            httpServletResponse.setStatus(503);
        }
    }
}
