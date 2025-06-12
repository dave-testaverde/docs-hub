package com.dt.dev.documentservice.domain.event;

public class DocumentUploadEvent {

    private String documentId;
    private String content;

    public DocumentUploadEvent() {
    }

    public DocumentUploadEvent(String documentId, String content) {
        this.documentId = documentId;
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getContent() {
        return content;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
