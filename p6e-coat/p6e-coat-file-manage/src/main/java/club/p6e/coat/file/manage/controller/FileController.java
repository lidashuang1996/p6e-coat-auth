package club.p6e.coat.file.manage.controller;

import club.p6e.coat.file.manage.infrastructure.context.FileContext;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.error.ParameterException;
import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PutMapping("/update/{id}")
    public ResultContext update(@PathVariable String id, @RequestBody FileContext.UpdateRequest param) {
        if (param == null) {
            throw new ParameterException();
        }
        if (param.getName() == null
                && (param.getTags() == null || param.getTags().size() == 0)) {
            throw new ParameterException();
        }
        return ResultContext.build();
    }

    @DeleteMapping("/delete/{id}")
    public ResultContext delete(@PathVariable String id) {
        return ResultContext.build();
    }

}
