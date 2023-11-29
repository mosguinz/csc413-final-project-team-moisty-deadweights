package request;

public class CustomParser {

    // extract java useable values from a raw http request string
    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages
    public static ParsedRequest parse(String request) {
        var parsedRequest = new ParsedRequest();
        request = request.trim();

        String[] req = request.split("\n\n", 2);
        String body = req.length == 1 ? null : req[1];

        String[] startLine = req[0].split(" ");
        String method = startLine[0],
                target = startLine[1],
                version = startLine[2];

        String[] targetFragment = target.split("\\?", 2);
        String path = targetFragment[0];

        if (targetFragment.length == 2) {
            for (String arg : targetFragment[1].split("&")) {
                String[] kvPair = arg.split("=", 2);
                parsedRequest.setQueryParam(kvPair[0], kvPair[1]);
            }
        }

        String[] headers = req[0].split("\n");
        for (int i = 1; i < headers.length; i++) { // skip start-line
            String line = headers[i];

            String[] headerKeyValue = line.split(": *", 2);
            String key = headerKeyValue[0];
            String value = headerKeyValue[1];
            parsedRequest.setHeaderValue(key, value);

            if (!key.equalsIgnoreCase("cookie")) {
                continue;
            }
            
            String[] cookies = value.split(";");
            for (String cookie : cookies) {
                String[] cookieKeyValue = cookie.split("=", 2);
                parsedRequest.setCookieValue(cookieKeyValue[0], cookieKeyValue[1]);
            }
        }

        parsedRequest.setMethod(method);
        parsedRequest.setPath(path);
        parsedRequest.setBody(body);

        return parsedRequest;
    }
}
