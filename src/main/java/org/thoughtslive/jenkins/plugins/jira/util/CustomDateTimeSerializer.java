package org.thoughtslive.jenkins.plugins.jira.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

/**
 * Custom DateTime Serializer, JIRA takes strings as an input, need to apply this serializer before
 * we send a json to JIRA.
 * 
 * @author nrayapati
 *
 */
public class CustomDateTimeSerializer extends StdScalarSerializer<DateTime> {

  public CustomDateTimeSerializer() {
    super(DateTime.class);
  }

  @Override
  public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider provider)
      throws IOException, JsonGenerationException {
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTimeAsString = fmt.print(dateTime);
    jsonGenerator.writeString(dateTimeAsString);
  }
}
