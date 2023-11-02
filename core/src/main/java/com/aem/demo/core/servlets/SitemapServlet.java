package com.aem.demo.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/public/aem-demo/sitemap")
@ServiceDescription("Sitemap Servlet")
public class SitemapServlet extends SlingAllMethodsServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Hello from SitemapServlet");

        resp.setContentType("text/plain");
        resp.getWriter().write("Hello from Sitemap!");
    }
}
