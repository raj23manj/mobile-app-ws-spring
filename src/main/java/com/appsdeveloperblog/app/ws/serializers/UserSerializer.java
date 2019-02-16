package com.appsdeveloperblog.app.ws.serializers;

import java.io.IOException;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class UserSerializer extends StdSerializer<UserDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2935611622932488613L;

	public UserSerializer(Class<UserDto> t) {
		super(t);
	}
	
	public UserSerializer() {
		this(null);
	}

	@Override
	public void serialize(UserDto value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("firstName", value.getFirstName());
        jgen.writeStringField("lastName", value.getLastName());
        //jgen.writeNumberField("lastName", value.getLastName());
        jgen.writeEndObject();	
	}

}
