package club.p6e.auth;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthPathMatcher {

    private final List<PathPattern> list = Collections.synchronizedList(new ArrayList<>());

    public boolean match(String path) {
        final PathContainer container = PathContainer.parsePath(path);
        for (final PathPattern pattern : list) {
            if (pattern.matches(container)) {
                return true;
            }
        }
        return false;
    }

    public PathPattern register(String path) {
        final PathPattern parser = new PathPatternParser().parse(path);
        list.add(parser);
        return parser;
    }

    public void unregister(PathPattern path) {
        list.remove(path);
    }

}
