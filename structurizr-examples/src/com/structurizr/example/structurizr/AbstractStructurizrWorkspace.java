package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

abstract class AbstractStructurizrWorkspace {

    static final String ANONYMOUS_USER = "Anonymous User";
    static final String AUTHENTICATED_USER = "Authenticated User";

    static final String STRUCTURIZR = "Structurizr";
    static final String STRUCTURIZR_CLIENT = "Structurizr Client";
    static final String PINGDOM = "Pingdom";
    static final String SENDGRID = "SendGrid";
    static final String TAXAMO = "Taxamo";
    static final String BRAINTREE = "Braintree";

    static final String WEB_BROWSER = "Web Browser";
    static final String WWW = "www.structurizr.com";
    static final String API = "api.structurizr.com";
    static final String DATABASE = "Database";

    static final String LOGGING_COMPONENT = "LoggingComponent";

    static final String DATABASE_TAG = "Database";

    private static final String FILENAME = "structurizr.json";

    protected void writeToFile(Workspace workspace) throws Exception {
        FileWriter fileWriter = new FileWriter(new File(FILENAME));
        JsonWriter jsonWriter = new JsonWriter(true);
        jsonWriter.write(workspace, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    protected Workspace readFromFile() throws Exception {
        FileReader fileReader = new FileReader(new File(FILENAME));
        JsonReader jsonReader = new JsonReader();
        Workspace workspace = jsonReader.read(fileReader);
        fileReader.close();

        return workspace;
    }

    protected void replaceLocalSourcePathWithRemoteUrl(Container container, String remoteSourcePath) throws Exception  {
        String currentDirectory = new File(".").getCanonicalPath();
        for (Component component : container.getComponents()) {
            if (component.getSourcePath() != null) {
                component.setSourcePath(component.getSourcePath().replace(currentDirectory, remoteSourcePath));
            }
        }
    }

}
