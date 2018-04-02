package org.kivio.rest;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.kivio.application.ImportBean;
import org.kivio.application.InstrumentDao;
import org.kivio.application.PortfolioDao;
import org.kivio.entities.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("api")
public class DepotService {
    private Logger log = LoggerFactory.getLogger(DepotService.class);
    private static String CONTENT_ID = "file";

    @Inject
    private ImportBean importBean;

    @Inject
    private PortfolioDao portfolioDao;

    @Inject
    private InstrumentDao instrumentDao;

    @POST
    @Path("import")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response loadAssets(List<Attachment> attachments) {
        log.info("Attachments: {}", attachments.size());
        try {
            for (Attachment attachment : attachments) {
                if (attachment != null) {
                    InputStream is = attachment.getDataHandler().getInputStream();
                    importBean.createPortfolio(is);
                    is.close();
                } else {
                    log.error("no attachment found");
                }
            }
        } catch (IOException e) {
            log.error("no stream open", e);
            return Response.serverError().build();
        }

        return Response.ok("upload completed").build();
    }

    @GET
    @Path("status")
    public String getStatus() {
        return "Running";
    }

    @POST
    @Path("instrument")
    public Instrument addInstrument(@QueryParam("isin") String isin) {
        Instrument i = new Instrument(isin, null);
        instrumentDao.save(i);
        return i;
    }

    @GET
    @Path("nav-dates")
    public void getNavDates() {
        log.info("Lese NAV-Daten aus...");
        for(Date d : portfolioDao.findNavDates()) {
            log.info("Date: {}", d);
        }
    }
}
