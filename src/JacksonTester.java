import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JacksonTester {
   public static void main(String args[]){
   
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = "{\"sender\":8454997084, \"receiver\":9760737797, \"message\":\"Hello! Bro.\", \"messageStatus\": }";
		
      try{
         MessageProtocol msg = mapper.readValue(jsonString, MessageProtocol.class);
         
         System.out.println(msg);
         
         mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
         jsonString = mapper.writeValueAsString(msg);
         
         System.out.println(jsonString);
      }
      catch (JsonParseException e) { e.printStackTrace();}
      catch (JsonMappingException e) { e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace(); }
   }
}

