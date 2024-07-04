package org.crpt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Api api = new Api();
        api.CrptApi(TimeUnit.MINUTES, 10);

    }
}

class Api {
    static String domain = "https://ismp.crpt.ru/";
    static String url = "api/v3/lk/documents/create";

    TimeUnit timeUnit;
    int requestLimit;
    long resetTime;

    Semaphore semaphore;

    int counter = 0;
    Thread[] threads;
    int i = 0;



    void CrptApi(TimeUnit timeUnit, int requestLimit) throws InterruptedException {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.semaphore = new Semaphore(requestLimit);
        this.threads = new Thread[requestLimit];

        start();

    }

    void request() throws IOException, URISyntaxException, InterruptedException {
        //подпись
        String signature = "signature";
        //http-запрос
        Document document = setupDocument();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String json = objectMapper.writeValueAsString(document);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(domain + url))
                .headers("Content-Type", "application/json",
                        "signature", signature)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Document setupDocument() {
        Description description = new Description();
        description.setParticipantInn("string");
        List<Description> descriptions = List.of(description);

        Product product = new Product();
        product.setCertificateDocument("string");
        product.setCertificateDocumentDate(LocalDate.parse("2020-01-23"));
        product.setCertificateDocumentNumber("string");
        product.setOwnerInn("string");
        product.setProducerInn("string");
        product.setProductionDate(LocalDate.parse("2020-01-23"));
        product.setTnvedCode("string");
        product.setUitCode("string");
        product.setUituCode("string");

        List<Product> products = List.of(product);

        Document document = new Document();
        document.setProducts(products);
        document.setDescriptionn(descriptions);

        document.setDocId("string");
        document.setDocStatus("string");
        document.setDocType(DocType.LP_INTRODUCE_GOODS);
        document.setImportRequest(true);
        document.setOwnerInn("string");
        document.setParticipantInn("string");
        document.setProducerInn("string");
        document.setProductionDate(LocalDate.parse("2020-01-23"));
        document.setProductionType("string");
        document.setRegDate(LocalDate.parse("2020-01-23"));
        document.setRegNumber("string");

        return document;
    }



    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    class Document {
        private List<Description> description;
        private String docId;
        private String docStatus;
        private DocType docType;
        private Boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private LocalDate productionDate;
        private String productionType;

        private List<Product> products;
        private LocalDate regDate;
        private String regNumber;

        public List<Description> getDescription() {
            return description;
        }

        public void setDescriptionn(List<Description> description) {
            this.description = description;
        }


        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }
        public DocType getDocType() {
            return docType;
        }

        public void setDocType(DocType docType) {
            this.docType = docType;
        }
        public String getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }
        public Boolean getImportRequest() {
            return importRequest;
        }

        public void setImportRequest(Boolean importRequest) {
            this.importRequest = importRequest;
        }
        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }
        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }
        public LocalDate getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(LocalDate productionDate) {
            this.productionDate = productionDate;
        }
        public String getProductionType() {
            return productionType;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public LocalDate getRegDate() {
            return regDate;
        }

        public void setRegDate(LocalDate regDate) {
            this.regDate = regDate;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }


    }
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    class Description {
        private String participantInn;

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    class Product {
        private String certificateDocument;
        private LocalDate certificateDocumentDate;
        private String certificateDocumentNumber;
        private String ownerInn;
        private String producerInn;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;

        public String getCertificateDocument() {
            return certificateDocument;
        }
        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }
        public LocalDate getCertificateDocumentDate() {
            return certificateDocumentDate;
        }
        public void setCertificateDocumentDate(LocalDate certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }
        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }
        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }
        public String getOwnerInn() {
            return ownerInn;
        }
        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }
        public String getProducerInn() {
            return producerInn;
        }
        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }
        public LocalDate getProductionDate() {
            return productionDate;
        }
        public void setProductionDate(LocalDate productionDate) {
            this.productionDate = productionDate;
        }
        public String getTnvedCode() {
            return tnvedCode;
        }
        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }
        public String getUitCode() {
            return uitCode;
        }
        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }
        public String getUituCode() {
            return uituCode;
        }
        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }

    }

    enum DocType {
        LP_INTRODUCE_GOODS
    }


    class Request {

        void sendHttpRequest() throws IOException, InterruptedException, URISyntaxException {
            counter += 1;
            if (counter >= requestLimit) {
                counter = 0;
            }
            request();
        }
     }

    void start() throws InterruptedException {
        sendRequest(new Api.Request());
        threads[counter].join();
        System.out.println("start");
    }

    void sendRequest(Request request) {
        RequestHandler requestHandler = new RequestHandler(request);
        Thread thread = createThread(i, requestHandler);
        thread.start();
    }

    Thread createThread(int i, RequestHandler requestHandler) {
        threads[i] = new Thread(requestHandler);
        return threads[i];
    }
    class RequestHandler implements Runnable {
        final Request request;

        RequestHandler(Request request) {
            this.request = request;

        }

        @Override
        public void run() {
            try {
                //ограничение на количество запросов
                semaphore.acquire();
                long currentTime = System.currentTimeMillis();
                if (currentTime >= resetTime) {
                    resetTime = currentTime + timeUnit.toMillis(1);
                    counter = 0;
                }
                if (counter >= requestLimit) {
                    Thread.sleep(resetTime - currentTime);
                }

                request.sendHttpRequest();

            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }
    }

}
