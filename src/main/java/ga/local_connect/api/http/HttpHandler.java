package ga.local_connect.api.http;

import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.object.APIError;
import ga.local_connect.api.util.EnumHelper;
import ga.local_connect.api.util.JsonHelper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpHandler extends AbstractHandler {
    public HttpHandler() {
        EndpointManager.register(Endpoints.class);
    }

    @Override
    public void handle(String target, Request req,
                       HttpServletRequest sReq, HttpServletResponse sRes) throws IOException {
        Object obj = null;

        try {
            var path = req.getHttpURI().getPath();
            var data = path.split("/");
            if (data.length < 3) {
                throw new LocalConnectException(
                    HttpStatuses.BAD_REQUEST,
                    APIErrorType.INVALID_URI
                );
            }

            var method = EnumHelper.valueOf(HttpMethodType.class, req.getMethod());
            var category = EnumHelper.valueOf(EndpointCategory.class, data[1]);
            var endpoint = data[2];

            if (category == null || endpoint.isEmpty()) {
                throw new LocalConnectException(
                    HttpStatuses.NOT_FOUND,
                    APIErrorType.ENDPOINT_NOT_FOUND
                );
            }

            obj = EndpointManager.call(method, category, endpoint, req);
        } catch (LocalConnectException e) {
            sRes.setStatus(e.getStatus());
            obj = new APIError(e.getCode());
        } catch (NumberFormatException e) {
            sRes.setStatus(HttpStatuses.BAD_REQUEST);
            obj = new APIError(APIErrorType.INVALID_NUMBER);
        } catch (IOException e) {
            sRes.setStatus(HttpStatuses.INTERNAL_SERVER_ERROR);
            obj = new APIError(APIErrorType.IO_ERROR);
        } catch (Throwable t) {
            sRes.setStatus(HttpStatuses.INTERNAL_SERVER_ERROR);
            obj = new APIError(APIErrorType.UNKNOWN_ERROR);
            t.printStackTrace();
        } finally {
            if (obj == null) {
                sRes.setStatus(HttpStatuses.NO_CONTENT);
            } else if (obj instanceof byte[]) {
                sRes.setContentType("image/jpeg");

                try (var stream = sRes.getOutputStream()) {
                    stream.write((byte[]) obj);
                }
            } else {
                sRes.setContentType("application/json; charset=UTF-8");

                try (var writer = sRes.getWriter()) {
                    writer.println(JsonHelper.serialize(obj));
                    writer.flush();
                }
            }
        }
    }
}
