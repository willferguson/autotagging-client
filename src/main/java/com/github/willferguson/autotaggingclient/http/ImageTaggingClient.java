package com.github.willferguson.autotaggingclient.http;

import com.github.willferguson.autotaggingclient.TaggingOperation;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by will on 01/09/2016.
 */
public interface ImageTaggingClient {

    public EnumSet<TaggingOperation> fetchOperations();

    public String fetchTags(String contentType, InputStream data, EnumSet<TaggingOperation> operations, int numberOfTags);
}
