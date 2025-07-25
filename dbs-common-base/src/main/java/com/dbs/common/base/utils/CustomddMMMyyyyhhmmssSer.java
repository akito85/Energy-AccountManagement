package com.dbs.common.base.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomddMMMyyyyhhmmssSer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat(Constant.FORMAT_DATETIME_VIEW);
        String formattedDate = formatter.format(t);
        jg.writeString(formattedDate);
    }
}
