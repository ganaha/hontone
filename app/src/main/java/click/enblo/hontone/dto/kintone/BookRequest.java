package click.enblo.hontone.dto.kintone;

/**
 * Request.
 */
public class BookRequest {

    public String app;
    public Record record;

    /**
     * Constructor.
     */
    public BookRequest(String app, Record record) {
        this.app = app;
        this.record = record;
    }
}
