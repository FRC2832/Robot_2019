package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.Timer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractCsvLayout;
import org.apache.logging.log4j.core.layout.CsvParameterLayout;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.IOException;
import java.nio.charset.Charset;

@Plugin(
        name = "TimestampedCsvParameterLayout",
        category = "Core",
        elementType = "layout",
        printObject = true
)
public class TimestampedCsvParameterLayout extends AbstractCsvLayout {
    private boolean initialized;

    public static AbstractCsvLayout createDefaultLayout() {
        return new TimestampedCsvParameterLayout((Configuration)null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), (String)null, (String)null);
    }

    public static AbstractCsvLayout createLayout(CSVFormat format) {
        return new TimestampedCsvParameterLayout((Configuration)null, Charset.forName("UTF-8"), format, (String)null, (String)null);
    }

    @PluginFactory
    public static AbstractCsvLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value = "format",defaultString = "Default") String format, @PluginAttribute("delimiter") Character delimiter, @PluginAttribute("escape") Character escape, @PluginAttribute("quote") Character quote, @PluginAttribute("quoteMode") QuoteMode quoteMode, @PluginAttribute("nullString") String nullString, @PluginAttribute("recordSeparator") String recordSeparator, @PluginAttribute(value = "charset",defaultString = "UTF-8") Charset charset, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
        CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
        return new TimestampedCsvParameterLayout(config, charset, csvFormat, header, footer);
    }

    public TimestampedCsvParameterLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
        super(config, charset, csvFormat, header, footer);
    }

    public String toSerializable(LogEvent event) {
        Message message = event.getMessage();
        Object[] parameters = message.getParameters();
        StringBuilder buffer = getStringBuilder();
        if(initialized)
            buffer.append(Timer.getFPGATimestamp() + ",");
        else {
            buffer.append("Time,");
            initialized = true;
        }
        try {
            this.getFormat().printRecord(buffer, parameters);
            return buffer.toString();
        } catch (IOException var6) {
            StatusLogger.getLogger().error(message, var6);
            return this.getFormat().getCommentMarker() + " " + var6;
        }
    }
}
