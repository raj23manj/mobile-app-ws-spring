package com.appsdeveloperblog.app.ws.serializers;

import java.io.IOException;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AddressEntitySerializer extends StdSerializer<AddressEntity> {


	public AddressEntitySerializer(Class<AddressEntity> t) {
		super(t);
	}
	
	public AddressEntitySerializer() {
		this(null);
	}

	@Override
	public void serialize(AddressEntity value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("city", value.getCity());
        jgen.writeStringField("country", value.getCountry());

        //jgen.writeObjectField("addresses", value.getAddresses());
        //jgen.writeNumberField("lastName", value.getLastName());
        jgen.writeEndObject();	
	}

}