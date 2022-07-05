package com.example.demo.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GsonUtils {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(YearMonth.class, new YearMonthTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
            .disableHtmlEscaping()
            .create();

    private GsonUtils() {

    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    private static class YearMonthTypeAdapter extends TypeAdapter<YearMonth> {

        @Override
        public void write(JsonWriter out, YearMonth value) throws IOException {
            if (value != null) {
                out.value(value.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            } else {
                out.nullValue();
            }
        }

        @Override
        public YearMonth read(JsonReader in) throws IOException {
            return in.nextString() == null ? null : YearMonth.parse(in.nextString(), DateTimeFormatter.ofPattern("yyyy-MM"));
        }

    }

    private static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value != null) {
                out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return in.nextString() == null ? null : LocalDate.parse(in.nextString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

    }

    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value != null) {
                out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return in.nextString() == null ? null : LocalDateTime.parse(in.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

    }

    private static class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

        @Override
        public void write(JsonWriter out, ZonedDateTime value) throws IOException {
            if (value != null) {
                out.value(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            } else {
                out.nullValue();
            }
        }

        @Override
        public ZonedDateTime read(JsonReader in) throws IOException {
            return in.nextString() == null ? null : ZonedDateTime.parse(in.nextString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

    }

}
