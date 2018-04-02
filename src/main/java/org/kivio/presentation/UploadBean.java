package org.kivio.presentation;

import org.kivio.application.ImportBean;
import org.kivio.application.LogBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;

/**
 * Managed Bean for uploading a file. This is a facade for #ImportBean.
 */
@Named
@ViewScoped
public class UploadBean implements Serializable {
    private static Logger log = LoggerFactory.getLogger(UploadBean.class);
    private Part file;

    @Inject
    private ImportBean importBean;

    public void upload() {
        if (file == null) {
            log.error("no file");   // todo: message noch nach außen werfen
            return;
        }

        try {
            log.info("Starting upload of {}", file.getName());
            importBean.createPortfolio(file.getInputStream());
        } catch (IOException e) {
            log.error("Upload failed: {}", e.getMessage());
        }

        log.info("upload of {} was successful.", file.getName());
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}
