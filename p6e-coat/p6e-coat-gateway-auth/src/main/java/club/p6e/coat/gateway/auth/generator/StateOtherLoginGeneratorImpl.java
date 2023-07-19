package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;

/**
 * @author lidashuang
 * @version 1.0
 */
public class StateOtherLoginGeneratorImpl implements StateOtherLoginGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.random(8, true, false);
    }

}
