package com.appsdeveloperblog.app.ws.serializers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AddressDtoSerializer extends StdSerializer<AddressDTO> {
    
   public AddressDtoSerializer() {
       this(null);
   }
  
   public AddressDtoSerializer(Class<AddressDTO> t) {
       super(t);
   }
   
   @Override
   public void serialize(
		   AddressDTO value, JsonGenerator jgen, SerializerProvider provider) 
     throws IOException, JsonProcessingException {
 
       jgen.writeStartObject();
       jgen.writeNumberField("id", value.getId());
       jgen.writeStringField("city", value.getCity());
       jgen.writeStringField("country", value.getCountry());
       //jgen.writeObjectField("user", value.getUserDetails());
       writeInnerObject(jgen, value);
       
       jgen.writeEndObject();
   }
   
   private void writeInnerObject(JsonGenerator jgen, AddressDTO row) throws IOException
   {
	   jgen.writeFieldName("userDetails");
	   
	   Map<String, Object> returnObject= new HashMap<>();
	   
	   returnObject.put("id", row.getUserDetails().getId());
	   returnObject.put("email", row.getUserDetails().getEmail());
	   
	   
	   jgen.writeObject(returnObject);
	   
   }
}
