package club.p6e.cloud.test.controller.su;

import com.darvi.hksi.badminton.lib.controller.filter.CrossDomainWebFilter;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.stereotype.Component;


/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@WebFilter(filterName = "CrFilter", urlPatterns = {"*"})
public class CrFilter extends CrossDomainWebFilter {
}
