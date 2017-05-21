package org.thoughtslive.jenkins.plugins.jira.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * Custom DateTime Deserializer.
 * 
 * @author nrayapati
 *
 */
public class CustomDateTimeDeserializer extends StdScalarDeserializer<DateTime> {

  private static final long serialVersionUID = -5344421368581876814L;

  public CustomDateTimeDeserializer() {
    super(DateTime.class);
  }

  @Override
  public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    JsonToken currentToken = jsonParser.getCurrentToken();
    if (currentToken == JsonToken.VALUE_STRING) {
      String dateTimeAsString = jsonParser.getText().trim();
      DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
          .withZone(DateTimeZone.forID("Europe/London"));
      DateTime result = fmt.parseDateTime(dateTimeAsString);
      return result;
    }
    throw deserializationContext.mappingException(handledType());
  }
}
