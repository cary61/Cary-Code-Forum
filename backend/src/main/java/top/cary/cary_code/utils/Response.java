package top.cary.cary_code.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {

    private Integer status;

    private String msg;

    private T data;

    // 根据是否成功返回ok()或fail()
    public static Response resultOf(boolean b) {
        if (b) {
            return Response.ok();
        } else {
            return Response.fail();
        }
    }

    public static Response ok() {
        return Response.ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<T>(200, "OK", data);
    }

    public static Response fail() {
        return Response.fail(null);
    }

    public static Response failWithMsg(String msg) {
        Response response = Response.fail();
        response.setMsg(msg);
        return response;
    }

    public static Response fail(Integer status, String msg) {
        return new Response(status, msg,null);
    }

    public static <T> Response<T> fail(T data) {
        return new Response<T>(400, "Bad Request", data);
    }
}
