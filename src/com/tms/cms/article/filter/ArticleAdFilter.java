package com.tms.cms.article.filter;

import com.tms.cms.ad.model.Ad;
import com.tms.cms.ad.model.AdLocation;
import com.tms.cms.ad.model.AdModule;
import com.tms.cms.core.model.ContentUtil;
import kacang.Application;

import javax.servlet.http.HttpServletRequest;

/**
 * A filter that is used to parse and process special embedded comments.
 * To add more filters, subclass this class and
 * register with the ArticleFilterFactory class.
 */
public class ArticleAdFilter extends ArticleFilter {

    public String getPrefix() {
        return "<!-- Ad: ";
    }
    
    public String getSuffix() {
        return " -->";
    }

    public String process(HttpServletRequest request, String params) {
        try {
            // get ad location
            String adLocationName = params.trim();
            AdModule adModule = (AdModule)Application.getInstance().getModule(AdModule.class);
            AdLocation adLocation = adModule.getAdLocationByName(adLocationName);

            // view event
            boolean preview = ContentUtil.isPreviewRequest(request);
            Ad ad = adModule.viewAdEvent(request, adLocationName, preview);

            // generate HTML
            String defaultAdClickUrl = request.getContextPath() + "/cmsadmin/ad/adClick.jsp";
            StringBuffer output = new StringBuffer();
            if (ad.isUseScript()) {
                output.append(ad.getScript());
            }
            else {
                output.append("<a ");
                if (ad.isNewWindow()) {
                    output.append(" target=\"adWindow\" ");
                }
                output.append("href=\"" + defaultAdClickUrl + "?adId=" + ad.getAdId() + "\">");
                output.append("<img src=\"" + request.getContextPath() + "/storage/" + ad.getImageFile() + "\" alt=\"" + ad.getAlternateText() + "\"" + getImageDimension(adLocation) + " border=\"0\" align=\"right\">");
                output.append("</a>");

            }
            return output.toString();
        }
        catch (Exception e) {
            return "<!-- " + params + ": " + e.toString() + " -->";
        }
    }

    protected String getImageDimension(AdLocation adLocation) {
        String imageDimension = "";

        if(adLocation !=null) {
            switch(adLocation.getAdType()) {
                case AdLocation.AD_TYPE_FULL_BANNER_468_x_60:
                    imageDimension = "width=468 height=60"; break;

                case AdLocation.AD_TYPE_HALF_BANNER_234_x_60:
                    imageDimension = "width=234 height=60";
                    break;

                case AdLocation.AD_TYPE_MICRO_BAR_88_x_31:
                    imageDimension = "width=88 height=31";
                    break;

                case AdLocation.AD_TYPE_BUTTON_1_120_x_90:
                    imageDimension = "width=120 height=90";
                    break;

                case AdLocation.AD_TYPE_BUTTON_2_120_x_60:
                    imageDimension = "width=120 height=60";
                    break;

                case AdLocation.AD_TYPE_WIDE_SKYSCRAPER_160_x_600:
                    imageDimension = "width=160 height=600";
                    break;

                case AdLocation.AD_TYPE_SKYSCRAPER_120_x_600:
                    imageDimension = "width=120 height=600";
                    break;

                case AdLocation.AD_TYPE_SQUARE_BUTTON_125_x_125:
                    imageDimension = "width=125 height=125";
                    break;

                case AdLocation.AD_TYPE_RECTANGLE_180_x_150:
                    imageDimension = "width=180 height=150";
                    break;

                case AdLocation.AD_TYPE_VERTICAL_BANNER_120_x_240:
                    imageDimension = "width=120 height=240";
                    break;

                case AdLocation.AD_TYPE_MEDIUM_RECTANGLE_300_x_250:
                    imageDimension = "width=300 height=250";
                    break;

                case AdLocation.AD_TYPE_SQUARE_POP_UP_250_x_250:
                    imageDimension = "width=250 height=250";
                    break;

                case AdLocation.AD_TYPE_VERTICAL_RECTANGLE_240_x_400:
                    imageDimension = "width=240 height=400";
                    break;

                case AdLocation.AD_TYPE_LARGE_RECTANGLE_336_x_280:
                    imageDimension = "width=336 height=280";
                    break;

                case AdLocation.AD_TYPE_CUSTOM_FREE_SIZE:
                default:
                    imageDimension = "";
            }
        }
        return imageDimension;
    }


}
