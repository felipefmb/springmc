package cursomc.com.felipebatista.cursomc.resources.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private String msg;
    private Long timestamp;

    public StandardError(Integer status, String msg, Long timestamp) {
        super();
        this.status = status;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public StandardError setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public StandardError setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public StandardError setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
