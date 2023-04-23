package club.p6e.coat.file.manage.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PutMapping("/update/{id}")
    public void update(@PathVariable String id) {

    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {

    }

}
