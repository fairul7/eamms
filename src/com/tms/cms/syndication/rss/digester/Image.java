/*
 * $Header: /home/cvspublic/jakarta-commons/digester/src/java/org/apache/commons/digester/rss/Image.java,v 1.5 2003/04/16 11:23:51 jstrachan Exp $
 * $Revision: 1.5 $
 * $Date: 2003/04/16 11:23:51 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package com.tms.cms.syndication.rss.digester;

import java.io.PrintWriter;
import java.io.Serializable;


/**
 * <p>Implementation object representing an <strong>image</strong> in the
 * <em>Rich Site Summary</em> DTD, version 0.91.  This class may be subclassed
 * to further specialize its behavior.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.5 $ $Date: 2003/04/16 11:23:51 $
 */

public class Image implements Serializable {


    // ------------------------------------------------------------- Properties


    /**
     * The image description (1-100 characters).
     */
    protected String description = null;

    public String getDescription() {
        return (this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * The image height in pixels (1-400).
     */
    protected int height = 31;

    public int getHeight() {
        return (this.height);
    }

    public void setHeight(int height) {
        this.height = height;
    }


    /**
     * The image link (1-500 characters).
     */
    protected String link = null;

    public String getLink() {
        return (this.link);
    }

    public void setLink(String link) {
        this.link = link;
    }


    /**
     * The image alternate text (1-100 characters).
     */
    protected String title = null;

    public String getTitle() {
        return (this.title);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * The image location URL (1-500 characters).
     */
    protected String url = null;

    public String getURL() {
        return (this.url);
    }

    public void setURL(String url) {
        this.url = url;
    }


    /**
     * The image width in pixels (1-400).
     */
    protected int width = 31;

    public int getWidth() {
        return (this.width);
    }

    public void setWidth(int width) {
        this.width = width;
    }


    // -------------------------------------------------------- Package Methods


    /**
     * Render this channel as XML conforming to the RSS 0.91 specification,
     * to the specified writer.
     *
     * @param writer The writer to render output to
     */
    void render(PrintWriter writer) {

        writer.println("    <image>");

        writer.print("      <title>");
        writer.print(title);
        writer.println("</title>");

        writer.print("      <url>");
        writer.print(url);
        writer.println("</url>");

        if (link != null) {
            writer.print("      <link>");
            writer.print(link);
            writer.println("</link>");
        }

        writer.print("      <width>");
        writer.print(width);
        writer.println("</width>");

        writer.print("      <height>");
        writer.print(height);
        writer.println("</height>");

        if (description != null) {
            writer.print("      <description>");
            writer.print(description);
            writer.println("</description>");
        }

        writer.println("    </image>");

    }


}
