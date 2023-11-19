package com.aem.demo.core.components.internal.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/permissionCheck")
public class AuthCheckerServlet extends SlingSafeMethodsServlet {
    private final static Logger LOG = LoggerFactory.getLogger(AuthCheckerServlet.class);

    @Override
    public void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        final String URI = request.getParameter("uri");

        Session session = request.getResourceResolver().adaptTo(Session.class);

        try {
            session.checkPermission(URI, Session.ACTION_READ);
            LOG.info("AuthChecker READ Access OK for {}", URI);
            response.setStatus(SlingHttpServletResponse.SC_OK);
        } catch (RepositoryException e) {
            LOG.info("AuthChecker READ Access FORBIDDEN for {}", URI);
            response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
        }
    }
}
