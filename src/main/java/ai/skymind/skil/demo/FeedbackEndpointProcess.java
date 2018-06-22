package ai.skymind.skil.demo;

import org.datavec.api.transform.metadata.ColumnMetaData;
import org.datavec.api.transform.metadata.DoubleMetaData;
import org.datavec.api.transform.transform.BaseColumnTransform;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Writable;
import org.nd4j.shade.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class FeedbackEndpointProcess extends BaseColumnTransform {
    private static String workspaceName = System.getProperty("workspace", "resume_feedback");

    public FeedbackEndpointProcess() {}

    public FeedbackEndpointProcess(@JsonProperty("columnName") String columnName) {
        super(columnName);
    }

    @Override
    public ColumnMetaData getNewColumnMetaData(String newName, ColumnMetaData columnMetaData) {
        return new DoubleMetaData(newName);
    }

    private void logStuff(String input) {
        String[] parts = input.split(",");
        String label;
        String output;

        if (parts.length >= 2) {
            label = parts[0];
            output = parts[1] + "\n";
        } else {
            label = "error";
            output = input + "\n";
        }

        try {
            Path resumeDir = Paths.get("/opt", "skil", "plugins", "files", workspaceName);
            Path labelDir = Paths.get("/opt", "skil", "plugins", "files", workspaceName, label);

            if (!Files.exists(resumeDir)) {
                Files.createDirectories(resumeDir);
            }

            if (!Files.exists(labelDir)) {
                Files.createDirectories(labelDir);
            }

            Path outFile = Paths.get("/opt", "skil", "plugins", "files", workspaceName, label, UUID.randomUUID().toString() + ".txt");

            Files.write(outFile, output.getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public Writable map(Writable writable) {
        logStuff(writable.toString());

        return new DoubleWritable(42.0);
    }

    @Override
    public String toString() {
        return "FeedbackEndpointProcess(columnName = " + columnName + ")";
    }

    @Override
    public Object map(Object input) {
        logStuff(String.valueOf(input));

        return 42.0;
    }
}
