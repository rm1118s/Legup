package edu.rpi.legup.utility;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class dependencyTracker {
    public static void main(String[] args) {
        Project project = Project.getProject("legup");
        StringBuilder markdownString = new StringBuilder("|dependency|license| \n |:-- |:--| \n ");
        for (Configuration configuration : project.getConfigurations()) {
            for (Dependency dependency : configuration.getAllDependencies()) {
                String name = dependency.getName();
                String description = dependency.getDescription();
                String[] arr = description.split("license");
                String license;

                if (arr.length == 1) {
                    // TODO: find an api to search for license if it is not in the description
                    license = "Unknow";
                } else {
                    license = arr[1].split(" ")[0];
                }

                markdownString.append("|").append(name).append("|").append(license).append("|");

            }
        }

        try (FileWriter writer = new FileWriter("dependency.markdown")) {
            // Write to the file using the write() method
            writer.write(String.valueOf(markdownString));
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}
