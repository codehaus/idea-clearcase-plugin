// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) braces fieldsfirst splitstr(nl) nonlb space 
// Source File Name:   IconUtil.java

package org.intellij.plugins.ui;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.*;

public class IconUtil {

    public static final Icon DEFAULT_ICON = getDefaultIcon();

    public IconUtil() {
    }

    public static Icon getIcon(String path) {
        if (path == null) {
            return DEFAULT_ICON;
        }
        URL url = (org.intellij.plugins.ui.IconUtil.class).getResource(path);
        if (url == null) {
            try {
                url = new URL(path);
            }
            catch (MalformedURLException e) {
                return DEFAULT_ICON;
            }
        }
        Icon icon = new ImageIcon(url);
        if (icon.getIconWidth() < 0 || icon.getIconHeight() < 0) {
            return DEFAULT_ICON;
        } else {
            return icon;
        }
    }

    private static Icon getDefaultIcon() {
        BufferedImage bi = new BufferedImage(18, 18, 3);
        Graphics2D g2 = bi.createGraphics();
        g2.setBackground(Color.red);
        g2.clearRect(0, 0, bi.getWidth(), bi.getHeight());
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2.0F));
        GeneralPath x = new GeneralPath();
        x.moveTo(0.0F, 0.0F);
        x.lineTo(bi.getWidth() - 1, bi.getHeight() - 1);
        x.moveTo(0.0F, bi.getHeight() - 1);
        x.lineTo(bi.getWidth() - 1, 0.0F);
        g2.draw(x);
        return new ImageIcon(bi);
    }

    public static Color parseColor(String rgba) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 128;
        StringTokenizer t = new StringTokenizer(rgba);
        try {
            if (t.hasMoreTokens()) {
                red = nextSample(t);
            }
            if (t.hasMoreTokens()) {
                green = nextSample(t);
            }
            if (t.hasMoreTokens()) {
                blue = nextSample(t);
            }
            if (t.hasMoreTokens()) {
                alpha = nextSample(t);
            }
        }
        catch (NumberFormatException nfe) { }
        return new Color(red, green, blue, alpha);
    }

    private static int nextSample(StringTokenizer t) {
        return Math.min(Math.abs(Integer.valueOf(t.nextToken()).intValue()), 255);
    }

    public static String encodeColor(Color color) {
        return color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " " + color.getAlpha();
    }

}
