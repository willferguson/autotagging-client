package com.github.willferguson.autotaggingclient;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Created by will on 01/09/2016.
 */
public enum TaggingOperation {

    FACE_DETECTION,
    LANDMARK_DETECTION,
    LOGO_DETECTION,
    LABEL_DETECTION,
    TEXT_DETECTION,
    SAFE_SEARCH_DETECTION,
    IMAGE_PROPERTIES;

    public static EnumSet<TaggingOperation> setOf(String... ops) {

        return Arrays.stream(ops)
                .map(op -> TaggingOperation.valueOf(op.toUpperCase()))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(TaggingOperation.class)));
    }
}
