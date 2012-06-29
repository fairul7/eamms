package com.tms.cms.template;

import kacang.Application;

import javax.servlet.*;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This filter is used to define a specific locale for a defined mapping in web.xml, such that
 * any URL within that mapping will utilize that locale instead of the default application locale.
 * This feature can be used to support have multiple language websites.
 *
 * Use the filter parameters "cms.site.language" and "cms.site.country" to set the desired locale.
 * For example, a URL mapping of /zh/* with language "zh" and country "CN" will result in all
 * pages within the path /zh to have the locale Chinese (China).
 */
public class TemplateLocaleFilter implements Filter {

    public static final String LOCALE_LANGUAGE = "cms.site.language";
    public static final String LOCALE_COUNTRY = "cms.site.country";

    private FilterConfig filterConfig;
    private Locale locale;

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;

        Application app = Application.getInstance();
        String lang = filterConfig.getInitParameter(LOCALE_LANGUAGE);
        if (lang == null || lang.trim().length() == 0) {
            // set default locale
            locale = app.getLocale();
        } else {
            // set custom locale
            String country = filterConfig.getInitParameter(LOCALE_COUNTRY);
            locale = new Locale(lang, country);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Application app = Application.getInstance();

        if (locale == app.getLocale()) {
            filterChain.doFilter(request, response);
        } else {
            // save existing locale
            Locale appLocale = app.getLocale();
            String appBundleName = app.getResourceBundleName();

            try {
                // set new locale
                ResourceBundle bundle = ResourceBundle.getBundle(appBundleName, locale);
                Application.setThreadLocale(locale, appBundleName, bundle);
                LocalizationContext lc = new LocalizationContext(bundle, locale);
                Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, lc);
                Config.set(request, Config.FMT_LOCALE, locale);

                filterChain.doFilter(request, response);
            }
            finally {
                // restore old locale
                Application.resetThreadLocale();
                Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, appBundleName);
                Config.set(request, Config.FMT_LOCALE, appLocale);
            }
        }
    }

    public void destroy() {
    }

}
