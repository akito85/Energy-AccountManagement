package com.dbs.common.base.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomddMMMyyyyhhmmssDes extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        try {
            Locale locale = new Locale("id", "ID");
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_DATETIME_VIEW, locale);
            return sdf.parse(jp.getText());
        } catch (ParseException ex) {
            Logger.getLogger(CustomddMMMyyyyDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
