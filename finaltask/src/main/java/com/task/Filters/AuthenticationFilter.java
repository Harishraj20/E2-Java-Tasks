package com.task.Filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AuthenticationFilter implements Filter {
    protected static final Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthenticationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("Inside doFilter method");
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Requested URL: " + httpRequest.getRequestURI());

        Object user = httpRequest.getSession().getAttribute("LoginUser");
        if (user == null) {
            logger.warn("User not authenticated, redirecting to login page");

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
            return; 
        }

        logger.info("User is authenticated, proceeding with request");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("AuthenticationFilter destroyed");
    }
}