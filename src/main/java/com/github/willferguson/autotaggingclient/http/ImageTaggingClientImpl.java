package com.github.willferguson.autotaggingclient.http;

import com.github.willferguson.autotaggingclient.TaggingOperation;
import com.github.willferguson.autotaggingclient.http.ribbon.ImageTaggingClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * Created by will on 31/08/2016.
 */
@Component
@RibbonClient(name = "nps-autotagging", configuration = ImageTaggingClientConfiguration.class)
public class ImageTaggingClientImpl implements ImageTaggingClient{

    private static final Logger logger = LoggerFactory.getLogger(ImageTaggingClientImpl.class);

    private static final String TYPES_URL = "http://nps-autotagging/tag/types";
    private static final String TAGGING_URL = "http://nps-autotagging/tag";
    private RestTemplate restTemplate;

    @Autowired
    public ImageTaggingClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnumSet<TaggingOperation> fetchOperations() {

        ResponseEntity<EnumSet<TaggingOperation>> responseEntity = this.restTemplate.exchange(
                TYPES_URL,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<java.util.EnumSet<TaggingOperation>>() {});

        return handleResponse(responseEntity);

    }

    @Override
    public String fetchTags(String contentType, InputStream data, EnumSet<TaggingOperation> operations, int numberOfTags) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<>();
        multipartMap.add("file", new InputStreamResource(data));
        operations.forEach(type -> {
            multipartMap.add("type", type);
        });
        multipartMap.add("numberOfTags", numberOfTags);


//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setBufferRequestBody(false);
//        restTemplate.setRequestFactory(requestFactory);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartMap, header);

        return this.restTemplate.postForObject(
                TAGGING_URL,
                requestEntity,
                String.class);

        //return handleResponse(responseEntity);



    }

    private <T> T handleResponse(ResponseEntity<T> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        else {
            throw new RuntimeException(responseEntity.getStatusCode().toString() + " returned when trying to fetch operations");
        }
    }

}
