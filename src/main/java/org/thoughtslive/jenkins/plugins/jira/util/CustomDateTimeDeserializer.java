package org.thoughtslive.jenkins.plugins.jira.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;

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
      return DateTime.parse(dateTimeAsString);
    }
    throw deserializationContext.mappingException(handledType());
  }
}
