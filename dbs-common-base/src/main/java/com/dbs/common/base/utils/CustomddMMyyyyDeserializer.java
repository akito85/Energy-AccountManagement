/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 * 
 * @author RachmatY
 */
public class CustomddMMyyyyDeserializer extends JsonDeserializer<Date>{

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        try {
            if (jp.getText() != null && jp.getText() != "") {
                 Locale locale = new Locale("id", "ID");
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_DATE_OTHER, locale);
            return sdf.parse(jp.getText());
            } else {
                return null;
            }
           
        } catch (ParseException ex) {
            Logger.getLogger(CustomddMMyyyyDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
