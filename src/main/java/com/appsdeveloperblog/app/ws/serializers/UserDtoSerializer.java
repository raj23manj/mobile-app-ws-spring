package com.appsdeveloperblog.app.ws.serializers;

import java.io.IOException;

import org.springframework.beans.BeanUtils;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class UserDtoSerializer extends StdSerializer<UserDto> {
    // https://stackoverflow.com/questions/53863697/jsongenerationexception-when-serializing-nested-object-using-custom-serializer-i
   public UserDtoSerializer() {
       this(null);
   }
  
   public UserDtoSerializer(Class<UserDto> t) {
       super(t);
   }
   
   @Override
   public void serialize(
		   UserDto value, JsonGenerator jgen, SerializerProvider provider) 
     throws IOException, JsonProcessingException {
 
       jgen.writeStartObject();
       jgen.writeNumberField("id", value.getId());
       jgen.writeStringField("firstName", value.getFirstName());
       jgen.writeStringField("lastName", value.getLastName());
       writeInnerObject(jgen, value);
      
       jgen.writeEndObject();
   }
   
   private void writeInnerObject(JsonGenerator jgen, UserDto row) throws IOException
   {
	   jgen.writeFieldName("addresses");
	   
	   jgen.writeStartArray();
       for (Object arg : row.getAddresses())
       {
    	   AddressesRest userModel = new AddressesRest();
		   BeanUtils.copyProperties(arg, userModel);
           jgen.writeObject(userModel);
    	   //jgen.writeString(arg.toString());
       }
       jgen.writeEndArray();
   }
}