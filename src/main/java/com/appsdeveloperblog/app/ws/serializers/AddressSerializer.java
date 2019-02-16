package com.appsdeveloperblog.app.ws.serializers;

import java.io.IOException;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


public class AddressSerializer extends StdSerializer<AddressDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2935611622932488613L;

	public AddressSerializer(Class<AddressDTO> t) {
		super(t);
	}
	
	public AddressSerializer() {
		this(null);
	}

	@Override
	public void serialize(AddressDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("city", value.getCity());
        jgen.writeStringField("country", value.getCountry());

        //jgen.writeObjectField("addresses", value.getAddresses());
        //jgen.writeNumberField("lastName", value.getLastName());
        jgen.writeEndObject();	
	}

}
