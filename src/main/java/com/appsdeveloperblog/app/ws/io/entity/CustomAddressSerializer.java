package com.appsdeveloperblog.app.ws.io.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomAddressSerializer extends StdSerializer<List<AddressEntity>> {
	/**
	 * 
	 */
	//private static final long serialVersionUID = -2471337775332993738L;

	public CustomAddressSerializer() {
        this(null);
    }
 
    public CustomAddressSerializer(Class<List<AddressEntity>> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      List<AddressEntity> items, 
      JsonGenerator generator, 
      SerializerProvider provider) 
      throws IOException, JsonProcessingException {
         
        List<Integer> ids = new ArrayList<>();
        for (AddressEntity item : items) {
            ids.add((int) item.getId());
        }
        generator.writeObject(ids);
    }
}
