package com.github.willferguson.autotaggingclient;

import com.github.willferguson.autotaggingclient.http.ImageTaggingClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by will on 31/08/2016.
 */
@Component
public class ImageTaggingRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ImageTaggingRunner.class);

    private ImageTaggingClientImpl imageTaggingClientImpl;

    @Autowired
    public ImageTaggingRunner(ImageTaggingClientImpl imageTaggingClientImpl) {
        this.imageTaggingClientImpl = imageTaggingClientImpl;
    }

    /**
     * Really can't be bothered with a complex command line syntax so pay attention! :)
     * Example usage:
     *
     * java -jar tagging.jar tag /home/will/test.jpg image/jpeg LOGO_DETECTION,TEXT_DETECTION 5
     * java -jar tagging.jar ops
     *
     * 1st param - tag | ops // Mode tag or ops
     * 2nd param - image/jpeg // Content type
     * 3nd param - LOGO_DETECTION,TEXT_DETECTION // Comma separated list of operations (when tagging)
     * 4rd param - 3 // Number of tags to generate per type. (when tagging)
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting ImageTagging Client");
        if (args.length < 1) {
            System.out.println(printUsage());
            throw new RuntimeException("Bad Params");
        }
        String mode = args[0];
        if (mode.equalsIgnoreCase("ops")) {
            System.out.println("Fetch applicable operations...");
            String ops = imageTaggingClientImpl.fetchOperations().toString();
            System.out.println(ops);
        }
        else if (mode.equalsIgnoreCase("tag")) {
            String imagePath = args[1];
            String contentType = args[2];
            String operations = args[3];
            int numberOfTags = Integer.parseInt(args[4]);

            System.out.println("Fetching " + numberOfTags + " tags for " + imagePath + "(" + contentType + ") with operations " + operations);
            InputStream inputStream;
            if (imagePath.startsWith("http")) {
                inputStream = new URL(imagePath).openStream();
            }
            else {
                inputStream = new FileInputStream(new File(imagePath));
            }
            String[] ops = operations.split(",");
            String tagResponse = imageTaggingClientImpl.fetchTags(contentType, inputStream, TaggingOperation.setOf(ops), numberOfTags);
            System.out.println(tagResponse);
        }
        else {
            System.err.println(printUsage());
        }

    }

    private String printUsage() {
        return "Usage should go here ...";
    }
}
