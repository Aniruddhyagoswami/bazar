package org.ecommerce.project.execptions;

public class APIExecption  extends  RuntimeException{
    private static final long serialVersionUID = 1L;
    public APIExecption() {
    }
    public APIExecption(String message) {
        super(message);
    }
}
