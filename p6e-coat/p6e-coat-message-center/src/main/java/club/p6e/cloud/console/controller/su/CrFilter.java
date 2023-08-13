package club.p6e.cloud.console.controller.su;

import com.darvi.hksi.badminton.lib.controller.filter.CrossDomainWebFilter;
import jakarta.servlet.annotation.WebFilter;


/**
 * @author lidashuang
 * @version 1.0
 */
@WebFilter(filterName = "CrFilter", urlPatterns = {"*"})
public class CrFilter extends CrossDomainWebFilter {
}
