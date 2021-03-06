package com.feed_the_beast.ftbcurseappbot.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public class CommonMarkUtils {
    private Parser parser;
    private HtmlRenderer renderer;

    public CommonMarkUtils () {
        List<Extension> extensions = Arrays.asList(TablesExtension.create(), StrikethroughExtension.create(), HeadingAnchorExtension.create(), InsExtension.create());
        parser = Parser.builder().extensions(extensions).build();
        renderer = HtmlRenderer.builder().extensions(extensions).build();

    }

    public static String Bold (String in) {
        return "**" + in + "**";
    }

    public static String Italics (String in) {
        return "*" + in + "*";
    }

    public static String h1 (String in) {
        return "# " + in + "\n";
    }

    public static String h2 (String in) {
        return "## " + in + "\n";
    }

    public static String h3 (String in) {
        return "### " + in + "\n";
    }

    public static String h4 (String in) {
        return "#### " + in + "\n";
    }

    public static String link (String title, String link) {
        return "[" + title + "](" + link + ")";
    }

    public static String image (String image) {
        return "!" + link("Image", image);
    }

    public static String blockquote (String in) {
        return "> " + in;
    }

    public static String list (String in) {
        return "* " + in + "\n";
    }

    public static String horizontalRule () {
        return "---";
    }

    public static String inline (String in) {
        return "`" + in + "`";
    }

    public static String codeBlock (String in) {
        return "```\n" + in + "\n```";
    }

    public static String tableHeader (String... items) {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (String s : items) {
            b.append(s).append("|");
        }
        b.append("\n").append("|");
        for (String s : items) {
            b.append("---").append("|");
        }
        b.append("\n");
        return b.toString();
    }

    public static String tableRow (String... items) {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (String s : items) {
            if (s == null) {
                b.append("|");
            } else {
                b.append(s).append("|");
            }
        }
        b.append("\n");
        return b.toString();
    }

    public String renderToHTML (String md) {
        Node document = parser.parse(md);
        return renderer.render(document);

    }

}
